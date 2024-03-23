package Models.UserFactory;

import Models.User.normalUser;
import Models.User.user;

public class normalUserFactory implements userFactory{

    @Override
    public user createUser() {
        return new normalUser(); 
    }
    
}
