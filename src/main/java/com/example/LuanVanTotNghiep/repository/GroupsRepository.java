package com.example.LuanVanTotNghiep.repository;

import com.example.LuanVanTotNghiep.entity.Groups;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupsRepository extends JpaRepository<Groups,String> {
    boolean existsByContent(String content);
}
