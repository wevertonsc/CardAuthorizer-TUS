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

import com.tus.oop1.card_authorizer.dao.OperationDAO;
import com.tus.oop1.card_authorizer.model.Brand;
import com.tus.oop1.card_authorizer.model.Card;
import com.tus.oop1.card_authorizer.model.Client;
import com.tus.oop1.card_authorizer.model.History;
import com.tus.oop1.card_authorizer.model.Messages;
import com.tus.oop1.card_authorizer.repo.CardRepo;
import com.tus.oop1.card_authorizer.repo.HistoryRepo;
import com.tus.oop1.card_authorizer.repo.MessagesRepo;
import com.tus.oop1.card_authorizer.util.JavaLuhnAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("OperationService Unit Tests")
class OperationServiceTest {

    @Mock
    private CardRepo cardRepository;

    @Mock
    private MessagesRepo messagesRepository;

    @Mock
    private HistoryRepo historyRepository;

    @Mock
    private JavaLuhnAlgorithm luhnAlgorithm;

    @InjectMocks
    private OperationService operationService;

    private OperationDAO validOperation;
    private Card testCard;
    private Client testClient;
    private Brand testBrand;
    private Messages successMessage;
    private Messages errorMessage;

    @BeforeEach
    void setUp() {
        // Setup test client
        testClient = new Client();
        testClient.setId(1L);
        testClient.setName("John Doe");
        testClient.setEmail("john.doe@example.com");

        // Setup test brand
        testBrand = new Brand();
        testBrand.setId(1L);
        testBrand.setName("Visa");

        // Setup test card
        testCard = new Card();
        testCard.setId(1L);
        testCard.setNumber("4532015112830366");
        testCard.setExpiration("12/25");
        testCard.setCvv("123");
        testCard.setLimits(5000.0f);
        testCard.setBalance(3000.0f);
        testCard.setClient(testClient);
        testCard.setBrand(testBrand);

        // Setup valid operation
        validOperation = new OperationDAO();
        validOperation.setName("John Doe");
        validOperation.setNumber("4532015112830366");
        validOperation.setExpiration("12/25");
        validOperation.setCvv("123");
        validOperation.setEmail("john.doe@example.com");
        validOperation.setBrand("Visa");
        validOperation.setType("PURCHASE");
        validOperation.setValue(100.0f);

        // Setup messages
        errorMessage = new Messages();
        errorMessage.setId(1L);
        errorMessage.setMessage("ERROR");
        errorMessage.setDescription("Invalid card number");

        successMessage = new Messages();
        successMessage.setId(6L);
        successMessage.setMessage("SUCCESS");
        successMessage.setDescription("Operation completed successfully");
    }

    @Test
    @DisplayName("Should successfully process valid operation")
    void testExecuteOperation_Success() {
        // Arrange
        when(luhnAlgorithm.validateCreditCardNumber(anyString())).thenReturn(true);
        when(cardRepository.findCardByNumber(anyString())).thenReturn(testCard);
        when(messagesRepository.findMessagesById(6L)).thenReturn(successMessage);
        when(cardRepository.save(any(Card.class))).thenReturn(testCard);
        when(historyRepository.save(any(History.class))).thenReturn(new History());

        // Act
        Messages result = operationService.executeOperation(validOperation);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertEquals(successMessage.getId(), result.getId(), "Should return success message");
        assertEquals(successMessage.getMessage(), result.getMessage());

        verify(luhnAlgorithm, times(1)).validateCreditCardNumber(anyString());
        verify(cardRepository, times(1)).findCardByNumber(validOperation.getNumber());
        verify(cardRepository, times(1)).save(any(Card.class));
        verify(historyRepository, times(1)).save(any(History.class));
    }

    @Test
    @DisplayName("Should reject operation with invalid Luhn algorithm")
    void testExecuteOperation_InvalidLuhn() {
        // Arrange
        when(luhnAlgorithm.validateCreditCardNumber(anyString())).thenReturn(false);
        when(messagesRepository.findMessagesById(1L)).thenReturn(errorMessage);

        // Act
        Messages result = operationService.executeOperation(validOperation);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertEquals(errorMessage.getId(), result.getId(), "Should return error message");

        verify(luhnAlgorithm, times(1)).validateCreditCardNumber(anyString());
        verify(cardRepository, never()).findCardByNumber(anyString());
        verify(cardRepository, never()).save(any(Card.class));
    }

    @Test
    @DisplayName("Should reject operation when card not found in database")
    void testExecuteOperation_CardNotFound() {
        // Arrange
        when(luhnAlgorithm.validateCreditCardNumber(anyString())).thenReturn(true);
        when(cardRepository.findCardByNumber(anyString())).thenReturn(null);
        when(messagesRepository.findMessagesById(1L)).thenReturn(errorMessage);

        // Act
        Messages result = operationService.executeOperation(validOperation);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertEquals(errorMessage.getId(), result.getId(), "Should return error message");

        verify(cardRepository, times(1)).findCardByNumber(validOperation.getNumber());
        verify(cardRepository, never()).save(any(Card.class));
    }

    @Test
    @DisplayName("Should reject operation with invalid cardholder name")
    void testExecuteOperation_InvalidCardholderName() {
        // Arrange
        validOperation.setName("Jane Smith"); // Wrong name

        when(luhnAlgorithm.validateCreditCardNumber(anyString())).thenReturn(true);
        when(cardRepository.findCardByNumber(anyString())).thenReturn(testCard);

        Messages nameErrorMessage = new Messages();
        nameErrorMessage.setId(2L);
        nameErrorMessage.setMessage("ERROR");
        nameErrorMessage.setDescription("Invalid cardholder information");
        when(messagesRepository.findMessagesById(2L)).thenReturn(nameErrorMessage);

        // Act
        Messages result = operationService.executeOperation(validOperation);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertEquals(2L, result.getId(), "Should return cardholder error message");

        verify(cardRepository, never()).save(any(Card.class));
    }

    @Test
    @DisplayName("Should reject operation with invalid email")
    void testExecuteOperation_InvalidEmail() {
        // Arrange
        validOperation.setEmail("wrong.email@example.com"); // Wrong email

        when(luhnAlgorithm.validateCreditCardNumber(anyString())).thenReturn(true);
        when(cardRepository.findCardByNumber(anyString())).thenReturn(testCard);

        Messages emailErrorMessage = new Messages();
        emailErrorMessage.setId(2L);
        when(messagesRepository.findMessagesById(2L)).thenReturn(emailErrorMessage);

        // Act
        Messages result = operationService.executeOperation(validOperation);

        // Assert
        assertEquals(2L, result.getId(), "Should return email error message");
        verify(cardRepository, never()).save(any(Card.class));
    }

    @Test
    @DisplayName("Should reject operation with invalid expiration date")
    void testExecuteOperation_InvalidExpiration() {
        // Arrange
        validOperation.setExpiration("01/26"); // Wrong expiration

        when(luhnAlgorithm.validateCreditCardNumber(anyString())).thenReturn(true);
        when(cardRepository.findCardByNumber(anyString())).thenReturn(testCard);

        Messages expirationErrorMessage = new Messages();
        expirationErrorMessage.setId(3L);
        when(messagesRepository.findMessagesById(3L)).thenReturn(expirationErrorMessage);

        // Act
        Messages result = operationService.executeOperation(validOperation);

        // Assert
        assertEquals(3L, result.getId(), "Should return expiration error message");
        verify(cardRepository, never()).save(any(Card.class));
    }

    @Test
    @DisplayName("Should reject operation with invalid CVV")
    void testExecuteOperation_InvalidCVV() {
        // Arrange
        validOperation.setCvv("999"); // Wrong CVV

        when(luhnAlgorithm.validateCreditCardNumber(anyString())).thenReturn(true);
        when(cardRepository.findCardByNumber(anyString())).thenReturn(testCard);

        Messages cvvErrorMessage = new Messages();
        cvvErrorMessage.setId(4L);
        when(messagesRepository.findMessagesById(4L)).thenReturn(cvvErrorMessage);

        // Act
        Messages result = operationService.executeOperation(validOperation);

        // Assert
        assertEquals(4L, result.getId(), "Should return CVV error message");
        verify(cardRepository, never()).save(any(Card.class));
    }

    @Test
    @DisplayName("Should reject operation with insufficient balance")
    void testExecuteOperation_InsufficientBalance() {
        // Arrange
        validOperation.setValue(5000.0f); // More than available balance

        when(luhnAlgorithm.validateCreditCardNumber(anyString())).thenReturn(true);
        when(cardRepository.findCardByNumber(anyString())).thenReturn(testCard);

        Messages balanceErrorMessage = new Messages();
        balanceErrorMessage.setId(5L);
        when(messagesRepository.findMessagesById(5L)).thenReturn(balanceErrorMessage);

        // Act
        Messages result = operationService.executeOperation(validOperation);

        // Assert
        assertEquals(5L, result.getId(), "Should return insufficient balance error message");
        verify(cardRepository, never()).save(any(Card.class));
    }

    @Test
    @DisplayName("Should update card balance correctly after successful operation")
    void testExecuteOperation_BalanceUpdate() {
        // Arrange
        Float initialBalance = testCard.getBalance();
        Float operationValue = validOperation.getValue();

        when(luhnAlgorithm.validateCreditCardNumber(anyString())).thenReturn(true);
        when(cardRepository.findCardByNumber(anyString())).thenReturn(testCard);
        when(messagesRepository.findMessagesById(6L)).thenReturn(successMessage);
        when(historyRepository.save(any(History.class))).thenReturn(new History());

        ArgumentCaptor<Card> cardCaptor = ArgumentCaptor.forClass(Card.class);
        when(cardRepository.save(cardCaptor.capture())).thenReturn(testCard);

        // Act
        operationService.executeOperation(validOperation);

        // Assert
        Card savedCard = cardCaptor.getValue();
        Float expectedBalance = initialBalance - operationValue;
        assertEquals(expectedBalance, savedCard.getBalance(),
                "Balance should be reduced by operation value");
    }

    @Test
    @DisplayName("Should create history record with correct information")
    void testExecuteOperation_HistoryCreation() {
        // Arrange
        when(luhnAlgorithm.validateCreditCardNumber(anyString())).thenReturn(true);
        when(cardRepository.findCardByNumber(anyString())).thenReturn(testCard);
        when(messagesRepository.findMessagesById(6L)).thenReturn(successMessage);
        when(cardRepository.save(any(Card.class))).thenReturn(testCard);

        ArgumentCaptor<History> historyCaptor = ArgumentCaptor.forClass(History.class);
        when(historyRepository.save(historyCaptor.capture())).thenReturn(new History());

        // Act
        operationService.executeOperation(validOperation);

        // Assert
        History savedHistory = historyCaptor.getValue();
        assertAll("History properties",
                () -> assertNotNull(savedHistory.getCard(), "History should have card reference"),
                () -> assertNotNull(savedHistory.getDateOperation(), "History should have operation date"),
                () -> assertEquals(validOperation.getValue(), savedHistory.getValueOperation(),
                        "History should record operation value"),
                () -> assertEquals(validOperation.getType(), savedHistory.getTypeOperation(),
                        "History should record operation type"),
                () -> assertNotNull(savedHistory.getMessages(), "History should have message reference")
        );
    }

    @Test
    @DisplayName("Should handle card number with spaces")
    void testExecuteOperation_CardNumberWithSpaces() {
        // Arrange
        validOperation.setNumber("4532 0151 1283 0366");

        when(luhnAlgorithm.validateCreditCardNumber(anyString())).thenReturn(true);
        when(cardRepository.findCardByNumber(anyString())).thenReturn(testCard);
        when(messagesRepository.findMessagesById(6L)).thenReturn(successMessage);
        when(cardRepository.save(any(Card.class))).thenReturn(testCard);
        when(historyRepository.save(any(History.class))).thenReturn(new History());

        // Act
        Messages result = operationService.executeOperation(validOperation);

        // Assert
        assertEquals(successMessage.getId(), result.getId());
        verify(luhnAlgorithm, times(1)).validateCreditCardNumber(contains("4532015112830366"));
    }

    @Test
    @DisplayName("Should handle exception during operation processing")
    void testExecuteOperation_Exception() {
        // Arrange
        when(luhnAlgorithm.validateCreditCardNumber(anyString()))
                .thenThrow(new RuntimeException("Unexpected error"));
        when(messagesRepository.findMessagesById(1L)).thenReturn(errorMessage);

        // Act
        Messages result = operationService.executeOperation(validOperation);

        // Assert
        assertEquals(errorMessage.getId(), result.getId(),
                "Should return error message on exception");
        verify(cardRepository, never()).save(any(Card.class));
    }

    @Test
    @DisplayName("Should validate operation with exact balance amount")
    void testExecuteOperation_ExactBalanceAmount() {
        // Arrange
        validOperation.setValue(3000.0f); // Exact balance amount

        when(luhnAlgorithm.validateCreditCardNumber(anyString())).thenReturn(true);
        when(cardRepository.findCardByNumber(anyString())).thenReturn(testCard);
        when(messagesRepository.findMessagesById(6L)).thenReturn(successMessage);
        when(cardRepository.save(any(Card.class))).thenReturn(testCard);
        when(historyRepository.save(any(History.class))).thenReturn(new History());

        // Act
        Messages result = operationService.executeOperation(validOperation);

        // Assert
        assertEquals(successMessage.getId(), result.getId(),
                "Should allow operation with exact balance amount");
        assertEquals(0.0f, testCard.getBalance(),
                "Balance should be zero after operation");
    }

    @Test
    @DisplayName("Should reject card with only digits validation")
    void testExecuteOperation_NonDigitCardNumber() {
        // Arrange
        validOperation.setNumber("4532-0151-1283-0366"); // Contains dashes

        when(messagesRepository.findMessagesById(1L)).thenReturn(errorMessage);

        // Act
        Messages result = operationService.executeOperation(validOperation);

        // Assert
        assertEquals(errorMessage.getId(), result.getId());
    }

    @Test
    @DisplayName("Should process multiple operations sequentially")
    void testExecuteOperation_MultipleOperations() {
        // Arrange
        when(luhnAlgorithm.validateCreditCardNumber(anyString())).thenReturn(true);
        when(cardRepository.findCardByNumber(anyString())).thenReturn(testCard);
        when(messagesRepository.findMessagesById(6L)).thenReturn(successMessage);
        when(cardRepository.save(any(Card.class))).thenReturn(testCard);
        when(historyRepository.save(any(History.class))).thenReturn(new History());

        // Act
        Messages result1 = operationService.executeOperation(validOperation);

        validOperation.setValue(50.0f);
        Messages result2 = operationService.executeOperation(validOperation);

        // Assert
        assertAll("Multiple operations",
                () -> assertEquals(successMessage.getId(), result1.getId()),
                () -> assertEquals(successMessage.getId(), result2.getId()),
                () -> verify(cardRepository, times(2)).save(any(Card.class)),
                () -> verify(historyRepository, times(2)).save(any(History.class))
        );
    }
}