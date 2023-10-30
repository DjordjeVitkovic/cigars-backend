package com.futing.diary.exception;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.security.sasl.AuthenticationException;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GenericExceptionHandler extends ResponseEntityExceptionHandler {

  private final Clock clock;

  @ExceptionHandler(ValidationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  protected ResponseEntity<Object> handleValidationExceptions(Exception ex, WebRequest request) {
    log.warn(ex.getMessage(), ex);
    ExceptionResponseDTO respDto = new ExceptionResponseDTO(LocalDateTime.now(clock),
      HttpStatus.BAD_REQUEST.value(), ex.getMessage(), getPathDetails(request));
    return new ResponseEntity<>(respDto, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(NotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  protected ResponseEntity<Object> handleNotFoundException(Exception ex, WebRequest request) {
    log.warn(ex.getMessage(), ex);
    ExceptionResponseDTO respDto = new ExceptionResponseDTO(LocalDateTime.now(clock),
      HttpStatus.NOT_FOUND.value(), ex.getMessage(), getPathDetails(request));
    return new ResponseEntity<>(respDto, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(BadRequestException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  protected ResponseEntity<Object> handleBadRequestException(Exception ex, WebRequest request) {
    log.warn(ex.getMessage(), ex);
    ExceptionResponseDTO respDto = new ExceptionResponseDTO(LocalDateTime.now(clock),
      HttpStatus.BAD_REQUEST.value(), ex.getMessage(), getPathDetails(request));
    return new ResponseEntity<>(respDto, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(HttpMessageConversionException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  protected ResponseEntity<Object> handleMessageConversionException(Exception ex, WebRequest request) {
    log.warn(ex.getMessage(), ex);
    ExceptionResponseDTO respDto = new ExceptionResponseDTO(LocalDateTime.now(clock),
      HttpStatus.BAD_REQUEST.value(), "Bad input data type!", getPathDetails(request));
    return new ResponseEntity<>(respDto, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  protected ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
    log.error(ex.getMessage(), ex);
    ExceptionResponseDTO respDto = new ExceptionResponseDTO(LocalDateTime.now(clock),
      HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), getPathDetails(request));
    return new ResponseEntity<>(respDto, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(JwtException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<Object> handleJwtException(JwtException ex, WebRequest request) {
    log.warn(ex.getMessage(), ex);
    ExceptionResponseDTO respDto = new ExceptionResponseDTO(LocalDateTime.now(clock),
      HttpStatus.BAD_REQUEST.value(), ex.getMessage(), getPathDetails(request));
    return new ResponseEntity<>(respDto, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(BadCredentialsException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException ex, WebRequest request) {
    log.warn(ex.getMessage(), ex);
    ExceptionResponseDTO respDto = new ExceptionResponseDTO(LocalDateTime.now(clock),
      HttpStatus.UNAUTHORIZED.value(), ex.getMessage(), getPathDetails(request));
    return new ResponseEntity<>(respDto, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(AuthorizationException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ResponseEntity<Object> handleAuthorizationException(AuthorizationException ex, WebRequest request) {
    log.warn(ex.getMessage(), ex);
    ExceptionResponseDTO respDto = new ExceptionResponseDTO(LocalDateTime.now(clock),
      HttpStatus.UNAUTHORIZED.value(), ex.getMessage(), getPathDetails(request));
    return new ResponseEntity<>(respDto, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(AuthenticationException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ResponseEntity<Object> handleAuthenticationException(AuthenticationException ex, WebRequest request) {
    log.warn(ex.getMessage(), ex);
    ExceptionResponseDTO respDto = new ExceptionResponseDTO(LocalDateTime.now(clock),
      HttpStatus.UNAUTHORIZED.value(), ex.getMessage(), getPathDetails(request));
    return new ResponseEntity<>(respDto, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request) {
    log.warn(ex.getMessage(), ex);
    ExceptionResponseDTO respDto = new ExceptionResponseDTO(LocalDateTime.now(clock),
      HttpStatus.BAD_REQUEST.value(), ex.getMessage(), getPathDetails(request));
    return new ResponseEntity<>(respDto, HttpStatus.BAD_REQUEST);
  }

  private String getPathDetails(WebRequest request) {
    String webRequest = request.toString();
    return webRequest.substring(webRequest.indexOf('=') + 1, webRequest.indexOf(';'));
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
    List<String> details = ex.getBindingResult().getAllErrors().stream()
      .map(ObjectError::getDefaultMessage)
      .collect(Collectors.toList());

    ExceptionResponseDTO respDto = new ExceptionResponseDTO(LocalDateTime.now(clock), HttpStatus.BAD_REQUEST.value(),
      details.toString(), getPathDetails(request));
    log.warn(respDto.getExceptionMessage());
    return new ResponseEntity<>(respDto, HttpStatus.BAD_REQUEST);
  }
}
