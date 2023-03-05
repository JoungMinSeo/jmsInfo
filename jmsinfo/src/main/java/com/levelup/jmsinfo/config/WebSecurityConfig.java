package com.levelup.jmsinfo.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.levelup.jmsinfo.config.filter.CustomAuthenticationFilter;
import com.levelup.jmsinfo.config.filter.JwtAuthorizationFilter;
import com.levelup.jmsinfo.config.handler.CustomAuthFailureHandler;
import com.levelup.jmsinfo.config.handler.CustomAuthSuccessHandler;
import com.levelup.jmsinfo.config.handler.CustomAuthenticationProvider;
/**
 * @purpose Spring Security ?™˜ê²? ?„¤? •
 * ?›¹ ?„œë¹„ìŠ¤ê°? ë¡œë“œ ? ?•Œ Spring Container ?˜?•´ ê´?ë¦¬ê? ?˜?Š” ?´?˜?Š¤?´ë©? ?‚¬?š©??— ???•œ ?˜ì¸ì¦â?™ê³¼ ?˜ì¸ê°??™ì— ???•œ êµ¬ì„±?„ Bean ë©”ì„œ?“œë¡? ì£¼ì…
 * 
 * @  ?ˆ˜? •?¼            ?ˆ˜? •?       ?ˆ˜? •?‚´?š©
 * @ ---------       ---------   -------------------------------
 * @ 2023.02.08       ? •ë¯¼ì„œ       ìµœì´ˆ?ƒ?„±
 *
 * @author ? •ë¯¼ì„œ
 * @since  2023.02.08
 *
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    /**
     * 1. ? •?  ??›(Resource)?— ???•´?„œ ?¸ì¦ëœ ?‚¬?š©?ê°?  ? •?  ??›?˜ ? ‘ê·¼ì— ???•´ ?˜ì¸ê°??™ì— ???•œ ?„¤? •?„ ?‹´?‹¹?•˜?Š” ë©”ì„œ?“œ?´?‹¤.
     *
     * @return WebSecurityCustomizer
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // ? •?  ??›?— ???•´?„œ Securityë¥? ? ?š©?•˜ì§? ?•Š?Œ?œ¼ë¡? ?„¤? •
        return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    /**
     * 2. HTTP?— ???•´?„œ ?˜ì¸ì¦â?™ê³¼ ?˜ì¸ê°??™ë?? ?‹´?‹¹?•˜?Š” ë©”ì„œ?“œ?´ë©? ?•„?„°ë¥? ?†µ?•´ ?¸ì¦? ë°©ì‹ê³? ?¸ì¦? ? ˆì°¨ì— ???•´?„œ ?“±ë¡í•˜ë©? ?„¤? •?„ ?‹´?‹¹?•˜?Š” ë©”ì„œ?“œ?´?‹¤.
     *
     * @param http HttpSecurity
     * @return SecurityFilterChain
     * @throws Exception Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // [STEP1] ?„œë²„ì— ?¸ì¦ì •ë³´ë?? ???¥?•˜ì§? ?•Šê¸°ì— csrfë¥? ?‚¬?š©?•˜ì§? ?•Š?Š”?‹¤.
        http.csrf().disable();

        // [STEP2] form ê¸°ë°˜?˜ ë¡œê·¸?¸?— ???•´ ë¹? ?™œ?„±?™”?•˜ë©? ì»¤ìŠ¤???œ¼ë¡? êµ¬ì„±?•œ ?•„?„°ë¥? ?‚¬?š©?•œ?‹¤.
        http.formLogin().disable();

        // [STEP3] ?† ?°?„ ?™œ?š©?•˜?Š” ê²½ìš° ëª¨ë“  ?š”ì²??— ???•´ '?¸ê°?'?— ???•´?„œ ?‚¬?š©.
        http.authorizeHttpRequests((authz) -> authz.anyRequest().permitAll());

        // [STEP4] Spring Security Custom Filter Load - Form '?¸ì¦?'?— ???•´?„œ ?‚¬?š©
        http.addFilterBefore(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        // [STEP5] Session ê¸°ë°˜?˜ ?¸ì¦ê¸°ë°˜ì„ ?‚¬?š©?•˜ì§? ?•Šê³? JWTë¥? ?´?š©?•˜?—¬?„œ ?¸ì¦?
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // [STEP6] Spring Security JWT Filter Load
        http.addFilterBefore(jwtAuthorizationFilter(), BasicAuthenticationFilter.class);

        // [STEP7] ìµœì¢… êµ¬ì„±?•œ ê°’ì„ ?‚¬?š©?•¨.
        return http.build();
    }


    /**
     * 3. authenticate ?˜ ?¸ì¦? ë©”ì„œ?“œë¥? ? œê³µí•˜?Š” ë§¤ë‹ˆ? ¸ë¡?'Provider'?˜ ?¸?„°?˜?´?Š¤ë¥? ?˜ë¯¸í•©?‹ˆ?‹¤.
     * - ê³¼ì •: CustomAuthenticationFilter ?†’ AuthenticationManager(interface) ?†’ CustomAuthenticationProvider(implements)
     *
     * @return AuthenticationManager
     */
    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(customAuthenticationProvider());
    }

    /**
     * 4. '?¸ì¦?' ? œê³µìë¡? ?‚¬?š©??˜ ?´ë¦„ê³¼ ë¹„ë?ë²ˆí˜¸ê°? ?š”êµ¬ë©?‹ˆ?‹¤.
     * - ê³¼ì •: CustomAuthenticationFilter ?†’ AuthenticationManager(interface) ?†’ CustomAuthenticationProvider(implements)
     *
     * @return CustomAuthenticationProvider
     */
    @Bean
    public CustomAuthenticationProvider customAuthenticationProvider() {
        return new CustomAuthenticationProvider(bCryptPasswordEncoder());
    }

    /**
     * 5. ë¹„ë?ë²ˆí˜¸ë¥? ?•”?˜¸?™”?•˜ê¸? ?œ„?•œ BCrypt ?¸ì½”ë”©?„ ?†µ?•˜?—¬ ë¹„ë?ë²ˆí˜¸?— ???•œ ?•”?˜¸?™”ë¥? ?ˆ˜?–‰?•©?‹ˆ?‹¤.
     *
     * @return BCryptPasswordEncoder
     */
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    /**
     * 6. ì»¤ìŠ¤???„ ?ˆ˜?–‰?•œ '?¸ì¦?' ?•„?„°ë¡? ? ‘ê·? URL, ?°?´?„° ? „?‹¬ë°©ì‹(form) ?“± ?¸ì¦? ê³¼ì • ë°? ?¸ì¦? ?›„ ì²˜ë¦¬?— ???•œ ?„¤? •?„ êµ¬ì„±?•˜?Š” ë©”ì„œ?“œ?…?‹ˆ?‹¤.
     *
     * @return CustomAuthenticationFilter
     */
    @Bean
    public CustomAuthenticationFilter customAuthenticationFilter() {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManager());
        customAuthenticationFilter.setFilterProcessesUrl("/user/login");     // ? ‘ê·? URL
        customAuthenticationFilter.setAuthenticationSuccessHandler(customLoginSuccessHandler());    // '?¸ì¦?' ?„±ê³? ?‹œ ?•´?‹¹ ?•¸?“¤?Ÿ¬ë¡? ì²˜ë¦¬ë¥? ? „ê°??•œ?‹¤.
        customAuthenticationFilter.setAuthenticationFailureHandler(customLoginFailureHandler());    // '?¸ì¦?' ?‹¤?Œ¨ ?‹œ ?•´?‹¹ ?•¸?“¤?Ÿ¬ë¡? ì²˜ë¦¬ë¥? ? „ê°??•œ?‹¤.
        customAuthenticationFilter.afterPropertiesSet();
        return customAuthenticationFilter;
    }

    /**
     * 7. Spring Security ê¸°ë°˜?˜ ?‚¬?š©??˜ ? •ë³´ê? ë§ì„ ê²½ìš° ?ˆ˜?–‰?´ ?˜ë©? ê²°ê³¼ê°’ì„ ë¦¬í„´?•´ì£¼ëŠ” Handler
     *
     * @return CustomLoginSuccessHandler
     */
    @Bean
    public CustomAuthSuccessHandler customLoginSuccessHandler() {
        return new CustomAuthSuccessHandler();
    }

    /**
     * 8. Spring Security ê¸°ë°˜?˜ ?‚¬?š©??˜ ? •ë³´ê? ë§ì? ?•Š?„ ê²½ìš° ?ˆ˜?–‰?´ ?˜ë©? ê²°ê³¼ê°’ì„ ë¦¬í„´?•´ì£¼ëŠ” Handler
     *
     * @return CustomAuthFailureHandler
     */
    @Bean
    public CustomAuthFailureHandler customLoginFailureHandler() {
        return new CustomAuthFailureHandler();
    }


    /**
     * 9. JWT ?† ?°?„ ?†µ?•˜?—¬?„œ ?‚¬?š©?ë¥? ?¸ì¦í•©?‹ˆ?‹¤.
     *
     * @return JwtAuthorizationFilter
     */
    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter();
    }


}