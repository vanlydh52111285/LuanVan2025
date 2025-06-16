package com.example.LuanVanTotNghiep.repository;

import com.example.LuanVanTotNghiep.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, String> {
   boolean existsByFullname(String fullname);
   Optional<Users> findByFullname(String fullname);
   boolean existsByEmail(String email);
   Optional<Users> findByEmail(String email);

}
