package App.model.auth;

import App.model.comm.User;
import com.sun.net.httpserver.HttpServer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public final class AuthorizationService {
    public static final String ACCESS_SERVER_POINT = "accounts.spotify.com";
    public static final String SCHEME = "https";
    public static final String HOST;
    public static final String ID;
    public static final String QUERY;
    private static final String secret;

    private String currentCode;

    public AuthorizationService() {
        super();
    }

    static  {
        try {
            ID = new String(Files.readAllBytes(Paths.get("app_data/client_id.txt")));
            secret = new String(Files.readAllBytes(Paths.get("app_data/client_secret.txt")));
            HOST = new String(Files.readAllBytes(Paths.get("app_data/host.txt")));
            QUERY = "client_id=" + ID + "&redirect_uri=" + HOST + "&response_type=code";
        } catch (IOException e) {
            throw new RuntimeException("Error loading client id and secret, check if files are in app_data directory and try again.");
        }
    }

    public boolean startServerAndWaitForCode() throws AuthorizationException {
        final var responseWrapper = new Object() {
            String authorizationCode = "";
        };
        final ExecutorService ex = Executors.newSingleThreadExecutor();
        final CountDownLatch c = new CountDownLatch(1);
        try {
            HttpServer server = HttpServer.create();
            server.bind(new InetSocketAddress(8080), 1);
            server.createContext("/", httpExchange -> {
                String requestQuery = httpExchange.getRequestURI().getQuery();
                String responseMessage;
                if (requestQuery == null) {
                    responseMessage = "Authorization code not found. Try again. Make sure you used provided URL.";
                    httpExchange.sendResponseHeaders(401, responseMessage.length());
                    httpExchange.getResponseBody().write(responseMessage.getBytes());
                    httpExchange.getResponseBody().close();
                    httpExchange.close();
                } else if (requestQuery.contains("error=access_denied")) {
                    responseMessage = "Authorization cancelled.";
                    httpExchange.sendResponseHeaders(401, responseMessage.length());
                    httpExchange.getResponseBody().write(responseMessage.getBytes());
                    httpExchange.getResponseBody().close();
                    httpExchange.close();
                    c.countDown();
                } else if (requestQuery.contains("code")) {
                    currentCode = getAuthorizationCodeFromQuery(requestQuery);
                    responseWrapper.authorizationCode = getAuthorizationCodeFromQuery(requestQuery);
                    responseMessage = "Got the code. Return back to your program.";
                    httpExchange.sendResponseHeaders(200, responseMessage.length());
                    httpExchange.getResponseBody().write(responseMessage.getBytes());
                    httpExchange.getResponseBody().close();
                    httpExchange.close();
                    c.countDown();
                }

            });
            server.setExecutor(ex);
            server.start();
            c.await();
            ex.shutdown();
            ex.awaitTermination(1000, TimeUnit.MILLISECONDS);
            server.stop(1);
            return !(currentCode == null);
        } catch (IOException e) {
            throw new AuthorizationException("IO exception during binding server to port.", e);
        } catch (InterruptedException ie) {
            throw new AuthorizationException("Internal error, Executor Service interrupted during termination", ie);
        }
    }

    //gets code from response query
    private String getAuthorizationCodeFromQuery (String requestQuery) {
        String[] queryPairs = requestQuery.split("&");
        List<QueryKeyValuePair> list = Arrays.stream(queryPairs)
                .map(string -> {
                    String[] keyValue = string.split("=");
                    return new QueryKeyValuePair(keyValue[0], keyValue[1]); })
                .collect(Collectors.toList());

        String code = "";
        for (QueryKeyValuePair queryKeyValuePair : list) {
            if (queryKeyValuePair.getKey().equals("code")) {
                code = queryKeyValuePair.getValue();
            }
        }
        return code;
    }

    public User requestTokensAndCreateUser() throws AuthorizationException {
        String responseJson;
        try {
            responseJson = requestTokens();
        } catch (IOException e) {
            throw new AuthorizationException("IO exception during making http request.", e);
        } catch (InterruptedException ie) {
            throw new AuthorizationException("Interrupted exception during sending http requests to spotify api.", ie);
        }
        User user = User.CreateUserFromJson(responseJson);
        user.setAuthorizationCode(currentCode);
        return user;
    }

    private String requestTokens() throws IOException, InterruptedException {
        String responseJson = "";
        HttpResponse<String> response = makeRequest();

        if (response != null && response.body().contains("access_token")) {
            responseJson = response.body();
        }
        return responseJson;
    }

    private HttpResponse<String> makeRequest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "Basic " + getEncodedIdAndSecret())
                .header("Content-Type", "application/x-www-form-urlencoded")
                .uri(URI.create(SCHEME + "://" + ACCESS_SERVER_POINT + "/api/token"))
                .POST(HttpRequest.BodyPublishers.ofString(buildRequestBody()))
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private String buildRequestBody() {
        StringBuilder requestBody = new StringBuilder();
        requestBody.append("grant_type");
        requestBody.append("=");
        requestBody.append("authorization_code");
        requestBody.append("&");
        requestBody.append("code");
        requestBody.append("=");
        requestBody.append(currentCode);
        requestBody.append("&");
        requestBody.append("redirect_uri");
        requestBody.append("=");
        requestBody.append(HOST);
        requestBody.append("&");
        requestBody.append("client_id");
        requestBody.append("=");
        requestBody.append(ID);

        return requestBody.toString();
    }

    private String getEncodedIdAndSecret() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] idBytes = ID.getBytes();
        byte[] colon = ":".getBytes();
        byte[] secretBytes = secret.getBytes();
        outputStream.write(idBytes);
        outputStream.write(colon);
        outputStream.write(secretBytes);
        return Base64.getEncoder()
                .encodeToString(outputStream.toByteArray());
    }

    static class QueryKeyValuePair {
        private final String key;
        private final String value;

        public QueryKeyValuePair(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }
    }
}
