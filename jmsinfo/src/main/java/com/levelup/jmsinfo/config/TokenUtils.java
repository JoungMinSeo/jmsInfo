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
 * @purpose JWT κ΄?? ¨? ? ?° Util
 * 
 * @  ?? ?Ό          ?? ?       ?? ?΄?©
 * @ ---------       ---------   -------------------------------
 * @ 2023.02.09       ? λ―Όμ       μ΅μ΄??±
 * @ 2023.02.22       ? λ―Όμ       Refresh Token μΆκ?
 * @ 2023.02.24       ? λ―Όμ       getTokenPayload method μΆκ?
 * @ 2023.02.28       ? λ―Όμ       ? ?° ? ?¨?κ°? λ³?κ²?
 * 
 * @author ? λ―Όμ
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
     * ?¬?©? ? λ³΄λ?? κΈ°λ°?Όλ‘? ? ?°? ??±??¬ λ°ν ?΄μ£Όλ λ©μ?
     *
     * @param user : ?¬?©? ? λ³? VO
     * @return String : ? ?°
     */
    public static String generateAccessToken(UserVO user) {
        // ?¬?©? ????€λ₯? κΈ°μ??Όλ‘? JWT ? ?°? λ°κΈ??¬ λ°ν?΄μ€λ?€.
        JwtBuilder builder = Jwts.builder()
                .setHeader(createHeader())                              // Header κ΅¬μ±
                .setClaims(createClaims(user))                          // Payload - Claims κ΅¬μ±
                .signWith(SignatureAlgorithm.HS256, createSignature())  // Signature κ΅¬μ±
                .setExpiration(createAccTokenExpiredDate());            // Expired Date κ΅¬μ±
        return builder.compact();
    }
    
    /**
     * RefreshToken ??±?? λ©μ?
     *
     * @return String : ? ?°
     */
    public static String generateRefreshToken(UserVO user) {
    	 JwtBuilder builder = Jwts.builder()
    			 .setSubject(String.valueOf(user.getUserId()))			   // Payload - Subject κ΅¬μ±			
                 .signWith(SignatureAlgorithm.HS256, createSignature())    // Signature κ΅¬μ±
                 .setExpiration(createRefExpiredDate());                   // Expired Date κ΅¬μ±
    	 
         return builder.compact();
    }

    /**
     * ? ?°? κΈ°λ°?Όλ‘? ?¬?©? ? λ³΄λ?? λ°ν ?΄μ£Όλ λ©μ?
     *
     * @param token String : ? ?°
     * @return String : ?¬?©? ? λ³?
     */
    public static String parseTokenToUserInfo(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    
    /**
     * λ§λ£? ? ?°? ?¬?©? ? λ³΄λ?? λ°ν ?΄μ£Όλ λ©μ?
     *
     * @param token String : ? ?°
     * @return String : token payload λΆ?λΆ?
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
     * ? ?¨? ? ?°?Έμ§? ??Έ ?΄μ£Όλ λ©μ?
     *
     * @param token String  : ? ?°
     * @return boolean      : ? ?¨?μ§? ?¬λΆ? λ°ν
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
     * Header ?΄? ? ?°? μΆμΆ?©??€.
     *
     * @param header ?€?
     * @return String
     */
    public static String getTokenFromHeader(String header) {
        return header.split(" ")[1];
    }

    /**
     * λ¦¬ν? ? ? ?°? λ§λ£κΈ°κ°? μ§?? ?? ?¨?
     *
     * @return Calendar
     */
    private static Date createRefExpiredDate() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 14); // Refresh ? ?° κΈ°κ° 2μ£?
        return c.getTime();
    }
    
    /**
     * ??Έ?€ ? ?°? λ§λ£κΈ°κ°? μ§?? ?? ?¨?
     *
     * @return Calendar
     */
    private static Date createAccTokenExpiredDate() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR, 1); // Access ? ?° κΈ°κ° 1?κ°?
        return c.getTime();
    }

    /**
     * JWT? "?€?" κ°μ ??±?΄μ£Όλ λ©μ?
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
     * ?¬?©? ? λ³΄λ?? κΈ°λ°?Όλ‘? ?΄??? ??±?΄μ£Όλ λ©μ?
     *
     * @param user ?¬?©? ? λ³? VO
     * @return Map<String, Object>
     */
    private static Map<String, Object> createClaims(UserVO user) {
        // κ³΅κ° ?΄? ?? ?¬?©?? ?΄λ¦κ³Ό ?΄λ©μΌ? ?€? ??¬ ? λ³΄λ?? μ‘°ν?  ? ??€.
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
     * JWT "?λͺ?(Signature)" λ°κΈ? ?΄μ£Όλ λ©μ?
     *
     * @return Key
     */
    private static Key createSignature() {
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(jwtSecretKey);
        return new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    /**
     * ? ?° ? λ³΄λ?? κΈ°λ°?Όλ‘? Claims ? λ³΄λ?? λ°νλ°λ λ©μ?
     *
     * @param token : ? ?°
     * @return Claims : Claims
     */
    public static Claims getClaimsFormToken(String token) {
        return Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(jwtSecretKey))
                .parseClaimsJws(token).getBody();
    }

    /**
     * ? ?°? κΈ°λ°?Όλ‘? ?¬?©? ? λ³΄λ?? λ°νλ°λ λ©μ?
     *
     * @param token : ? ?°
     * @return String : ?¬?©? ??΄?
     */
    public static String getUserIdFromToken(String token, String key) {
        Claims claims = getClaimsFormToken(token);
        return claims.get(key).toString();
    }
    
}