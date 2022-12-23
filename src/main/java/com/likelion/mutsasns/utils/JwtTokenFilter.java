package com.likelion.mutsasns.utils;

import com.likelion.mutsasns.domain.entity.User;
import com.likelion.mutsasns.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    private final UserService userService;

    //실제 필터링 로직 : Jwt Token의 Authentication 정보를 현재 실행 중인 Security Context에 저장하기 위함
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //Token을 가지고 와줍니다.
        String jwt = resolveToken(request);
        log.info("[doFilterInternal] token 값 추출완료. token : {}", jwt);

        log.info("[doFilterInternal] token 값 유효성 체크 시작");
        if(StringUtils.hasText(jwt) && !tokenProvider.isExpired(jwt)){
            UsernamePasswordAuthenticationToken authenticationToken = getAuthentication(jwt);
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
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

    public UsernamePasswordAuthenticationToken getAuthentication(String token){
        User userDetails = userService.findUserByUserName(tokenProvider.getUserName(token));
        log.info("userDetails : {}", userDetails.getUserName());
        return new UsernamePasswordAuthenticationToken(userDetails.getUserName(), "", List.of(new SimpleGrantedAuthority("USER")));
    }
}
