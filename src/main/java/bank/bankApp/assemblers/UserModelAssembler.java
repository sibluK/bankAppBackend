package bank.bankApp.assemblers;

import bank.bankApp.controllers.UserController;
import bank.bankApp.models.User;
import bank.bankApp.models.UserModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class UserModelAssembler extends RepresentationModelAssemblerSupport<User, UserModel> {

    public UserModelAssembler() {
        super(UserController.class, UserModel.class);
    }

    @Override
    public UserModel toModel(User user) {
        UserModel model = new UserModel();
        model.setId(user.getId());
        model.setUsername(user.getUsername());
        model.setEmail(user.getEmail());
        model.setFirstName(user.getFirstName());
        model.setLastName(user.getLastName());
        model.setAccounts(user.getAccounts());

        model.add(linkTo(methodOn(UserController.class).getUserById(user.getId())).withSelfRel());
        model.add(linkTo(methodOn(UserController.class).getUsers()).withRel("users"));

        return model;
    }
}