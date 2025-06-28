package com.example.LuanVanTotNghiep.repository;

import com.example.LuanVanTotNghiep.entity.Districts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DistrictsRepository extends JpaRepository<Districts, String> {
    @Query("SELECT d FROM Districts d WHERE d.province.province_id = :province_id")
    List<Districts> findByProvince_ProvinceId(@Param("province_id") String provinceId);

    @Query("SELECT COUNT(d) > 0 FROM Districts d WHERE d.province.province_id = :provinceId")
    boolean existsByProvince_ProvinceIdAndDistrict_id(String provinceId);

    @Query("SELECT d FROM Districts d WHERE d.district_id = :districtId")
    Optional<Districts> findBydistrictId(@Param("districtId") String districts_id);
}
