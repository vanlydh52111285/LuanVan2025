package com.example.LuanVanTotNghiep.repository;

import com.example.LuanVanTotNghiep.entity.Aspirations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AspirationsRepository extends JpaRepository<Aspirations, String> {
}
