package com.levelup.jmsinfo.config;

import java.security.Key;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.levelup.jmsinfo.user.vo.UserVO;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.log4j.Log4j2;

/**
 * @purpose JWT �??��?�� ?��?�� Util
 * 
 * @  ?��?��?��          ?��?��?��       ?��?��?��?��
 * @ ---------       ---------   -------------------------------
 * @ 2023.02.09       ?��민서       최초?��?��
 * @ 2023.02.22       ?��민서       Refresh Token 추�?
 * @ 2023.02.24       ?��민서       getTokenPayload method 추�?
 * @ 2023.02.28       ?��민서       ?��?�� ?��?��?���? �?�?
 * 
 * @author ?��민서
 * @since  2023.02.09
 *
 */
@Log4j2
@Component
public class TokenUtils {

    private static String jwtSecretKey;

    @Value("${custom.jwt-secret-key}")
    public void setJwtSecretKey(String jwtSecretKey) {
    	this.jwtSecretKey = jwtSecretKey;
    }
    
    /**
     * ?��?��?�� ?��보�?? 기반?���? ?��?��?�� ?��?��?��?�� 반환 ?��주는 메서?��
     *
     * @param user : ?��?��?�� ?���? VO
     * @return String : ?��?��
     */
    public static String generateAccessToken(UserVO user) {
        // ?��?��?�� ?��???���? 기�??���? JWT ?��?��?�� 발급?��?�� 반환?��줍니?��.
        JwtBuilder builder = Jwts.builder()
                .setHeader(createHeader())                              // Header 구성
                .setClaims(createClaims(user))                          // Payload - Claims 구성
                .signWith(SignatureAlgorithm.HS256, createSignature())  // Signature 구성
                .setExpiration(createAccTokenExpiredDate());            // Expired Date 구성
        return builder.compact();
    }
    
    /**
     * RefreshToken ?��?��?��?�� 메소?��
     *
     * @return String : ?��?��
     */
    public static String generateRefreshToken(UserVO user) {
    	 JwtBuilder builder = Jwts.builder()
    			 .setSubject(String.valueOf(user.getUserId()))			   // Payload - Subject 구성			
                 .signWith(SignatureAlgorithm.HS256, createSignature())    // Signature 구성
                 .setExpiration(createRefExpiredDate());                   // Expired Date 구성
    	 
         return builder.compact();
    }

    /**
     * ?��?��?�� 기반?���? ?��?��?�� ?��보�?? 반환 ?��주는 메서?��
     *
     * @param token String : ?��?��
     * @return String : ?��?��?�� ?���?
     */
    public static String parseTokenToUserInfo(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    
    /**
     * 만료?�� ?��?��?�� ?��?��?�� ?��보�?? 반환 ?��주는 메서?��
     *
     * @param token String : ?��?��
     * @return String : token payload �?�?
     */
    public static String getTokenPayload(String token) {
    	
    	Base64.Decoder decoder = Base64.getUrlDecoder();
    	String[] parts = token.split("\\.");
    	
    	String tokenHeader = new String(decoder.decode(parts[0]));
    	String payload = new String(decoder.decode(parts[1]));
    	
    	log.info("Headers: ", tokenHeader);
    	log.info("Payload: ", payload); 
    	
    	return payload;
    }

    /**
     * ?��?��?�� ?��?��?���? ?��?�� ?��주는 메서?��
     *
     * @param token String  : ?��?��
     * @return boolean      : ?��?��?���? ?���? 반환
     */
    public static boolean isValidToken(String token) {
    	Claims claims = null;
        try {
            claims = getClaimsFormToken(token);

            log.info("expireTime :" + claims.getExpiration());
            log.info("userId :" + claims.get("userId"));

            return true;
        } catch (ExpiredJwtException exception) {
            return false;
        } catch (JwtException exception) {
            return false;
        } catch (NullPointerException exception) {
            return false;
        }
    }

    /**
     * Header ?��?�� ?��?��?�� 추출?��?��?��.
     *
     * @param header ?��?��
     * @return String
     */
    public static String getTokenFromHeader(String header) {
        return header.split(" ")[1];
    }

    /**
     * 리프?��?�� ?��?��?�� 만료기간?�� �??��?��?�� ?��?��
     *
     * @return Calendar
     */
    private static Date createRefExpiredDate() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 14); // Refresh ?��?�� 기간 2�?
        return c.getTime();
    }
    
    /**
     * ?��?��?�� ?��?��?�� 만료기간?�� �??��?��?�� ?��?��
     *
     * @return Calendar
     */
    private static Date createAccTokenExpiredDate() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR, 1); // Access ?��?�� 기간 1?���?
        return c.getTime();
    }

    /**
     * JWT?�� "?��?��" 값을 ?��?��?��주는 메서?��
     *
     * @return HashMap<String, Object>
     */
    private static Map<String, Object> createHeader() {
        Map<String, Object> header = new HashMap<>();

        header.put("typ", "JWT");
        header.put("alg", "HS256");
        header.put("regDate", System.currentTimeMillis());
        return header;
    }

    /**
     * ?��?��?�� ?��보�?? 기반?���? ?��?��?��?�� ?��?��?��주는 메서?��
     *
     * @param user ?��?��?�� ?���? VO
     * @return Map<String, Object>
     */
    private static Map<String, Object> createClaims(UserVO user) {
        // 공개 ?��?��?��?�� ?��?��?��?�� ?��름과 ?��메일?�� ?��?��?��?�� ?��보�?? 조회?�� ?�� ?��?��.
        Map<String, Object> claims = new HashMap<>();

        log.info("userNo :" + user.getUserNo());
        log.info("userId :" + user.getUserId());
        log.info("userNm :" + user.getUsername());

        claims.put("userNo", user.getUserNo());
        claims.put("userId", user.getUserId());
        claims.put("userName", user.getUsername());
        
        return claims;
    }

    /**
     * JWT "?���?(Signature)" 발급?�� ?��주는 메서?��
     *
     * @return Key
     */
    private static Key createSignature() {
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(jwtSecretKey);
        return new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    /**
     * ?��?�� ?��보�?? 기반?���? Claims ?��보�?? 반환받는 메서?��
     *
     * @param token : ?��?��
     * @return Claims : Claims
     */
    public static Claims getClaimsFormToken(String token) {
        return Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(jwtSecretKey))
                .parseClaimsJws(token).getBody();
    }

    /**
     * ?��?��?�� 기반?���? ?��?��?�� ?��보�?? 반환받는 메서?��
     *
     * @param token : ?��?��
     * @return String : ?��?��?�� ?��?��?��
     */
    public static String getUserIdFromToken(String token, String key) {
        Claims claims = getClaimsFormToken(token);
        return claims.get(key).toString();
    }
    
}