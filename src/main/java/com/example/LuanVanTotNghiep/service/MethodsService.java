package com.example.LuanVanTotNghiep.service;

import com.example.LuanVanTotNghiep.dto.request.MethodsRequest;
import com.example.LuanVanTotNghiep.dto.response.MethodResponse;
import com.example.LuanVanTotNghiep.entity.Methods;
import com.example.LuanVanTotNghiep.exception.AppException;
import com.example.LuanVanTotNghiep.exception.ErrorCode;
import com.example.LuanVanTotNghiep.mapper.MethodsMapper;
import com.example.LuanVanTotNghiep.repository.MethodsRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
public class MethodsService {
    MethodsRepository methodsRepository;
    MethodsMapper methodsMapper;

    @Transactional
    public MethodResponse createMethod(MethodsRequest request) {
        log.info("Creating method with request: {}", request);
        if (methodsRepository.existsById(request.getMethod_id())) {
            throw new AppException(ErrorCode.DATA_ALREADY_EXISTS);
        }
        Methods methodd = methodsMapper.toCreateMethod(request);
        Methods saved = methodsRepository.save(methodd);
        log.info("Created method: {}", saved);
        return methodsMapper.toMethodResponse(saved);
    }

    @Transactional
    public MethodResponse updateMethod(String methodId, MethodsRequest request) {
        log.info("Updating method ID: {} with request: {}", methodId, request);
        Methods method = methodsRepository.findByMethod_id(methodId)
                .orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND));

        // Đảm bảo method_id không bị thay đổi
        if (method.getMethod_id() == null) {
            throw new AppException(ErrorCode.INVALID_STATE);
        }

        methodsMapper.updateMethod(method, request);

        // Kiểm tra lại method_id trước khi lưu
        if (method.getMethod_id() == null || !method.getMethod_id().equals(methodId)) {
            method.setMethod_id(methodId); // Gán lại từ path
        }

        Methods updated = methodsRepository.save(method);
        log.info("Updated method: id={}, method_name={}", updated.getMethod_id(), updated.getMethod_name());
        return methodsMapper.toMethodResponse(updated);
    }

    @Transactional
    public void deleteMethod(String methodId) {
        log.info("Deleting method with ID: {}", methodId);
        Methods methodd = methodsRepository.findByMethod_id(methodId)
                .orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND));
        methodsRepository.delete(methodd);
        log.info("Deleted method with ID: {}", methodId);
    }

    public MethodResponse getMethodById(String methodId) {
        log.info("Fetching method with ID: {}", methodId);
        Methods method = methodsRepository.findByMethod_id(methodId)
                .orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND));
        return methodsMapper.toMethodResponse(method);
    }

    public List<MethodResponse> getAllMethods() {
        log.info("Fetching all methods");
        List<Methods> methods = methodsRepository.findAll();
        if (methods.isEmpty()) {
            throw new AppException(ErrorCode.DATA_NOT_FOUND);
        }
        return methodsMapper.listMethod(methods);
    }
}
