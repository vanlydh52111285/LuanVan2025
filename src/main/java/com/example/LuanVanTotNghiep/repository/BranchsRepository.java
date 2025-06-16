package com.example.LuanVanTotNghiep.repository;

import com.example.LuanVanTotNghiep.entity.Branchs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface BranchsRepository extends JpaRepository<Branchs, String> {
    boolean existsByBranchname(String branchname);

    @Query("SELECT b FROM Branchs b LEFT JOIN FETCH b.groups WHERE b.branch_id = :branchId")
    Optional<Branchs> findByIdWithGroups(@Param("branchId") String branchId);


}