package com.example.LuanVanTotNghiep.repository;

import com.example.LuanVanTotNghiep.entity.Branchs;
import com.example.LuanVanTotNghiep.entity.Groups;
import com.example.LuanVanTotNghiep.entity.Programs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface BranchsRepository extends JpaRepository<Branchs, String> {
    boolean existsByBranchname(String branchname);

    @Query("SELECT b FROM Branchs b  WHERE b.branch_id = :branchId")
    Optional<Branchs> findByIdWithGroups(@Param("branchId") String branchId);
    @Query("SELECT b FROM Branchs b  WHERE b.branch_id = :branchId")
    Optional<Branchs> findByIdWithPrograms(@Param("branchId") String branchId);
    @Query("SELECT p FROM Branchs b JOIN b.programs p WHERE b.branch_id = :branchId")
    List<Programs> findByProgramsByBranchId(String branchId);
    @Query("SELECT g FROM Branchs b JOIN b.groups g WHERE b.branch_id = :branchId")
    List<Groups> findByGroupsByBranchId(String branchId);
    @Query("SELECT b FROM Branchs b JOIN b.programs p WHERE p.program_id = :programId")
    List<Branchs> findBranchsByProgramId(@Param("programId") String programId);
    @Query("SELECT b FROM Branchs b " +
            "JOIN b.programs p " +
            "JOIN b.groups g " +
            "WHERE p.program_id = :programId")
    List<Branchs> findBranchsByProgramIdWithAtLeastOneGroup(@Param("programId") String programId);
    @Query("SELECT DISTINCT b FROM Branchs b " +
            "JOIN FETCH b.groups g " +
            "JOIN FETCH b.programs p " +
            "WHERE b.type = true " +
            "AND g.type = true " +
            "AND p.type = true "
            )
    List<Branchs> findBranchesWithAtLeastOneGroupAndProgram();
}