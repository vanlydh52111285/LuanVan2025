package com.example.LuanVanTotNghiep.service;

import com.example.LuanVanTotNghiep.dto.request.ProgramsRequest;
import com.example.LuanVanTotNghiep.dto.response.ProgramsResponse;
import com.example.LuanVanTotNghiep.entity.Programs;
import com.example.LuanVanTotNghiep.exception.AppException;
import com.example.LuanVanTotNghiep.exception.ErrorCode;
import com.example.LuanVanTotNghiep.mapper.ProgramsMapper;
import com.example.LuanVanTotNghiep.repository.ProgramsRepository;
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
public class ProgramsService {
    ProgramsRepository programsRepository;
    ProgramsMapper programsMapper;
    public ProgramsResponse createProgram(ProgramsRequest request){
        if(programsRepository.existsById(request.getProgram_id())){
            throw new AppException(ErrorCode.PROGRAMID_EXISTS);
        }
        if (programsRepository.existsByProgramname(request.getProgramname())){
            throw new AppException(ErrorCode.PROGRAMNAME_EXISTS);
        }
        Programs programs = programsMapper.toCreateProgram(request);
        return programsMapper.toProgramResponse(programsRepository.save(programs));
    }
    public List<ProgramsResponse> getAllPrograms(){
        List<ProgramsResponse> list = programsMapper.listPrograms(programsRepository.findAll());
        return list;
    }
    public List<ProgramsResponse> getAllProgramsTrue(){
        List<ProgramsResponse> list = programsMapper.listPrograms(programsRepository.findProgramsByTypeTrue());
        return list;
    }
//    public ProgramsResponse updateProgram(String id, ProgramsRequest request){
//        if (programsRepository.existsById(id)){
//            throw new AppException(ErrorCode.CONSTRAINT_VIOLATION);
//        }
//    }
}
