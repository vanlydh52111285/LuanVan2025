package com.example.LuanVanTotNghiep.repository;

import com.example.LuanVanTotNghiep.entity.Documents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentsRepository extends JpaRepository<Documents, Integer> {

    @Query("SELECT d FROM Documents d WHERE d.application IS NULL AND d.user.user_id = :userId")
    List<Documents> findByApplicationIsNullAndUserId(@Param("userId") String userId);

    @Query("SELECT d FROM Documents d WHERE d.user.user_id = :userId AND d.application.application_id = :applicationId")
    List<Documents> findByUserIdAndApplicationId(@Param("userId") String userId, @Param("applicationId") String applicationId);

    @Query("SELECT d FROM Documents d WHERE d.document_id = :documentId AND d.user.user_id = :userId")
    Optional<Documents> findByDocumentIdAndUserId(@Param("documentId") Integer documentId, @Param("userId") String userId);
}
