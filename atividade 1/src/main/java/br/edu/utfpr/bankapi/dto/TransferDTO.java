package br.edu.utfpr.bankapi.dto;

import br.edu.utfpr.bankapi.exception.NegativeAmountException;

public record TransferDTO(long sourceAccountNumber, long receiverAccountNumber, double amount) {
    public TransferDTO {
        if (amount <= 0) {
            throw new NegativeAmountException("O valor de transferência não pode ser negativo");
        }
    }
}
