package br.com.fiap.mercadoexpress.security;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/** Disponibiliza o usuário logado e o perfil para todas as views Thymeleaf. */
@ControllerAdvice
public class GlobalModelAdvice {

    @ModelAttribute("isAdmin")
    public boolean isAdmin(Authentication auth) {
        return auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));
    }

    @ModelAttribute("usuarioLogado")
    public String usuarioLogado(Authentication auth) {
        return auth != null ? auth.getName() : null;
    }
}
