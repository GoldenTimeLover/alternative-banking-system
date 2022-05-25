package utils.http;

import okhttp3.*;

import java.io.IOException;

public class AdminHttpClient {

    private final static OkHttpClient HTTP_CLIENT =
            new OkHttpClient.Builder()
                    .followRedirects(false)
                    .build();

    public static void runAsync(String finalUrl, String method, RequestBody body, Callback callback) {
        Request request = new Request.Builder()
                .url(finalUrl).method(method, body)
                .build();

        AdminHttpClient.HTTP_CLIENT.newCall(request).enqueue(callback);
    }

    public static void runAsyncWithRequest(Request request, Callback callback) {
        AdminHttpClient.HTTP_CLIENT.newCall(request).enqueue(callback);
    }

    public static Response runSyncWithRequest(Request request) throws IOException {
        return  AdminHttpClient.HTTP_CLIENT.newCall(request).execute();
    }


    public static void shutdown() {
        System.out.println("Shutting down HTTP CLIENT");
        HTTP_CLIENT.dispatcher().executorService().shutdown();
        HTTP_CLIENT.connectionPool().evictAll();
    }
}
