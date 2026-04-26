import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class MyService {
    private static Router router = new Router();
    public static void main(String[] args) {
        int port = 8080;


        try (ServerSocket serverSocket = new ServerSocket(port)) {

            router.addRoute("/hello", req -> {
                String name = req.getQueryParams().get("name");

                if (name == null) {
                    return "Hello Guest!";
                }

                return "Hello " + name;
            });

            System.out.println("Server started on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();

                // Each request handled in a new thread
                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket socket){
        try(
                InputStream input = socket.getInputStream();
                OutputStream output = socket.getOutputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input))
                ){
            String requestLine = reader.readLine();
            System.out.println(requestLine);

            if (requestLine == null) return;

            String[] parts = requestLine.split(" ");
            String method = parts[0];
            String fullPath = parts[1];

            String path;
            Map<String, String> queryParams = new HashMap<>();

            if (fullPath.contains("?")) {
                String[] split = fullPath.split("\\?");
                path = split[0];

                String queryString = split[1];

                String[] pairs = queryString.split("&");

                for (String pair : pairs) {
                    String[] kv = pair.split("=");
                    queryParams.put(kv[0], kv[1]);
                }
            } else {
                path = fullPath;
            }

            HttpRequest request = new HttpRequest(method, path, queryParams);

            System.out.println("Method: " + method);
            System.out.println("Path: " + path);

            Handler handler = router.getHandler(path);

            String responseBody;
            String statusLine;

            if (handler != null) {
                responseBody = handler.handle(request);
                statusLine = "HTTP/1.1 200 OK";
            } else {
                responseBody = "404 Not Found";
                statusLine = "HTTP/1.1 404 Not Found";
            }

            String httpResponse =
                    statusLine + "\r\n" +
                            "Content-Type: text/plain\r\n" +
                            "Content-Length: " + responseBody.length() + "\r\n" +
                            "\r\n" +
                            responseBody;

            output.write(httpResponse.getBytes());
            output.flush();

            socket.close();

        } catch (IOException e) {
            e.printStackTrace();

        }

    }

}
