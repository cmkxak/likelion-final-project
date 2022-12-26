package com.likelion.mutsasns.config;

import com.likelion.mutsasns.service.UserService;
import com.likelion.mutsasns.utils.JwtTokenFilter;
import com.likelion.mutsasns.utils.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity //스프링 시큐리티 필터가 스프링 FilterChain에 등록됨.
public class SecurityConfig {

    private final String PERMIT_URL[] = {
            "/api/v1/users/join", "/api/v1/users/login", "/api/v1/hello"};

    private final String PERMIT_URL_SWAGGER[] = {
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/webjars/**",
            "/swagger/**"
    };

    private final JwtTokenProvider tokenProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .httpBasic().disable()
                .csrf().disable()
                .cors().and()

                .authorizeRequests()
                .antMatchers(PERMIT_URL).permitAll()
                .antMatchers(PERMIT_URL_SWAGGER).permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/posts/**").permitAll()
                .anyRequest().authenticated()

                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .addFilterBefore(new JwtTokenFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}