package com.obervatorio_pedagogico.backend.infrastructure.utils.base64;

import java.util.Base64;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

@Service
public class Base64Service {
    public <T> String encode(byte[] data) throws JsonProcessingException {
        return Base64.getEncoder().encodeToString(data);
    }

    public byte[] decode(String encodedString) {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
        return decodedBytes;
    }
}
