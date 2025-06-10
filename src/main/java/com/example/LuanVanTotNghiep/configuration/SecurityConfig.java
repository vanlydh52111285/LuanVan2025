package com.example.LuanVanTotNghiep.configuration;

import com.example.LuanVanTotNghiep.configuration.JwtAuthenticationEntryPoint;
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
    private final String[]  PUBLIC_ENOPOINTS_POST={"/student/register","/auth/student/login","/auth/intro","/auth/admin/login"};
    private final String[]  PUBLIC_ENOPOINTS_GET={"/groups"};
    private final String[] ADMIN_POST={"/admin/create-cadre","/groups"};
    private final String[] ADMIN_GET={"/student/update-users", "/api/applications/getAllApplications"};
    private final String[] ADMIN_PUT={};
    private final String[] ADMIN_DELETE={"/admin/users/{id}"};
    private final String[] CADRE_POST={};
    private final String[] CADRE_GET={"/api/applications/getAllApplications"};
    private final String[] CADRE_PUT={};
    private final String[] CADRE_DELETE={};
    private final String[] STUDENT_POST={"/api/documents/create", "/api/applications/create"};
    private final String[] STUDEN_GET={"/api/documents/images", "/api/applications/getApplicationsByUser"};
    private final String[] STUDENT_PUT={"/student/update-users", "/api/applications/update/{applicationId}", "/api/documents/update/{documentId}"};
    private final String[] STUDENT_DELETE={"/api/applications/delete/{applicationId}" ,"/api/documents/delete/{documentId}"};



    @Value("${jwt.signerKey}")
    private String signerKey;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.authorizeHttpRequests(
                request ->
                        request.requestMatchers(HttpMethod.POST,PUBLIC_ENOPOINTS_POST).permitAll()
                                .requestMatchers(HttpMethod.GET,PUBLIC_ENOPOINTS_GET).permitAll()
                                .requestMatchers(HttpMethod.POST,ADMIN_POST).hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET,ADMIN_GET).hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT,ADMIN_PUT).hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE,ADMIN_DELETE).hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST,CADRE_POST).hasRole("CADRE")
                                .requestMatchers(HttpMethod.GET,CADRE_GET).hasRole("CADRE")
                                .requestMatchers(HttpMethod.PUT,CADRE_PUT).hasRole("CADRE")
                                .requestMatchers(HttpMethod.DELETE,CADRE_DELETE).hasRole("CADRE")
                                .requestMatchers(HttpMethod.POST,STUDENT_POST).hasRole("STUDENT")
                                .requestMatchers(HttpMethod.GET,STUDEN_GET).hasRole("STUDENT")
                                .requestMatchers(HttpMethod.PUT,STUDENT_PUT).hasRole("STUDENT")
                                .requestMatchers(HttpMethod.DELETE,STUDENT_DELETE).hasRole("STUDENT")

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
