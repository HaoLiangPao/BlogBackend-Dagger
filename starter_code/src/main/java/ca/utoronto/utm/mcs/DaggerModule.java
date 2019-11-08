package ca.utoronto.utm.mcs;

import java.io.IOException;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpServer;

import javax.inject.Inject;
import dagger.Module;
import dagger.Provides;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Module (injects = {App.class}, library = true) //TODO: Add in any new classes here
class DaggerModule {
    Config config;

    DaggerModule(Config cfg) {
        config = cfg;
    }

    @Provides MongoClient provideMongoClient() {
        /* TODO: Fill in this function */
        return null;
    }

    @Provides HttpServer provideHttpServer() {
        /* TODO: Fill in this function */
        return null;
    }
}
