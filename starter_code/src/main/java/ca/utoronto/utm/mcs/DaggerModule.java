package ca.utoronto.utm.mcs;

import java.io.IOException;
import com.sun.net.httpserver.HttpServer;

import dagger.Module;
import dagger.Provides;



@Module (injects = {App.class, MongoDB.class, Post.class, postConvertor.class, putPost.class,
    deletePost.class, getPost.class}, library = true) //TODO: Add in any new classes here
class DaggerModule {
    Config config;
    Server server;

    DaggerModule(Config cfg) {
        config = cfg;
    }


    @Provides MongoDB provideMongoDB() {
        // return a MongoClient created from a local class with required Database Name and
        // Collection name
        System.out.println("Log: provideMongoDB");
        return new MongoDB();
    }

    @Provides putPost providePutPost() {
        // return a putPost class to handel communication with mongoDB when PUT is used
        System.out.println("Log: putPost is created");
        return new putPost();
    }

    @Provides deletePost providedeletePost() {
        // return a deletePost class to handel communication with mongoDB when DELETE is used
        System.out.println("Log: deletePost is created");
        return new deletePost();
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
        System.out.println("Log: server is created");
        return server.getServer();
    }
}
