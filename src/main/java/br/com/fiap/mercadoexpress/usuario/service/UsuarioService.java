package br.com.fiap.mercadoexpress.usuario.service;

import br.com.fiap.mercadoexpress.usuario.dto.CadastroForm;
import br.com.fiap.mercadoexpress.usuario.entity.Usuario;
import br.com.fiap.mercadoexpress.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder encoder;

    public boolean usernameExiste(String username) {
        return repository.existsByUsername(username);
    }

    /** Cadastro público: sempre cria perfil USER. */
    public Usuario registrar(CadastroForm form) {
        return repository.save(Usuario.builder()
                .nome(form.getNome())
                .username(form.getUsername())
                .senha(encoder.encode(form.getSenha()))
                .perfil("USER")
                .build());
    }

    public List<Usuario> findAll() {
        return repository.findAll();
    }
}
