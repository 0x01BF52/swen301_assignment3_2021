package nz.ac.wgtn.swen301.a3.client;

import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class Client {
    static IllegalArgumentException argExp = new IllegalArgumentException("This has the following parameters (in this order):\n" +
        "a. type (either csv or excel) -- the type of data requested\n" +
        "b. fileName -- a local file name used to store the data fetched");

    public static void main(String[] args) throws Exception {
        if (args.length != 2 || args[1] == null || args[0] == null) {
            throw argExp;
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
