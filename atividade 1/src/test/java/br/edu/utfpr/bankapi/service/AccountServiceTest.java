package br.edu.utfpr.bankapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.edu.utfpr.bankapi.dto.AccountDTO;
import br.edu.utfpr.bankapi.exception.NotFoundException;
import br.edu.utfpr.bankapi.model.Account;
import br.edu.utfpr.bankapi.repository.AccountRepository;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @InjectMocks
    private AccountService accountService;

    @Mock
    private AccountRepository accountRepository;

    @Test
    void testGetByNumber() {
        long accountNumber = 12345L;
        Optional<Account> account = Optional.of(new Account());

        when(accountRepository.getByNumber(accountNumber)).thenReturn(account);

        Optional<Account> result = accountService.getByNumber(accountNumber);

        assertTrue(result.isPresent());
        assertEquals(account, result);
    }

    @Test
    void testGetAll() {
        List<Account> accounts = List.of(new Account(), new Account());
        when(accountRepository.findAll()).thenReturn(accounts);

        List<Account> result = accountService.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testSave() {
        AccountDTO dto = new AccountDTO("Michael Johnson", 1122334455L, 0.0, 500.0); // Note que o saldo inicial é 0
        Account savedAccount = new Account();
    
        when(accountRepository.save(any(Account.class))).thenReturn(savedAccount);
    
        Account result = accountService.save(dto);
    
        assertNotNull(result);
    }
    

    @Test
    void testUpdate() throws NotFoundException {
        long id = 1L;
        AccountDTO dto = new AccountDTO("Jane Roe", 987654321L, 500.00, 300.00);
        Optional<Account> account = Optional.of(new Account());
    
        when(accountRepository.findById(id)).thenReturn(account);
        when(accountRepository.save(any(Account.class))).thenReturn(account.get());
    
        Account result = accountService.update(id, dto);
    
        assertNotNull(result);
    }
    
}
