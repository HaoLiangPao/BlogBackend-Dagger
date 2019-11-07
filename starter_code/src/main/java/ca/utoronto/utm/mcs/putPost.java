package ca.utoronto.utm.mcs;

import com.mongodb.DBObject;
import com.mongodb.client.MongoCollection;
import com.sun.net.httpserver.HttpHandler;
import com.sun.xml.internal.fastinfoset.util.StringArray;
import dagger.ObjectGraph;
import javax.inject.Inject;
import org.bson.Document;
import org.json.*;
import com.mongodb.client.MongoClient;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public class putPost implements HttpHandler {

  @Inject MongoDB database;
  @Inject Post post;
  @Inject postConvertor convertor;

  //constructor: post and client is injected so the constructor does not have to do anything
  public putPost(){
    ObjectGraph objectGraph = ObjectGraph.create(new DaggerModule(new Config()));
    post = objectGraph.get(Post.class);
    database  = objectGraph.get(MongoDB.class);
    convertor = objectGraph.get(postConvertor.class);
  }

  public void handle(HttpExchange r) {
    try {
      System.out.println(r.getRequestMethod());
      if (r.getRequestMethod().equals("PUT")) {
        handlePut(r);
      }
      //Undefined HTTP methods used on valid endPoint
      else{
        r.sendResponseHeaders(500, -1);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // perform query in MongoDB
  private void handlePut(HttpExchange r) throws IOException {
    try {
      String body = Utils.convert(r.getRequestBody());
      JSONObject deserialized = new JSONObject(body);

      //If any parameters are not given return 400 as BAD REQUEST
      if (!deserialized.has("title") || !deserialized.has("author") ||
          !deserialized.has("content") || !deserialized.has("tags")) {
        r.sendResponseHeaders(400, -1);
      }
      else {
        //Test if correct type of data is added

        System.out.println(deserialized.getString("title"));
        System.out.println(deserialized.getString("title").getClass());
        System.out.println(deserialized.getString("author"));
        System.out.println(deserialized.getString("author").getClass());
        System.out.println(deserialized.getString("content"));
        System.out.println(deserialized.getString("content").getClass());
        System.out.println(deserialized.getString("tags"));
        System.out.println(deserialized.get("tags").getClass());
        String[] tags = new String[] deserialized.get("tags");


        if ((deserialized.getString("title").getClass().equals(String.class)) &
            (deserialized.getString("author").getClass().equals(String.class)) &
            (deserialized.getString("content").getClass().equals(String.class)) &
            (deserialized.get("tags").getClass().equals(String[].class))){
          post.setTitle(deserialized.getString("title"));
          post.setAuthor(deserialized.getString("author"));
          post.setContent(deserialized.getString("content"));
          post.setTags((String[]) deserialized.get("tags"));
        }
        else { //return 400 if the data type is not correct
          r.sendResponseHeaders(400, -1);
        }

        System.out.println("post is successfully created");

        //interaction with database
        add(convertor.toDocument(post), r);

        //result for server-client interaction
        r.sendResponseHeaders(200, 0);
        OutputStream os = r.getResponseBody();
        os.close();
      }
    }
    //if deserilized failed, (ex: JSONObeject Null Value)
    catch(JSONException e) {
      r.sendResponseHeaders(400, -1);
    }
    //if server connection / database connection failed
    catch(Exception e) {
      System.out.println("handelPut: something happened");
      r.sendResponseHeaders(500, -1);
    }
  }

  private void add(Document dbDocument, HttpExchange r) throws Exception {
    MongoDB mongoDB = database;
    MongoCollection collection = mongoDB.getClient().getDatabase("csc301a2").getCollection("posts");
    try {
      System.out.println(dbDocument.toString());
      System.out.println(collection);

      collection.insertOne(dbDocument);

      System.out.println("after insert document");
    }
    catch (Exception e){
      r.sendResponseHeaders(111, -1);
    }
    System.out.println("the post is successfully added into the database");
    mongoDB.close();
  }

}