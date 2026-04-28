import java.util.Map;

public class HttpRequest {

    private String method;
    private String path;
    private Map<String, String> queryParams;
    private String body;
    private Map<String, Object> jsonBody;

    public HttpRequest(String method, String path,
                       Map<String, String> queryParams,
                       String body, Map<String, Object> jsonBody) {
        this.method = method;
        this.path = path;
        this.queryParams = queryParams;
        this.body = body;
        this.jsonBody = jsonBody;
    }

    public Map<String, Object> getJsonBody() {
        return jsonBody;
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