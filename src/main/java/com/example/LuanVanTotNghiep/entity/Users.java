package com.example.LuanVanTotNghiep.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;


import java.util.Date;
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

    @OneToMany(mappedBy = "application_id", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Applications> applicationList;

    @OneToMany(mappedBy = "document_id", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Documents> documentsList;

    public Users(String userId) {
        super();
        this.user_id = userId;
    }
}
