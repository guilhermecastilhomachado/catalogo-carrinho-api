package br.ufu.catalogocarrinhoapi.excecao;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Builder
public class RespostaErro {

    private LocalDateTime dataHora;
    private Integer status;
    private String erro;
    private String mensagem;
    private String caminho;
    private Map<String, String> campos;
}