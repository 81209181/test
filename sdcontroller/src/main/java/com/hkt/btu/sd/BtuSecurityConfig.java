package com.hkt.btu.sd;

import com.hkt.btu.common.spring.security.access.BtuAccessDeniedHandler;
import com.hkt.btu.common.spring.security.access.BtuDaoAuthenticationProvider;
import com.hkt.btu.common.spring.security.access.intercept.BtuSecurityInterceptor;
import com.hkt.btu.common.spring.security.access.intercept.BtuSecurityMetadataSource;
import com.hkt.btu.common.spring.security.authentication.LdapAuthenticationProvider;
import com.hkt.btu.common.spring.security.web.authentication.BtuExceptionMappingAuthenticationFailureHandler;
import com.hkt.btu.common.spring.security.web.authentication.BtuLoginSuccessHandler;
import com.hkt.btu.common.spring.security.web.authentication.BtuLoginUrlAuthenticationEntryPoint;
import com.hkt.btu.common.spring.security.web.authentication.logout.BtuLogoutSuccessHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.annotation.Resource;

import static com.hkt.btu.common.spring.security.web.authentication.BtuLoginUrlAuthenticationEntryPoint.LOGIN_URI;


@Configuration
@EnableWebSecurity
public class BtuSecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource(name = "customLoginSuccessHandler")
    BtuLoginSuccessHandler btuLoginSuccessHandler;

    @Resource(name = "customExceptMapAuthFailureHandler")
    BtuExceptionMappingAuthenticationFailureHandler btuExcepMapAuthFailureHandler;

    @Resource(name = "customLogoutSuccessHandler")
    BtuLogoutSuccessHandler btuLogoutSuccessHandler;

    @Resource(name = "customBtuSecurityInterceptor")
    BtuSecurityInterceptor btuSecurityInterceptor;

    @Resource(name = "customBtuDaoAuthenticationProvider")
    BtuDaoAuthenticationProvider btuDaoAuthenticationProvider;

    @Resource(name = "customAccessDeniedHandler")
    BtuAccessDeniedHandler btuAccessDeniedHandler;

    @Resource(name = "customLoginUrlAuthenticationEntryPoint")
    BtuLoginUrlAuthenticationEntryPoint btuLoginUrlAuthenticationEntryPoint;

    @Resource(name = "btuPasswordEncoder")
    BCryptPasswordEncoder btuPasswordEncoder;

    @Resource(name = "LdapAuthenticationProvider")
    LdapAuthenticationProvider ldapAuth;


    @SuppressWarnings("RedundantThrows")
    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        btuDaoAuthenticationProvider.setPasswordEncoder(btuPasswordEncoder);
        //auth.authenticationProvider(btuDaoAuthenticationProvider);
        auth.authenticationProvider(ldapAuth);
        // BCryptPasswordEncoder Online: https://www.dailycred.com/article/bcrypt-calculator
        // BCrypt round: 10 (spring security default)
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
                // Enforce login
                .authorizeRequests()
                .antMatchers(
                        BtuSecurityMetadataSource.RESERVED_ANT_PATH_ROOT,
                        BtuSecurityMetadataSource.RESERVED_ANT_PATH_LOGIN,
                        BtuSecurityMetadataSource.RESERVED_ANT_PATH_RESET_PWD,
                        BtuSecurityMetadataSource.RESERVED_ANT_PATH_RESET_PWD_OTP,
                        BtuSecurityMetadataSource.RESERVED_ANT_PATH_JS,
                        BtuSecurityMetadataSource.RESERVED_ANT_PATH_CSS,
                        BtuSecurityMetadataSource.RESERVED_ANT_PATH_LIB,
                        BtuSecurityMetadataSource.RESERVED_ANT_PATH_IMG,
                        BtuSecurityMetadataSource.RESERVED_ANT_PATH_WEBJAR,
                        BtuSecurityMetadataSource.RESERVED_ANT_PATH_ERROR,
                        BtuSecurityMetadataSource.RESERVED_ANT_PATH_PUBLIC).permitAll()
                .anyRequest().authenticated()

                // Login config
                .and()
                .formLogin()
                .loginPage(LOGIN_URI) // login request mapping
                .successHandler(btuLoginSuccessHandler)
                .failureHandler(btuExcepMapAuthFailureHandler)
                .permitAll()

                // Logout config
                .and()
                .logout()
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/public/logout")) // logout request mapping
                .logoutSuccessHandler(btuLogoutSuccessHandler)
                .permitAll()

                // User group control over incoming uri
                .and()
                .addFilterAfter(btuSecurityInterceptor, FilterSecurityInterceptor.class)

                .exceptionHandling()
                .authenticationEntryPoint(btuLoginUrlAuthenticationEntryPoint)
                .accessDeniedHandler(btuAccessDeniedHandler)
        ;

        /* HTTP falls over HTTPS is done and assured by server setting.
         * Hence, https should not be enforced by application.
         */
//                .requiresChannel()
//                .antMatchers("/**").requiresSecure() // always require https
    }

}