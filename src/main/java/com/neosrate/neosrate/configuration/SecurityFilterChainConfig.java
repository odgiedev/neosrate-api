package com.neosrate.neosrate.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityFilterChainConfig {
    final
    SecurityFilter securityFilter;

    public SecurityFilterChainConfig(SecurityFilter securityFilter) {
        this.securityFilter = securityFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/api/user/signin").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/user/create").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/user/get/recent").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/comment/get/all").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/comment/get/all/{community}").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/post/get/all/{maxPerPage}/{userId}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/post/get/recent").permitAll()
                        .requestMatchers(HttpMethod.GET , "/api/post/get/all/user/{maxPerPage}/{userId}/{username}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/post/get/all/community/{maxPerPage}/{community}/{userId}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/post/search/{maxPerPage}/{userId}/{searchQuery}").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/userprofile/get/{username}").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/community/get/community/{community}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/community/get/all/community").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/community/get/participants/{community}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/community/get/owner/{username}").permitAll()

                        .anyRequest().authenticated()
                )

//                        .anyRequest().permitAll())
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
