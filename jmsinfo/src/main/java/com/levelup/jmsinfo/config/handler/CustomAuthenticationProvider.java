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
 * @purpose ? ?¬λ°μ? ?¬?©?? ??΄??? λΉλ?λ²νΈλ₯? κΈ°λ°?Όλ‘? λΉμ¦??€ λ‘μ§? μ²λ¦¬??¬ ?¬?©?? ?μΈμ¦β?μ ???΄? κ²?μ¦μ ???? ?΄??€???€.
 * CustomAuthenticationFilterλ‘? λΆ??° ??±? ? ?°? ?΅??¬ ?UserDetailsService?λ?? ?΅?΄ ?°?΄?°λ² μ΄?€ ?΄?? ? λ³΄λ?? μ‘°ν?©??€.
 * 
 * @  ?? ?Ό            ?? ?       ?? ?΄?©
 * @ ---------       ---------   -------------------------------
 * @ 2023.02.08       ? λ―Όμ       μ΅μ΄??±
 *
 * @author ? λ―Όμ
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

        // 'AuthenticaionFilter' ?? ??±? ? ?°?Όλ‘λ??° ??΄??? λΉλ?λ²νΈλ₯? μ‘°ν?¨
        String userId = token.getName();
        String userPw = (String) token.getCredentials();

        // Spring Security - UserDetailsServiceλ₯? ?΅?΄ DB?? ??΄?λ‘? ?¬?©? μ‘°ν
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
