package com.example.LuanVanTotNghiep.mapper;

import com.example.LuanVanTotNghiep.dto.request.SubjectsRequest;
import com.example.LuanVanTotNghiep.dto.response.SubjectResponse;
import com.example.LuanVanTotNghiep.entity.Subjects;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SubjectsMapper {
    Subjects toCreateSubjects(SubjectsRequest request);
    SubjectResponse toSubjectResponse(Subjects subjects);
    void updateSubjects(@MappingTarget Subjects subjects, SubjectsRequest request);
    List<SubjectResponse> listSubjects(List<Subjects> subjects);
}
