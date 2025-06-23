package com.example.LuanVanTotNghiep.repository;

import com.example.LuanVanTotNghiep.entity.Groups;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupsRepository extends JpaRepository<Groups,String> {
    boolean existsByGroupname(String groupname);
    @Query("SELECT g FROM Groups g WHERE g.type = true")
    List<Groups> findGroupsByTypeTrue();
    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END FROM Branchs b JOIN b.groups g WHERE g.group_id = :groupId")
    boolean existsByGroupId(@Param("groupId") String groupId);
}
