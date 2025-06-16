package com.example.LuanVanTotNghiep.service;

import com.example.LuanVanTotNghiep.dto.request.ProgramsRequest;
import com.example.LuanVanTotNghiep.dto.response.ProgramsResponse;
import com.example.LuanVanTotNghiep.entity.Programs;
import com.example.LuanVanTotNghiep.exception.AppException;
import com.example.LuanVanTotNghiep.exception.ErrorCode;
import com.example.LuanVanTotNghiep.mapper.ProgramsMapper;
import com.example.LuanVanTotNghiep.repository.ProgramRepository;
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
    ProgramRepository programRepository;
    ProgramsMapper programsMapper;
    public ProgramsResponse createProgram(ProgramsRequest request){
        if(programRepository.existsById(request.getProgram_id())){
            throw new AppException(ErrorCode.PROGRAMID_EXISTS);
        }
        if (programRepository.existsByProgramname(request.getProgramname())){
            throw new AppException(ErrorCode.PROGRAMNAME_EXISTS);
        }
        Programs programs = programsMapper.toCreateProgram(request);
        return programsMapper.toProgramResponse(programRepository.save(programs));
    }
    public List<ProgramsResponse> getAllPrograms(){
        List<ProgramsResponse> list = programsMapper.listPrograms(programRepository.findAll());
        return list;
    }
}
