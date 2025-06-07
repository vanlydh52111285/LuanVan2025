package com.example.LuanVanTotNghiep.mapper;

import com.example.LuanVanTotNghiep.dto.request.UsersRequest;
import com.example.LuanVanTotNghiep.dto.response.UsersResponse;
import com.example.LuanVanTotNghiep.entity.Users;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;
@Mapper(componentModel = "spring")
public interface UsersMapper {
    Users toCreateUser(UsersRequest request);
    UsersResponse toUserResponse(Users users);
    void updateUser(@MappingTarget Users users, UsersRequest request );
    List<UsersResponse> listUsers(List<Users> usersList);
}
