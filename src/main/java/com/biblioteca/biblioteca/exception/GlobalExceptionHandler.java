package com.biblioteca.biblioteca.exception;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

// Esta classe centraliza o tratamento de exceptions da aplicacao.
//
// @RestControllerAdvice diz ao Spring:
// "sempre que uma exception acontecer nos controllers,
// use esta classe para decidir a resposta HTTP".
//
// Isso evita repetir try/catch em todo controller.
//
// Em projetos reais, esta e uma das abordagens mais comuns e saudaveis
// para tratamento global de erros em APIs REST.
//
// Importante para conectar com Spring:
// esta classe e um bean gerenciado pelo framework.
// O Spring a encontra automaticamente por causa do component scan.
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Este metodo trata o caso em que um recurso nao foi encontrado.
    //
    // @ExceptionHandler diz ao Spring:
    // "quando essa exception acontecer, use este metodo".
    //
    // Assim, se o service lancar RecursoNaoEncontradoException,
    // o Spring desvia para ca e monta a resposta HTTP 404.
    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroResposta> handleRecursoNaoEncontrado(
        RecursoNaoEncontradoException ex,
        HttpServletRequest request
    ) {
        ErroResposta erro = new ErroResposta(
            LocalDateTime.now(),
            HttpStatus.NOT_FOUND.value(),
            HttpStatus.NOT_FOUND.getReasonPhrase(),
            ex.getMessage(),
            request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    // Este metodo trata erros de regra de negocio.
    //
    // Exemplo futuro:
    // tentar emprestar um livro que nao tem quantidade disponivel.
    @ExceptionHandler(RegraDeNegocioException.class)
    public ResponseEntity<ErroResposta> handleRegraDeNegocio(
        RegraDeNegocioException ex,
        HttpServletRequest request
    ) {
        ErroResposta erro = new ErroResposta(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST.value(),
            HttpStatus.BAD_REQUEST.getReasonPhrase(),
            ex.getMessage(),
            request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    // Este metodo trata falhas de validacao dos DTOs.
    //
    // Exemplo:
    // se UsuarioRequest chegar com email invalido ou documento fora do padrao,
    // o Spring pode lancar MethodArgumentNotValidException.
    //
    // Aqui pegamos esses erros e montamos uma resposta mais amigavel.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidacao(
        MethodArgumentNotValidException ex,
        HttpServletRequest request
    ) {
        Map<String, String> camposComErro = new HashMap<>();

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            camposComErro.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("timestamp", LocalDateTime.now());
        resposta.put("status", HttpStatus.BAD_REQUEST.value());
        resposta.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());
        resposta.put("message", "Erro de validacao nos campos enviados.");
        resposta.put("path", request.getRequestURI());
        resposta.put("fields", camposComErro);

        return ResponseEntity.badRequest().body(resposta);
    }

    // Este tratamento cobre o caso em que a rota requisitada nao existe.
    //
    // Exemplo:
    // POST /
    // GET /rota-que-nao-existe
    //
    // Sem esse tratamento, esse tipo de erro poderia cair no fallback generico
    // e aparecer como 500, o que seria conceitualmente errado.
    //
    // Se a rota nao existe, o status correto e 404 Not Found.
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErroResposta> handleNoResourceFound(
        NoResourceFoundException ex,
        HttpServletRequest request
    ) {
        ErroResposta erro = new ErroResposta(
            LocalDateTime.now(),
            HttpStatus.NOT_FOUND.value(),
            HttpStatus.NOT_FOUND.getReasonPhrase(),
            "A rota solicitada nao foi encontrada.",
            request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    // Este tratamento cobre o caso em que a rota existe,
    // mas o metodo HTTP usado nao e aceito.
    //
    // Exemplo:
    // enviar POST para um endpoint que so aceita GET.
    //
    // Nessa situacao, o status mais correto e 405 Method Not Allowed.
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErroResposta> handleMetodoNaoSuportado(
        HttpRequestMethodNotSupportedException ex,
        HttpServletRequest request
    ) {
        ErroResposta erro = new ErroResposta(
            LocalDateTime.now(),
            HttpStatus.METHOD_NOT_ALLOWED.value(),
            HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase(),
            "O metodo HTTP utilizado nao e suportado para esta rota.",
            request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(erro);
    }

    // Este e um tratamento generico para erros inesperados.
    //
    // Boa pratica:
    // ter um fallback final evita que a API devolva stack traces crus
    // ou respostas despadronizadas para o cliente.
    //
    // Em ambiente real, tambem seria comum registrar logs detalhados aqui.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResposta> handleGenerica(
        Exception ex,
        HttpServletRequest request
    ) {
        ErroResposta erro = new ErroResposta(
            LocalDateTime.now(),
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
            "Ocorreu um erro interno inesperado.",
            request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
    }
}
