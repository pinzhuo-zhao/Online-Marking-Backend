package com.feredback.feredback_backend.config;
import com.feredback.feredback_backend.security.*;
import com.feredback.feredback_backend.security.filter.TokenLoginFilter;
import com.feredback.feredback_backend.security.handler.LogoutFailureHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @program: FE-Redback
 * @description:
 * @author: Pinzhuo Zhao, StudentID:1043915
 * @create: 2022-04-11 00:33
 **/
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        // using Bcrypt provided by spring security
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService customUserService() {
        return new UserDetailsServiceImpl();
    }

    @Autowired
    private AuthenticationSuccessHandler successHandler;

    @Autowired
    private AuthenticationFailureHandler failureHandler;

    @Autowired
    private LogoutSuccessHandler logoutHandler;

    @Autowired
    private AuthenticationEntryPoint unauthorizedEntryPoint;

    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    @Autowired
    private TokenLoginFilter tokenLoginFilter;

    @Bean
    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }


    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOriginPattern("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.setAllowCredentials(true);
        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(source);
    }



    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //        auth.userDetailsService(customUserService());
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setHideUserNotFoundExceptions(true);
        provider.setUserDetailsService(customUserService());
        provider.setPasswordEncoder(passwordEncoder());
        auth.authenticationProvider(provider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
     /*   http.authorizeRequests()
                .antMatchers("/api/user/login","/logout").permitAll()
                .anyRequest().authenticated()
                .and().httpBasic()
                .and().csrf().disable()
                .logout().logoutUrl("/logout").logoutSuccessHandler(new LogoutHandler());
*/

//        http.cors().and().csrf().disable();
        http.cors().and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().permitAll().successHandler(successHandler)
                .failureHandler(failureHandler)
                .and()
                .logout().addLogoutHandler(new LogoutFailureHandler()).logoutSuccessHandler(logoutHandler)
                .deleteCookies("JSESSIONID")
                .and().exceptionHandling()
                .authenticationEntryPoint(unauthorizedEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
                .and().authorizeRequests()
                .antMatchers("/api/candidates/getAll").hasAuthority("tutor")
                .antMatchers("/api/**/coordinator/**").hasAuthority("coordinator")
                .antMatchers("/api/**/admin/**").hasAuthority("admin")
                .antMatchers("/api/**").hasAuthority("tutor")
                .antMatchers("/api/user/login").anonymous()

                //only one user can use a particular account at once
                .and().sessionManagement().maximumSessions(1);
        http.addFilterBefore(tokenLoginFilter, UsernamePasswordAuthenticationFilter.class);

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/api/user/sendVerificationCode");
        web.ignoring().antMatchers("/api/user/changePassword");
        web.ignoring().antMatchers("/api/user/login");

    }

    //JWT
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}