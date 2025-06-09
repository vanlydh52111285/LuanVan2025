package com.example.LuanVanTotNghiep.repository;

import com.example.LuanVanTotNghiep.entity.Applications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ApplicationRepository extends JpaRepository<Applications, String> {
    @Query("SELECT COUNT(a) FROM Applications a WHERE a.application_id LIKE :pattern")
    Long countByApplicationIdLike(String pattern);
}
