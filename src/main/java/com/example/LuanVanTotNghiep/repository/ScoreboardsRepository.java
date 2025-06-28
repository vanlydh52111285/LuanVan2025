package com.example.LuanVanTotNghiep.repository;

import com.example.LuanVanTotNghiep.entity.Scoreboards;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ScoreboardsRepository extends JpaRepository<Scoreboards, String> {
    @Query("SELECT s FROM Scoreboards s WHERE s.applications.application_id = :applicationId")
    Optional<Scoreboards> findByApplicationId(@Param("applicationId") String applicationId);
}
