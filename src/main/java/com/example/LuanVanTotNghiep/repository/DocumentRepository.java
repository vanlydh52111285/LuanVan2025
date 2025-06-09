package com.example.LuanVanTotNghiep.repository;

import com.example.LuanVanTotNghiep.entity.Documents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Documents, Integer> {

    @Query("SELECT d FROM Documents d WHERE d.application IS NULL AND d.user.user_id = :userId")
    List<Documents> findByApplicationIsNullAndUserId(@Param("userId") Long userId);

    @Query("SELECT d FROM Documents d WHERE d.user.user_id = :userId AND d.application.application_id = :applicationId")
    List<Documents> findByUserIdAndApplicationId(@Param("userId") Long userId, @Param("applicationId") String applicationId);
}
