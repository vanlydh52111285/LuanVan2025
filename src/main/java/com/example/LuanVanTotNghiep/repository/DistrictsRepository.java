package com.example.LuanVanTotNghiep.repository;

import com.example.LuanVanTotNghiep.entity.Districts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DistrictsRepository extends JpaRepository<Districts, String> {
    @Query("SELECT d FROM Districts d WHERE d.province.province_id = :province_id")
    List<Districts> findByProvince_ProvinceId(@Param("province_id") String provinceId);
    @Query("SELECT d FROM Districts d WHERE d.district_id = :districtId")
    Districts findBydistrictId(@Param("districtId") String districts_id);
}
