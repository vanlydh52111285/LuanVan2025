package com.example.LuanVanTotNghiep.service;

import com.example.LuanVanTotNghiep.dto.request.UsersRequest;
import com.example.LuanVanTotNghiep.dto.response.UsersResponse;
import com.example.LuanVanTotNghiep.entity.Users;
import com.example.LuanVanTotNghiep.enums.Role;
import com.example.LuanVanTotNghiep.exception.AppException;
import com.example.LuanVanTotNghiep.exception.ErrorCode;
import com.example.LuanVanTotNghiep.mapper.UsersMapper;
import com.example.LuanVanTotNghiep.repository.UsersRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
public class UsersService {
    UsersRepository usersRepository;
    UsersMapper usersMapper;
    PasswordEncoder passwordEncoder;
    public UsersResponse createStudent(UsersRequest request){
        if(usersRepository.existsByEmail(request.getEmail())){
            throw new AppException(ErrorCode.EMAIL_EXISTS);
        }
        Users users = usersMapper.toCreateUser(request);
        users.setPassword(passwordEncoder.encode(request.getPassword()));
        users.setCreated_at(new Date(System.currentTimeMillis()));
        HashSet<String>roles = new HashSet<>();
        roles.add(Role.STUDENT.name());
        users.setRole(roles);
        return  usersMapper.toUserResponse(usersRepository.save(users));
    }
    public UsersResponse createCadre(UsersRequest request){
        if(usersRepository.existsByEmail(request.getEmail())){
            throw new AppException(ErrorCode.EMAIL_EXISTS);
        }
        Users users = usersMapper.toCreateUser(request);
        users.setPassword(passwordEncoder.encode(request.getPassword()));
        users.setCreated_at(new Date(System.currentTimeMillis()));
        HashSet<String>roles = new HashSet<>();
        roles.add(Role.CADRE.name());
        users.setRole(roles);
        return  usersMapper.toUserResponse(usersRepository.save(users));
    }
    public List<UsersResponse> getAllUsers(){
        List<UsersResponse> list = usersMapper.listUsers(usersRepository.findAll());
        return list;
    }
    @PostAuthorize("returnObject.users_id == authentication.users_id")
    public UsersResponse getUsers(String id){
        Users users = usersRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.UNAUTHORIZED));
        return usersMapper.toUserResponse(users);
    }

    public void deleteUsers(String id){
        usersRepository.deleteById(id);
    }

    public UsersResponse updateUsers(String id , UsersRequest request){
        Users users = usersRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.USERNAME_NOT_FOUND));
        usersMapper.updateUser(users,request);
        return usersMapper.toUserResponse(usersRepository.save(users));
    }
}
