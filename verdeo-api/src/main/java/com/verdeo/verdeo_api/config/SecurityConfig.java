package com.verdeo.verdeo_api.config;

import com.verdeo.verdeo_api.security.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Desactivar CSRF (no usamos cookies, usamos JWT en header)
                .csrf(csrf -> csrf.disable())

                // Sin sesión en servidor — cada request es independiente
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // ──────────────────────────────────────────
                // REGLAS DE ACCESO
                // ──────────────────────────────────────────
                .authorizeHttpRequests(auth -> auth

                        // ── AUTH: completamente público ──
                        .requestMatchers("/api/auth/**").permitAll()

                        // ── PRODUCTOS: leer es público, escribir es ADMIN ──
                        .requestMatchers(HttpMethod.GET,  "/api/products/**").permitAll()
                        .requestMatchers(HttpMethod.POST,   "/api/products/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,    "/api/products/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH,  "/api/products/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/products/**").hasRole("ADMIN")

                        // ── CATEGORÍAS: leer es público, escribir es ADMIN ──
                        .requestMatchers(HttpMethod.GET,  "/api/categories/**").permitAll()
                        .requestMatchers(HttpMethod.POST,   "/api/categories/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,    "/api/categories/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/categories/**").hasRole("ADMIN")

                        // ── CARRITO: requiere estar autenticado ──
                        .requestMatchers("/api/cart/**").authenticated()

                        // ── CONTACTO: enviar es público, leer/borrar es ADMIN ──
                        .requestMatchers(HttpMethod.POST, "/api/contact").permitAll()
                        .requestMatchers("/api/contact/**").hasRole("ADMIN")

                        // ── USUARIOS: solo ADMIN lista todos, cada uno gestiona el suyo ──
                        .requestMatchers(HttpMethod.GET, "/api/users").hasRole("ADMIN")
                        .requestMatchers("/api/users/**").authenticated()

                        // ── REFRESH TOKENS: solo ADMIN (mantenimiento) ──
                        .requestMatchers("/api/refresh-tokens/**").hasRole("ADMIN")

                        // Cualquier otra ruta no listada → requiere autenticación
                        .anyRequest().authenticated()
                )

                // Registrar nuestro filtro JWT ANTES del filtro estándar de Spring
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // PasswordEncoder como Bean para que pueda inyectarse en cualquier Service
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}