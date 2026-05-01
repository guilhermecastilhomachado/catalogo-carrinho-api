package br.ufu.catalogocarrinhoapi.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "categorias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome da categoria e obrigatorio.")
    @Size(max = 100, message = "O nome da categoria deve ter no maximo 100 caracteres.")
    @Column(nullable = false, length = 100, unique = true)
    private String nome;

    @Size(max = 255, message = "A descricao deve ter no maximo 255 caracteres.")
    @Column(length = 255)
    private String descricao;
}