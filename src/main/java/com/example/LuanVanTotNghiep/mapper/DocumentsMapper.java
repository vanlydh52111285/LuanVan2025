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
    @Mapping(target = "document_id", ignore = true)
    @Mapping(target = "document_link_cccd", ignore = true)
    @Mapping(target = "document_link_hoc_ba_lop10", ignore = true)
    @Mapping(target = "document_link_hoc_ba_lop11", ignore = true)
    @Mapping(target = "document_link_hoc_ba_lop12", ignore = true)
    @Mapping(target = "document_link_bang_tot_nghiep_thpt", ignore = true)
    @Mapping(target = "document_link_ket_qua_thi_thpt", ignore = true)
    @Mapping(target = "document_link_ket_qua_thi_dgnl", ignore = true)
    @Mapping(target = "document_link_chung_chi_ngoai_ngu", ignore = true)
    @Mapping(target = "user",ignore = true)
    @Mapping(target = "application", expression = "java(request.getApplicationId() != null ? com.example.LuanVanTotNghiep.entity.Applications.builder().application_id(request.getApplicationId()).build() : null)")
    Documents toCreateDocuments(DocumentsRequest request);

    @Mapping(source = "document_id", target = "documentId")
    @Mapping(source = "document_link_cccd", target = "cccdUrl")
    @Mapping(source = "document_link_hoc_ba_lop10", target = "hocBaLop10Url")
    @Mapping(source = "document_link_hoc_ba_lop11", target = "hocBaLop11Url")
    @Mapping(source = "document_link_hoc_ba_lop12", target = "hocBaLop12Url")
    @Mapping(source = "document_link_bang_tot_nghiep_thpt", target = "bangTotNghiepThptUrl")
    @Mapping(source = "document_link_ket_qua_thi_thpt", target = "ketQuaThiThptUrl")
    @Mapping(source = "document_link_ket_qua_thi_dgnl", target = "ketQuaThiDgnlUrl")
    @Mapping(source = "document_link_chung_chi_ngoai_ngu", target = "chungChiNgoaiNguUrl")
    @Mapping(source = "user.user_id", target = "userId")
    @Mapping(source = "application.application_id", target = "applicationId", defaultExpression = "java(null)")
    DocumentsResponse toDocumentsResponse(Documents documents);

    @Mapping(target = "document_id", ignore = true)
    @Mapping(target = "document_link_cccd", ignore = true)
    @Mapping(target = "document_link_hoc_ba_lop10", ignore = true)
    @Mapping(target = "document_link_hoc_ba_lop11", ignore = true)
    @Mapping(target = "document_link_hoc_ba_lop12", ignore = true)
    @Mapping(target = "document_link_bang_tot_nghiep_thpt", ignore = true)
    @Mapping(target = "document_link_ket_qua_thi_thpt", ignore = true)
    @Mapping(target = "document_link_ket_qua_thi_dgnl", ignore = true)
    @Mapping(target = "document_link_chung_chi_ngoai_ngu", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "application", expression = "java(request.getApplicationId() != null ? com.example.LuanVanTotNghiep.entity.Applications.builder().application_id(request.getApplicationId()).build() : null)")
    void updateDocuments(@MappingTarget Documents documents, DocumentsRequest request);

    List<DocumentsResponse> listDocuments(List<Documents> documentsList);
}