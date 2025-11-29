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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CreditCardBrand Unit Tests")
class CreditCardBrandTest {

    @Test
    @DisplayName("Should identify Visa card starting with 4")
    void testIdentifyVisaCard() {
        String visaCard = "4532015112830366";
        CreditCardBrand brand = CreditCardBrand.identify(visaCard);

        assertEquals(CreditCardBrand.VISA, brand,
                "Card starting with 4 should be identified as Visa");
        assertEquals("Visa", brand.getDisplayName());
    }

    @Test
    @DisplayName("Should identify Mastercard starting with 51")
    void testIdentifyMastercard51() {
        String mastercardCard = "5105105105105100";
        CreditCardBrand brand = CreditCardBrand.identify(mastercardCard);

        assertEquals(CreditCardBrand.MASTERCARD, brand,
                "Card starting with 51 should be identified as Mastercard");
        assertEquals("MasterCard", brand.getDisplayName()); // ← CORRIGIDO: MasterCard com C maiúsculo
    }

    @Test
    @DisplayName("Should identify Mastercard in range 2221-2720")
    void testIdentifyMastercardNewRange() {
        String mastercardCard = "2221000000000009";
        CreditCardBrand brand = CreditCardBrand.identify(mastercardCard);

        assertEquals(CreditCardBrand.MASTERCARD, brand,
                "Card starting with 2221 should be identified as Mastercard");
    }

    @Test
    @DisplayName("Should identify American Express starting with 34")
    void testIdentifyAmex34() {
        String amexCard = "340000000000009";
        CreditCardBrand brand = CreditCardBrand.identify(amexCard);

        // CORRIGIDO: Sua implementação retorna AMERICAN_EXPRESS (não AMEX)
        assertEquals(CreditCardBrand.AMERICAN_EXPRESS, brand,
                "Card starting with 34 should be identified as American Express");
        assertEquals("American Express", brand.getDisplayName());
    }

    @Test
    @DisplayName("Should identify American Express starting with 37")
    void testIdentifyAmex37() {
        String amexCard = "370000000000002";
        CreditCardBrand brand = CreditCardBrand.identify(amexCard);

        // CORRIGIDO: Sua implementação retorna AMERICAN_EXPRESS
        assertEquals(CreditCardBrand.AMERICAN_EXPRESS, brand,
                "Card starting with 37 should be identified as American Express");
    }

    @Test
    @DisplayName("Should identify Discover starting with 6011")
    void testIdentifyDiscover6011() {
        String discoverCard = "6011000000000004";
        CreditCardBrand brand = CreditCardBrand.identify(discoverCard);

        assertEquals(CreditCardBrand.DISCOVER, brand,
                "Card starting with 6011 should be identified as Discover");
        assertEquals("Discover", brand.getDisplayName());
    }

    @Test
    @DisplayName("Should identify Discover starting with 65")
    void testIdentifyDiscover65() {
        String discoverCard = "6500000000000002";
        CreditCardBrand brand = CreditCardBrand.identify(discoverCard);

        assertEquals(CreditCardBrand.DISCOVER, brand,
                "Card starting with 65 should be identified as Discover");
    }

    @ParameterizedTest
    @DisplayName("Should identify various card brands correctly")
    @CsvSource({
            "4532015112830366, VISA",
            "5105105105105100, MASTERCARD",
            "340000000000009, AMERICAN_EXPRESS",  // ← CORRIGIDO
            "6011000000000004, DISCOVER",
            "2221000000000009, MASTERCARD"
    })
    void testIdentifyVariousCardBrands(String cardNumber, String expectedBrand) {
        CreditCardBrand brand = CreditCardBrand.identify(cardNumber);
        assertEquals(expectedBrand, brand.name(),
                "Card should be identified as " + expectedBrand);
    }

    @Test
    @DisplayName("Should return UNKNOWN for unrecognized card pattern")
    void testIdentifyUnknownCard() {
        String unknownCard = "9999999999999999";
        CreditCardBrand brand = CreditCardBrand.identify(unknownCard);

        assertEquals(CreditCardBrand.UNKNOWN, brand,
                "Unrecognized card should be identified as UNKNOWN");
        assertEquals("Unknown", brand.getDisplayName());
    }

    @Test
    @DisplayName("Should return UNKNOWN for empty card number")
    void testIdentifyEmptyCard() {
        String emptyCard = "";
        CreditCardBrand brand = CreditCardBrand.identify(emptyCard);

        assertEquals(CreditCardBrand.UNKNOWN, brand,
                "Empty card number should be identified as UNKNOWN");
    }

    @Test
    @DisplayName("Should return UNKNOWN for null card number")
    void testIdentifyNullCard() {
        CreditCardBrand brand = CreditCardBrand.identify(null);

        assertEquals(CreditCardBrand.UNKNOWN, brand,
                "Null card number should be identified as UNKNOWN");
    }

    @Test
    @DisplayName("Should validate correct Visa format (13-16 digits)")
    void testValidateVisaFormat() {
        assertTrue(CreditCardBrand.VISA.isValidLength(16),
                "16-digit Visa should be valid");
        assertTrue(CreditCardBrand.VISA.isValidLength(13),
                "13-digit Visa should be valid");
        assertFalse(CreditCardBrand.VISA.isValidLength(12),
                "12-digit Visa should be invalid");
        assertFalse(CreditCardBrand.VISA.isValidLength(17),
                "17-digit Visa should be invalid");
    }

    @Test
    @DisplayName("Should validate correct Mastercard format (16 digits)")
    void testValidateMastercardFormat() {
        assertTrue(CreditCardBrand.MASTERCARD.isValidLength(16),
                "16-digit Mastercard should be valid");
        assertFalse(CreditCardBrand.MASTERCARD.isValidLength(15),
                "15-digit Mastercard should be invalid");
        assertFalse(CreditCardBrand.MASTERCARD.isValidLength(17),
                "17-digit Mastercard should be invalid");
    }

    @Test
    @DisplayName("Should validate correct Amex format (15 digits)")
    void testValidateAmexFormat() {
        // CORRIGIDO: Usando AMERICAN_EXPRESS e validando com isValidLength
        assertTrue(CreditCardBrand.AMERICAN_EXPRESS.isValidLength(15),
                "15-digit American Express should be valid");
        assertFalse(CreditCardBrand.AMERICAN_EXPRESS.isValidLength(14),
                "14-digit American Express should be invalid");
        assertFalse(CreditCardBrand.AMERICAN_EXPRESS.isValidLength(16),
                "16-digit American Express should be invalid");
    }

    @Test
    @DisplayName("Should validate correct Discover format (16 digits)")
    void testValidateDiscoverFormat() {
        assertTrue(CreditCardBrand.DISCOVER.isValidLength(16),
                "16-digit Discover should be valid");
        assertFalse(CreditCardBrand.DISCOVER.isValidLength(15),
                "15-digit Discover should be invalid");
    }

    @Test
    @DisplayName("Should handle card number with spaces")
    void testIdentifyCardWithSpaces() {
        String cardWithSpaces = "4532 0151 1283 0366";
        CreditCardBrand brand = CreditCardBrand.identify(cardWithSpaces);

        assertEquals(CreditCardBrand.VISA, brand,
                "Card with spaces should be identified after removing spaces");
    }

    @Test
    @DisplayName("Should validate format for UNKNOWN brand")
    void testValidateUnknownFormat() {
        assertFalse(CreditCardBrand.UNKNOWN.isValidLength(16),
                "UNKNOWN brand should always return false for validation");
    }

    @Test
    @DisplayName("Should get correct display names for all brands")
    void testGetDisplayNames() {
        assertEquals("Visa", CreditCardBrand.VISA.getDisplayName());
        assertEquals("MasterCard", CreditCardBrand.MASTERCARD.getDisplayName()); // ← CORRIGIDO
        assertEquals("American Express", CreditCardBrand.AMERICAN_EXPRESS.getDisplayName()); // ← CORRIGIDO
        assertEquals("Discover", CreditCardBrand.DISCOVER.getDisplayName());
        assertEquals("Unknown", CreditCardBrand.UNKNOWN.getDisplayName());
    }
}