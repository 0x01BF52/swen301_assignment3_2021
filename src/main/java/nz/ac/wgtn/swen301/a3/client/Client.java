package nz.ac.wgtn.swen301.a3.client;

import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class Client {
    public static void main(String[] args) throws Exception {
        if (args.length != 2 || args[1] == null || args[0] == null) {
            System.out.println("This has the following parameters (in this order):\na. type (either csv or excel) -- the type of data requested\nb. fileName -- a local file name used to store the data fetched");
            return;
        }
        var fileName = args[1];
        var type = args[0];
        var client = new OkHttpClient();
        Request request;
        switch (type) {
            case "excel":
                request = new Request.Builder().get().url("http://localhost:8080/resthome4logs/statsxls").build();
                Files.write(Path.of(fileName), Objects.requireNonNull(client.newCall(request).execute().body()).bytes());
                break;
            case "csv":
                request = new Request.Builder().get().url("http://localhost:8080/resthome4logs/statscsv").build();
                Files.write(Path.of(fileName), Objects.requireNonNull(client.newCall(request).execute().body()).bytes());
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
    }
}
