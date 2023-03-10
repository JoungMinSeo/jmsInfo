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
 * @purpose Spring Security ?κ²? ?€? 
 * ?Ή ?λΉμ€κ°? λ‘λ ? ? Spring Container ??΄ κ΄?λ¦¬κ? ?? ?΄??€?΄λ©? ?¬?©?? ??? ?μΈμ¦β?κ³Ό ?μΈκ°??μ ??? κ΅¬μ±? Bean λ©μ?λ‘? μ£Όμ
 * 
 * @  ?? ?Ό            ?? ?       ?? ?΄?©
 * @ ---------       ---------   -------------------------------
 * @ 2023.02.08       ? λ―Όμ       μ΅μ΄??±
 *
 * @author ? λ―Όμ
 * @since  2023.02.08
 *
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    /**
     * 1. ? ?  ??(Resource)? ???΄? ?Έμ¦λ ?¬?©?κ°?  ? ?  ??? ? κ·Όμ ???΄ ?μΈκ°??μ ??? ?€? ? ?΄?Ή?? λ©μ??΄?€.
     *
     * @return WebSecurityCustomizer
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // ? ?  ??? ???΄? Securityλ₯? ? ?©?μ§? ???Όλ‘? ?€? 
        return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    /**
     * 2. HTTP? ???΄? ?μΈμ¦β?κ³Ό ?μΈκ°??λ?? ?΄?Ή?? λ©μ??΄λ©? ??°λ₯? ?΅?΄ ?Έμ¦? λ°©μκ³? ?Έμ¦? ? μ°¨μ ???΄? ?±λ‘νλ©? ?€? ? ?΄?Ή?? λ©μ??΄?€.
     *
     * @param http HttpSecurity
     * @return SecurityFilterChain
     * @throws Exception Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // [STEP1] ?λ²μ ?Έμ¦μ λ³΄λ?? ???₯?μ§? ?κΈ°μ csrfλ₯? ?¬?©?μ§? ???€.
        http.csrf().disable();

        // [STEP2] form κΈ°λ°? λ‘κ·Έ?Έ? ???΄ λΉ? ??±??λ©? μ»€μ€???Όλ‘? κ΅¬μ±? ??°λ₯? ?¬?©??€.
        http.formLogin().disable();

        // [STEP3] ? ?°? ??©?? κ²½μ° λͺ¨λ  ?μ²?? ???΄ '?Έκ°?'? ???΄? ?¬?©.
        http.authorizeHttpRequests((authz) -> authz.anyRequest().permitAll());

        // [STEP4] Spring Security Custom Filter Load - Form '?Έμ¦?'? ???΄? ?¬?©
        http.addFilterBefore(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        // [STEP5] Session κΈ°λ°? ?Έμ¦κΈ°λ°μ ?¬?©?μ§? ?κ³? JWTλ₯? ?΄?©??¬? ?Έμ¦?
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // [STEP6] Spring Security JWT Filter Load
        http.addFilterBefore(jwtAuthorizationFilter(), BasicAuthenticationFilter.class);

        // [STEP7] μ΅μ’ κ΅¬μ±? κ°μ ?¬?©?¨.
        return http.build();
    }


    /**
     * 3. authenticate ? ?Έμ¦? λ©μ?λ₯? ? κ³΅ν? λ§€λ? Έλ‘?'Provider'? ?Έ?°??΄?€λ₯? ?λ―Έν©??€.
     * - κ³Όμ : CustomAuthenticationFilter ? AuthenticationManager(interface) ? CustomAuthenticationProvider(implements)
     *
     * @return AuthenticationManager
     */
    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(customAuthenticationProvider());
    }

    /**
     * 4. '?Έμ¦?' ? κ³΅μλ‘? ?¬?©?? ?΄λ¦κ³Ό λΉλ?λ²νΈκ°? ?κ΅¬λ©??€.
     * - κ³Όμ : CustomAuthenticationFilter ? AuthenticationManager(interface) ? CustomAuthenticationProvider(implements)
     *
     * @return CustomAuthenticationProvider
     */
    @Bean
    public CustomAuthenticationProvider customAuthenticationProvider() {
        return new CustomAuthenticationProvider(bCryptPasswordEncoder());
    }

    /**
     * 5. λΉλ?λ²νΈλ₯? ??Έ??κΈ? ?? BCrypt ?Έμ½λ©? ?΅??¬ λΉλ?λ²νΈ? ??? ??Έ?λ₯? ???©??€.
     *
     * @return BCryptPasswordEncoder
     */
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    /**
     * 6. μ»€μ€??? ??? '?Έμ¦?' ??°λ‘? ? κ·? URL, ?°?΄?° ? ?¬λ°©μ(form) ?± ?Έμ¦? κ³Όμ  λ°? ?Έμ¦? ? μ²λ¦¬? ??? ?€? ? κ΅¬μ±?? λ©μ????€.
     *
     * @return CustomAuthenticationFilter
     */
    @Bean
    public CustomAuthenticationFilter customAuthenticationFilter() {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManager());
        customAuthenticationFilter.setFilterProcessesUrl("/user/login");     // ? κ·? URL
        customAuthenticationFilter.setAuthenticationSuccessHandler(customLoginSuccessHandler());    // '?Έμ¦?' ?±κ³? ? ?΄?Ή ?Έ?€?¬λ‘? μ²λ¦¬λ₯? ? κ°???€.
        customAuthenticationFilter.setAuthenticationFailureHandler(customLoginFailureHandler());    // '?Έμ¦?' ?€?¨ ? ?΄?Ή ?Έ?€?¬λ‘? μ²λ¦¬λ₯? ? κ°???€.
        customAuthenticationFilter.afterPropertiesSet();
        return customAuthenticationFilter;
    }

    /**
     * 7. Spring Security κΈ°λ°? ?¬?©?? ? λ³΄κ? λ§μ κ²½μ° ???΄ ?λ©? κ²°κ³Όκ°μ λ¦¬ν΄?΄μ£Όλ Handler
     *
     * @return CustomLoginSuccessHandler
     */
    @Bean
    public CustomAuthSuccessHandler customLoginSuccessHandler() {
        return new CustomAuthSuccessHandler();
    }

    /**
     * 8. Spring Security κΈ°λ°? ?¬?©?? ? λ³΄κ? λ§μ? ?? κ²½μ° ???΄ ?λ©? κ²°κ³Όκ°μ λ¦¬ν΄?΄μ£Όλ Handler
     *
     * @return CustomAuthFailureHandler
     */
    @Bean
    public CustomAuthFailureHandler customLoginFailureHandler() {
        return new CustomAuthFailureHandler();
    }


    /**
     * 9. JWT ? ?°? ?΅??¬? ?¬?©?λ₯? ?Έμ¦ν©??€.
     *
     * @return JwtAuthorizationFilter
     */
    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter();
    }


}