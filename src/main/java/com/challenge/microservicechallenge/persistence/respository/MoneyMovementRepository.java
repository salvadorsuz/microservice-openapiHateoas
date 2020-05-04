package com.challenge.microservicechallenge.persistence.respository;

import com.challenge.microservicechallenge.persistence.entitiy.MoneyMovement;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MoneyMovementRepository extends JpaRepository<MoneyMovement, Long> {

    List<MoneyMovement> findByReference(String reference);

    List<MoneyMovement> findByIban(String iban);

}
