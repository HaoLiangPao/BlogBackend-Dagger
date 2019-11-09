package ca.utoronto.utm.mcs;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dagger.ObjectGraph;
import javax.inject.Inject;

public class requestHandle implements HttpHandler {

  @Inject putPost put;
  @Inject deletePost delete;
//  @Inject getPost get;


  @Override
  public void handle(HttpExchange r) {
    try {
      ObjectGraph objectGraph = ObjectGraph.create(new DaggerModule(new Config()));
      put = objectGraph.get(putPost.class);
      delete = objectGraph.get(deletePost.class);
//      put = objectGraph.getClass(putPost.class);
      System.out.println("\nHttp Method is: " + r.getRequestMethod());
      if (r.getRequestMethod().equals("PUT")) {
        put.handlePut(r);
      } else if (r.getRequestMethod().equals("DELETE")) {
        delete.handleDelete(r);
      }
//      else if (r.getRequestMethod().equals("GET")){
//        get.handleGet(r);
//      }
      //Undefined HTTP methods used on valid endPoint
      else {
        r.sendResponseHeaders(500, -1);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
