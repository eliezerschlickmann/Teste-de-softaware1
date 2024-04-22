package br.edu.utfpr.bankapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.edu.utfpr.bankapi.dto.AccountDTO;
import br.edu.utfpr.bankapi.exception.NotFoundException;
import br.edu.utfpr.bankapi.model.Account;
import br.edu.utfpr.bankapi.service.AccountService;


@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

    @InjectMocks
    private AccountController accountController;

    @Mock
    private AccountService accountService;

    @Test
    void testCreate() {
        AccountDTO dto = new AccountDTO("Alice Smith", 987654321L, 500.00, 200.00);
        Account account = new Account();
    
        when(accountService.save(dto)).thenReturn(account);
    
        ResponseEntity<Object> response = accountController.create(dto);
    
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(account, response.getBody());
    }

    @Test
    void testUpdate() throws NotFoundException {
        long id = 1L;
        AccountDTO dto = new AccountDTO("John Doe", 123456789L, 1000.00, 500.00);
        Account account = new Account();
    
        when(accountService.update(id, dto)).thenReturn(account);
    
        ResponseEntity<Object> response = accountController.update(id, dto);
    
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(account, response.getBody());
    }

    @Test
    void testGetAll() {
        List<Account> accounts = List.of(new Account(), new Account());

        when(accountService.getAll()).thenReturn(accounts);

        List<Account> response = accountController.getAll();

        assertNotNull(response);
        assertEquals(2, response.size());
    }

    @Test
    void testGetByNumber() {
        long number = 12345L;
        Account account = new Account();
        Optional<Account> optionalAccount = Optional.of(account);

        when(accountService.getByNumber(number)).thenReturn(optionalAccount);

        ResponseEntity<Object> response = accountController.getByNumber(number);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(account, response.getBody());
    }
}

