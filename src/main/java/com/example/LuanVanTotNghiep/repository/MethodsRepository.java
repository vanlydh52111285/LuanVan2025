package com.example.LuanVanTotNghiep.repository;

import com.example.LuanVanTotNghiep.entity.Methods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MethodsRepository extends JpaRepository<Methods, String> {
    @Query("SELECT m FROM Methods m WHERE m.method_id = :method_id")
    Optional<Methods> findByMethod_id(@Param("method_id") String method_id);

    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END FROM Methods m WHERE m.method_id = :method_id")
    boolean existsByMethod_id(@Param("method_id") String method_id);
}
