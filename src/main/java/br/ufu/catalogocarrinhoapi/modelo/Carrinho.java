package br.ufu.catalogocarrinhoapi.modelo;

import br.ufu.catalogocarrinhoapi.enumeracao.StatusCarrinho;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carrinhos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Carrinho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusCarrinho status;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valorTotal;

    @Column(nullable = false)
    private LocalDateTime dataCriacao;

    @OneToMany(mappedBy = "carrinho", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @Builder.Default
    private List<ItemCarrinho> itens = new ArrayList<>();

    @PrePersist
    public void antesDeSalvar() {
        this.dataCriacao = LocalDateTime.now();

        if (this.status == null) {
            this.status = StatusCarrinho.ABERTO;
        }

        if (this.valorTotal == null) {
            this.valorTotal = BigDecimal.ZERO;
        }
    }
}