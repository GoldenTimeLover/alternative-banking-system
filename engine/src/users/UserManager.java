package users;

import core.engine.ABSEngine;
import core.entities.Customer;

import java.util.*;


/***
 * class for managing user login logouts
 * check if users exist in the system ect..
 */
public class UserManager {

    private final Map<String, UserInfo> userMap;

    private String adminName = "";

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public UserManager() {
        userMap = new HashMap<>();
    }

    public synchronized void addAdmin(String adminUsername, ABSEngine engine) {
        if(!userMap.containsKey(adminUsername)){
            engine.getCustomers().add(new Customer(adminUsername,true));
        }
        userMap.put(adminUsername,new UserInfo(adminUsername,true,true));
        adminName = adminUsername;
    }

    public synchronized String getAdminName() {
        return adminName;
    }



    public synchronized void addCustomer(String username,ABSEngine engine) {
            if(!userMap.containsKey(username)){
                engine.getCustomers().add(new Customer(username,false));
                engine.getNotifications().put(username,new ArrayList<>());
            }
            userMap.put(username,new UserInfo(username,false,true));
    }

    public synchronized void logoutUser(String username) {
        if(userMap.containsKey(username)){
            boolean isA = userMap.get(username).isAdmin;
            userMap.put(username,new UserInfo(username,isA,false));
            if(isA){
                adminName = "";
            }
        }

    }


    public synchronized boolean isUserExists(String username) {
        return adminName.equals(username)||
                userMap.containsKey(username);
    }


    public synchronized boolean isUserLoggedIn(String userName){
        return userMap.containsKey(userName) && userMap.get(userName).isLoggedIn;
    }


}
