package com.example.LuanVanTotNghiep.service;

import com.example.LuanVanTotNghiep.dto.request.ApplicationsRequest;
import com.example.LuanVanTotNghiep.dto.response.ApplicationsResponse;
import com.example.LuanVanTotNghiep.entity.Applications;
import com.example.LuanVanTotNghiep.entity.Documents;
import com.example.LuanVanTotNghiep.entity.Users;
import com.example.LuanVanTotNghiep.enums.ApplicationTypeEnum;
import com.example.LuanVanTotNghiep.exception.AppException;
import com.example.LuanVanTotNghiep.exception.ErrorCode;
import com.example.LuanVanTotNghiep.mapper.ApplicationsMapper;
import com.example.LuanVanTotNghiep.repository.ApplicationsRepository;
import com.example.LuanVanTotNghiep.repository.UsersRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApplicationsService {
    @Autowired
    ApplicationsRepository applicationsRepository;

    @Autowired
    DocumentsService documentsService;

    @Autowired
    ApplicationsMapper applicationsMapper;

    @Autowired
    AuthenticationService authenticationService;


    public String generateApplicationId(ApplicationTypeEnum type, int methodId) {
        String prefix = type == ApplicationTypeEnum.UNDERGRADUATE ? "DH" : "SDH";
        String year = String.valueOf(Year.now().getValue());
        int maxSequence = type == ApplicationTypeEnum.UNDERGRADUATE ? 3000 : 1000;

        if (type == ApplicationTypeEnum.UNDERGRADUATE) {
            if (methodId < 1 || methodId > 3) {
                throw new AppException(ErrorCode.INVALID_REQUEST);
            }
        }

        String searchPattern = type == ApplicationTypeEnum.UNDERGRADUATE ? prefix + year + methodId + "%" : prefix + year + "%";
        int sequence = applicationsRepository.countByApplicationIdLike(searchPattern).intValue() + 1;

        if (sequence > maxSequence) {
            throw new AppException(ErrorCode.DATA_CONFLICT);
        }

        return type == ApplicationTypeEnum.UNDERGRADUATE
                ? String.format("%s%s%d%04d", prefix, year, methodId, sequence)
                : String.format("%s%s%04d", prefix, year, sequence);
    }

    @Transactional
    public ApplicationsResponse createApplication(ApplicationsRequest request) {
        log.info("Received request: {}", request);
        if (request.getApplicationType() == null || request.getStatus() == null) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }
        String userId = authenticationService.getAuthenticatedUserId();
        Applications application = applicationsMapper.toCreateApplications(request);
        application.setUser(new Users(userId));
        if (application.getApplication_type() == null) {
            log.error("Application type is null after mapping for request: {}", request);
            throw new AppException(ErrorCode.UNEXPECTED_ERROR);
        }
        if (application.getApplication_type() == ApplicationTypeEnum.UNDERGRADUATE) {
            if (application.getAdmission_method() == null) {
                throw new AppException(ErrorCode.INVALID_REQUEST);
            }
            int methodId = application.getAdmission_method().getMethod_id();
            if (methodId < 1 || methodId > 3) {
                throw new AppException(ErrorCode.INVALID_REQUEST);
            }
        } else {
            application.setAdmission_method(null);
        }
        int methodId = application.getApplication_type() == ApplicationTypeEnum.UNDERGRADUATE ? application.getAdmission_method().getMethod_id() : 0;
        application.setApplication_id(generateApplicationId(application.getApplication_type(), methodId));
        application.setCreate_date(new Date());
        Applications saved = applicationsRepository.saveAndFlush(application); // Flush to ensure DB write
        documentsService.updateDocumentsForApplication(saved);
        ApplicationsResponse response = applicationsMapper.toApplicationsResponse(saved);
        log.info("Response: {}", response);
        return response;
    }

    public List<ApplicationsResponse> getAllApplications() {
        return applicationsMapper.listApplications(applicationsRepository.findAll());
    }

    public List<ApplicationsResponse> getApplicationsByUserId(String userId) {
        List<Applications> applications = applicationsRepository.findByUserId(userId);
        return applications.stream()
                .map(applicationsMapper::toApplicationsResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ApplicationsResponse updateApplication(String applicationId, ApplicationsRequest request) {
        log.info("Updating application with ID: {}", applicationId);
        String userId = authenticationService.getAuthenticatedUserId();
        Applications application = applicationsRepository.findById(applicationId)
                .orElseThrow(() -> new AppException(ErrorCode.CONTENT_NO_EXISTS));

        // Kiểm tra quyền sở hữu
        if (!application.getUser().getUser_id().equals(userId)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        applicationsMapper.updateApplications(application, request);
        if (application.getApplication_type() == null) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }

        // Kiểm tra tiền tố applicationId
        if (applicationId.startsWith("SDH")) {
            application.setAdmission_method(null);
        } else if (application.getApplication_type() == ApplicationTypeEnum.UNDERGRADUATE) {
            if (application.getAdmission_method() == null) {
                throw new AppException(ErrorCode.INVALID_REQUEST);
            }
            int methodId = application.getAdmission_method().getMethod_id();
            if (methodId < 1 || methodId > 3) {
                throw new AppException(ErrorCode.INVALID_REQUEST);
            }
        } else {
            application.setAdmission_method(null);
        }

        application.setUpdate_date(new Date());
        Applications saved = applicationsRepository.saveAndFlush(application);
        ApplicationsResponse response = applicationsMapper.toApplicationsResponse(saved);
        log.info("Updated application response: {}", response);
        return response;
    }

    @Transactional
    public void deleteApplication(String applicationId) {
        if (!applicationsRepository.existsById(applicationId)) {
            throw new AppException(ErrorCode.CONTENT_NO_EXISTS);
        }
        String userId = authenticationService.getAuthenticatedUserId();
        documentsService.deleteAllDocumentsByApplicationId(userId, applicationId);
        applicationsRepository.deleteById(applicationId);
    }

    public List<String> getDocumentImagesByUserIdAndApplicationId(String userId, String applicationId) {
        if (userId == null || userId.isEmpty() || applicationId == null || applicationId.isEmpty()) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }
        List<Documents> documents = documentsService.getDocumentsByUserIdAndApplicationId(userId, applicationId);
        if (documents.isEmpty()) {
            throw new AppException(ErrorCode.DATA_NOT_FOUND);
        }
        return documents.stream()
                .flatMap(doc -> Stream.of(
                        doc.getDocument_link_cccd(),
                        doc.getDocument_link_hoc_ba_lop10(),
                        doc.getDocument_link_hoc_ba_lop11(),
                        doc.getDocument_link_hoc_ba_lop12(),
                        doc.getDocument_link_bang_tot_nghiep_thpt(),
                        doc.getDocument_link_ket_qua_thi_thpt(),
                        doc.getDocument_link_ket_qua_thi_dgnl(),
                        doc.getDocument_link_chung_chi_ngoai_ngu()
                ))
                .filter(url -> url != null && !url.isEmpty())
                .collect(Collectors.toList());
    }
}