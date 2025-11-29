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

import com.tus.oop1.card_authorizer.model.Card; // Use CardModel, não Card
import com.tus.oop1.card_authorizer.service.CardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/v1/card")
public class CardController {
    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping(path = "{number}")
    public ResponseEntity<?> getCard(@PathVariable("number") String number) {
        try {
            Card card = cardService.getCard(number);
            return ResponseEntity.ok(card);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Card not found with number: " + number);
        }
    }
}