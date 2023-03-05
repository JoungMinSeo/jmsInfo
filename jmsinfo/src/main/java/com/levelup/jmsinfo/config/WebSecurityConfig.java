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
 * @purpose Spring Security ?���? ?��?��
 * ?�� ?��비스�? 로드 ?��?�� Spring Container ?��?�� �?리�? ?��?�� ?��?��?��?���? ?��?��?��?�� ???�� ?�인증�?�과 ?�인�??�에 ???�� 구성?�� Bean 메서?���? 주입
 * 
 * @  ?��?��?��            ?��?��?��       ?��?��?��?��
 * @ ---------       ---------   -------------------------------
 * @ 2023.02.08       ?��민서       최초?��?��
 *
 * @author ?��민서
 * @since  2023.02.08
 *
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    /**
     * 1. ?��?�� ?��?��(Resource)?�� ???��?�� ?��증된 ?��?��?���?  ?��?�� ?��?��?�� ?��근에 ???�� ?�인�??�에 ???�� ?��?��?�� ?��?��?��?�� 메서?��?��?��.
     *
     * @return WebSecurityCustomizer
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // ?��?�� ?��?��?�� ???��?�� Security�? ?��?��?���? ?��?��?���? ?��?��
        return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    /**
     * 2. HTTP?�� ???��?�� ?�인증�?�과 ?�인�??��?? ?��?��?��?�� 메서?��?���? ?��?���? ?��?�� ?���? 방식�? ?���? ?��차에 ???��?�� ?��록하�? ?��?��?�� ?��?��?��?�� 메서?��?��?��.
     *
     * @param http HttpSecurity
     * @return SecurityFilterChain
     * @throws Exception Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // [STEP1] ?��버에 ?��증정보�?? ???��?���? ?��기에 csrf�? ?��?��?���? ?��?��?��.
        http.csrf().disable();

        // [STEP2] form 기반?�� 로그?��?�� ???�� �? ?��?��?��?���? 커스???���? 구성?�� ?��?���? ?��?��?��?��.
        http.formLogin().disable();

        // [STEP3] ?��?��?�� ?��?��?��?�� 경우 모든 ?���??�� ???�� '?���?'?�� ???��?�� ?��?��.
        http.authorizeHttpRequests((authz) -> authz.anyRequest().permitAll());

        // [STEP4] Spring Security Custom Filter Load - Form '?���?'?�� ???��?�� ?��?��
        http.addFilterBefore(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        // [STEP5] Session 기반?�� ?��증기반을 ?��?��?���? ?���? JWT�? ?��?��?��?��?�� ?���?
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // [STEP6] Spring Security JWT Filter Load
        http.addFilterBefore(jwtAuthorizationFilter(), BasicAuthenticationFilter.class);

        // [STEP7] 최종 구성?�� 값을 ?��?��?��.
        return http.build();
    }


    /**
     * 3. authenticate ?�� ?���? 메서?���? ?��공하?�� 매니?���?'Provider'?�� ?��?��?��?��?���? ?��미합?��?��.
     * - 과정: CustomAuthenticationFilter ?�� AuthenticationManager(interface) ?�� CustomAuthenticationProvider(implements)
     *
     * @return AuthenticationManager
     */
    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(customAuthenticationProvider());
    }

    /**
     * 4. '?���?' ?��공자�? ?��?��?��?�� ?��름과 비�?번호�? ?��구됩?��?��.
     * - 과정: CustomAuthenticationFilter ?�� AuthenticationManager(interface) ?�� CustomAuthenticationProvider(implements)
     *
     * @return CustomAuthenticationProvider
     */
    @Bean
    public CustomAuthenticationProvider customAuthenticationProvider() {
        return new CustomAuthenticationProvider(bCryptPasswordEncoder());
    }

    /**
     * 5. 비�?번호�? ?��?��?��?���? ?��?�� BCrypt ?��코딩?�� ?��?��?�� 비�?번호?�� ???�� ?��?��?���? ?��?��?��?��?��.
     *
     * @return BCryptPasswordEncoder
     */
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    /**
     * 6. 커스???�� ?��?��?�� '?���?' ?��?���? ?���? URL, ?��?��?�� ?��?��방식(form) ?�� ?���? 과정 �? ?���? ?�� 처리?�� ???�� ?��?��?�� 구성?��?�� 메서?��?��?��?��.
     *
     * @return CustomAuthenticationFilter
     */
    @Bean
    public CustomAuthenticationFilter customAuthenticationFilter() {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManager());
        customAuthenticationFilter.setFilterProcessesUrl("/user/login");     // ?���? URL
        customAuthenticationFilter.setAuthenticationSuccessHandler(customLoginSuccessHandler());    // '?���?' ?���? ?�� ?��?�� ?��?��?���? 처리�? ?���??��?��.
        customAuthenticationFilter.setAuthenticationFailureHandler(customLoginFailureHandler());    // '?���?' ?��?�� ?�� ?��?�� ?��?��?���? 처리�? ?���??��?��.
        customAuthenticationFilter.afterPropertiesSet();
        return customAuthenticationFilter;
    }

    /**
     * 7. Spring Security 기반?�� ?��?��?��?�� ?��보�? 맞을 경우 ?��?��?�� ?���? 결과값을 리턴?��주는 Handler
     *
     * @return CustomLoginSuccessHandler
     */
    @Bean
    public CustomAuthSuccessHandler customLoginSuccessHandler() {
        return new CustomAuthSuccessHandler();
    }

    /**
     * 8. Spring Security 기반?�� ?��?��?��?�� ?��보�? 맞�? ?��?�� 경우 ?��?��?�� ?���? 결과값을 리턴?��주는 Handler
     *
     * @return CustomAuthFailureHandler
     */
    @Bean
    public CustomAuthFailureHandler customLoginFailureHandler() {
        return new CustomAuthFailureHandler();
    }


    /**
     * 9. JWT ?��?��?�� ?��?��?��?�� ?��?��?���? ?��증합?��?��.
     *
     * @return JwtAuthorizationFilter
     */
    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter();
    }


}