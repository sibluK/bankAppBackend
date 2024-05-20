package bank.bankApp.repositories;

import bank.bankApp.models.Account;
import bank.bankApp.models.AccountTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ITransactionRepository extends JpaRepository<AccountTransaction, Long> {

    List<AccountTransaction> findAllByAccountId(Long accountId);
}
