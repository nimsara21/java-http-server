import java.util.Map;

public class HttpRequest {
    private String method;
    private String path;
    private Map<String, String> queryParams;

    public HttpRequest(String method, String path, Map<String, String> queryParams){
        this.method = method;
        this.path = path;
        this.queryParams = queryParams;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getQueryParams() {
        return queryParams;
    }
}
