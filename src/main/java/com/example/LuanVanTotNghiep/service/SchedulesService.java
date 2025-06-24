package com.example.LuanVanTotNghiep.service;

import com.example.LuanVanTotNghiep.dto.request.Programs_Branchs_SchedulesRequest;
import com.example.LuanVanTotNghiep.dto.request.SchedulesRequest;
import com.example.LuanVanTotNghiep.dto.response.Programs_Branchs_SchedulesResponse;
import com.example.LuanVanTotNghiep.dto.response.SchedulesResponse;
import com.example.LuanVanTotNghiep.entity.Branchs;
import com.example.LuanVanTotNghiep.entity.Programs;
import com.example.LuanVanTotNghiep.entity.Programs_Branchs_Schedules;
import com.example.LuanVanTotNghiep.entity.Schedules;
import com.example.LuanVanTotNghiep.exception.AppException;
import com.example.LuanVanTotNghiep.exception.ErrorCode;
import com.example.LuanVanTotNghiep.mapper.Programs_Branchs_SchedulesMapper;
import com.example.LuanVanTotNghiep.mapper.SchedulesMapper;
import com.example.LuanVanTotNghiep.repository.BranchsRepository;
import com.example.LuanVanTotNghiep.repository.ProgramsRepository;
import com.example.LuanVanTotNghiep.repository.Programs_Branchs_SchedulesRepository;
import com.example.LuanVanTotNghiep.repository.SchedulesRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class SchedulesService {
    SchedulesRepository schedulesRepository;
    SchedulesMapper schedulesMapper;
    BranchsRepository branchsRepository;
    ProgramsRepository programsRepository;
    Programs_Branchs_SchedulesMapper programsBranchsSchedulesMapper;
    Programs_Branchs_SchedulesRepository programsBranchsSchedulesRepository;

    public SchedulesResponse createSchedule(SchedulesRequest request) {
        // Kiểm tra schedulename tồn tại
        if (schedulesRepository.existsBySchedulename(request.getSchedulename())) {
            throw new AppException(ErrorCode.SCHEDULENAME_EXISTS);
        }

        // Kiểm tra star_time và end_time
        Date starTime = request.getStar_time();
        Date endTime = request.getEnd_time();
        if (starTime == null || endTime == null || !isValidTimeRange(starTime, endTime)) {
            throw new AppException(ErrorCode.INVALID_TIME_RANGE);
        }

        // Kiểm tra star_time > max(end_time) + 2 days
        Date maxEndTime = schedulesRepository.findMaxEndTime();
        if (maxEndTime != null && !isStarTimeValid(starTime, maxEndTime)) {
            throw new AppException(ErrorCode.INVALID_START_TIME);
        }

        Schedules schedules = schedulesMapper.tocreateSchedule(request);
        return schedulesMapper.toScheduleResponse(schedulesRepository.save(schedules));
    }

    @Transactional
    public List<Programs_Branchs_SchedulesResponse> createListPBS(List<Programs_Branchs_SchedulesRequest> requests) {
        if (requests == null || requests.isEmpty()) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }

        List<Programs_Branchs_Schedules> list = requests.stream().map(request -> {
            Programs_Branchs_Schedules pbs = programsBranchsSchedulesMapper.toCreateProgramsBranchsSchedules(request);

            // Tìm và gán thực thể Program
            if (request.getProgram_id() == null) {
                throw new AppException(ErrorCode.PROGRAMID_NOT_FOUND);
            }
            Programs program = programsRepository.findById(request.getProgram_id())
                    .orElseThrow(() -> new AppException(ErrorCode.PROGRAMID_NOT_FOUND));
            pbs.setProgram(program);

            // Tìm và gán thực thể Branch
            if (request.getBranch_id() == null) {
                throw new AppException(ErrorCode.BRANCHID_NOT_FOUND);
            }
            Branchs branch = branchsRepository.findById(request.getBranch_id())
                    .orElseThrow(() -> new AppException(ErrorCode.BRANCHID_NOT_FOUND));
            pbs.setBranch(branch);

            // Tìm và gán thực thể Schedule
            if (request.getSchedule_id() == null) {
                throw new AppException(ErrorCode.SCHEDULE_NOT_FOUND);
            }
            Schedules schedule = schedulesRepository.findById(request.getSchedule_id())
                    .orElseThrow(() -> new AppException(ErrorCode.SCHEDULE_NOT_FOUND));
            pbs.setSchedule(schedule);

            return pbs;
        }).collect(Collectors.toList());

        // Lưu danh sách thực thể
        List<Programs_Branchs_Schedules> savedList = programsBranchsSchedulesRepository.saveAll(list);
        return programsBranchsSchedulesMapper.listProgramsBranchsSchedulesResponse(savedList);
    }

    @Transactional(readOnly = true)
    public List<Programs_Branchs_SchedulesResponse> getProgramsBranchsSchedules(String id) {
        if (!programsBranchsSchedulesRepository.existsByScheduleId(id)) {
            throw new AppException(ErrorCode.SCHEDULE_NOT_FOUND);
        }
        return programsBranchsSchedulesMapper.listProgramsBranchsSchedulesResponse(
                programsBranchsSchedulesRepository.findByScheduleId(id));
    }

    @Transactional(readOnly = true)
    public List<SchedulesResponse> getAllSchedules() {

        return schedulesMapper.listSchedulesResponse(schedulesRepository.findAll());
    }

    private boolean isValidTimeRange(Date starTime, Date endTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(starTime);
        calendar.add(Calendar.DAY_OF_MONTH, 7);
        return calendar.getTime().before(endTime) || calendar.getTime().equals(endTime);
    }

    private boolean isStarTimeValid(Date starTime, Date maxEndTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(maxEndTime);
        calendar.add(Calendar.DAY_OF_MONTH, 2);
        return starTime.after(calendar.getTime());
    }
}