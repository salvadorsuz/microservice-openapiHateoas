package com.challenge.microservicechallenge.persistence.respository;

import com.challenge.microservicechallenge.persistence.entitiy.MoneyMovement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import javax.persistence.EntityManager;
import java.util.List;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class MoneyMovementRepositoryTest {

    private final String IBAN= "123";
    private final Double AMOUNT= 10D;
    private final String REFERENCE= "AAA-111";

    private final String OTHER_IBAN= "456";
    private final String OTHER_REFERENCE= "AAA-222";

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MoneyMovementRepository moneyMovementRepository;

    private MoneyMovement moneyMovement;

    @BeforeEach
    public void setUp() {
        moneyMovement = MoneyMovement.builder().reference(REFERENCE).amount(AMOUNT).iban(IBAN).build();
        entityManager.persist(moneyMovement);
        entityManager.flush();
    }

    @Test
    @DisplayName("Find by reference valid reference ")
    void givenValidReferenceThenReturnMoneyMovement() {
        List<MoneyMovement> response = moneyMovementRepository.findByReference(REFERENCE);

        assertAll(
                "result",
                () -> assertNotNull(response),
                () -> assertThat(response, hasItem(moneyMovement))
        );
    }

    @Test
    @DisplayName("Find by reference NOT valid reference ")
    void givenNotValidReferenceThenReturnEmpty() {
        List<MoneyMovement> response = moneyMovementRepository.findByReference(OTHER_REFERENCE);

        assertAll(
                "result",
                () -> assertNotNull(response),
                () -> assertThat(response, is(empty()))
        );
    }

    @Test
    @DisplayName("Find by IBAN valid IBAN ")
    void givenValidIbanThenReturnMoneyMovement() {
        List<MoneyMovement> response = moneyMovementRepository.findByIban(IBAN);

        assertAll(
                "result",
                () -> assertNotNull(response),
                () -> assertThat(response, hasItem(moneyMovement))
        );
    }

    @Test
    @DisplayName("Find by IBAN NOT valid IBAN ")
    void givenNotValidIbanThenReturnEmpty() {
        List<MoneyMovement> response = moneyMovementRepository.findByIban(OTHER_IBAN);

        assertAll(
                "result",
                () -> assertNotNull(response),
                () -> assertThat(response, is(empty()))
        );
    }

}