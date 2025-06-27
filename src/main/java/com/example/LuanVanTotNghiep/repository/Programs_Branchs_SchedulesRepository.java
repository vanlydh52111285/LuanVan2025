package com.example.LuanVanTotNghiep.repository;

import com.example.LuanVanTotNghiep.entity.Programs_Branchs_Schedules;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Programs_Branchs_SchedulesRepository extends JpaRepository<Programs_Branchs_Schedules , String> {
    @Query("SELECT pbs FROM Programs_Branchs_Schedules pbs " +
            "JOIN FETCH pbs.program p " +
            "JOIN FETCH pbs.branch b " +
            "JOIN FETCH pbs.schedule s " +
            "WHERE pbs.schedule.schedule_id = :scheduleId " +
            "AND b.status = true " +
            "AND p.type = true")
    List<Programs_Branchs_Schedules> findByScheduleId(@Param("scheduleId") String scheduleId);
    @Query("SELECT CASE WHEN COUNT(pbs) > 0 THEN true ELSE false END FROM Programs_Branchs_Schedules pbs WHERE pbs.schedule.schedule_id = :scheduleId")
    boolean existsByScheduleId(@Param("scheduleId") String scheduleId);

}
