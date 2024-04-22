package br.edu.utfpr.bankapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import br.edu.utfpr.bankapi.dto.TransferDTO;
import br.edu.utfpr.bankapi.dto.WithdrawDTO;
import br.edu.utfpr.bankapi.model.Account;
import br.edu.utfpr.bankapi.model.Transaction;
import br.edu.utfpr.bankapi.service.TransactionService;
import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestEntityManager
@Transactional
@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {


    
         @InjectMocks
        private TransactionController transactionController;
    
        @Mock
        private TransactionService transactionService;

    @Autowired
    MockMvc mvc;

    // Gerenciador de persistência para os testes des classe
    @Autowired
    TestEntityManager entityManager;

    Account account; // Conta para os testes

    @BeforeEach
    void setup() {
        account = new Account("Lauro Lima",
                12346, 1000, 0);
        entityManager.persist(account); // salvando uma conta
    }

    @Test
    void deveriaRetornarStatus400ParaRequisicaoInvalida() throws Exception {
        // ARRANGE
        var json = "{}"; // Body inválido

        // ACT
        var res = mvc.perform(
                MockMvcRequestBuilders.post("/transaction/deposit")
                        .content(json).contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // ASSERT
        Assertions.assertEquals(400, res.getStatus());
    }

    @Test
    void deveriaRetornarStatus201ParaRequisicaoOK() throws Exception {
        // ARRANGE

        var json = """
                {
                    "receiverAccountNumber": 12346,
                    "amount": 200
                }
                    """;

        // ACT
        var res = mvc.perform(
                MockMvcRequestBuilders.post("/transaction/deposit")
                        .content(json).contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // ASSERT
        Assertions.assertEquals(201, res.getStatus());
        Assertions.assertEquals("application/json", res.getContentType());
    }

    @Test
    void deveriaRetornarDadosCorretosNoJson() throws Exception {
        // ARRANGE
        var json = """
                {
                    "receiverAccountNumber": 12346,
                    "amount": 200
                }
                    """;

        // ACT + ASSERT
        var res = mvc.perform(
                MockMvcRequestBuilders.post("/transaction/deposit")
                        .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.receiverAccount.number",
                        Matchers.equalTo(12346)))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.amount", Matchers.equalTo(200)));
    }


    @Test
    void testTransfer() throws Exception {
        TransferDTO dto = new TransferDTO(123456789L, 987654321L, -300.00);
        Transaction transaction = new Transaction();
    
        when(transactionService.transfer(dto)).thenReturn(transaction);
    
        ResponseEntity<Object> response = transactionController.transfer(dto);
    
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(transaction, response.getBody());
    }

    @Test
    void testWithdraw() throws Exception {
        WithdrawDTO dto = new WithdrawDTO(123456789L, 200.00); //Se colocar negativo nao passa no teste
        Transaction transaction = new Transaction();
    
        when(transactionService.withdraw(dto)).thenReturn(transaction);
    
        ResponseEntity<Object> response = transactionController.withdraw(dto);
    
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(transaction, response.getBody());
    }

    
}
