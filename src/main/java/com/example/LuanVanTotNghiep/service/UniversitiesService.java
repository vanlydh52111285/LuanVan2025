package com.example.LuanVanTotNghiep.service;

import com.example.LuanVanTotNghiep.dto.request.UniversitiesRequest;
import com.example.LuanVanTotNghiep.dto.response.UniversitiesResponse;
import com.example.LuanVanTotNghiep.entity.Universities;
import com.example.LuanVanTotNghiep.exception.AppException;
import com.example.LuanVanTotNghiep.exception.ErrorCode;
import com.example.LuanVanTotNghiep.mapper.UniversitiesMapper;
import com.example.LuanVanTotNghiep.repository.UniversitiesRepository;
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
public class UniversitiesService {
    UniversitiesRepository universitiesRepository;
    UniversitiesMapper universitiesMapper;
    public UniversitiesResponse createUniversity(UniversitiesRequest request){
        if(universitiesRepository.existsById(request.getUniversity_id())){
            throw new AppException(ErrorCode.UNIVERSITYID_EXISTS);
        }
        if (universitiesRepository.existsByUniversityfullname(request.getUniversityfullname())){
            throw new AppException(ErrorCode.UNIVERSITYFULLNAME_EXISTS);
        }
        if (universitiesRepository.existsByUniversityname(request.getUniversityname())){
            throw new AppException(ErrorCode.UNIVERSITYNAME_EXISTS);
        }
        Universities universities = universitiesMapper.toCreateUniversity(request);

        return universitiesMapper.toUniversityResponse(universitiesRepository.save(universities));
    }
    public List<UniversitiesResponse> getUniversity(){
        List<UniversitiesResponse> list = universitiesMapper.listUniversities(universitiesRepository.findAll());
        return list;
    }
}
