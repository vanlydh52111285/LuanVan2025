package com.example.LuanVanTotNghiep.repository;

import com.example.LuanVanTotNghiep.entity.Applications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationsRepository extends JpaRepository<Applications, String> {
    @Query("SELECT COUNT(a) FROM Applications a WHERE a.application_id LIKE :pattern")
    Long countByApplicationIdLike(String pattern);


    @Query("SELECT a FROM Applications a WHERE a.user.user_id = :userId")
    List<Applications> findByUserId(@Param("userId") String userId);
}
