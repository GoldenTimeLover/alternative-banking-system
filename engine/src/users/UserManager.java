package users;

import core.entities.Customer;

import java.util.*;

public class UserManager {

    private final List<String> usersList;
    private final Map<String, Customer> customerMap;
    private String adminName = null;

    public UserManager() {
        usersList = new ArrayList<>();
        customerMap = new HashMap<>();
    }

    public synchronized void addAdmin(String adminUsername) {
        adminName = adminUsername;
    }
    public synchronized String getAdminName() {
        return adminName;
    }

    public synchronized Map<String,Customer> getCustomerMap() {
        return customerMap;
    }

    public synchronized void addCustomer(String username) {
        usersList.add(username);
        if(!customerMap.containsKey(username)) {
            customerMap.put(username, new Customer("not sure why this is here..",1,null,null,null));
        }
    }

    public synchronized void removeUser(String username) {
        if(usersList.contains(username))
            usersList.remove(username);

    }

    public boolean isUserExists(String username) {
        return (adminName.equals(username)||usersList.contains(username));
    }


}
