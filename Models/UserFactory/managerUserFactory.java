package Models.UserFactory;

import Models.User.managerUser;
import Models.User.user;

public class managerUserFactory extends userFactory{

    @Override
    public user createUser(int uid, String username, String password, String bidWant) {
        return new managerUser(uid, username, password, bidWant);
    }
    
}
