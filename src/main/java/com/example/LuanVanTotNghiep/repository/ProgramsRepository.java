package com.example.LuanVanTotNghiep.repository;

import com.example.LuanVanTotNghiep.entity.Programs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProgramsRepository extends JpaRepository<Programs, String> {

    boolean existsByProgramname(String programname);
    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END FROM Branchs b JOIN b.programs p WHERE p.program_id = :programId")
    boolean existsByProgramIdInBranchs(@Param("programId") String programId);
    @Query("SELECT p FROM Programs p WHERE p.type = true")
    List<Programs> findProgramsByTypeTrue();
}
