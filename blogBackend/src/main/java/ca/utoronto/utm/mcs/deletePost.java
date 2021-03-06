package ca.utoronto.utm.mcs;

import com.mongodb.client.MongoCollection;
import com.sun.net.httpserver.HttpExchange;
import dagger.ObjectGraph;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Hashtable;
import javax.inject.Inject;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONException;
import org.json.JSONObject;

public class deletePost {

  @Inject MongoDB database;
  @Inject Post post;
  @Inject postConvertor convertor;

  //constructor: post and client is injected so the constructor does not have to do anything
  public deletePost(){
    ObjectGraph objectGraph = ObjectGraph.create(new DaggerModule(new Config()));
    post = objectGraph.get(Post.class);
    database  = objectGraph.get(MongoDB.class);
    convertor = objectGraph.get(postConvertor.class);
  }

  // perform query in MongoDB
  public void handleDelete(HttpExchange r) throws IOException {
    try {
      String body = Utils.convert(r.getRequestBody());
      JSONObject deserialized = new JSONObject(body);

      //If any parameters are not given return 400 as BAD REQUEST
      if (!deserialized.has("_id")) {
        System.out.println("Error Message: request dose not have '_id'\n");
        r.sendResponseHeaders(400, -1);
      } else {
        // Check if the input data type is not what required, return 400 as BAD REQUEST
        if ((deserialized.get("_id").getClass().equals(String.class))) {
          System.out.println("Log: data type is consistent, id is:" +
              deserialized.get("_id"));
          // delete the document associates to the id in mongoDBse
          delete(deserialized.getString("_id"), r);
          database.close();
          OutputStream os = r.getResponseBody();
          os.close();
        }
        else{
          System.out.println("Error Message: incompatible input data type of '_id'");
          r.sendResponseHeaders(400, -1);
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

  private void delete(String id, HttpExchange r) throws Exception {

    try {
      //get access to the collection
      MongoCollection collection = database.getClient().getDatabase("csc301a2")
          .getCollection("posts");
      //store the id and change the type to be used in a mongodb query
      ObjectId objectId = new ObjectId(id);
      Hashtable queryPair = new Hashtable();
      queryPair.put("_id", objectId);
      Document query = new Document(queryPair);
      // add the document to the database
      if (collection.deleteOne(query).getDeletedCount() != 0){
        System.out.println("Log: delete operation is completed");
        //result for server-client interaction
        r.sendResponseHeaders(200, 0);
      }
      else  {
        System.out.println("Error Message: the post is not found in the database, delete did not"
            + "complete");
        r.sendResponseHeaders(404, -1);
      }
    }
    catch (Exception e){
      // object id is string but not able to parsed, return 400 as BAD REQUEST
      r.sendResponseHeaders(400, -1);
    }
  }
}

