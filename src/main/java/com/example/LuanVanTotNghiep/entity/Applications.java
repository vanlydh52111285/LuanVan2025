package com.example.LuanVanTotNghiep.entity;

import com.example.LuanVanTotNghiep.enums.ApplicationEnum;
import com.example.LuanVanTotNghiep.enums.ApplicationTypeEnum;
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
@Table(name = "applications")
public class Applications {

    @Id
    String application_id;

    @Enumerated(EnumType.STRING)
    ApplicationTypeEnum application_type;

    @Enumerated(EnumType.STRING)
    ApplicationEnum status;
    int priority;
    float total_score;
    Date create_date;
    Date update_date;
    String notes;

    @ManyToOne
    @JoinColumn(name = "user_id")
    Users user;

    @ManyToOne
    @JoinColumn(name = "method_id", nullable = false)
    Methods admission_method;

    @OneToMany(mappedBy = "document_id", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Documents> documentsList;
    // Thêm mối quan hệ N-N với Aspirations
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "applications_aspirations",
            joinColumns = @JoinColumn(name = "application_id"),
            inverseJoinColumns = @JoinColumn(name = "aspiration_id")
    )
    Set<Aspirations> aspirations = new HashSet<>();
    public Applications(String applicationId) {
    }
}
