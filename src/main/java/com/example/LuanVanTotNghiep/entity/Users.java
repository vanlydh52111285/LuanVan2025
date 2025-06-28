package com.example.LuanVanTotNghiep.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;


import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String user_id;
    @Column(name = "full_name")
    String fullname;
    Date date_of_brith;
    String id_number;
    String phone_number;
    String email;
    String password;
    Date created_at;
    Set<String> role;
    boolean sex;
    boolean is_verify;
    String otp;
    String adress;
    // Thêm mối quan hệ 1-N với Provinces
    @ManyToOne
    @JoinColumn(name = "province_id", nullable = false)
    Provinces province;

    // Thêm mối quan hệ 1-N với Districts
    @ManyToOne
    @JoinColumn(name = "district_id", nullable = false)
    Districts district;

    @OneToMany(mappedBy = "application_id", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Applications> applicationList;

    @OneToMany(mappedBy = "document_id", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Documents> documentsList;

    public Users(String userId) {
        super();
        this.user_id = userId;
    }

    @ManyToMany
    @JoinTable(
            name = "users_notifications",
            joinColumns = @JoinColumn(name = "notification_id"), // Khóa chính của Notifications
            inverseJoinColumns = @JoinColumn(name = "user_id") // Khóa chính của Users
    )
    Set<Notifications> notifications = new HashSet<>();


 }
