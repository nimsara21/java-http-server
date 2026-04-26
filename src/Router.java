import java.util.HashMap;
import java.util.Map;

public class Router {
    private final Map<String, Handler> routes = new HashMap<>();

    public void addRoute(String path, Handler handler){
        routes.put(path, handler);
    }

    public Handler getHandler(String path){
        return routes.get(path);
    }
}
