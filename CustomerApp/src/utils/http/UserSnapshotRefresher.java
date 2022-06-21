package utils.http;

import com.google.gson.Gson;
import com.sun.javafx.fxml.builder.URLBuilder;
import core.dtos.CustomerSnapshot;
import core.dtos.LoansDTO;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import utils.CustomerPaths;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

public class UserSnapshotRefresher extends TimerTask {

    public UserSnapshotRefresher(String username,Consumer<CustomerSnapshot> snapshotConsumer) {
        this.username = username;
        this.snapshotConsumer = snapshotConsumer;
    }

    private final String username;
    private final Consumer<CustomerSnapshot> snapshotConsumer;

    @Override
    public void run() {


        final String url = HttpUrl
                .parse(CustomerPaths.GET_SNAPSHOT)
                .newBuilder()
                .addQueryParameter("username",username)
                .build()
                .toString();

        CustomerHttpClient.runAsync(url,"GET",null, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String s = response.body().string();
                Gson gson = new Gson();
                CustomerSnapshot data = gson.fromJson(s,CustomerSnapshot.class);
                snapshotConsumer.accept(data);
            }
        });
    }
}
