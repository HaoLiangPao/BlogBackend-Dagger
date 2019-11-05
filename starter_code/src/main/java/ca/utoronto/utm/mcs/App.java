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
    @Inject MongoClient client;
    @Inject Config config;

    public void run()
    {
        /* TODO: Add Working Context Here */

        // some endpoints for required functionality
//        server.createContext("/api/v1/addActor", new addActor(database));
//        server.createContext("/api/v1/addMovie", new addMovie(database));
//        server.createContext("/api/v1/addRelationship", new addRelationship(database));
//        server.createContext("/api/v1/getActor", new getActor(database));
//        server.createContext("/api/v1/getMovie", new getMovie(database));
//        server.createContext("/api/v1/hasRelationship", new hasRelationship(database));
//        server.createContext("/api/v1/computeBaconPath", new computeBaconPath(database));
//        server.createContext("/api/v1/computeBaconNumber", new computeBaconNumber(database));

        server.setExecutor(null);
        server.start();
        System.out.printf("Server started on port %d...\n", config.port);
    }

    public static void main(String[] args) throws URISyntaxException
    {
        ObjectGraph objectGraph = ObjectGraph.create(new DaggerModule(new Config()));
        App app = objectGraph.get(App.class);
        app.run();
    }
}