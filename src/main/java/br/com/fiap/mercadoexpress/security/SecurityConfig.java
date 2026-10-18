package br.com.fiap.mercadoexpress.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // públicas
                .requestMatchers("/", "/index.html", "/login", "/cadastro", "/acesso-negado",
                                 "/webjars/**", "/error").permitAll()
                .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**",
                                 "/banco-tranquilo/**").permitAll()
                // somente ADMIN
                .requestMatchers("/admin/**").hasRole("ADMIN")
                // Parte 2: loja/carrinho e API de compras
                .requestMatchers("/loja/**", "/api/compras/**").hasAnyRole("ADMIN", "USER")
                .requestMatchers("/web/produtos/novo", "/web/produtos/*/editar").hasRole("ADMIN")
                // consulta: ADMIN e USER
                .requestMatchers(HttpMethod.GET, "/web/produtos/**").hasAnyRole("ADMIN", "USER")
                // criar, atualizar e deletar (POST): ADMIN
                .requestMatchers("/web/produtos/**").hasRole("ADMIN")
                // API REST/HATEOAS do CP4 (HTTP Basic)
                .requestMatchers(HttpMethod.GET, "/mercado/**").hasAnyRole("ADMIN", "USER")
                .requestMatchers("/mercado/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")                       // tela de login customizada
                .defaultSuccessUrl("/web/produtos", true)  // área restrita após o login
                .failureUrl("/login?error")
                .permitAll()
            )
            .logout(logout -> logout.logoutSuccessUrl("/login?logout").permitAll())
            .httpBasic(Customizer.withDefaults())
            .csrf(csrf -> csrf.ignoringRequestMatchers("/mercado/**", "/api/**", "/banco-tranquilo/**"))
            .exceptionHandling(ex -> ex.accessDeniedPage("/acesso-negado"));
        return http.build();
    }
}
