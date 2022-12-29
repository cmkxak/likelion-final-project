package com.likelion.mutsasns.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;

    //실제 필터링 로직 : Jwt Token의 Authentication 정보를 현재 실행 중인 Security Context에 저장하기 위함
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = resolveToken(request); // 요청 header에서 Token 꺼내줌

        log.info("===[doFilterInternal] token 값 추출 완료 - 토큰 : {}===", jwt);

        log.info("===[doFilterInternal] token 값 만료 여부 체크 시작 (토큰 검증 로직)===" );
        if(StringUtils.hasText(jwt) && !tokenProvider.isExpired(jwt)){ //토큰이 존재하고, 만료되지 않았으면
            Authentication authentication = tokenProvider.getAuthentication(jwt); //tokenProvider를 통해 인증 객체가 생성되도록 구현
            SecurityContextHolder.getContext().setAuthentication(authentication); //인증 객체를 SecurityContextHolder에 저장
            log.info("authentication 객체 : {}", authentication);
        }
        log.info("===[doFilterInternal] token 값 만료 여부 체크 종료===");

        filterChain.doFilter(request, response);
    }

    //토큰 정보 가져오기
    public String resolveToken(HttpServletRequest request){
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("authentication Header:{}", header);

        if(StringUtils.hasText(header) && header.startsWith("Bearer ")){
            return header.substring(7);
        }
        return null;
    }
}
