package com.challenge.microservicechallenge.service.status;

import com.challenge.microservicechallenge.model.Channels;
import com.challenge.microservicechallenge.model.Status;
import com.challenge.microservicechallenge.model.TransactionStatus;
import com.challenge.microservicechallenge.model.converter.MoneyMovementToTransactionStatusATMConverter;
import com.challenge.microservicechallenge.model.converter.MoneyMovementToTransactionStatusClientConverter;
import com.challenge.microservicechallenge.model.converter.MoneyMovementToTransactionStatusInternalConverter;
import com.challenge.microservicechallenge.persistence.entitiy.MoneyMovement;
import com.challenge.microservicechallenge.persistence.respository.MoneyMovementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@SpringBootTest
@DisplayName("TransactionStatusService tests ")
class TransactionStatusServiceTest {

    private final String IBAN= "123";
    private final Double AMOUNT= 10D;
    private final String REFERENCE= "AAA-111";

    @InjectMocks
    private TransactionStatusService transactionStatusService;

    @Mock
    private MoneyMovementRepository repository;

    @Mock
    private MoneyMovementToTransactionStatusClientConverter moneyMovementToTransactionStatusClientConverter;

    @Mock
    private MoneyMovementToTransactionStatusATMConverter moneyMovementToTransactionStatusATMConverter;

    @Mock
    private MoneyMovementToTransactionStatusInternalConverter moneyMovementToTransactionStatusInternalConverter;

    private MoneyMovement moneyMovement;

    @BeforeEach
    public void init() {
        moneyMovement = MoneyMovement.builder().reference(REFERENCE).amount(AMOUNT).iban(IBAN).build();
    }

    @Nested
    @DisplayName("Test plan Invalid Reference")
    class WhenInvalidTransactionStatus {

        @Test
        @DisplayName("Status by invalid reference and no channel")
        public void givenInvalidReferenceNoChannelThenInvalid() {
            //given
            when(repository.findByReference(anyString())).thenReturn(Collections.EMPTY_LIST);

            //when
            TransactionStatus result = transactionStatusService.getStatus(REFERENCE, Optional.empty());

            //then
            assertAll(
                    "result",
                    () -> assertNotNull(result),
                    () -> assertEquals(REFERENCE, result.getReference()),
                    () -> assertEquals(Status.INVALID, result.getStatus())
            );

            verify(repository, times(1)).findByReference(anyString());
            verifyNoInteractions(moneyMovementToTransactionStatusClientConverter);
            verifyNoInteractions(moneyMovementToTransactionStatusATMConverter);
            verifyNoInteractions(moneyMovementToTransactionStatusInternalConverter);
        }
    }

    @Nested
    @DisplayName("Test plan valid reference and channel")
    class WhenValidReferenceTransactionStatus  {
        @Test
        @DisplayName("Valid reference and Default CLIENT channel")
        public void givenValidReferenceNoChannelThenDefaultConverterApplied() {
            //given
            when(repository.findByReference(anyString())).thenReturn(Collections.singletonList(moneyMovement));

            //when
            TransactionStatus result = transactionStatusService.getStatus(REFERENCE, Optional.empty());

            //then
            verify(repository, times(1)).findByReference(anyString());
            verify(moneyMovementToTransactionStatusClientConverter, times(1)).convert(any(MoneyMovement.class));
            verifyNoInteractions(moneyMovementToTransactionStatusATMConverter);
            verifyNoInteractions(moneyMovementToTransactionStatusInternalConverter);
        }

        @Test
        @DisplayName("Valid reference and CLIENT channel")
        public void givenValidReferenceClientChannelThenConverterApplied() {
            //given
            when(repository.findByReference(anyString())).thenReturn(Collections.singletonList(moneyMovement));

            //when
            TransactionStatus result = transactionStatusService.getStatus(REFERENCE, Optional.of(Channels.CLIENT));

            //then
            verify(repository, times(1)).findByReference(anyString());
            verify(moneyMovementToTransactionStatusClientConverter, times(1)).convert(any(MoneyMovement.class));
            verifyNoInteractions(moneyMovementToTransactionStatusATMConverter);
            verifyNoInteractions(moneyMovementToTransactionStatusInternalConverter);
        }

        @Test
        @DisplayName("Valid reference and ATM channel")
        public void givenValidReferenceATMChannelThenConverterApplied() {
            //given
            when(repository.findByReference(anyString())).thenReturn(Collections.singletonList(moneyMovement));

            //when
            TransactionStatus result = transactionStatusService.getStatus(REFERENCE, Optional.of(Channels.ATM));

            //then
            verify(repository, times(1)).findByReference(anyString());
            verify(moneyMovementToTransactionStatusATMConverter, times(1)).convert(any(MoneyMovement.class));
            verifyNoInteractions(moneyMovementToTransactionStatusClientConverter);
            verifyNoInteractions(moneyMovementToTransactionStatusInternalConverter);
        }

        @Test
        @DisplayName("Valid reference and INTERNAL channel")
        public void givenValidReferenceINTERNALChannelThenConverterApplied() {
            //given
            when(repository.findByReference(anyString())).thenReturn(Collections.singletonList(moneyMovement));

            //when
            TransactionStatus result = transactionStatusService.getStatus(REFERENCE, Optional.of(Channels.INTERNAL));

            //then
            verify(repository, times(1)).findByReference(anyString());
            verify(moneyMovementToTransactionStatusInternalConverter, times(1)).convert(any(MoneyMovement.class));
            verifyNoInteractions(moneyMovementToTransactionStatusClientConverter);
            verifyNoInteractions(moneyMovementToTransactionStatusATMConverter);
        }
    }

}