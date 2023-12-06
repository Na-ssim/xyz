package com.ets.gti619.authmodule.security;

import com.ets.gti619.authmodule.features.role.Roles;
import com.ets.gti619.authmodule.features.user.UserAuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.ets.gti619.authmodule.features.role.Roles.*;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class WebSecurityConfig {

    private UserAuthenticationService userAuthenticationService;
    private PasswordEncoder passwordEncoder;
    private LoginSuccessHandler loginSuccessHandler;
    private LoginFailureHandler loginFailureHandler;
    private UserLoginProtectionFilter userLoginProtectionFilter;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        // configure AuthenticationManager so that it knows from where to load
        // user for matching credentials
        // Use BCryptPasswordEncoder
        auth.userDetailsService(userAuthenticationService).passwordEncoder(passwordEncoder);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
            .addFilterBefore(userLoginProtectionFilter, UsernamePasswordAuthenticationFilter.class)
            .authorizeHttpRequests(authz -> authz
                    // Config Controller
                    .requestMatchers("/events").hasAuthority(ADMIN.getRole())
                    .requestMatchers(HttpMethod.GET, "/locked_out_users").hasAuthority(ADMIN.getRole())
                    .requestMatchers(HttpMethod.POST, "/locked_out_users").hasAuthority(ADMIN.getRole())
                    .requestMatchers("/clients_residentiels").hasAnyAuthority(ADMIN.getRole(), PREP_CLIENTS_RESIDENTIELS.getRole())
                    .requestMatchers("/clients_affaire").hasAnyAuthority(ADMIN.getRole(), PREP_CLIENTS_AFFAIRE.getRole())
                    .requestMatchers("/config_params").hasAuthority(ADMIN.getRole())
                    // Unsecured pages
                    .requestMatchers("/login-failed").permitAll()
                    .requestMatchers("/locked-out").permitAll()
                    .requestMatchers("/error").permitAll()
                    .requestMatchers("/logout").permitAll()
                    .requestMatchers("/too-many-logins").permitAll()
                    .requestMatchers("/js/**").permitAll()
                    .requestMatchers("/config").hasAnyAuthority(ADMIN.getRole(), PREP_CLIENTS_AFFAIRE.getRole(), PREP_CLIENTS_RESIDENTIELS.getRole())
                    // User Controller
                    .requestMatchers(HttpMethod.GET, "/new").hasAuthority(ADMIN.getRole())
                    .requestMatchers(HttpMethod.POST, "/new").hasAuthority(ADMIN.getRole())
                    .requestMatchers(HttpMethod.GET, "/update_password").hasAnyAuthority(ADMIN.getRole(), PREP_CLIENTS_AFFAIRE.getRole(), PREP_CLIENTS_RESIDENTIELS.getRole())
                    .requestMatchers(HttpMethod.POST, "/update_password").hasAnyAuthority(ADMIN.getRole(), PREP_CLIENTS_AFFAIRE.getRole(), PREP_CLIENTS_RESIDENTIELS.getRole())
                    .requestMatchers(HttpMethod.GET, "/reset_expired_password").hasAnyAuthority(ADMIN.getRole(), PREP_CLIENTS_AFFAIRE.getRole(), PREP_CLIENTS_RESIDENTIELS.getRole())
                    .requestMatchers(HttpMethod.POST, "reset_expired_password").hasAnyAuthority(ADMIN.getRole(), PREP_CLIENTS_AFFAIRE.getRole(), PREP_CLIENTS_RESIDENTIELS.getRole())
                    .requestMatchers(HttpMethod.POST, "/temp_pwd").hasAuthority(ADMIN.getRole())
                    .anyRequest().authenticated())
            .formLogin(form -> form
                    .loginPage("/login")
                    .successHandler(loginSuccessHandler)
                    .failureHandler(loginFailureHandler)
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .permitAll())
            .logout(logout -> logout
                    .logoutSuccessUrl("/login?logout")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID"));

        return httpSecurity.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfiguration) throws Exception {
        return authConfiguration.getAuthenticationManager();
    }
}
