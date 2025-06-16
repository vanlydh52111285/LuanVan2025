package com.example.LuanVanTotNghiep.repository;

import com.example.LuanVanTotNghiep.entity.Programs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgramRepository extends JpaRepository<Programs, String> {
    boolean existsByProgramname(String programname);
}
