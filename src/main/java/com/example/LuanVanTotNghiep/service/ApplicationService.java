package com.example.LuanVanTotNghiep.service;

import com.example.LuanVanTotNghiep.entity.Applications;
import com.example.LuanVanTotNghiep.enums.ApplicationTypeEnum;
import com.example.LuanVanTotNghiep.repository.ApplicationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Year;

@Service
public class ApplicationService {
    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private DocumentService documentService;

    public String generateApplicationId(ApplicationTypeEnum type, Integer methodId) {
        String prefix = type == ApplicationTypeEnum.UNDERGRADUATE ? "DH" : "SDH";
        String year = String.valueOf(Year.now().getValue());
        int maxSequence = type == ApplicationTypeEnum.UNDERGRADUATE ? 3000 : 1000;

        if (type == ApplicationTypeEnum.UNDERGRADUATE) {
            if (methodId == null || methodId < 1 || methodId > 3) {
                throw new IllegalArgumentException("Method ID must be 1, 2, or 3 for UNDERGRADUATE.");
            }
        }

        String searchPattern = type == ApplicationTypeEnum.UNDERGRADUATE ? prefix + year + methodId + "%" : prefix + year + "%";
        int sequence = applicationRepository.countByApplicationIdLike(searchPattern).intValue() + 1;

        if (sequence > maxSequence) {
            throw new IllegalStateException("Maximum applications reached for " + type);
        }

        return type == ApplicationTypeEnum.UNDERGRADUATE
                ? String.format("%s%s%d%04d", prefix, year, methodId, sequence)
                : String.format("%s%s%04d", prefix, year, sequence);
    }

    @Transactional
    public Applications saveApplication(Applications application) {
        ApplicationTypeEnum type = application.getApplication_type();
        if (type == ApplicationTypeEnum.UNDERGRADUATE) {
            if (application.getAdmission_method() == null || application.getAdmission_method().getMethod_id() == 0) {
                throw new IllegalArgumentException("Admission method is required for UNDERGRADUATE.");
            }
        } else {
            application.setAdmission_method(null);
        }

        Integer methodId = type == ApplicationTypeEnum.UNDERGRADUATE ? application.getAdmission_method().getMethod_id() : null;
        application.setApplication_id(generateApplicationId(type, methodId));
        application.setCreate_date(new java.util.Date());

        Applications savedApplication = applicationRepository.save(application);
        documentService.updateDocumentsForApplication(savedApplication);

        return savedApplication;
    }
}
