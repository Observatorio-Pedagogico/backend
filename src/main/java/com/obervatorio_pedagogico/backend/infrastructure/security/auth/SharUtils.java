package com.obervatorio_pedagogico.backend.infrastructure.security.auth;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.stereotype.Service;

@Service
public class SharUtils {
    
    public String shar256(String senha) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest messageDigest =  MessageDigest.getInstance("SHA-256");
        messageDigest.update(senha.getBytes("UTF-8"));
        return new BigInteger(1, messageDigest.digest()).toString(16);
    }
}
