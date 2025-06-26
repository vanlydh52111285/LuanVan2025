package com.example.LuanVanTotNghiep.repository;

import com.example.LuanVanTotNghiep.entity.Districts;
import com.example.LuanVanTotNghiep.entity.Provinces;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProvincesRepository extends JpaRepository<Provinces, String> {
    @Query("SELECT p FROM Provinces p WHERE p.province_id = :province_id")
    Provinces findByProvinceId(@Param("province_id") String provinceId);
}
