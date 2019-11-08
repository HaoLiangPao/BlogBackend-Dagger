package ca.utoronto.utm.mcs;

import java.io.IOException;
import com.sun.net.httpserver.HttpServer;

import dagger.Module;
import dagger.Provides;


@Module (injects = {App.class, MongoDB.class, Post.class, postConvertor.class, putPost.class, deletePost.class}, library = true) //TODO: Add in any new classes here
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

    @Provides Post providePost() {
        // return a Post class to store necessary data for a legit post
        Post post = new Post();
        System.out.println("Log: Post is created");
        return post.getPost();
    }

    @Provides postConvertor provideConvertor(){
        // return a postConvertor class to convert data from document to Post
        postConvertor convertor = new postConvertor();
        System.out.println("Log: postConvertor is created");
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
