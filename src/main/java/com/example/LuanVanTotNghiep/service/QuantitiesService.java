package com.example.LuanVanTotNghiep.service;

import com.example.LuanVanTotNghiep.dto.request.QuantitiesRequest;
import com.example.LuanVanTotNghiep.dto.response.Branchs_QuantitiesResponse;
import com.example.LuanVanTotNghiep.dto.response.QuantitiesResponse;
import com.example.LuanVanTotNghiep.entity.Branchs;
import com.example.LuanVanTotNghiep.entity.Quantities;
import com.example.LuanVanTotNghiep.exception.AppException;
import com.example.LuanVanTotNghiep.exception.ErrorCode;
import com.example.LuanVanTotNghiep.mapper.BranchsMapper;
import com.example.LuanVanTotNghiep.mapper.QuantitiesMapper;
import com.example.LuanVanTotNghiep.repository.BranchsRepository;
import com.example.LuanVanTotNghiep.repository.QuantitiesRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class QuantitiesService {
    QuantitiesRepository quantitiesRepository;
    QuantitiesMapper quantitiesMapper;
    BranchsRepository branchsRepository;
    BranchsMapper branchsMapper;
    @Transactional
    public QuantitiesResponse createQuantity(QuantitiesRequest request) {
        if (request.getBranch_id().isEmpty()) {
            throw new AppException(ErrorCode.DATA_NOT_FOUND);
        }
        if (quantitiesRepository.existByYears(request.getBranch_id(), request.getAdmission_year())) {
            throw new AppException(ErrorCode.QUANTITY_EXISTS);
        }

        // Tìm entity Branchs từ branch_id
        Branchs branchs = branchsRepository.findById(request.getBranch_id())
                .orElseThrow(() -> new AppException(ErrorCode.BRANCHID_EXISTS));

        // Tạo Quantities từ request
        Quantities quantities = quantitiesMapper.toCreateQuantity(request);

        // Khởi tạo branchs nếu cần
        if (quantities.getBranchs() == null) {
            quantities.setBranchs(new HashSet<>());
        }

        // Tải đầy đủ collection branchs (nếu cần)
        Hibernate.initialize(quantities.getBranchs());

        log.info("Before adding branchs: {}", quantities.getBranchs());
        quantities.getBranchs().add(branchs);
        log.info("After adding branchs: {}", quantities.getBranchs());

        // Lưu Quantities vào database
        Quantities savedQuantities = quantitiesRepository.save(quantities);

        // Ánh xạ sang QuantitiesResponse để trả về
        return quantitiesMapper.toQuantityResponse(savedQuantities);
    }
    public List<QuantitiesResponse> getAllQuantities(){
        List<QuantitiesResponse> list = quantitiesMapper.listQuantities(quantitiesRepository.findAll());
        return list;
    }
}
