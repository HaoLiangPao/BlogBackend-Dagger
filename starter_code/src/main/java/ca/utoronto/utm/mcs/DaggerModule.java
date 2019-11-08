package ca.utoronto.utm.mcs;

import java.io.IOException;
import com.sun.net.httpserver.HttpServer;

import dagger.Module;
import dagger.Provides;
import com.mongodb.client.MongoClient;


@Module (injects = {App.class, MongoDB.class, Post.class, postConvertor.class, getPost.class
}, library = true) //TODO: Add in any new classes here
class DaggerModule {
    Config config;
    Server server;

    DaggerModule(Config cfg) {
        config = cfg;
    }

    @Provides MongoDB provideMongoClient() {
        // return a MongoClient created from a local class with required Database Name and
        // Collection name
        return new MongoDB();
    }

    @Provides Post providePost(){
        Post post = new Post();
        return post.getPost();
    }
    @Provides getPost providegetPost(){
        getPost post = new getPost();
        return post;
    }

    @Provides postConvertor provideConvertor(){
        postConvertor convertor = new postConvertor();
        return convertor.getPostConvertor();
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
