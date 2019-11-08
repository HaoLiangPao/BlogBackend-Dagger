package ca.utoronto.utm.mcs;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import javax.inject.Inject;

public class requestHandle implements HttpHandler {

  @Inject putPost put;
  @Inject deletePost delete;
//  @Inject getPost get;


  @Override
  public void handle(HttpExchange r) {
    try {
      System.out.println("Http Method is: " + r.getRequestMethod());
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
