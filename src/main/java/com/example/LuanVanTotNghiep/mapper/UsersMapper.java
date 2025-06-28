package com.example.LuanVanTotNghiep.mapper;

import com.example.LuanVanTotNghiep.dto.request.UsersRequest;
import com.example.LuanVanTotNghiep.dto.response.UsersResponse;
import com.example.LuanVanTotNghiep.entity.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
@Mapper(componentModel = "spring")
public interface UsersMapper {

    @Mapping(target = "sex",source = "sex")
    Users toCreateUser(UsersRequest request);

    @Mapping(target = "sex",source = "sex")
    @Mapping(target ="districtsResponse" ,source = "district")
    @Mapping(target ="provincesResponse" ,source = "province")
    UsersResponse toUserResponse(Users users);
    @Mapping(target = "user_id", ignore = true)
    @Mapping(target = "sex",source = "sex")

    void updateUser(@MappingTarget Users users, UsersRequest request );
//    @Mapping(target = "is_verify",source = "is_verify")
    @Mapping(target = "sex",source = "sex")
    List<UsersResponse> listUsers(List<Users> usersList);
}
