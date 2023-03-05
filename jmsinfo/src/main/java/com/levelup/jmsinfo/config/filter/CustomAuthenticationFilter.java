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
 * @purpose ?��?��?��?? 비�?번호 기반?�� ?��?��?���? Form ?��?��?���? ?��?��?�� 받아 '?���?'?�� ?��?��?��?�� ?��?��?��?��?��.
 *
 * @  ?��?��?��          ?��?��?��       ?��?��?��?��
 * @ ---------       ---------    -------------------------------
 * @ 2023.02.08       ?��민서       최초?��?��
 * @ 2023.02.09       ?��민서       TODO: 권한?�� ?���? Handle ?��?�� 
 *
 * @author ?��민서
 * @since  2023.02.08
 *
 */
@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    /**
     * �??��?�� URL�? form ?��?��?�� ?��???�� 경우 ?��?��미터 ?��보�?? �??��?��?��.
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
     * Request�? 받�? ID?? ?��?��?��?�� 기반?���? ?��?��?�� 발급?��?��.
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
            log.info("첫번�?.CustomAuthenticationFilter :: userId:" + user.getUserId() + " userPw:" + user.getUserPassword());

            // ID?? ?��?��?��?���? 기반?���? ?��?�� 발급
            return new UsernamePasswordAuthenticationToken(user.getUserId(), user.getUserPassword());
        } catch (UsernameNotFoundException ae) {
            throw new UsernameNotFoundException(ae.getMessage());
        } catch (Exception e) {
            throw new Exception(e.getMessage(), e.getCause());
        }

    }

}