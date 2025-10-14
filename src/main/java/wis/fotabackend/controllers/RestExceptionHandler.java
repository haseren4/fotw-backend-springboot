package wis.fotabackend.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class RestExceptionHandler {

    private Map<String, Object> buildBody(HttpStatus status, String error, String message, String path) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", Instant.now().toString());
        body.put("status", status.value());
        body.put("error", error);
        body.put("message", message);
        body.put("path", path);
        return body;
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleResponseStatus(ResponseStatusException ex, WebRequest request) {
        HttpStatus status = ex.getStatusCode() instanceof HttpStatus ? (HttpStatus) ex.getStatusCode() : HttpStatus.valueOf(ex.getStatusCode().value());
        String path = request.getDescription(false).replace("uri=", "");
        Map<String, Object> body = buildBody(status, status.getReasonPhrase(), ex.getReason(), path);
        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String path = request.getDescription(false).replace("uri=", "");
        Map<String, Object> body = buildBody(status, status.getReasonPhrase(), ex.getMessage(), path);
        return ResponseEntity.status(status).body(body);
    }
}
