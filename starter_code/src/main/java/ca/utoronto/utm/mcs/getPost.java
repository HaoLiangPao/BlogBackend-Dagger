package ca.utoronto.utm.mcs;

import com.mongodb.client.MongoClient;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class getPost implements HttpHandler {
  private MongoClient client;
  private String ID;
  private Map getResponse;

  //constructor
  public getPost(MongoClient client) {
    this.client = client;
  }

  @Override
  public void handle(HttpExchange r) throws IOException {
    try {
      if (r.getRequestMethod().equals("GET")) {
        handleGet(r);
      }
      //Undefined HTTP methods used on valid endPoint
      else{
        r.sendResponseHeaders(500, -1);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void handleGet(HttpExchange r) throws IOException{
    try {
      String body = Utils.convert(r.getRequestBody());
      JSONObject deserialized = new JSONObject(body);

      //See body and deserilized
      System.out.println("addActor-HandelGet get input:");
      System.out.println(deserialized);
      //If actorID is not given return 400 as BAD REQUEST
      if (!deserialized.has("actorID")) {
        r.sendResponseHeaders(400, -1);
      }
      else {
        ID = deserialized.getString("actorID");
        //Interaction with database + assign values to JSONObjects already
        get(ID);
        JSONObject responseJSON = new JSONObject(getResponse);
        byte[] result = responseJSON.toString().getBytes();
        OutputStream os = r.getResponseBody();
        //valid actorID passed in and valid result responded by database
        if (responseJSON.length() != 0) {
          result = responseJSON.toString().getBytes();
          r.sendResponseHeaders(200, result.length);
          os.write(result);
        }
        //actorID not found in the database and 404 return as NO DATA FOUND
        else{
          r.sendResponseHeaders(404, -1);
        }
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
