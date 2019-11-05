package ca.utoronto.utm.mcs;

import java.io.IOException;
import com.sun.net.httpserver.HttpServer;

import dagger.Module;
import dagger.Provides;
import com.mongodb.client.MongoClient;


@Module (injects = {App.class}, library = true) //TODO: Add in any new classes here
class DaggerModule {
    Config config;
    Server server;

    DaggerModule(Config cfg) {
        config = cfg;
    }

    @Provides MongoClient provideMongoClient() {
        // return a MongoClient created from a local class with required Database Name and
        // Collection name
        MongoDB clientDB = new MongoDB();
        return clientDB.getClient();
    }

    @Provides HttpServer provideHttpServer() {
        // create a HttpServer at ip and port according to config
        try {
            this.server = new Server(config.ip, config.port);
        }
        catch (IOException e){
            server = null;
        }
        return server.getServer();
    }
}
