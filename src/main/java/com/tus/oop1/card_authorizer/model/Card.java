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

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.Setter;
import lombok.Getter;

import jakarta.persistence.OneToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Column;
import jakarta.persistence.GenerationType;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.List;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "history")
@ToString(exclude = "history")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false)
    private String number;
    private String expiration;
    private String cvv;
    private Float limits;
    private Float balance;

    @ManyToOne
    private Client client;

    @ManyToOne
    private Brand brand;

    @OneToMany
    @JsonManagedReference("card-history")  // ADICIONADO: Gerencia a referência para History
    private List<History> history;
}
