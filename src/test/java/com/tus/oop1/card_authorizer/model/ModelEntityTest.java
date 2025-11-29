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

package com.tus.oop1.card_authorizer.model;

import com.tus.oop1.card_authorizer.dao.OperationDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Model Entity Tests")
class ModelEntityTest {

    @DisplayName("Card Model Tests")
    class CardModelTest {
        private Card card;
        private Client client;
        private Brand brand;

        @BeforeEach
        void setUp() {
            client = new Client();
            client.setId(1L);
            client.setName("John Doe");

            brand = new Brand();
            brand.setId(1L);
            brand.setName("Visa");

            card = new Card();
            card.setId(1L);
            card.setNumber("4532015112830366");
            card.setExpiration("12/25");
            card.setCvv("123");
            card.setLimits(5000.0f);
            card.setBalance(3000.0f);
            card.setClient(client);
            card.setBrand(brand);
        }

        @Test
        @DisplayName("Should create card with all properties")
        void testCardCreation() {
            assertAll("Card properties",
                    () -> assertEquals(1L, card.getId()),
                    () -> assertEquals("4532015112830366", card.getNumber()),
                    () -> assertEquals("12/25", card.getExpiration()),
                    () -> assertEquals("123", card.getCvv()),
                    () -> assertEquals(5000.0f, card.getLimits()),
                    () -> assertEquals(3000.0f, card.getBalance()),
                    () -> assertNotNull(card.getClient()),
                    () -> assertNotNull(card.getBrand())
            );
        }

        @Test
        @DisplayName("Should update card balance")
        void testUpdateBalance() {
            Float newBalance = 2500.0f;
            card.setBalance(newBalance);
            assertEquals(newBalance, card.getBalance());
        }

        @Test
        @DisplayName("Should set and get history")
        void testCardHistory() {
            List<History> historyList = new ArrayList<>();
            History history = new History();
            history.setId(1L);
            historyList.add(history);

            card.setHistory(historyList);
            assertNotNull(card.getHistory());
            assertEquals(1, card.getHistory().size());
        }

        @Test
        @DisplayName("Should test equals and hashCode")
        void testEqualsAndHashCode() {
            Card card2 = new Card();
            card2.setId(1L);
            card2.setNumber("4532015112830366");
            card2.setExpiration("12/25");
            card2.setCvv("123");
            card2.setLimits(5000.0f);
            card2.setBalance(3000.0f);
            card2.setClient(client);
            card2.setBrand(brand);

            assertEquals(card, card2);
            assertEquals(card.hashCode(), card2.hashCode());
        }

        @Test
        @DisplayName("Should test toString")
        void testToString() {
            String cardString = card.toString();
            assertNotNull(cardString);
            assertTrue(cardString.contains("4532015112830366"));
        }
    }

    @DisplayName("Client Model Tests")
    class ClientModelTest {
        private Client client;

        @BeforeEach
        void setUp() {
            client = new Client();
            client.setId(1L);
            client.setName("John Doe");
            client.setEmail("john.doe@example.com");
        }

        @Test
        @DisplayName("Should create client with all properties")
        void testClientCreation() {
            assertAll("Client properties",
                    () -> assertEquals(1L, client.getId()),
                    () -> assertEquals("John Doe", client.getName()),
                    () -> assertEquals("john.doe@example.com", client.getEmail())
            );
        }

        @Test
        @DisplayName("Should set and get cards")
        void testClientCards() {
            List<Card> cards = new ArrayList<>();
            Card card = new Card();
            card.setId(1L);
            cards.add(card);

            client.setCard(cards);
            assertNotNull(client.getCard());
            assertEquals(1, client.getCard().size());
        }

        @Test
        @DisplayName("Should update client information")
        void testUpdateClient() {
            client.setName("Jane Smith");
            client.setEmail("jane.smith@example.com");

            assertEquals("Jane Smith", client.getName());
            assertEquals("jane.smith@example.com", client.getEmail());
        }

        @Test
        @DisplayName("Should test equals and hashCode")
        void testEqualsAndHashCode() {
            Client client2 = new Client();
            client2.setId(1L);
            client2.setName("John Doe");
            client2.setEmail("john.doe@example.com");

            assertEquals(client, client2);
            assertEquals(client.hashCode(), client2.hashCode());
        }
    }

    @DisplayName("Brand Model Tests")
    class BrandModelTest {
        private Brand brand;

        @BeforeEach
        void setUp() {
            brand = new Brand();
            brand.setId(1L);
            brand.setName("Visa");
        }

        @Test
        @DisplayName("Should create brand with all properties")
        void testBrandCreation() {
            assertAll("Brand properties",
                    () -> assertEquals(1L, brand.getId()),
                    () -> assertEquals("Visa", brand.getName())
            );
        }

        @Test
        @DisplayName("Should set and get cards")
        void testBrandCards() {
            List<Card> cards = new ArrayList<>();
            Card card = new Card();
            card.setId(1L);
            cards.add(card);

            brand.setCard(cards);
            assertNotNull(brand.getCard());
            assertEquals(1, brand.getCard().size());
        }

        @Test
        @DisplayName("Should update brand name")
        void testUpdateBrand() {
            brand.setName("Mastercard");
            assertEquals("Mastercard", brand.getName());
        }
    }

    @DisplayName("History Model Tests")
    class HistoryModelTest {
        private History history;
        private Card card;
        private Messages messages;

        @BeforeEach
        void setUp() {
            card = new Card();
            card.setId(1L);

            messages = new Messages();
            messages.setId(6L);
            messages.setMessage("SUCCESS");

            history = new History();
            history.setId(1L);
            history.setBalance(2900.0f);
            history.setValueOperation(100.0f);
            history.setTypeOperation("PURCHASE");
            history.setDateOperation(new Date());
            history.setCard(card);
            history.setMessages(messages);
        }

        @Test
        @DisplayName("Should create history with all properties")
        void testHistoryCreation() {
            assertAll("History properties",
                    () -> assertEquals(1L, history.getId()),
                    () -> assertEquals(2900.0f, history.getBalance()),
                    () -> assertEquals(100.0f, history.getValueOperation()),
                    () -> assertEquals("PURCHASE", history.getTypeOperation()),
                    () -> assertNotNull(history.getDateOperation()),
                    () -> assertNotNull(history.getCard()),
                    () -> assertNotNull(history.getMessages())
            );
        }

        @Test
        @DisplayName("Should update history balance")
        void testUpdateHistoryBalance() {
            history.setBalance(2800.0f);
            assertEquals(2800.0f, history.getBalance());
        }

        @Test
        @DisplayName("Should test different operation types")
        void testOperationTypes() {
            history.setTypeOperation("WITHDRAWAL");
            assertEquals("WITHDRAWAL", history.getTypeOperation());

            history.setTypeOperation("REFUND");
            assertEquals("REFUND", history.getTypeOperation());
        }
    }

    @DisplayName("Messages Model Tests")
    class MessagesModelTest {
        private Messages messages;

        @BeforeEach
        void setUp() {
            messages = new Messages();
            messages.setId(6L);
            messages.setMessage("SUCCESS");
            messages.setDescription("Operation completed successfully");
        }

        @Test
        @DisplayName("Should create messages with all properties")
        void testMessagesCreation() {
            assertAll("Messages properties",
                    () -> assertEquals(6L, messages.getId()),
                    () -> assertEquals("SUCCESS", messages.getMessage()),
                    () -> assertEquals("Operation completed successfully",
                            messages.getDescription())
            );
        }

        @Test
        @DisplayName("Should set and get history")
        void testMessagesHistory() {
            List<History> historyList = new ArrayList<>();
            History history = new History();
            history.setId(1L);
            historyList.add(history);

            messages.setHistory(historyList);
            assertNotNull(messages.getHistory());
            assertEquals(1, messages.getHistory().size());
        }

        @Test
        @DisplayName("Should update message content")
        void testUpdateMessage() {
            messages.setMessage("ERROR");
            messages.setDescription("Card not found");

            assertEquals("ERROR", messages.getMessage());
            assertEquals("Card not found", messages.getDescription());
        }
    }

    @DisplayName("OperationDAO Tests")
    class OperationDAOTest {
        private OperationDAO operation;

        @BeforeEach
        void setUp() {
            operation = new OperationDAO();
            operation.setName("John Doe");
            operation.setNumber("4532015112830366");
            operation.setExpiration("12/25");
            operation.setCvv("123");
            operation.setEmail("john.doe@example.com");
            operation.setBrand("Visa");
            operation.setType("PURCHASE");
            operation.setValue(100.0f);
        }

        @Test
        @DisplayName("Should create operation DAO with all properties")
        void testOperationDAOCreation() {
            assertAll("Operation DAO properties",
                    () -> assertEquals("John Doe", operation.getName()),
                    () -> assertEquals("4532015112830366", operation.getNumber()),
                    () -> assertEquals("12/25", operation.getExpiration()),
                    () -> assertEquals("123", operation.getCvv()),
                    () -> assertEquals("john.doe@example.com", operation.getEmail()),
                    () -> assertEquals("Visa", operation.getBrand()),
                    () -> assertEquals("PURCHASE", operation.getType()),
                    () -> assertEquals(100.0f, operation.getValue())
            );
        }

        @Test
        @DisplayName("Should update operation value")
        void testUpdateOperationValue() {
            operation.setValue(250.0f);
            assertEquals(250.0f, operation.getValue());
        }

        @Test
        @DisplayName("Should test toString")
        void testToString() {
            String operationString = operation.toString();
            assertNotNull(operationString);
            assertTrue(operationString.contains("John Doe"));
            assertTrue(operationString.contains("4532015112830366"));
        }

        @Test
        @DisplayName("Should test all args constructor")
        void testAllArgsConstructor() {
            OperationDAO op = new OperationDAO(
                    "Jane Smith",
                    "5425233430109903",
                    "06/26",
                    "456",
                    "jane.smith@example.com",
                    "Mastercard",
                    "WITHDRAWAL",
                    200.0f
            );

            assertAll("All args constructor",
                    () -> assertEquals("Jane Smith", op.getName()),
                    () -> assertEquals("5425233430109903", op.getNumber()),
                    () -> assertEquals("06/26", op.getExpiration()),
                    () -> assertEquals("456", op.getCvv()),
                    () -> assertEquals("jane.smith@example.com", op.getEmail()),
                    () -> assertEquals("Mastercard", op.getBrand()),
                    () -> assertEquals("WITHDRAWAL", op.getType()),
                    () -> assertEquals(200.0f, op.getValue())
            );
        }
    }
}