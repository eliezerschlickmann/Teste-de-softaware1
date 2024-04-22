package br.edu.utfpr.bankapi.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import br.edu.utfpr.bankapi.dto.TransferDTO;
import br.edu.utfpr.bankapi.dto.WithdrawDTO;
import br.edu.utfpr.bankapi.exception.NotFoundException;
import br.edu.utfpr.bankapi.model.Account;
import br.edu.utfpr.bankapi.model.Transaction;
import br.edu.utfpr.bankapi.repository.TransactionRepository;
import br.edu.utfpr.bankapi.validations.AvailableAccountValidation;
import br.edu.utfpr.bankapi.validations.AvailableBalanceValidation;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AvailableAccountValidation availableAccountValidation;

    @Mock
    private AvailableBalanceValidation availableBalanceValidation;

    @Test
void testWithdraw() throws NotFoundException {
    WithdrawDTO dto = new WithdrawDTO(123456789L, 150.00);
    Account account = new Account(); // A inicialização mais detalhada dependerá da implementação do Account
    account.setBalance(200.00); // Exemplo de inicialização de saldo
    Transaction transaction = new Transaction();

    when(availableAccountValidation.validate(dto.sourceAccountNumber())).thenReturn(account);
    when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

    Transaction result = transactionService.withdraw(dto);

    assertNotNull(result);
    verify(availableBalanceValidation).validate(any(Transaction.class));
}

@Test
void testTransfer() throws NotFoundException {
    TransferDTO dto = new TransferDTO(123456789L, 987654321L, 100.00);
    Account source = new Account();
    source.setBalance(500.00); // Exemplo de saldo inicial na conta de origem
    Account receiver = new Account();
    receiver.setBalance(200.00); // Exemplo de saldo inicial na conta de destino
    Transaction transaction = new Transaction();

    when(availableAccountValidation.validate(dto.sourceAccountNumber())).thenReturn(source);
    when(availableAccountValidation.validate(dto.receiverAccountNumber())).thenReturn(receiver);
    when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

    Transaction result = transactionService.transfer(dto);

    assertNotNull(result);
    verify(availableBalanceValidation).validate(any(Transaction.class));
}
}
