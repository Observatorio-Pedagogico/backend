package com.obervatorio_pedagogico.backend.infrastructure.utils.httpResponse;

import com.obervatorio_pedagogico.backend.presentation.shared.Response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ResponseService {
    private final MessageService messageService;

    public ResponseService(MessageService messageService) {
        this.messageService = messageService;
    }

    public <T> ResponseEntity<Response<T>> create(T data) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new Response<T>(data));
    }

    public <T> ResponseEntity<Response<T>> ok(T data) {
        return ResponseEntity.ok(new Response<T>(data));
    }

    public <T> ResponseEntity<Response<String>> ok(String messageSource) {
        return ResponseEntity.ok(new Response<String>(messageService.getMessage(messageSource)));
    }

    public <T> ResponseEntity<Response<String>> ok(String messageSource, String text) {
        return ResponseEntity.ok(new Response<String>(messageService.getMessage(messageSource) + text));
    }

    public <T> ResponseEntity<T> notFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    public ResponseEntity<?> noContent() {
        return ResponseEntity.noContent().build();
    }
}
