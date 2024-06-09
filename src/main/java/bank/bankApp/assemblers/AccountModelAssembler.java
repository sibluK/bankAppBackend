package bank.bankApp.assemblers;

import bank.bankApp.controllers.AccountController;
import bank.bankApp.models.Account;
import bank.bankApp.models.AccountModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;



@Component
public class AccountModelAssembler extends RepresentationModelAssemblerSupport<Account, AccountModel> {

    public AccountModelAssembler() {
        super(AccountController.class, AccountModel.class);
    }

    @Override
    public AccountModel toModel(Account account) {
        AccountModel model = instantiateModel(account);

        model.setId(account.getId());
        model.setNumber(account.getNumber());
        model.setType(account.getType());
        model.setBalance(account.getBalance());
        model.setUserId(account.getUser().getId());
        model.setTransactions(account.getTransactions());

        model.add(linkTo(methodOn(AccountController.class).getAccounts()).withRel("accounts"));
        model.add(linkTo(methodOn(AccountController.class).getAccountById(account.getId())).withSelfRel());

        return model;
    }
}
