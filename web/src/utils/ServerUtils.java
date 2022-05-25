package utils;

import jakarta.servlet.ServletContext;
import users.UserManager;

public class ServerUtils {

    private static final String USER_MANAGER_ATTRIBUTE_NAME = "userManager";

    public synchronized static UserManager getUserManager(ServletContext servletContext) {

        if (servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME) == null) {
            servletContext.setAttribute(USER_MANAGER_ATTRIBUTE_NAME, new UserManager());
        }
        return (UserManager) servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME);
    }
}
