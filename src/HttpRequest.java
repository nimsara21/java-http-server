import java.util.Map;

public class HttpRequest {

    private String method;
    private String path;
    private Map<String, String> queryParams;
    private String body;

    public HttpRequest(String method, String path,
                       Map<String, String> queryParams,
                       String body) {
        this.method = method;
        this.path = path;
        this.queryParams = queryParams;
        this.body = body;
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

    public String getBody() {
        return body;
    }
}