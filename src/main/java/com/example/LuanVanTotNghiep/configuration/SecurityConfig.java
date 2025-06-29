package com.example.LuanVanTotNghiep.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    //=====================PUBLIC_ENOPOINTS_GET=====================
    private final String[]  PUBLIC_ENDPOINTS_GET={
            "/notifications/mark-read/{notification_id}", "/provinces/all",
            "/provinces/province/{province_id}", "/districts/all",
            "/districts/province/{province_id}", "/schools/all",
            "/schools/province/{province_id}",
            "/program-true","/groups-true",
            "/schedules","/list-pbs/{schedule_id}",
            "/branch-by-group-program","/branchs-by-program/{program_id}",
            "/programs-by-branch/{branch_id}","/groups-by-branch/{branch_id}",
            "/groups","/university",
            "/api/excel/download","/branch",
            "/program", "/methods/getMethodById/{method_id}",
            "/methods/getAllMethods", "/subjects/all/excludedSubIds",
    };
    private final String[]  PUBLIC_ENDPOINTS_POST={
            "/student/otp",
            "/student/register","/auth/student/login",
            "/auth/intro","/auth/admin/login"};
    private final String[]  PUBLIC_ENDPOINTS_PUT={
            "/notifications/read/{notification_id}"
    };

    //=====================ADMIN_ENOPOINTS=====================
    private final String[] ADMIN_GET={
            "/student/update-users", "/applications/getAllApplications",
    };
    private final String[] ADMIN_POST={
            "/create-list-pbs","/schedule",
            "/create-branch-program","/admin/create-cadre",
            "/groups","/university",
            "/branch","/create-branch-group",
            "/program","/admin/create-cadre",
            "/notifications/create", "/api/applications/create",
            "/provinces/import", "/provinces/create",
            "/districts/create", "/districts/import",
            "shools/import", "schools/create",
            "/subjects/import", "/subjects/create",
            "/groups/import", "/branch/import",
    };
    private final String[] ADMIN_DELETE={
            "/groups/{id}","/admin/users/{id}",
            "/delete-branch-group","/notifications/delete/{notification_id}",
            "/provinces/delete/{province_id}", "/districts/delete/{province_id}/{district_id}",
            "/districts/delete/list-district/{province_id}","/schools/delete/{province_id}/{school_id}",
            "/schools/delete/list-schools/{province_id}", "subjects/delete/{sub_id}",
            "/branch/delete/{id}"
    };
    private final String[] ADMIN_PUT={
            "/groups/{id}", "/provinces/update/{province_id}",
            "/districts/update/{province_id}/{district_id}", "/schools/update/{province_id}/{school_id}",
            "/subject/update/{sub_id}", "/branch/delete/{id}"
    };

    //=====================CADRE_ENOPOINTS=====================
    private final String[] CADRE_GET={
            "/api/applications/getAllApplications"
    };
    private final String[] CADRE_POST={

    };
    private final String[] CADRE_DELETE={

    };
    private final String[] CADRE_PUT={

    };

    //=====================STUDENT_ENOPOINTS=====================
    private final String[] STUDENT_GET={
            "/documents/images",
            "/applications/getApplicationsByUser", "/subjects/all",
            "/scoreboards/get-by-application/{application_id}", "/aspirations/create",
    };
    private final String[] STUDENT_POST={
            "/documents/create", "/applications/create",
            "/scoreboards/create"
    };
    private final String[] STUDENT_DELETE={
            "/applications/delete/{applicationId}",
            "/documents/delete/{applicationId}/{documentId}"
    };
    private final String[] STUDENT_PUT={
            "/student/update-users",
            "/applications/update/{applicationId}",
            "/documents/update/{documentId}"
    };



    @Value("${jwt.signerKey}")
    private String signerKey;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.authorizeHttpRequests(
                request ->
                        request
                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                .requestMatchers(HttpMethod.GET,PUBLIC_ENDPOINTS_GET).permitAll()
                                .requestMatchers(HttpMethod.POST,PUBLIC_ENDPOINTS_POST).permitAll()
                                .requestMatchers(HttpMethod.PUT,PUBLIC_ENDPOINTS_PUT).permitAll()
                                .requestMatchers(HttpMethod.GET,ADMIN_GET).hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST,ADMIN_POST).hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE,ADMIN_DELETE).hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT,ADMIN_PUT).hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET,CADRE_GET).hasRole("CADRE")
                                .requestMatchers(HttpMethod.POST,CADRE_POST).hasRole("CADRE")
                                .requestMatchers(HttpMethod.DELETE,CADRE_DELETE).hasRole("CADRE")
                                .requestMatchers(HttpMethod.PUT,CADRE_PUT).hasRole("CADRE")
                                .requestMatchers(HttpMethod.GET,STUDENT_GET).hasRole("STUDENT")
                                .requestMatchers(HttpMethod.POST,STUDENT_POST).hasRole("STUDENT")
                                .requestMatchers(HttpMethod.DELETE,STUDENT_DELETE).hasRole("STUDENT")
                                .requestMatchers(HttpMethod.PUT,STUDENT_PUT).hasRole("STUDENT")
                                .anyRequest().authenticated());

        httpSecurity.oauth2ResourceServer(oath2->
                oath2.jwt(jwtConfigurer ->
                        jwtConfigurer.decoder(jwtDecoder())
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                ).authenticationEntryPoint(new JwtAuthenticationEntryPoint())
        );
        httpSecurity.csrf(AbstractHttpConfigurer::disable);



        return httpSecurity.build();
    }
    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter(){
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter=new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
        JwtAuthenticationConverter jwtAuthenticationConverter=new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }
    @Bean
    JwtDecoder jwtDecoder(){
        SecretKeySpec spec=new SecretKeySpec(signerKey.getBytes(),"HS512");

        return NimbusJwtDecoder.withSecretKey(spec)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
    }
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(5);
    }
}
