
package studioyoga.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import studioyoga.project.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(
                        "/css/**", "/js/**", "/img/**", "/font/**",
                        "/", "/classes", "/landing", "/schedule", "/prices", "/rules", "/events", "/blog", "/guide",
                        "/login", "/faq", "/location", "/formRegister")
                .permitAll()
                // Solo pides login para reservar o ver reservas
                .requestMatchers("/classes/reserve/**", "/classes/myReservations", "/classes/cancelReservation/**")
                .authenticated()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/user/classes/**").hasRole("USER")
                .anyRequest().permitAll()
        )
              // Gestión de sesiones: máximo 1 sesión por usuario, redirección si expira
        .sessionManagement(session -> session
            .maximumSessions(1)
            .expiredUrl("/login?expired")
        )
                // Configuración de login
                .formLogin(form -> form
                        .loginPage("/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .successHandler(authenticationSuccessHandler())
                        .permitAll())
                // Configuración de logout
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll())
                // Manejo de excepciones
                .exceptionHandling(exception -> exception
                        .accessDeniedPage("/403"));

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            if (authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
                response.sendRedirect("/admin/dashboard");
            } else if (authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER"))) {

                response.sendRedirect("/classes");
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
