package com.example.LuanVanTotNghiep.repository;

import com.example.LuanVanTotNghiep.entity.Schedules;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface SchedulesRepository extends JpaRepository<Schedules, String> {
    boolean existsBySchedulename(String schedulename);
    @Query("SELECT MAX(s.end_time) FROM Schedules s")
    Date findMaxEndTime();
    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END " +
            "FROM Schedules s " +
            "WHERE :currentTime BETWEEN s.star_time AND s.end_time")
    boolean isCurrentTimeInScheduleRange(@Param("currentTime") Date currentTime);
    @Query("SELECT CASE WHEN COUNT(pbs) > 0 THEN true ELSE false END " +
            "FROM Programs_Branchs_Schedules pbs " +
            "WHERE pbs.branch.branch_id = :branchId " +
            "AND pbs.schedule.star_time <= :currentTime " +
            "AND pbs.schedule.end_time >= :currentTime")
    boolean isBranchInActiveSchedule(@Param("branchId") String branchId, @Param("currentTime") Date currentTime);
}
