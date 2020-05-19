package com.challenge.microservicechallenge.integrationTests;

import com.challenge.microservicechallenge.persistence.entitiy.MoneyMovement;
import com.challenge.microservicechallenge.persistence.respository.MoneyMovementRepository;
import com.challenge.microservicechallenge.web.model.ChannelsDto;
import com.challenge.microservicechallenge.web.model.StatusDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DisplayName("TransactionController tests ")
@Transactional
public class TransactionsStatusIT {

    private final String IBAN = "123";
    private final Double AMOUNT = 10D;
    private final String REFERENCE = "AAA-111";

    private final String OTHER_REFERENCE = "AAA-222";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MoneyMovementRepository repository;

    private MoneyMovement moneyMovement;

    @BeforeEach
    public void setUp() {
        moneyMovement = MoneyMovement.builder().reference(REFERENCE).amount(AMOUNT).iban(IBAN).build();
    }

    @Test
    @DisplayName("Status transaction with NOT valid reference and CLIENT channel then INVALID")
    public void givenInvalidReferenceThenReturnInvalidStatus() throws Exception {
        //when
        final ResultActions result = mockMvc.perform(
                get("/transactions/{reference}/status", OTHER_REFERENCE)
                        .param("channel", ChannelsDto.CLIENT.toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        //then
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.reference").value(OTHER_REFERENCE));
        result.andExpect(jsonPath("$.status").value(StatusDto.INVALID.toString()));
    }

    @Test
    @DisplayName("Status transaction with valid reference, past transaction and CLIENT channel then SETTLED")
    public void givenValidReferenceClientChannelThenReturnSettledStatus() throws Exception {
        //given
        LocalDate localDate = LocalDate.now().minusDays(1);
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        moneyMovement.setDate(date);

        repository.save(moneyMovement);

        //when
        final ResultActions result = mockMvc.perform(
                get("/transactions/{reference}/status",REFERENCE)
                        .param("channel", ChannelsDto.CLIENT.toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        //then
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.reference").value(REFERENCE));
        result.andExpect(jsonPath("$.status").value(StatusDto.SETTLED.toString()));
    }

    @Test
    @DisplayName("Status transaction with valid reference, today transaction and CLIENT channel then PENDING")
    public void givenValidReferenceClientChannelThenReturnPendingStatus() throws Exception {
        //given
        LocalDate localDate = LocalDate.now();
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        moneyMovement.setDate(date);

        repository.save(moneyMovement);

        //when
        final ResultActions result = mockMvc.perform(
                get("/transactions/{reference}/status",REFERENCE)
                        .param("channel", ChannelsDto.CLIENT.toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        //then
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.reference").value(REFERENCE));
        result.andExpect(jsonPath("$.status").value(StatusDto.PENDING.toString()));
    }

    @Test
    @DisplayName("Status transaction with valid reference, future transaction and CLIENT channel then FUTURE")
    public void givenValidReferenceClientChannelThenReturnFutureStatus() throws Exception {
        //given
        LocalDate localDate = LocalDate.now().plusDays(1);
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        moneyMovement.setDate(date);

        repository.save(moneyMovement);

        //when
        final ResultActions result = mockMvc.perform(
                get("/transactions/{reference}/status",REFERENCE)
                        .param("channel", ChannelsDto.CLIENT.toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        //then
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.reference").value(REFERENCE));
        result.andExpect(jsonPath("$.status").value(StatusDto.FUTURE.toString()));
    }
}
