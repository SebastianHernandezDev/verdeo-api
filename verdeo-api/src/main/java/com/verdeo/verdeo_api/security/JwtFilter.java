package com.verdeo.verdeo_api.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * Se ejecuta UNA VEZ por cada request.
 * Si el token es válido, registra al usuario en el SecurityContext
 * para que Spring Security sepa quién está haciendo la petición.
 */
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Leer el header Authorization
        String authHeader = request.getHeader("Authorization");

        // 2. Si no hay header o no empieza con "Bearer ", dejamos pasar
        //    (SecurityConfig decidirá si la ruta es pública o no)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Extraer el token (quitar "Bearer ")
        String token = authHeader.substring(7);

        // 4. Validar el token
        if (!jwtUtil.isValid(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 5. Extraer userId y role del payload
        UUID userId = jwtUtil.extractUserId(token);
        String role  = jwtUtil.extractRole(token);

        // 6. Crear objeto de autenticación con el role
        //    Spring Security usa "ROLE_" como prefijo internamente
        var authority = new SimpleGrantedAuthority("ROLE_" + role);
        var authentication = new UsernamePasswordAuthenticationToken(
                userId,        // principal → podemos recuperarlo con @AuthenticationPrincipal
                null,          // credentials → no necesarias después de validar
                List.of(authority)
        );

        // 7. Registrar en el contexto de seguridad
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 8. Continuar con el request
        filterChain.doFilter(request, response);
    }
}