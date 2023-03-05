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
 * @purpose JWT ê´?? ¨?œ ?† ?° Util
 * 
 * @  ?ˆ˜? •?¼          ?ˆ˜? •?       ?ˆ˜? •?‚´?š©
 * @ ---------       ---------   -------------------------------
 * @ 2023.02.09       ? •ë¯¼ì„œ       ìµœì´ˆ?ƒ?„±
 * @ 2023.02.22       ? •ë¯¼ì„œ       Refresh Token ì¶”ê?
 * @ 2023.02.24       ? •ë¯¼ì„œ       getTokenPayload method ì¶”ê?
 * @ 2023.02.28       ? •ë¯¼ì„œ       ?† ?° ?œ ?š¨?‹œê°? ë³?ê²?
 * 
 * @author ? •ë¯¼ì„œ
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
     * ?‚¬?š©? ? •ë³´ë?? ê¸°ë°˜?œ¼ë¡? ?† ?°?„ ?ƒ?„±?•˜?—¬ ë°˜í™˜ ?•´ì£¼ëŠ” ë©”ì„œ?“œ
     *
     * @param user : ?‚¬?š©? ? •ë³? VO
     * @return String : ?† ?°
     */
    public static String generateAccessToken(UserVO user) {
        // ?‚¬?š©? ?‹œ???Š¤ë¥? ê¸°ì??œ¼ë¡? JWT ?† ?°?„ ë°œê¸‰?•˜?—¬ ë°˜í™˜?•´ì¤ë‹ˆ?‹¤.
        JwtBuilder builder = Jwts.builder()
                .setHeader(createHeader())                              // Header êµ¬ì„±
                .setClaims(createClaims(user))                          // Payload - Claims êµ¬ì„±
                .signWith(SignatureAlgorithm.HS256, createSignature())  // Signature êµ¬ì„±
                .setExpiration(createAccTokenExpiredDate());            // Expired Date êµ¬ì„±
        return builder.compact();
    }
    
    /**
     * RefreshToken ?ƒ?„±?•˜?Š” ë©”ì†Œ?“œ
     *
     * @return String : ?† ?°
     */
    public static String generateRefreshToken(UserVO user) {
    	 JwtBuilder builder = Jwts.builder()
    			 .setSubject(String.valueOf(user.getUserId()))			   // Payload - Subject êµ¬ì„±			
                 .signWith(SignatureAlgorithm.HS256, createSignature())    // Signature êµ¬ì„±
                 .setExpiration(createRefExpiredDate());                   // Expired Date êµ¬ì„±
    	 
         return builder.compact();
    }

    /**
     * ?† ?°?„ ê¸°ë°˜?œ¼ë¡? ?‚¬?š©? ? •ë³´ë?? ë°˜í™˜ ?•´ì£¼ëŠ” ë©”ì„œ?“œ
     *
     * @param token String : ?† ?°
     * @return String : ?‚¬?š©? ? •ë³?
     */
    public static String parseTokenToUserInfo(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    
    /**
     * ë§Œë£Œ?œ ?† ?°?˜ ?‚¬?š©? ? •ë³´ë?? ë°˜í™˜ ?•´ì£¼ëŠ” ë©”ì„œ?“œ
     *
     * @param token String : ?† ?°
     * @return String : token payload ë¶?ë¶?
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
     * ?œ ?š¨?•œ ?† ?°?¸ì§? ?™•?¸ ?•´ì£¼ëŠ” ë©”ì„œ?“œ
     *
     * @param token String  : ?† ?°
     * @return boolean      : ?œ ?š¨?•œì§? ?—¬ë¶? ë°˜í™˜
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
     * Header ?‚´?— ?† ?°?„ ì¶”ì¶œ?•©?‹ˆ?‹¤.
     *
     * @param header ?—¤?”
     * @return String
     */
    public static String getTokenFromHeader(String header) {
        return header.split(" ")[1];
    }

    /**
     * ë¦¬í”„? ˆ?‹œ ?† ?°?˜ ë§Œë£Œê¸°ê°„?„ ì§?? •?•˜?Š” ?•¨?ˆ˜
     *
     * @return Calendar
     */
    private static Date createRefExpiredDate() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 14); // Refresh ?† ?° ê¸°ê°„ 2ì£?
        return c.getTime();
    }
    
    /**
     * ?—‘?„¸?Š¤ ?† ?°?˜ ë§Œë£Œê¸°ê°„?„ ì§?? •?•˜?Š” ?•¨?ˆ˜
     *
     * @return Calendar
     */
    private static Date createAccTokenExpiredDate() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR, 1); // Access ?† ?° ê¸°ê°„ 1?‹œê°?
        return c.getTime();
    }

    /**
     * JWT?˜ "?—¤?”" ê°’ì„ ?ƒ?„±?•´ì£¼ëŠ” ë©”ì„œ?“œ
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
     * ?‚¬?š©? ? •ë³´ë?? ê¸°ë°˜?œ¼ë¡? ?´?˜?„?„ ?ƒ?„±?•´ì£¼ëŠ” ë©”ì„œ?“œ
     *
     * @param user ?‚¬?š©? ? •ë³? VO
     * @return Map<String, Object>
     */
    private static Map<String, Object> createClaims(UserVO user) {
        // ê³µê°œ ?´? ˆ?„?— ?‚¬?š©??˜ ?´ë¦„ê³¼ ?´ë©”ì¼?„ ?„¤? •?•˜?—¬ ? •ë³´ë?? ì¡°íšŒ?•  ?ˆ˜ ?ˆ?‹¤.
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
     * JWT "?„œëª?(Signature)" ë°œê¸‰?„ ?•´ì£¼ëŠ” ë©”ì„œ?“œ
     *
     * @return Key
     */
    private static Key createSignature() {
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(jwtSecretKey);
        return new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    /**
     * ?† ?° ? •ë³´ë?? ê¸°ë°˜?œ¼ë¡? Claims ? •ë³´ë?? ë°˜í™˜ë°›ëŠ” ë©”ì„œ?“œ
     *
     * @param token : ?† ?°
     * @return Claims : Claims
     */
    public static Claims getClaimsFormToken(String token) {
        return Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(jwtSecretKey))
                .parseClaimsJws(token).getBody();
    }

    /**
     * ?† ?°?„ ê¸°ë°˜?œ¼ë¡? ?‚¬?š©? ? •ë³´ë?? ë°˜í™˜ë°›ëŠ” ë©”ì„œ?“œ
     *
     * @param token : ?† ?°
     * @return String : ?‚¬?š©? ?•„?´?””
     */
    public static String getUserIdFromToken(String token, String key) {
        Claims claims = getClaimsFormToken(token);
        return claims.get(key).toString();
    }
    
}