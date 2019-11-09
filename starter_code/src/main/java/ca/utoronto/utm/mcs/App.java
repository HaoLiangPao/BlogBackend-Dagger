package ca.utoronto.utm.mcs;

import com.mongodb.client.MongoClient;
import java.net.URI;
import java.net.URISyntaxException;
import com.sun.net.httpserver.HttpServer;

import javax.inject.Inject;
import dagger.ObjectGraph;

public class App implements Runnable
{
    @Inject HttpServer server;
    @Inject Config config;
    @Inject getPost route_get;
    @Inject requestHandle handle;

    public void run()
    {
        /* TODO: Add Working Context Here */
        System.out.println("app running");
        server.createContext("/api/v1/post", handle);
        server.setExecutor(null);
        server.start();
        System.out.printf("Server started on port %d...\n", config.port);
    }

    public static void main(String[] args) {
        ObjectGraph objectGraph = ObjectGraph.create(new DaggerModule(new Config()));
        App app = objectGraph.get(App.class);
        app.run();
    }
}
