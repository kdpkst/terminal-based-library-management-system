package Factory.UserFactory;

import Models.User.user;

public abstract class userFactory {
    public abstract user createUser(int uid, String username, String password, String bidWant);
    public abstract user createUser();
}
