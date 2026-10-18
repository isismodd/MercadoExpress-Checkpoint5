package br.com.fiap.mercadoexpress.security;

import br.com.fiap.mercadoexpress.usuario.entity.Usuario;
import br.com.fiap.mercadoexpress.usuario.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/** Cria o usuário ADMIN inicial, caso ainda não exista. */
@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner criarAdmin(UsuarioRepository repository, PasswordEncoder encoder,
                                 @Value("${app.admin.username}") String username,
                                 @Value("${app.admin.senha}") String senha) {
        return args -> {
            if (!repository.existsByUsername(username)) {
                repository.save(Usuario.builder()
                        .nome("Administrador")
                        .username(username)
                        .senha(encoder.encode(senha))
                        .perfil("ADMIN")
                        .build());
            }
        };
    }
}
