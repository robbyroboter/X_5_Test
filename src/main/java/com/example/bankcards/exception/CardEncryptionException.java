// com.example.bankcards.exception.CardEncryptionException.java
package com.example.bankcards.exception;

public class CardEncryptionException extends RuntimeException {
    public CardEncryptionException(String message, Throwable cause) {
        super(message, cause);
    }
}
