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

import com.tus.oop1.card_authorizer.model.Messages;
import com.tus.oop1.card_authorizer.dao.OperationDAO;
import com.tus.oop1.card_authorizer.service.OperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/operation")
public class OperationController {

    @Autowired
    private OperationService operationService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> execute(@RequestBody OperationDAO operation) {
        try {
            Messages result = operationService.executeOperation(operation);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("Error executing operation: " + e.getMessage());
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing operation: " + e.getMessage());
        }
    }
}