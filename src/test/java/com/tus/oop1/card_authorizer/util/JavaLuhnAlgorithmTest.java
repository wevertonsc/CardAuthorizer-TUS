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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("JavaLuhnAlgorithm Unit Tests")
class JavaLuhnAlgorithmTest {

    private JavaLuhnAlgorithm luhnAlgorithm;

    @BeforeEach
    void setUp() {
        luhnAlgorithm = new JavaLuhnAlgorithm();
    }

    @Test
    @DisplayName("Should validate valid Visa card number")
    void testValidVisaCardNumber() {
        String validVisaCard = "4532015112830366";
        assertTrue(luhnAlgorithm.validateCreditCardNumber(validVisaCard),
                "Valid Visa card should pass Luhn validation");
    }

    @Test
    @DisplayName("Should validate valid Mastercard number")
    void testValidMastercardNumber() {
        String validMastercard = "5425233430109903";
        assertTrue(luhnAlgorithm.validateCreditCardNumber(validMastercard),
                "Valid Mastercard should pass Luhn validation");
    }

    @Test
    @DisplayName("Should validate valid American Express number")
    void testValidAmexNumber() {
        String validAmex = "374245455400126";
        assertTrue(luhnAlgorithm.validateCreditCardNumber(validAmex),
                "Valid Amex card should pass Luhn validation");
    }

    @ParameterizedTest
    @DisplayName("Should validate multiple valid card numbers")
    @ValueSource(strings = {
            "4532015112830366",  // Visa
            "5425233430109903",  // Mastercard
            "374245455400126",   // Amex
            "6011000990139424"   // Discover
    })
    void testMultipleValidCardNumbers(String cardNumber) {
        assertTrue(luhnAlgorithm.validateCreditCardNumber(cardNumber),
                "Card number " + cardNumber + " should be valid");
    }

    @Test
    @DisplayName("Should reject invalid card number with wrong checksum")
    void testInvalidCardNumberChecksum() {
        String invalidCard = "4532015112830367"; // Last digit wrong
        assertFalse(luhnAlgorithm.validateCreditCardNumber(invalidCard),
                "Invalid card with wrong checksum should fail validation");
    }

    @ParameterizedTest
    @DisplayName("Should reject multiple invalid card numbers")
    @ValueSource(strings = {
            "1234567890123456",  // Invalid checksum
            // "0000000000000000" foi removido - tecnicamente válido no algoritmo Luhn
            "9999999999999999",  // Invalid checksum
            "4532015112830367"   // Wrong checksum
    })
    void testMultipleInvalidCardNumbers(String cardNumber) {
        assertFalse(luhnAlgorithm.validateCreditCardNumber(cardNumber),
                "Card number " + cardNumber + " should be invalid");
    }

    @Test
    @DisplayName("Should handle single digit number")
    void testSingleDigitNumber() {
        String singleDigit = "0";
        assertTrue(luhnAlgorithm.validateCreditCardNumber(singleDigit),
                "Single digit 0 should pass Luhn validation");
    }

    @Test
    @DisplayName("Should validate short valid card numbers")
    void testShortValidCardNumber() {
        String shortValid = "18"; // Valid Luhn
        assertTrue(luhnAlgorithm.validateCreditCardNumber(shortValid),
                "Short valid number should pass validation");
    }

    @Test
    @DisplayName("Should reject short invalid card numbers")
    void testShortInvalidCardNumber() {
        String shortInvalid = "19"; // Invalid Luhn
        assertFalse(luhnAlgorithm.validateCreditCardNumber(shortInvalid),
                "Short invalid number should fail validation");
    }

    @Test
    @DisplayName("Should validate card number with all zeros except checksum")
    void testZeroesWithValidChecksum() {
        // NOTA: 0000000000000000 é tecnicamente válido no algoritmo de Luhn
        // Mas na prática, esse número não seria emitido por nenhum banco
        String zerosValid = "0000000000000000";
        // Removemos a asserção de que deveria ser inválido pois o algoritmo
        // considera matematicamente válido
        boolean result = luhnAlgorithm.validateCreditCardNumber(zerosValid);
        assertTrue(result, "All zeros is mathematically valid in Luhn algorithm");
    }

    @Test
    @DisplayName("Should handle very long card number")
    void testVeryLongCardNumber() {
        String longCard = "45320151128303665432015112830366";
        // This should still follow Luhn algorithm rules
        boolean result = luhnAlgorithm.validateCreditCardNumber(longCard);
        assertNotNull(result, "Should handle long card numbers");
    }
}