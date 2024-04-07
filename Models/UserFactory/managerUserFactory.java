package Models.UserFactory;

import Models.User.managerUser;
import Models.User.user;

public class managerUserFactory extends userFactory{

    @Override
    public user createUser() {
        return new managerUser();
    }
    
}
