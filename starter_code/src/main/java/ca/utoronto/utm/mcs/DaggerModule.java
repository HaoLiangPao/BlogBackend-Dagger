package ca.utoronto.utm.mcs;

import com.mongodb.client.MongoClientFactory;
import com.mongodb.client.MongoDatabase;
import java.io.IOException;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpServer;

import javax.inject.Inject;
import dagger.Module;
import dagger.Provides;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import javax.inject.Singleton;


@Module (injects = {App.class}, library = true) //TODO: Add in any new classes here
class DaggerModule {
    Config config;

    DaggerModule(Config cfg) {
        config = cfg;
    }

    @Provides MongoClient provideMongoClient() {
        // return a MongoClient created from a local class with required Database Name and
        // Collection name
        MongoDB clientDB = new MongoDB();
        return clientDB.getClient();
    }

    @Provides HttpServer provideHttpServer() throws IOException {
        // create a HttpServer at ip and port according to config
        return HttpServer.create(new InetSocketAddress(config.ip, config.port), 0);
    }
}
