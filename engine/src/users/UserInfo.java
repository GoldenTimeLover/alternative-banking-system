package users;

public class UserInfo {

    public String userName;
    public boolean isAdmin;
    public boolean isLoggedIn;

    public UserInfo(String userName, boolean isAdmin, boolean isLoggedIn) {
        this.userName = userName;
        this.isAdmin = isAdmin;
        this.isLoggedIn = isLoggedIn;
    }
}
