package com.example.LuanVanTotNghiep.repository;

import com.example.LuanVanTotNghiep.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, String> {
   boolean existsByFullname(String fullname);
   Optional<Users> findByFullname(String fullname);
   boolean existsByEmail(String email);
   Optional<Users> findByEmail(String email);
   @Query("SELECT u FROM Users u WHERE u.user_id = :userId")
   Users findByUsersId(@Param("userId") String user_id);
}
