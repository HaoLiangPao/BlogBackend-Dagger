package ca.utoronto.utm.mcs;

import com.sun.xml.internal.fastinfoset.util.StringArray;
import javax.inject.Inject;
import org.json.*;
import com.mongodb.client.MongoClient;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public class putPost {

  @Inject MongoClient client;

  private String title;
  private String author;
  private String content;
  private StringArray tags;

  private Map addResponse;

  //constructor: create a MongoDBClient
  public putPost(){
  }

  public void handle(HttpExchange r) {
    try {
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
      //Interacted with database and add actor, then return 200 as OK
      else {
        if (deserialized.getString("name").){
          this.title = deserialized.getString("name");
        }

        this.author = deserialized.getString("author");
        this.content = deserialized.getString("content");
        this.tags = (StringArray) deserialized.get("tags");


        //interaction with database
        add(name, ID);
        //result for server-client interaction
        JSONObject responseJSON = new JSONObject();
        responseJSON.put("name", addResponse.get("a.name"));
        responseJSON.put("actorID", addResponse.get("a.id"));
        byte[] result = responseJSON.toString().getBytes();
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
      r.sendResponseHeaders(500, -1);
    }
  }
}
