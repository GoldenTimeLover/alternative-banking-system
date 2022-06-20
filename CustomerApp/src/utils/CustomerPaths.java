package utils;

public class CustomerPaths {


    //project paths
    public static String PRIMARY = "/ui/primaryScene.fxml";
    public static String CUSTOMER_PANEL = "/ui/customerPanel/customerPanel.fxml";
    public static String CUSTOMER_LOGIN = "/ui/login/CustomerLogin.fxml";

    public static String CUSTOMER_MATCHING = "/ui/customerMatching/customerMatching.fxml";
    public static String CUSTOMER_PAYMENT = "/ui/customerPayment/customerPayment.fxml";
    public static String CUSTOMER_INFO = "/ui/customerInfo/customerInfo.fxml";
    public static String CUSTOMER_ADD_LOAN = "/ui/customerAddLoanPanel/customerAddLoanPanel.fxml";

    public static String LOAN_INFO = "/ui/components/loanDetails/loanDetail.fxml";
    public static String SINGLE_NOTIFICATION = "/ui/notificationArea/notification.fxml";
    public static String NOTIFICATION_AREA = "/ui/notificationArea/notificationArea.fxml";


    public static String LIGHT_PRIMARY_THEME = "/utils/resources/stylesheets/primarySceneLight.css";
    public static String MCDONALDS_PRIMARY_THEME = "/utils/resources/stylesheets/primarySceneMcdonalds.css";
    public static String DARK_PRIMARY_THEME = "/utils/resources/stylesheets/primarySceneDark.css";

    public static String MCDONALDS_BODY_THEME  = "/utils/resources/stylesheets/bodyComponentMcdonalds.css";
    public static String  LIGHT_BODY_THEME = "/utils/resources/stylesheets/bodyComponentLight.css";
    public static String DARK_BODY_THEME = "/utils/resources/stylesheets/bodyComponentDark.css";



    //server paths
    public final static String BASE_DOMAIN = "localhost";
    public final static String APPLICATION_NAME = "/web_Web_exploded";
    public final static String FULL_SERVER_PATH = "http://" + BASE_DOMAIN + ":8080" + APPLICATION_NAME;

    public final static String ADD_LOAN_PATH = FULL_SERVER_PATH + "/loan/add";
    public final static String ADD_TRANSACTION = FULL_SERVER_PATH + "/user/transaction";
    public final static String FILTER_LOANS = FULL_SERVER_PATH + "/loan/filter";
    public final static String LOGIN_PAGE = FULL_SERVER_PATH + "/user/login";
    public final static String GET_CATEGORIES = FULL_SERVER_PATH + "/loan/categories";
    public final static String FINANCE_LOAN = FULL_SERVER_PATH + "/loan/finance";
    public final static String GET_SNAPSHOT = FULL_SERVER_PATH + "/user/snapshot";
}
