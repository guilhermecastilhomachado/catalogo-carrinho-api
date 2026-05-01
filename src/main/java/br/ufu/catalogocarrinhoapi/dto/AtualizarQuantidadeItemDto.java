package br.ufu.catalogocarrinhoapi.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AtualizarQuantidadeItemDto {

    @NotNull(message = "A quantidade e obrigatoria.")
    @Min(value = 1, message = "A quantidade deve ser no minimo 1.")
    private Integer quantidade;
}