package hanaro.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import hanaro.jwt.JwtAuthenticationFilter;
import hanaro.response.ApiResponse;
import hanaro.response.code.status.ErrorStatus;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/member/signup",
                    "/member/signin",
                    "/swagger.html",
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/hanaweb/api-docs/**"
                ).permitAll()
                .anyRequest().authenticated()
            )
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint((req, res, e) -> writeError(res, ErrorStatus._UNAUTHORIZED))
                .accessDeniedHandler((req, res, e) -> writeError(res, ErrorStatus.MEMBER_NOT_AUTHORITY))
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    private void writeError(HttpServletResponse res, ErrorStatus status) throws IOException {
        var reason = status.getReasonHttpStatus();
        res.setStatus(reason.getHttpStatusCode().value());
        res.setContentType("application/json;charset=UTF-8");
        objectMapper.writeValue(
            res.getWriter(),
            ApiResponse.onFailure(reason.getCode(), reason.getMessage(), null)
        );
    }
}
