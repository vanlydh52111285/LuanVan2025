package com.example.LuanVanTotNghiep.controller;


import com.example.LuanVanTotNghiep.dto.request.AuthenticationRequest;
import com.example.LuanVanTotNghiep.dto.request.IntrospectRequest;
import com.example.LuanVanTotNghiep.dto.response.ApiResponse;
import com.example.LuanVanTotNghiep.dto.response.AuthenticationResponse;
import com.example.LuanVanTotNghiep.dto.response.IntrospectResponse;
import com.example.LuanVanTotNghiep.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;
    @PostMapping("/admin/login")
    ApiResponse<AuthenticationResponse> authenticateAdmin(@RequestBody AuthenticationRequest request){
        var result=authenticationService.authenticationResponse(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .code(1000)
                        .result(result)
                        .build();
    }
    @PostMapping("/cadre/login")
    ApiResponse<AuthenticationResponse> authenticateCadre(@RequestBody AuthenticationRequest request){
        var result=authenticationService.authenticationResponse(request);
        return ApiResponse.<AuthenticationResponse>builder().
                result(result)
                .code(1000)
                .build();
    }
    @PostMapping("/student/login")
    ApiResponse<AuthenticationResponse> authenticateCadreUsers(@RequestBody AuthenticationRequest request){
        var result=authenticationService.authenticationResponse(request);
        return ApiResponse.<AuthenticationResponse>builder().
                result(result)
                .code(1000)
                .build();
    }
    @PostMapping("/intro")
    ApiResponse<IntrospectResponse> authenticate(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        var result=authenticationService.introspect(request);
        return ApiResponse.<IntrospectResponse>builder().
                result(result)
                .code(1000)
                .build();
    }
}
