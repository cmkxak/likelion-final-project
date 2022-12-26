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
        //Token을 가지고 와줍니다.
        String jwt = resolveToken(request);
        log.info("[doFilterInternal] token 값 추출완료. token : {}", jwt);

        log.info("[doFilterInternal] token 값 만료 여부 체크");
        if(StringUtils.hasText(jwt) && !tokenProvider.isExpired(jwt)){
            Authentication authentication = tokenProvider.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        log.info("[doFilterInternal] token 값 만료 여부 체크 성공 및 SecurityContextHolder에 토큰 저장 완료");

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
