package com.example.LuanVanTotNghiep.repository;

import com.example.LuanVanTotNghiep.entity.Schools;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SchoolsRepository extends JpaRepository<Schools, String> {

    @Query("SELECT s FROM Schools s WHERE s.province.province_id = :province_id")
    List<Schools> findByProvinces_ProvinceId(@Param("province_id") String province_id);
}
