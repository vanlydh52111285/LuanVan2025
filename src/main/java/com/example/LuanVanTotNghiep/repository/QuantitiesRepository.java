package com.example.LuanVanTotNghiep.repository;

import com.example.LuanVanTotNghiep.entity.Quantities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface QuantitiesRepository extends JpaRepository<Quantities, String> {
    @Query("SELECT CASE WHEN COUNT(q) > 0 THEN true ELSE false END " +
            "FROM Quantities q JOIN q.branchs b " +
            "WHERE b.branch_id = :branch_id AND q.admission_year = :admission_year")
    boolean existByYears(@Param("branch_id") String branch_id, @Param("admission_year") int admission_year);
}
