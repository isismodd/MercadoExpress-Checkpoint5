package br.com.fiap.mercadoexpress.usuario.controller;

import br.com.fiap.mercadoexpress.usuario.dto.CadastroForm;
import br.com.fiap.mercadoexpress.usuario.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/cadastro")
    public String cadastro(Model model) {
        model.addAttribute("cadastro", new CadastroForm());
        return "auth/cadastro";
    }

    @PostMapping("/cadastro")
    public String registrar(@Valid @ModelAttribute("cadastro") CadastroForm form, BindingResult result) {
        if (usuarioService.usernameExiste(form.getUsername())) {
            result.rejectValue("username", "duplicado", "Este usuário já existe");
        }
        if (result.hasErrors()) {
            return "auth/cadastro";
        }
        usuarioService.registrar(form);
        return "redirect:/login?cadastrado";
    }

    @GetMapping("/acesso-negado")
    public String acessoNegado() {
        return "auth/acesso-negado";
    }
}
