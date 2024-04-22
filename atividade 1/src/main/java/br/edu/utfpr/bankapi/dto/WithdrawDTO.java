package br.edu.utfpr.bankapi.dto;

import br.edu.utfpr.bankapi.exception.NegativeAmountException;

public record WithdrawDTO(long sourceAccountNumber, double amount) {
    public WithdrawDTO {
        if (amount <= 0) {
            throw new NegativeAmountException("O Valor de Saque deve ser maior que 0");
        }
    }
}
