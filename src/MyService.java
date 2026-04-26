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

                if (req.getMethod().equals("POST")) {
                    return "Received POST body: " + req.getBody();
                }

                String name = req.getQueryParams().get("name");

                return (name == null) ? "Hello Guest" : "Hello " + name;
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
            if (requestLine == null) return;

            System.out.println(requestLine);

// Read headers (we stop at empty line)
            String line;
            int contentLength = 0;

            while (!(line = reader.readLine()).isEmpty()) {
                System.out.println(line);

                if (line.startsWith("Content-Length:")) {
                    contentLength = Integer.parseInt(line.split(":")[1].trim());
                }
            }

// Read body (IMPORTANT PART)
            StringBuilder body = new StringBuilder();

            if (contentLength > 0) {
                char[] buffer = new char[contentLength];
                reader.read(buffer, 0, contentLength);
                body.append(buffer);
            }

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

            HttpRequest request =
                    new HttpRequest(method, path, queryParams, body.toString());

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
