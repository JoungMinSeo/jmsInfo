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
 * @purpose ?ïÑ?ù¥?îî?? ÎπÑÎ?Î≤àÌò∏ Í∏∞Î∞ò?ùò ?ç∞?ù¥?Ñ∞Î•? Form ?ç∞?ù¥?Ñ∞Î°? ?†Ñ?Ü°?ùÑ Î∞õÏïÑ '?ù∏Ï¶?'?ùÑ ?ã¥?ãπ?ïò?äî ?ïÑ?Ñ∞?ûÖ?ãà?ã§.
 *
 * @  ?àò?†ï?ùº          ?àò?†ï?ûê       ?àò?†ï?Ç¥?ö©
 * @ ---------       ---------    -------------------------------
 * @ 2023.02.08       ?†ïÎØºÏÑú       ÏµúÏ¥à?Éù?Ñ±
 * @ 2023.02.09       ?†ïÎØºÏÑú       TODO: Í∂åÌïú?óê ?î∞Î•? Handle ?Ñ§?†ï 
 *
 * @author ?†ïÎØºÏÑú
 * @since  2023.02.08
 *
 */
@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    /**
     * Ïß??†ï?êú URLÎ°? form ?†Ñ?Ü°?ùÑ ?ïò???ùÑ Í≤ΩÏö∞ ?åå?ùºÎØ∏ÌÑ∞ ?†ïÎ≥¥Î?? Í∞??†∏?ò®?ã§.
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
     * RequestÎ°? Î∞õÏ? ID?? ?å®?ä§?õå?ìú Í∏∞Î∞ò?úºÎ°? ?Ü†?Å∞?ùÑ Î∞úÍ∏â?ïú?ã§.
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
            log.info("Ï≤´Î≤àÏß?.CustomAuthenticationFilter :: userId:" + user.getUserId() + " userPw:" + user.getUserPassword());

            // ID?? ?å®?ä§?õå?ìúÎ•? Í∏∞Î∞ò?úºÎ°? ?Ü†?Å∞ Î∞úÍ∏â
            return new UsernamePasswordAuthenticationToken(user.getUserId(), user.getUserPassword());
        } catch (UsernameNotFoundException ae) {
            throw new UsernameNotFoundException(ae.getMessage());
        } catch (Exception e) {
            throw new Exception(e.getMessage(), e.getCause());
        }

    }

}