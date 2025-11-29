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

import com.tus.oop1.card_authorizer.model.Card;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class CardService {
    private final com.tus.oop1.card_authorizer.repo.CardRepo cardRepo;

    public List<Card> getCards() {
        log.info("getCards");
        return cardRepo.findAll();
    }

    public Card getCard(String number) {
        try {
            return cardRepo.findCardByNumber(number);
        } catch (Exception e) {
            log.error("Error searching for a card number: {}", number, e);
            return null;
        }

    }
}
