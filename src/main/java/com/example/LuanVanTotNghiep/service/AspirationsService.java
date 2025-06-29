package com.example.LuanVanTotNghiep.service;

import com.example.LuanVanTotNghiep.dto.request.AspirationsRequest;
import com.example.LuanVanTotNghiep.dto.response.AspirationsResponse;
import com.example.LuanVanTotNghiep.entity.Applications;
import com.example.LuanVanTotNghiep.entity.Aspirations;
import com.example.LuanVanTotNghiep.entity.Branchs;
import com.example.LuanVanTotNghiep.exception.AppException;
import com.example.LuanVanTotNghiep.exception.ErrorCode;
import com.example.LuanVanTotNghiep.mapper.AspirationsMapper;
import com.example.LuanVanTotNghiep.repository.ApplicationsRepository;
import com.example.LuanVanTotNghiep.repository.AspirationsRepository;
import com.example.LuanVanTotNghiep.repository.BranchsRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AspirationsService {
    AspirationsRepository aspirationsRepository;
    AspirationsMapper aspirationsMapper;
    BranchsRepository branchsRepository;
    ApplicationsRepository applicationsRepository;

    @Transactional
    public AspirationsResponse createAspirations(AspirationsRequest request) {
        if (request.getPriority_order() < 1) {
            throw new AppException(ErrorCode.INVALID_INPUT, "Priority order phải lớn hơn hoặc bằng 1");
        }

        Branchs branch = branchsRepository.findById(request.getBranch_id())
                .orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND, "Không tìm thấy ngành học với mã: " + request.getBranch_id()));

        Aspirations aspirations = aspirationsMapper.toCreateAspirations(request);
        aspirations.setBranch(branch);

        String applicationId = null;
        if (request.getApplication_id() != null && !request.getApplication_id().trim().isEmpty()) {
            Applications application = applicationsRepository.findById(request.getApplication_id())
                    .orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND, "Không tìm thấy đơn xét tuyển với mã: " + request.getApplication_id()));
            application.getAspirations().add(aspirations);
            aspirationsRepository.save(aspirations); // Lưu để đảm bảo aspiration_id được sinh
            applicationsRepository.save(application); // Cập nhật quan hệ
            applicationId = request.getApplication_id();
        } else {
            // Nếu không có application_id, chỉ lưu Aspirations
            aspirations = aspirationsRepository.save(aspirations);
        }

        AspirationsResponse response = aspirationsMapper.toAspirationsResponse(aspirations);
        response.setBranch_id(branch.getBranch_id());
        response.setApplication_id(applicationId);
        return response;
    }
}
