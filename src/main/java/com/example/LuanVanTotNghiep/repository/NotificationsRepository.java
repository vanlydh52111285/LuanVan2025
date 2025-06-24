package com.example.LuanVanTotNghiep.repository;

import com.example.LuanVanTotNghiep.entity.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationsRepository extends JpaRepository<Notifications, String> {
}
