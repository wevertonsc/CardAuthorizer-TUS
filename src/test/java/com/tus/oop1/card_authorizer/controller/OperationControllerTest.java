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

package com.tus.oop1.card_authorizer.controller;

import com.tus.oop1.card_authorizer.dao.OperationDAO;
import com.tus.oop1.card_authorizer.model.Messages;
import com.tus.oop1.card_authorizer.service.OperationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("OperationController Unit Tests")
class OperationControllerTest {

    @Mock
    private OperationService operationService;

    @InjectMocks
    private OperationController operationController;

    private OperationDAO validOperation;
    private Messages successMessage;
    private Messages errorMessage;

    @BeforeEach
    void setUp() {
        validOperation = new OperationDAO();
        validOperation.setName("John Doe");
        validOperation.setNumber("4532015112830366");
        validOperation.setExpiration("12/25");
        validOperation.setCvv("123");
        validOperation.setEmail("john.doe@example.com");
        validOperation.setBrand("Visa");
        validOperation.setType("PURCHASE");
        validOperation.setValue(100.0f);

        successMessage = new Messages();
        successMessage.setId(6L);
        successMessage.setMessage("SUCCESS");
        successMessage.setDescription("Operation completed successfully");

        errorMessage = new Messages();
        errorMessage.setId(1L);
        errorMessage.setMessage("ERROR");
        errorMessage.setDescription("Invalid card number");
    }

    @Test
    @DisplayName("Should successfully execute valid operation")
    void testExecute_Success() {
        // Arrange
        when(operationService.executeOperation(any(OperationDAO.class)))
                .thenReturn(successMessage);

        // Act
        ResponseEntity<?> response = operationController.execute(validOperation);

        // Assert
        assertNotNull(response, "Response should not be null");
        assertEquals(HttpStatus.OK, response.getStatusCode(),
                "Status code should be 200 OK");
        assertNotNull(response.getBody(), "Response body should not be null");

        Messages returnedMessage = (Messages) response.getBody();
        assertEquals(successMessage.getId(), returnedMessage.getId(),
                "Returned message ID should match");
        assertEquals(successMessage.getMessage(), returnedMessage.getMessage(),
                "Returned message should match");

        verify(operationService, times(1)).executeOperation(validOperation);
    }

    @Test
    @DisplayName("Should return error message for invalid operation")
    void testExecute_InvalidOperation() {
        // Arrange
        when(operationService.executeOperation(any(OperationDAO.class)))
                .thenReturn(errorMessage);

        // Act
        ResponseEntity<?> response = operationController.execute(validOperation);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode(),
                "Status code should still be 200 OK");

        Messages returnedMessage = (Messages) response.getBody();
        assertEquals(errorMessage.getId(), returnedMessage.getId(),
                "Should return error message");
        assertEquals("ERROR", returnedMessage.getMessage());

        verify(operationService, times(1)).executeOperation(validOperation);
    }

    @Test
    @DisplayName("Should handle service exception and return 500")
    void testExecute_ServiceException() {
        // Arrange
        when(operationService.executeOperation(any(OperationDAO.class)))
                .thenThrow(new RuntimeException("Database connection error"));

        // Act
        ResponseEntity<?> response = operationController.execute(validOperation);

        // Assert
        assertNotNull(response, "Response should not be null");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode(),
                "Status code should be 500 INTERNAL SERVER ERROR");

        String errorBody = (String) response.getBody();
        assertNotNull(errorBody, "Error message should not be null");
        assertTrue(errorBody.contains("Error processing operation"),
                "Error message should contain expected text");
        assertTrue(errorBody.contains("Database connection error"),
                "Error message should contain exception message");

        verify(operationService, times(1)).executeOperation(validOperation);
    }

    @Test
    @DisplayName("Should handle null pointer exception")
    void testExecute_NullPointerException() {
        // Arrange
        when(operationService.executeOperation(any(OperationDAO.class)))
                .thenThrow(new NullPointerException("Null value encountered"));

        // Act
        ResponseEntity<?> response = operationController.execute(validOperation);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Error processing operation"));
    }

    @Test
    @DisplayName("Should handle operation with all fields populated")
    void testExecute_AllFieldsPopulated() {
        // Arrange
        when(operationService.executeOperation(any(OperationDAO.class)))
                .thenReturn(successMessage);

        // Act
        ResponseEntity<?> response = operationController.execute(validOperation);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());

        Messages returnedMessage = (Messages) response.getBody();
        assertEquals(successMessage.getMessage(), returnedMessage.getMessage());

        verify(operationService, times(1)).executeOperation(argThat(op ->
                op.getName().equals("John Doe") &&
                        op.getNumber().equals("4532015112830366") &&
                        op.getExpiration().equals("12/25") &&
                        op.getCvv().equals("123") &&
                        op.getEmail().equals("john.doe@example.com") &&
                        op.getBrand().equals("Visa") &&
                        op.getType().equals("PURCHASE") &&
                        op.getValue().equals(100.0f)
        ));
    }

    @Test
    @DisplayName("Should pass operation object correctly to service")
    void testExecute_OperationPassedCorrectly() {
        // Arrange
        when(operationService.executeOperation(validOperation))
                .thenReturn(successMessage);

        // Act
        operationController.execute(validOperation);

        // Assert
        verify(operationService).executeOperation(eq(validOperation));
    }

    @Test
    @DisplayName("Should handle multiple operation requests")
    void testExecute_MultipleRequests() {
        // Arrange
        when(operationService.executeOperation(any(OperationDAO.class)))
                .thenReturn(successMessage);

        // Act
        ResponseEntity<?> response1 = operationController.execute(validOperation);

        OperationDAO operation2 = new OperationDAO();
        operation2.setName("Jane Smith");
        operation2.setNumber("5425233430109903");
        operation2.setValue(200.0f);

        ResponseEntity<?> response2 = operationController.execute(operation2);

        // Assert
        assertAll("Multiple requests",
                () -> assertEquals(HttpStatus.OK, response1.getStatusCode()),
                () -> assertEquals(HttpStatus.OK, response2.getStatusCode()),
                () -> verify(operationService, times(2))
                        .executeOperation(any(OperationDAO.class))
        );
    }

    @Test
    @DisplayName("Should return correct message structure")
    void testExecute_MessageStructure() {
        // Arrange
        when(operationService.executeOperation(any(OperationDAO.class)))
                .thenReturn(successMessage);

        // Act
        ResponseEntity<?> response = operationController.execute(validOperation);

        // Assert
        Messages message = (Messages) response.getBody();
        assertAll("Message structure",
                () -> assertNotNull(message.getId(), "Message should have ID"),
                () -> assertNotNull(message.getMessage(), "Message should have message field"),
                () -> assertNotNull(message.getDescription(),
                        "Message should have description field"),
                () -> assertEquals(6L, message.getId()),
                () -> assertEquals("SUCCESS", message.getMessage()),
                () -> assertEquals("Operation completed successfully",
                        message.getDescription())
        );
    }

    @Test
    @DisplayName("Should handle operation with minimum value")
    void testExecute_MinimumValue() {
        // Arrange
        validOperation.setValue(0.01f);
        when(operationService.executeOperation(any(OperationDAO.class)))
                .thenReturn(successMessage);

        // Act
        ResponseEntity<?> response = operationController.execute(validOperation);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(operationService).executeOperation(argThat(op ->
                op.getValue().equals(0.01f)
        ));
    }

    @Test
    @DisplayName("Should handle operation with maximum value")
    void testExecute_MaximumValue() {
        // Arrange
        validOperation.setValue(999999.99f);
        when(operationService.executeOperation(any(OperationDAO.class)))
                .thenReturn(successMessage);

        // Act
        ResponseEntity<?> response = operationController.execute(validOperation);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(operationService).executeOperation(argThat(op ->
                op.getValue().equals(999999.99f)
        ));
    }

    @Test
    @DisplayName("Should verify endpoint POST mapping")
    void testExecute_EndpointMapping() {
        // Arrange
        when(operationService.executeOperation(any(OperationDAO.class)))
                .thenReturn(successMessage);

        // Act
        operationController.execute(validOperation);

        // Assert
        verify(operationService, times(1)).executeOperation(any(OperationDAO.class));
    }

    @Test
    @DisplayName("Should handle IllegalArgumentException")
    void testExecute_IllegalArgumentException() {
        // Arrange
        when(operationService.executeOperation(any(OperationDAO.class)))
                .thenThrow(new IllegalArgumentException("Invalid operation data"));

        // Act
        ResponseEntity<?> response = operationController.execute(validOperation);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        String errorMessage = (String) response.getBody();
        assertTrue(errorMessage.contains("Error processing operation"));
        assertTrue(errorMessage.contains("Invalid operation data"));
    }

    @Test
    @DisplayName("Should call service exactly once per request")
    void testExecute_ServiceCallCount() {
        // Arrange
        when(operationService.executeOperation(any(OperationDAO.class)))
                .thenReturn(successMessage);

        // Act
        operationController.execute(validOperation);

        // Assert
        verify(operationService, times(1)).executeOperation(validOperation);
        verify(operationService, only()).executeOperation(any(OperationDAO.class));
    }
}