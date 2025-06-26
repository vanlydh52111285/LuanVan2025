package com.example.LuanVanTotNghiep.repository;

import com.example.LuanVanTotNghiep.entity.Provinces;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProvincesRepository extends JpaRepository<Provinces, String> {

    @Query("SELECT COUNT(d) FROM Districts d WHERE d.province.province_id = :provinceId")
    long countDistrictsByProvinceId(@Param("provinceId") String provinceId);

    @Query("SELECT COUNT(s) FROM Schools s WHERE s.province.province_id = :provinceId")
    long countSchoolsByProvinceId(@Param("provinceId") String provinceId);
}
