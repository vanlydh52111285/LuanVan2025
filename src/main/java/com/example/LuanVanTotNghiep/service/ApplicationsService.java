package com.example.LuanVanTotNghiep.service;

import com.example.LuanVanTotNghiep.dto.request.ApplicationsRequest;
import com.example.LuanVanTotNghiep.dto.response.ApplicationsResponse;
import com.example.LuanVanTotNghiep.entity.Applications;
import com.example.LuanVanTotNghiep.entity.Documents;
import com.example.LuanVanTotNghiep.entity.Methods;
import com.example.LuanVanTotNghiep.entity.Users;
import com.example.LuanVanTotNghiep.enums.ApplicationTypeEnum;
import com.example.LuanVanTotNghiep.exception.AppException;
import com.example.LuanVanTotNghiep.exception.ErrorCode;
import com.example.LuanVanTotNghiep.mapper.ApplicationsMapper;
import com.example.LuanVanTotNghiep.repository.ApplicationsRepository;
import com.example.LuanVanTotNghiep.repository.MethodsRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
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
    ApplicationsRepository applicationsRepository;
    DocumentsService documentsService;
    ApplicationsMapper applicationsMapper;
    AuthenticationService authenticationService;
    MethodsRepository methodsRepository;


    public String generateApplicationId(ApplicationTypeEnum type, String methodId) {
        String prefix = type == ApplicationTypeEnum.UNDERGRADUATE ? "DH" : "SDH";
        String year = String.valueOf(Year.now().getValue());
        int maxSequence = type == ApplicationTypeEnum.UNDERGRADUATE ? 3000 : 1000;

        String searchPattern = type == ApplicationTypeEnum.UNDERGRADUATE ? prefix + year + methodId + "%" : prefix + year + "%";
        int sequence = applicationsRepository.countByApplicationIdLike(searchPattern).intValue() + 1;

        if (sequence > maxSequence) {
            throw new AppException(ErrorCode.DATA_CONFLICT);
        }

        return type == ApplicationTypeEnum.UNDERGRADUATE
                ? String.format("%s%s%s%04d", prefix, year, methodId, sequence)
                : String.format("%s%s%04d", prefix, year, sequence);
    }

    @Transactional
    public ApplicationsResponse createApplication(ApplicationsRequest request) {
        if (request.getApplication_type() == null || request.getStatus() == null) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }

        String userId = authenticationService.getAuthenticatedUserId();
        Applications application = applicationsMapper.toCreateApplications(request);
        application.setUser(new Users(userId));

        String methodId;
        if (request.getApplication_type() == ApplicationTypeEnum.UNDERGRADUATE) {
            methodId = request.getMethodId();
            if (methodId == null || methodId.isEmpty()) {
                throw new AppException(ErrorCode.INVALID_REQUEST);
            }
            if (!methodId.matches("^[1-3]$")) {
                throw new AppException(ErrorCode.INVALID_REQUEST);
            }
        } else {
            methodId = "POSTGRAD";
        }

        Methods method = methodsRepository.findByMethod_id(methodId)
                .orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND));
        application.setAdmission_method(method);

        application.setApplication_id(generateApplicationId(request.getApplication_type(), methodId));
        application.setCreate_date(new Date());
        Applications saved = applicationsRepository.saveAndFlush(application);
        documentsService.updateDocumentsForApplication(saved);

        ApplicationsResponse response = applicationsMapper.toApplicationsResponse(saved);
        response.setUserId(saved.getUser() != null ? saved.getUser().getUser_id() : null);
        response.setMethodId(saved.getAdmission_method() != null ? saved.getAdmission_method().getMethod_id() : null);
        return response;
    }

    public List<ApplicationsResponse> getAllApplications() {
        List<Applications> applications = applicationsRepository.findAll();
        if (applications.isEmpty()) {
            throw new AppException(ErrorCode.DATA_NOT_FOUND);
        }
        return applications.stream()
                .map(application -> {
                    ApplicationsResponse response = applicationsMapper.toApplicationsResponse(application);
                    response.setUserId(application.getUser() != null ? application.getUser().getUser_id() : null);
                    response.setMethodId(application.getAdmission_method() != null ? application.getAdmission_method().getMethod_id() : null);
                    return response;
                })
                .collect(Collectors.toList());
    }

    public List<ApplicationsResponse> getApplicationsByUserId(String userId) {
        List<Applications> applications = applicationsRepository.findByUserId(userId);
        if (applications.isEmpty()) {
            throw new AppException(ErrorCode.DATA_NOT_FOUND);
        }
        return applications.stream()
                .map(application -> {
                    ApplicationsResponse response = applicationsMapper.toApplicationsResponse(application);
                    response.setUserId(application.getUser() != null ? application.getUser().getUser_id() : null);
                    response.setMethodId(application.getAdmission_method() != null ? application.getAdmission_method().getMethod_id() : null);
                    return response;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public ApplicationsResponse updateApplication(String applicationId, ApplicationsRequest request) {
        String userId = authenticationService.getAuthenticatedUserId();
        Applications application = applicationsRepository.findById(applicationId)
                .orElseThrow(() -> new AppException(ErrorCode.CONTENT_NO_EXISTS));

        if (!application.getUser().getUser_id().equals(userId)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        applicationsMapper.updateApplications(application, request);
        String methodId;
        if (request.getApplication_type() == ApplicationTypeEnum.UNDERGRADUATE) {
            methodId = request.getMethodId();
            if (methodId == null || methodId.isEmpty()) {
                throw new AppException(ErrorCode.INVALID_REQUEST);
            }
            if (!methodId.matches("^[1-3]$")) {
                throw new AppException(ErrorCode.INVALID_REQUEST);
            }
        } else {
            methodId = "POSTGRAD";
        }

        Methods method = methodsRepository.findByMethod_id(methodId)
                .orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND));
        application.setAdmission_method(method);

        application.setUpdate_date(new Date());
        Applications saved = applicationsRepository.saveAndFlush(application);

        ApplicationsResponse response = applicationsMapper.toApplicationsResponse(saved);
        response.setUserId(saved.getUser() != null ? saved.getUser().getUser_id() : null);
        response.setMethodId(saved.getAdmission_method() != null ? saved.getAdmission_method().getMethod_id() : null);

        return response;
    }

    @Transactional
    public void deleteApplication(String applicationId) {
        Applications application = applicationsRepository.findById(applicationId)
                .orElseThrow(() -> new AppException(ErrorCode.CONTENT_NO_EXISTS));
        String userId = authenticationService.getAuthenticatedUserId();
        if (!application.getUser().getUser_id().equals(userId)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
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