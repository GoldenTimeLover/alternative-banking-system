package utils;

import core.engine.ABSEngine;
import core.engine.Engine;
import jakarta.servlet.ServletContext;
import users.UserManager;


public class ServerUtils {

    private static final String USER_MANAGER_ATTRIBUTE_NAME = "userManager";
    private static final String ENGINE_ATTRIBUTE_NAME = "engine";

    private static final Object userManagerLock = new Object();
    private static final Object engineLock = new Object();

    public static UserManager getUserManager(ServletContext servletContext) {


        synchronized (userManagerLock) {
            if (servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(USER_MANAGER_ATTRIBUTE_NAME, new UserManager());
            }
        }
        return (UserManager) servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME);
    }

    public static ABSEngine getEngine(ServletContext servletContext){

        synchronized (engineLock) {
            if (servletContext.getAttribute(ENGINE_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(ENGINE_ATTRIBUTE_NAME, new ABSEngine());
            }
        }
        return (ABSEngine) servletContext.getAttribute(ENGINE_ATTRIBUTE_NAME);

    }
}
