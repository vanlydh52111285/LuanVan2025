package com.example.LuanVanTotNghiep.repository;

import com.example.LuanVanTotNghiep.entity.ScoreboardSubject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScoreboardSubjectRepository extends JpaRepository<ScoreboardSubject, String> {
}
