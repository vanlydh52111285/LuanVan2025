package com.example.LuanVanTotNghiep.mapper;

import com.example.LuanVanTotNghiep.dto.request.DocumentsRequest;
import com.example.LuanVanTotNghiep.dto.response.DocumentsResponse;
import com.example.LuanVanTotNghiep.entity.Applications;
import com.example.LuanVanTotNghiep.entity.Documents;
import com.example.LuanVanTotNghiep.entity.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DocumentsMapper {
    Documents toCreateDocuments(DocumentsRequest request);
    DocumentsResponse toDocumentsResponse(Documents documents);
    void updateDocuments(@MappingTarget Documents documents, DocumentsRequest request);
    List<DocumentsResponse> listDocuments(List<Documents> documentsList);
}