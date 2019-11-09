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

public class putPost {

  @Inject MongoDB database;
  @Inject Post post;
  @Inject postConvertor convertor;
  private ArrayList<String> tags = new ArrayList<String>();
  private JSONArray inputTags;
  private String id;

  //constructor: post and client is injected so the constructor does not have to do anything
  public putPost(){
    ObjectGraph objectGraph = ObjectGraph.create(new DaggerModule(new Config()));
    post = objectGraph.get(Post.class);
    database  = objectGraph.get(MongoDB.class);
    System.out.println("database is created");
    convertor = objectGraph.get(postConvertor.class);
  }

  // perform query in MongoDB
  public void handlePut(HttpExchange r) throws IOException {
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
          inputTags = (JSONArray) deserialized.get("tags");
        }
        // return 400 if the data types for author, title, content are not correct
        // return 400 if the data type is not correct for tags. ex: [int, float, array...]
        else {
          System.out.println("Error Message: incompatible data type");
          r.sendResponseHeaders(400, -1);
        }
        for (int i = 0; i < inputTags.length(); i++){
          String tag = (String) inputTags.get(i);
          tags.add(tag);
        }
        post.setTags(tags);
        System.out.println(post.getTags());
        System.out.println("post is successfully created");

        //interaction with database
        id = add(convertor.toDocument(post), r);
        //close database client
        database.close();

        JSONObject response = new JSONObject();
        response.put("_id", id);

        //result for server-client interaction
        r.sendResponseHeaders(200, 0);
        OutputStream os = r.getResponseBody();
        os.write(response.toString().getBytes());
        os.close();
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

  private String add(Document dbDocument, HttpExchange r) throws Exception {
    MongoCollection collection = database.getClient().getDatabase("csc301a2")
        .getCollection("posts");
    System.out.println(dbDocument.toString());
    System.out.println(collection.toString());
    // add the document to the database
    collection.insertOne(dbDocument);
    Document docAdded = (Document) collection.find(dbDocument).iterator().next();
    System.out.println("Log: insert operation is completed");
    return docAdded.get("_id").toString();
  }
}
