package bank.bankApp.assemblers;

import bank.bankApp.controllers.TransactionController;
import bank.bankApp.models.AccountTransaction;
import bank.bankApp.models.TransactionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class TransactionModelAssembler extends RepresentationModelAssemblerSupport<AccountTransaction, TransactionModel> {

    public TransactionModelAssembler() {
        super(TransactionController.class, TransactionModel.class);
    }

    @Override
    public TransactionModel toModel(AccountTransaction entity) {
        TransactionModel model = instantiateModel(entity);

        model.setId(entity.getId());
        model.setDate(entity.getDate());
        model.setAmount(entity.getAmount());
        model.setType(entity.getType());

        return model;
    }
}
