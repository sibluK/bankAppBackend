package bank.bankApp.exceptions;

public class TransactionNotFoundException extends RuntimeException {
    public TransactionNotFoundException(Long id) {
        super("Could not find transaction with id: " + id);
    }
}
