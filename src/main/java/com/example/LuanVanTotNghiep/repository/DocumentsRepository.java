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

    @Query("SELECT d FROM Documents d WHERE d.application.application_id = :applicationId")
    List<Documents> findByApplicationId(@Param("applicationId") String applicationId);

    @Query("SELECT d FROM Documents d WHERE d.application.application_id = :applicationId AND d.document_id = :documentId")
    Optional<Documents> findByApplicationIdAndDocumentId(@Param("applicationId") String applicationId, @Param("documentId") int documentId);
}
