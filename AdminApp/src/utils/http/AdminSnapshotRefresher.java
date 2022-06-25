package utils.http;

import com.google.gson.Gson;
import core.dtos.AdminSnapshot;
import core.dtos.CustomerSnapshot;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import utils.resources.AdminPaths;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

public class AdminSnapshotRefresher extends TimerTask {



    private final Consumer<AdminSnapshot> snapshotConsumer;
    private final String userName;

    public AdminSnapshotRefresher(Consumer<AdminSnapshot> snapshotConsumer, String userName) {
        this.snapshotConsumer = snapshotConsumer;
        this.userName = userName;
    }

    /**
     * The action to be performed by this timer task.
     */
    @Override
    public void run() {

        final String url = HttpUrl
                .parse(AdminPaths.ADMIN_SNAPSHOT)
                .newBuilder()
                .addQueryParameter("username",userName)
                .build()
                .toString();

        AdminHttpClient.runAsync(url,"GET",null, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String s = response.body().string();
                Gson gson = new Gson();
                AdminSnapshot data = gson.fromJson(s,AdminSnapshot.class);
                snapshotConsumer.accept(data);
            }
        });

    }
}
