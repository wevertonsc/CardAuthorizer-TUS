/*
+- - - - - - - - - - - - - - - - - - - - - - - - - - - -+
| TUS - Technology University Shannon - Athlone         |
| Object-Oriented Programming I - (AL_KCNCM_9_1) 29468	|
+- - - - - - - - - - - - - - - - - - - - - - - - - - - -+
| Candidate: Weverton de Souza Castanho		            |
| Email: wevertonsc@gmail.com				            |
| Data: 02.NOVEMBER.2025					            |
+- - - - - - - - - - - - - - - - - - - - - - - - - - - -+
*/

package com.tus.oop1.card_authorizer.util;

import lombok.Getter;

@Getter
public enum CreditCardBrand {
    VISA("Visa", "4", 16, new int[]{13, 16}),
    MASTERCARD("MasterCard", "51,52,53,54,55,2221-2720", 16, new int[]{16}),
    AMERICAN_EXPRESS("American Express", "34,37", 15, new int[]{15}),
    DINERS_CLUB("Diners Club", "300-305,309,36,38,39", 14, new int[]{14, 16}),
    DISCOVER("Discover", "6011,622126-622925,644-649,65", 16, new int[]{16}),
    JCB("JCB", "3528-3589", 16, new int[]{16, 19}),
    MAESTRO("Maestro", "5018,5020,5038,5893,6304,6759,6761,6762,6763", 19, new int[]{12, 19}),
    UNKNOWN("Unknown", "", 0, new int[]{});

    // Getters
    private final String displayName;
    private final String iinRanges; // Issuer Identification Number
    private final int typicalLength;
    private final int[] validLengths;

    // Constructor
    CreditCardBrand(String displayName, String iinRanges, int typicalLength, int[] validLengths) {
        this.displayName = displayName;
        this.iinRanges = iinRanges;
        this.typicalLength = typicalLength;
        this.validLengths = validLengths;
    }

    // Method to check if a length is valid for this brand
    public boolean isValidLength(int length) {
        for (int validLength : validLengths) {
            if (validLength == length) {
                return true;
            }
        }
        return false;
    }

    // Method to identify the brand based on card number
    public static CreditCardBrand identify(String cardNumber) {
        if (cardNumber == null || cardNumber.trim().isEmpty()) {
            return UNKNOWN;
        }

        String cleanNumber = cardNumber.replaceAll("\\s+", "");

        // Check Visa (starts with 4)
        if (cleanNumber.startsWith("4")) {
            return VISA;
        }

        // Check American Express (starts with 34 or 37)
        if (cleanNumber.startsWith("34") || cleanNumber.startsWith("37")) {
            return AMERICAN_EXPRESS;
        }

        // Check MasterCard
        if (cleanNumber.startsWith("5")) {
            int firstTwoDigits = Integer.parseInt(cleanNumber.substring(0, 2));
            if (firstTwoDigits >= 51 && firstTwoDigits <= 55) {
                return MASTERCARD;
            }
        }

        // Check MasterCard (new range 2221-2720)
        if (cleanNumber.length() >= 4) {
            int firstFourDigits = Integer.parseInt(cleanNumber.substring(0, 4));
            if (firstFourDigits >= 2221 && firstFourDigits <= 2720) {
                return MASTERCARD;
            }
        }

        // Check Discover
        if (cleanNumber.startsWith("6011") ||
                cleanNumber.startsWith("65") ||
                (cleanNumber.length() >= 6 && checkDiscoverRange(cleanNumber))) {
            return DISCOVER;
        }

        return UNKNOWN;
    }

    private static boolean checkDiscoverRange(String cardNumber) {
        if (cardNumber.length() < 6) return false;

        int firstSixDigits = Integer.parseInt(cardNumber.substring(0, 6));
        return (firstSixDigits >= 622126 && firstSixDigits <= 622925) ||
                (firstSixDigits >= 644 && firstSixDigits <= 649);
    }

    // Method to validate basic card format
    public boolean isValidFormat(String cardNumber) {
        if (cardNumber == null) return false;

        String cleanNumber = cardNumber.replaceAll("\\s+", "");

        // Check if contains only digits
        if (!cleanNumber.matches("\\d+")) {
            return false;
        }

        // Check length
        return isValidLength(cleanNumber.length());
    }

    @Override
    public String toString() {
        return displayName;
    }
}