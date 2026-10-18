package br.com.fiap.mercadoexpress.usuario.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CadastroForm {

    @NotBlank(message = "Informe seu nome")
    private String nome;

    @NotBlank(message = "Informe um nome de usuário")
    @Size(min = 3, max = 50, message = "O usuário deve ter entre 3 e 50 caracteres")
    private String username;

    @NotBlank(message = "Informe uma senha")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    private String senha;
}
