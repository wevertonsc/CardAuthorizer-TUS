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

import org.springframework.stereotype.Component;

/*
    Java Credit Card Validation Luhn Algorithm
    https://dev.to/kevinmel2000/java-credit-card-validation-luhn-algorithm-198p
*/

@Component
public class JavaLuhnAlgorithm {

    public boolean validateCreditCardNumber(String number) {

        int[] size = new int[number.length()];
        for (int i = 0; i < number.length(); i++) {
            size[i] = Integer.parseInt(number.substring(i, i + 1));
        }
        for (int i = size.length - 2; i >= 0; i = i - 2) {
            int j = size[i];
            j = j * 2;
            if (j > 9) {
                j = j % 10 + 1;
            }
            size[i] = j;
        }
        int sum = 0;
        for (int i = 0; i < size.length; i++) {
            sum += size[i];
        }
        if (sum % 10 == 0) {
            return true;
        }
        return false;
    }
}