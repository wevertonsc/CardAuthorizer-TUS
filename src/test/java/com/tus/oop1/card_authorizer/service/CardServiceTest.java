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

package com.tus.oop1.card_authorizer.service;

import com.tus.oop1.card_authorizer.model.Brand;
import com.tus.oop1.card_authorizer.model.Card;
import com.tus.oop1.card_authorizer.model.Client;
import com.tus.oop1.card_authorizer.repo.CardRepo;
import javassist.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CardService Unit Tests")
class CardServiceTest {

    @Mock
    private CardRepo cardRepo;

    @InjectMocks
    private CardService cardService;

    private Card testCard;
    private Client testClient;
    private Brand testBrand;

    @BeforeEach
    void setUp() {
        testClient = new Client();
        testClient.setId(1L);
        testClient.setName("John Doe");
        testClient.setEmail("john.doe@example.com");

        testBrand = new Brand();
        testBrand.setId(1L);
        testBrand.setName("Visa");

        testCard = new Card();
        testCard.setId(1L);
        testCard.setNumber("4532015112830366");
        testCard.setExpiration("12/25");
        testCard.setCvv("123");
        testCard.setLimits(5000.0f);
        testCard.setBalance(3000.0f);
        testCard.setClient(testClient);
        testCard.setBrand(testBrand);
    }

    @Test
    @DisplayName("Should return all cards successfully")
    void testGetCards_Success() {
        // Arrange
        Card card2 = new Card();
        card2.setId(2L);
        card2.setNumber("5425233430109903");

        List<Card> expectedCards = Arrays.asList(testCard, card2);
        when(cardRepo.findAll()).thenReturn(expectedCards);

        // Act
        List<Card> actualCards = cardService.getCards();

        // Assert
        assertNotNull(actualCards, "Returned card list should not be null");
        assertEquals(2, actualCards.size(), "Should return 2 cards");
        assertEquals(expectedCards, actualCards, "Should return the expected list of cards");

        verify(cardRepo, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return empty list when no cards exist")
    void testGetCards_EmptyList() {
        // Arrange
        when(cardRepo.findAll()).thenReturn(Arrays.asList());

        // Act
        List<Card> actualCards = cardService.getCards();

        // Assert
        assertNotNull(actualCards, "Returned card list should not be null");
        assertTrue(actualCards.isEmpty(), "Card list should be empty");

        verify(cardRepo, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return card when valid card number is provided")
    void testGetCard_Success() throws NotFoundException {
        // Arrange
        String cardNumber = "4532015112830366";
        when(cardRepo.findCardByNumber(cardNumber)).thenReturn(testCard);

        // Act
        Card actualCard = cardService.getCard(cardNumber);

        // Assert
        assertNotNull(actualCard, "Returned card should not be null");
        assertEquals(testCard.getNumber(), actualCard.getNumber(),
                "Card number should match");
        assertEquals(testCard.getId(), actualCard.getId(),
                "Card ID should match");
        assertEquals(testCard.getClient().getName(), actualCard.getClient().getName(),
                "Client name should match");

        verify(cardRepo, times(1)).findCardByNumber(cardNumber);
    }

    @Test
    @DisplayName("Should return null when card is not found")
    void testGetCard_NotFound() throws NotFoundException {
        // Arrange
        String cardNumber = "9999999999999999";
        when(cardRepo.findCardByNumber(cardNumber)).thenReturn(null);

        // Act
        Card actualCard = cardService.getCard(cardNumber);

        // Assert
        assertNull(actualCard, "Should return null when card is not found");

        verify(cardRepo, times(1)).findCardByNumber(cardNumber);
    }

    @Test
    @DisplayName("Should handle repository exception gracefully")
    void testGetCards_RepositoryException() {
        // Arrange
        when(cardRepo.findAll()).thenThrow(new RuntimeException("Database connection error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            cardService.getCards();
        }, "Should throw RuntimeException when repository fails");

        verify(cardRepo, times(1)).findAll();
    }

    @Test
    @DisplayName("Should verify correct method invocation on repository")
    void testGetCard_VerifyRepositoryCall() throws NotFoundException {
        // Arrange
        String cardNumber = "4532015112830366";
        when(cardRepo.findCardByNumber(anyString())).thenReturn(testCard);

        // Act
        cardService.getCard(cardNumber);

        // Assert
        verify(cardRepo, times(1)).findCardByNumber(cardNumber);
        verify(cardRepo, never()).findAll();
    }

    @Test
    @DisplayName("Should return card with all properties correctly populated")
    void testGetCard_VerifyAllProperties() throws NotFoundException {
        // Arrange
        String cardNumber = "4532015112830366";
        when(cardRepo.findCardByNumber(cardNumber)).thenReturn(testCard);

        // Act
        Card actualCard = cardService.getCard(cardNumber);

        // Assert
        assertAll("Card properties",
                () -> assertEquals(testCard.getNumber(), actualCard.getNumber()),
                () -> assertEquals(testCard.getExpiration(), actualCard.getExpiration()),
                () -> assertEquals(testCard.getCvv(), actualCard.getCvv()),
                () -> assertEquals(testCard.getLimits(), actualCard.getLimits()),
                () -> assertEquals(testCard.getBalance(), actualCard.getBalance()),
                () -> assertNotNull(actualCard.getClient()),
                () -> assertNotNull(actualCard.getBrand())
        );
    }

    @Test
    @DisplayName("Should handle multiple sequential calls correctly")
    void testMultipleGetCardCalls() throws NotFoundException {
        // Arrange
        String cardNumber = "4532015112830366";
        when(cardRepo.findCardByNumber(cardNumber)).thenReturn(testCard);

        // Act
        Card firstCall = cardService.getCard(cardNumber);
        Card secondCall = cardService.getCard(cardNumber);
        Card thirdCall = cardService.getCard(cardNumber);

        // Assert
        assertAll("Multiple calls",
                () -> assertNotNull(firstCall),
                () -> assertNotNull(secondCall),
                () -> assertNotNull(thirdCall),
                () -> assertEquals(firstCall.getNumber(), secondCall.getNumber()),
                () -> assertEquals(secondCall.getNumber(), thirdCall.getNumber())
        );

        verify(cardRepo, times(3)).findCardByNumber(cardNumber);
    }
}