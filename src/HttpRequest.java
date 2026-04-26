public class HttpRequest {
    private String method;
    private String path;

    public HttpRequest(String method, String path){
        this.method = method;
        this.path = path;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }
}
