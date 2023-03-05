package com.levelup.jmsinfo.config.handler;

import javax.annotation.Resource;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.levelup.jmsinfo.user.vo.UserVO;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @purpose ? „?‹¬ë°›ì? ?‚¬?š©??˜ ?•„?´?””?? ë¹„ë?ë²ˆí˜¸ë¥? ê¸°ë°˜?œ¼ë¡? ë¹„ì¦ˆ?‹ˆ?Š¤ ë¡œì§?„ ì²˜ë¦¬?•˜?—¬ ?‚¬?š©??˜ ?˜ì¸ì¦â?™ì— ???•´?„œ ê²?ì¦ì„ ?ˆ˜?–‰?•˜?Š” ?´?˜?Š¤?…?‹ˆ?‹¤.
 * CustomAuthenticationFilterë¡? ë¶??„° ?ƒ?„±?•œ ?† ?°?„ ?†µ?•˜?—¬ ?˜UserDetailsService?™ë?? ?†µ?•´ ?°?´?„°ë² ì´?Š¤ ?‚´?—?„œ ? •ë³´ë?? ì¡°íšŒ?•©?‹ˆ?‹¤.
 * 
 * @  ?ˆ˜? •?¼            ?ˆ˜? •?       ?ˆ˜? •?‚´?š©
 * @ ---------       ---------   -------------------------------
 * @ 2023.02.08       ? •ë¯¼ì„œ       ìµœì´ˆ?ƒ?„±
 *
 * @author ? •ë¯¼ì„œ
 * @since  2023.02.08
 *
 */
@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Resource
    private UserDetailsService userDetailsService;

    @NonNull
    private BCryptPasswordEncoder passwordEncoder;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.debug("2.CustomAuthenticationProvider");

        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;

        // 'AuthenticaionFilter' ?—?„œ ?ƒ?„±?œ ?† ?°?œ¼ë¡œë??„° ?•„?´?””?? ë¹„ë?ë²ˆí˜¸ë¥? ì¡°íšŒ?•¨
        String userId = token.getName();
        String userPw = (String) token.getCredentials();

        // Spring Security - UserDetailsServiceë¥? ?†µ?•´ DB?—?„œ ?•„?´?””ë¡? ?‚¬?š©? ì¡°íšŒ
        UserVO user = (UserVO) userDetailsService.loadUserByUsername(userId);
        
        if(!passwordEncoder.matches(userPw, user.getPassword())) {
        	throw new BadCredentialsException(user.getUsername() + "Invalid password");
		}

		if(!user.isEnabled()) {
			throw new BadCredentialsException(user.getUsername() + "user Enabled");
		}
        
        return new UsernamePasswordAuthenticationToken(user, userPw, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }


}
