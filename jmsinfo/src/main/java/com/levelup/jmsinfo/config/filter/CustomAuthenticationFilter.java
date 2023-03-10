package com.levelup.jmsinfo.config.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.levelup.jmsinfo.user.vo.UserVO;

import lombok.extern.slf4j.Slf4j;

/**
 * @purpose ??΄??? λΉλ?λ²νΈ κΈ°λ°? ?°?΄?°λ₯? Form ?°?΄?°λ‘? ? ?‘? λ°μ '?Έμ¦?'? ?΄?Ή?? ??°???€.
 *
 * @  ?? ?Ό          ?? ?       ?? ?΄?©
 * @ ---------       ---------    -------------------------------
 * @ 2023.02.08       ? λ―Όμ       μ΅μ΄??±
 * @ 2023.02.09       ? λ―Όμ       TODO: κΆν? ?°λ₯? Handle ?€?  
 *
 * @author ? λ―Όμ
 * @since  2023.02.08
 *
 */
@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    /**
     * μ§?? ? URLλ‘? form ? ?‘? ???? κ²½μ° ??Όλ―Έν° ? λ³΄λ?? κ°?? Έ?¨?€.
     *
     * @param request  from which to extract parameters and perform the authentication
     * @param response the response, which may be needed if the implementation has to do a
     *                 redirect as part of a multi-stage authentication process (such as OpenID).
     * @return Authentication {}
     * @throws AuthenticationException {}
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authRequest;
        try {
            authRequest = getAuthRequest(request);
            setDetails(request, authRequest);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return this.getAuthenticationManager().authenticate(authRequest);
    }


    /**
     * Requestλ‘? λ°μ? ID?? ?¨?€?? κΈ°λ°?Όλ‘? ? ?°? λ°κΈ??€.
     *
     * @param request HttpServletRequest
     * @return UsernamePasswordAuthenticationToken
     * @throws Exception e
     */
    private UsernamePasswordAuthenticationToken getAuthRequest(HttpServletRequest request) throws Exception {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);
            UserVO user = objectMapper.readValue(request.getInputStream(), UserVO.class);
            log.info("μ²«λ²μ§?.CustomAuthenticationFilter :: userId:" + user.getUserId() + " userPw:" + user.getUserPassword());

            // ID?? ?¨?€??λ₯? κΈ°λ°?Όλ‘? ? ?° λ°κΈ
            return new UsernamePasswordAuthenticationToken(user.getUserId(), user.getUserPassword());
        } catch (UsernameNotFoundException ae) {
            throw new UsernameNotFoundException(ae.getMessage());
        } catch (Exception e) {
            throw new Exception(e.getMessage(), e.getCause());
        }

    }

}