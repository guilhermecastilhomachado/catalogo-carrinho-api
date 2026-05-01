package br.ufu.catalogocarrinhoapi.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "produtos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do produto e obrigatorio.")
    @Size(max = 150, message = "O nome do produto deve ter no maximo 150 caracteres.")
    @Column(nullable = false, length = 150)
    private String nome;

    @Size(max = 255, message = "A descricao deve ter no maximo 255 caracteres.")
    @Column(length = 255)
    private String descricao;

    @NotNull(message = "O preco do produto e obrigatorio.")
    @DecimalMin(value = "0.01", message = "O preco deve ser maior que zero.")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    @NotNull(message = "A quantidade em estoque e obrigatoria.")
    @Min(value = 0, message = "O estoque nao pode ser negativo.")
    @Column(nullable = false)
    private Integer quantidadeEstoque;

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;
}