package bank.bankApp.repositories;

import bank.bankApp.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IAccountRepository extends JpaRepository<Account, Long> {

    List<Account> findAllByUserId(Long userId);
}
