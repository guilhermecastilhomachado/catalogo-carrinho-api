package br.ufu.catalogocarrinhoapi.excecao;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class TratadorGlobalExcecao {

    @ExceptionHandler(RecursoNaoEncontradoExcecao.class)
    public ResponseEntity<RespostaErro> tratarRecursoNaoEncontrado(
            RecursoNaoEncontradoExcecao excecao,
            HttpServletRequest request) {

        RespostaErro respostaErro = RespostaErro.builder()
                .dataHora(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .erro("Recurso nao encontrado")
                .mensagem(excecao.getMessage())
                .caminho(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respostaErro);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RespostaErro> tratarErroValidacao(
            MethodArgumentNotValidException excecao,
            HttpServletRequest request) {

        Map<String, String> camposComErro = new LinkedHashMap<>();

        excecao.getBindingResult().getFieldErrors().forEach(erro ->
                camposComErro.put(erro.getField(), erro.getDefaultMessage())
        );

        RespostaErro respostaErro = RespostaErro.builder()
                .dataHora(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .erro("Erro de validacao")
                .mensagem("Um ou mais campos estao invalidos.")
                .caminho(request.getRequestURI())
                .campos(camposComErro)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respostaErro);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<RespostaErro> tratarRegraDeNegocio(
            IllegalArgumentException excecao,
            HttpServletRequest request) {

        RespostaErro respostaErro = RespostaErro.builder()
                .dataHora(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .erro("Regra de negocio violada")
                .mensagem(excecao.getMessage())
                .caminho(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respostaErro);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RespostaErro> tratarErroGenerico(
            Exception excecao,
            HttpServletRequest request) {

        RespostaErro respostaErro = RespostaErro.builder()
                .dataHora(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .erro("Erro interno do servidor")
                .mensagem("Ocorreu um erro inesperado na aplicacao.")
                .caminho(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respostaErro);
    }
}