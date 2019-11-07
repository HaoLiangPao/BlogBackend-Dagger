package ca.utoronto.utm.mcs;

import com.mongodb.DBObject;
import com.mongodb.client.MongoCollection;
import com.sun.net.httpserver.HttpHandler;
import com.sun.xml.internal.fastinfoset.util.StringArray;
import dagger.ObjectGraph;
import java.util.ArrayList;
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
  private ArrayList<String> tags = new ArrayList<String>();

  //constructor: post and client is injected so the constructor does not have to do anything
  public putPost(){
    ObjectGraph objectGraph = ObjectGraph.create(new DaggerModule(new Config()));
    post = objectGraph.get(Post.class);
    database  = objectGraph.get(MongoDB.class);
    convertor = objectGraph.get(postConvertor.class);
  }

  public void handle(HttpExchange r) {
    try {
      System.out.println("Http Method is: " + r.getRequestMethod());
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
        // Check if the input data type is not what required.
        if ((deserialized.getString("title").getClass().equals(String.class)) &
            (deserialized.getString("author").getClass().equals(String.class)) &
            (deserialized.getString("content").getClass().equals(String.class)) &
            (deserialized.get("tags").getClass().equals(JSONArray.class))){
          post.setTitle(deserialized.getString("title"));
          post.setAuthor(deserialized.getString("author"));
          post.setContent(deserialized.getString("content"));
          // special handling in tags since it should be an arrayList of strings.
          JSONArray inputTags = (JSONArray) deserialized.get("tags");
          try {
            for (int i = 0; i < inputTags.length(); i++){
              String tag = (String) inputTags.get(i);
              tags.add(tag);
            }
            post.setTags(tags);
            System.out.println(post.getTags());
            System.out.println(post);

            System.out.println("post is successfully created");

            //interaction with database
            add(convertor.toDocument(post), r);

            //result for server-client interaction
            r.sendResponseHeaders(200, 0);
            OutputStream os = r.getResponseBody();
            os.close();
          }
          // return 400 if the data types for author, title, content are not correct
          // return 400 if the data type is not correct for tags. ex: [int, float, array...]
          catch (Exception e){
            System.out.println("Error Message: incompatible data type");
            r.sendResponseHeaders(400, -1);
          }
        }
      }
    }
    //if deserilized failed, (ex: JSONObeject Null Value)
    catch(JSONException e) {
      System.out.println("Error Message: JSONObject exception");
      r.sendResponseHeaders(400, -1);
    }
    //if server connection / database connection failed
    catch(Exception e) {
      System.out.println("Error Message: [handelPut]something happened");
      r.sendResponseHeaders(500, -1);
    }
  }

  private void add(Document dbDocument, HttpExchange r) throws Exception {
    MongoCollection collection = database.getClient().getDatabase("csc301a2")
        .getCollection("posts");
    System.out.println(dbDocument.toString());
    System.out.println(collection.toString());
    // add the document to the database
    collection.insertOne(dbDocument);
    System.out.println("Log: insert operation is completed");
    database.close();
  }

}
