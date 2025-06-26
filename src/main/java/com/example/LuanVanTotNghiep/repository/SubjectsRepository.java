package com.example.LuanVanTotNghiep.repository;

import com.example.LuanVanTotNghiep.entity.Subjects;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectsRepository extends JpaRepository<Subjects, String> {
}
