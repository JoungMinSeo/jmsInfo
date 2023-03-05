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
 * @purpose ?��?��받�? ?��?��?��?�� ?��?��?��?? 비�?번호�? 기반?���? 비즈?��?�� 로직?�� 처리?��?�� ?��?��?��?�� ?�인증�?�에 ???��?�� �?증을 ?��?��?��?�� ?��?��?��?��?��?��.
 * CustomAuthenticationFilter�? �??�� ?��?��?�� ?��?��?�� ?��?��?�� ?�UserDetailsService?��?? ?��?�� ?��?��?��베이?�� ?��?��?�� ?��보�?? 조회?��?��?��.
 * 
 * @  ?��?��?��            ?��?��?��       ?��?��?��?��
 * @ ---------       ---------   -------------------------------
 * @ 2023.02.08       ?��민서       최초?��?��
 *
 * @author ?��민서
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

        // 'AuthenticaionFilter' ?��?�� ?��?��?�� ?��?��?��로�??�� ?��?��?��?? 비�?번호�? 조회?��
        String userId = token.getName();
        String userPw = (String) token.getCredentials();

        // Spring Security - UserDetailsService�? ?��?�� DB?��?�� ?��?��?���? ?��?��?�� 조회
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
