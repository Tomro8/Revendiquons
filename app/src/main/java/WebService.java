public class WebService {
    private static WebService instance;

    public static WebService getInstance() {
        if (instance == null) {
            instance = new WebService();
        }
        return instance;
    }

    private WebService() {
        super();
    }
}
