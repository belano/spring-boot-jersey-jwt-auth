package com.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;

import com.example.security.EntryPointUnauthorizedHandler;
import com.example.security.PreAuthenticatedTokenHeaderProcessingFilter;
import com.example.security.TokenBasedUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${demo.token.header}")
    private String tokenHeader;

    @Autowired
    private EntryPointUnauthorizedHandler unauthorizedHandler;

    @Bean
    @Override
    public UserDetailsService userDetailsServiceBean() throws Exception {
        return super.userDetailsServiceBean();
    }

    @Autowired
    public void registerGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(preauthAuthProvider())
            .inMemoryAuthentication()
            .withUser("user")
            .password("password")
            .roles("USER")
            .and()
            .withUser("admin")
            .password("password")
            .roles("USER", "ADMIN");
    }

    @Bean
    public TokenBasedUserDetailsService tokenBasedUserDetailsService() {
        return new TokenBasedUserDetailsService();
    }

    @Bean
    public PreAuthenticatedAuthenticationProvider preauthAuthProvider() {
        PreAuthenticatedAuthenticationProvider preauthAuthProvider = new PreAuthenticatedAuthenticationProvider();
        preauthAuthProvider.setPreAuthenticatedUserDetailsService(tokenBasedUserDetailsService());
        return preauthAuthProvider;
    }

    @Bean
    public PreAuthenticatedTokenHeaderProcessingFilter requestHeaderAuthenticationFilter(
            final AuthenticationManager authenticationManager) {
        PreAuthenticatedTokenHeaderProcessingFilter filter = new PreAuthenticatedTokenHeaderProcessingFilter();
        filter.setAuthenticationManager(authenticationManager);
        filter.setExceptionIfHeaderMissing(false);
        filter.setPrincipalRequestHeader(tokenHeader);
        filter.setInvalidateSessionOnPrincipalChange(true);
        filter.setCheckForPrincipalChanges(true);
        filter.setContinueFilterChainOnUnsuccessfulAuthentication(false);
        return filter;
    }

    // See https://github.com/spring-projects/spring-boot/issues/2173
    @Bean
    public FilterRegistrationBean registration(PreAuthenticatedTokenHeaderProcessingFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean(filter);
        registration.setEnabled(false);
        return registration;
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        // @formatter:off
        httpSecurity
            .csrf()
                .disable()
            .exceptionHandling()
                .authenticationEntryPoint(this.unauthorizedHandler)
            .and()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                .authorizeRequests()
                    .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                    .antMatchers("/auth/**").permitAll()
                .anyRequest()
                    .authenticated();
        // @formatter:on

        // Custom JWT based authentication
        httpSecurity.addFilter(requestHeaderAuthenticationFilter(authenticationManager()));
    }

}
