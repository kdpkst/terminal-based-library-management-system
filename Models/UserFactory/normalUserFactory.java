package Models.UserFactory;

import Models.User.normalUser;
import Models.User.user;

public class normalUserFactory extends userFactory{

    @Override
    public user createUser(int uid, String username, String password, String bidWant) {
        return new normalUser(uid, username, password, bidWant);
    }

    @Override
    public user createUser() {
        return new normalUser();
    }

}
