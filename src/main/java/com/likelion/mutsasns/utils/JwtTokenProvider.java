package com.likelion.mutsasns.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

//Token 생성과 유효성 검증 담당 클래스
@Slf4j
@Component
public class JwtTokenProvider {

    private final Long expireTimeMs;
    private String secretKey;

    public JwtTokenProvider(
            @Value("${jwt.token.secret}")String secretKey,
            @Value("${jwt.token.expireTimeMs}")long expireTimeMs) {
        this.secretKey = secretKey;
        this.expireTimeMs = expireTimeMs;
    }

    @PostConstruct
    protected void init(){
        log.info("[init] JwtTokenProvider 내 secretKey 초기화 시작 : {} ", this.secretKey);
        this.secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));
        log.info("[init] JwtTokenProvider 내 secretKey 초기화 완료 : {}", secretKey);
    }

    public String createToken(String userName) {
        Claims claims = Jwts.claims();
        claims.put("userName", userName);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTimeMs))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    //토큰의 유효성 검증
    public boolean validateToken(String token){
        Date expiredDate = extractClaims(token).getExpiration();
        return expiredDate.before(new Date());
    }

    //토큰에 저장된 userName 반환
    public String getUserName(String token){
        return extractClaims(token).get("userName", String.class);
    }

    //토큰 내용 추출
    private Claims extractClaims(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }
}
