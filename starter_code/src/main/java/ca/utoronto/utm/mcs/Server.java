package ca.utoronto.utm.mcs;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;

public class Server {

  private HttpServer server;

  public Server(String ip, int port) throws IOException {
    System.out.println("server running");
    this.server = HttpServer.create(new InetSocketAddress(ip, port), 0);
    System.out.println("server second running");
  }

  public HttpServer getServer(){
    return this.server;
  }

}
