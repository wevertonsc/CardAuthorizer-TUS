/*
+- - - - - - - - - - - - - - - - - - - - - - - - - - - -+
| TUS - Technology University Shannon - Athlone         |
| Object Oriented Programming I - (AL_KCNCM_9_1) 29468	|
+- - - - - - - - - - - - - - - - - - - - - - - - - - - -+
| Candidate: Weverton de Souza Castanho		            |
| Email: wevertonsc@gmail.com				            |
| Data: 02.NOVEMBER.2025					            |
+- - - - - - - - - - - - - - - - - - - - - - - - - - - -+
*/

package com.tus.oop1.card_authorizer.service;

import com.tus.oop1.card_authorizer.model.Messages;
import com.tus.oop1.card_authorizer.model.Card;
import com.tus.oop1.card_authorizer.model.History;
import com.tus.oop1.card_authorizer.dao.OperationDAO;
import com.tus.oop1.card_authorizer.repo.CardRepo;
import com.tus.oop1.card_authorizer.repo.MessagesRepo;
import com.tus.oop1.card_authorizer.repo.HistoryRepo;
import com.tus.oop1.card_authorizer.util.CreditCardBrand;
import com.tus.oop1.card_authorizer.util.JavaLuhnAlgorithm; // Import da classe Luhn
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.function.Predicate;

@Service
@Slf4j
@AllArgsConstructor
public class OperationService {
    private final CardRepo cardRepository;
    private final MessagesRepo messagesRepository;
    private final HistoryRepo historyRepository;
    private final JavaLuhnAlgorithm luhnAlgorithm;

    public Messages executeOperation(OperationDAO operation) {
        try {
            // - - Card number validation using Luhn Algorithm - -
            // Lambda predicate for credit card validation usando a classe JavaLuhnAlgorithm
            Predicate<String> creditCardValidator = cardNumber -> {
                // Remove any spaces from card number
                String cleanNumber = cardNumber.replaceAll("\\s+", "");

                // Validate basic format (only digits)
                if (!cleanNumber.matches("\\d+")) {
                    return false;
                }

                // Validate using Luhn Algorithm from JavaLuhnAlgorithm class
                return luhnAlgorithm.validateCreditCardNumber(cleanNumber);
            };

            // - - Brand identification and format validation - -
            // Lambda predicate for brand and format validation
            Predicate<String> brandFormatValidator = cardNumber -> {
                CreditCardBrand brand = CreditCardBrand.identify(cardNumber);

                // Check if the brand is known and a format is valid
                if (brand == CreditCardBrand.UNKNOWN) {
                    log.error("Unknown credit card brand!");
                    return false;
                }

                if (!brand.isValidFormat(cardNumber)) {
                    log.error("Invalid card format for brand: {}", brand.getDisplayName());
                    return false;
                }

                return true;
            };

            // Combined validation: Luhn algorithm + brand format
            if (!creditCardValidator.test(operation.getNumber()) ||
                    !brandFormatValidator.test(operation.getNumber())) {
                log.error("Invalid card number!");
                return messagesRepository.findMessagesById(1L);
            }

            // - - Verify if card exists in a database
            Card card = cardRepository.findCardByNumber(operation.getNumber());

            // - - Return message if card not found
            if (card == null) {
                log.error("Card not found in database!");
                return messagesRepository.findMessagesById(1L);
            }

            // - - Verify cardholder name - -
            if (!card.getClient().getName().equals(operation.getName())) {
                log.error("Cardholder name is invalid!");
                return messagesRepository.findMessagesById(2L);
            }

            // - - Verify email address - -
            if (!card.getClient().getEmail().equals(operation.getEmail())) {
                log.error("Email address is invalid!");
                return messagesRepository.findMessagesById(2L);
            }

            // - - Verify expiration date - -
            if (!card.getExpiration().equals(operation.getExpiration())) {
                log.error("Card expiration date is invalid!");
                return messagesRepository.findMessagesById(3L);
            }

            // - - Verify CVV code - -
            if (!card.getCvv().equals(operation.getCvv())) {
                log.error("CVV code is invalid!");
                return messagesRepository.findMessagesById(4L);
            }

            // - - Verify available balance - -
            if (card.getBalance() < operation.getValue()) {
                log.error("Transaction value exceeds available balance!");
                return messagesRepository.findMessagesById(5L);
            }

            // - - Process and finalize operation
            History history = new History();

            // Update card balance
            card.setBalance(card.getBalance() - operation.getValue());
            cardRepository.save(card);

            // Create operation history record
            history.setCard(card);
            history.setDateOperation(new Date());
            history.setBalance(card.getBalance());
            history.setValueOperation(operation.getValue());
            history.setTypeOperation(operation.getType());
            history.setMessages(messagesRepository.findMessagesById(6L));
            historyRepository.save(history);

            log.info("Operation completed successfully for card: {}",
                    CreditCardBrand.identify(operation.getNumber()).getDisplayName());

            return messagesRepository.findMessagesById(6L);

        } catch (Exception e) {
            log.error("Error executing operation: {}", e.getMessage(), e);
            return messagesRepository.findMessagesById(1L);
        }
    }
}