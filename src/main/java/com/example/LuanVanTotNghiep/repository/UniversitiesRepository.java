package com.example.LuanVanTotNghiep.repository;

import com.example.LuanVanTotNghiep.entity.Universities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UniversitiesRepository extends JpaRepository<Universities, String> {
    boolean existsByUniversityfullname(String universityfullname);
    boolean existsByUniversityname(String universityname);

}
