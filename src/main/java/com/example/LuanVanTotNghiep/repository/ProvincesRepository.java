package com.example.LuanVanTotNghiep.repository;

import com.example.LuanVanTotNghiep.entity.Districts;
import com.example.LuanVanTotNghiep.entity.Provinces;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProvincesRepository extends JpaRepository<Provinces, String> {

    @Query("SELECT COUNT(d) FROM Districts d WHERE d.province.province_id = :provinceId")
    long countDistrictsByProvinceId(@Param("provinceId") String provinceId);

    @Query("SELECT COUNT(s) FROM Schools s WHERE s.province.province_id = :provinceId")
    long countSchoolsByProvinceId(@Param("provinceId") String provinceId);

    @Query("SELECT p FROM Provinces p WHERE p.province_id = :province_id")
    Optional<Provinces> findByProvinceId(@Param("province_id") String provinceId);
}
