package com.example.bankcards.util;

public final class CardNumberMasker {

    private CardNumberMasker() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static String mask(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 16) {
            throw new IllegalArgumentException("Invalid card number length");
        }

        String lastFour = cardNumber.substring(cardNumber.length() - 4);
        return "**** **** **** " + lastFour;
    }
}
