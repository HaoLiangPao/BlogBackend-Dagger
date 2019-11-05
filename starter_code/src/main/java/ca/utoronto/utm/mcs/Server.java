package ca.utoronto.utm.mcs;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;

public class Server {

  private HttpServer server;

  public Server(String ip, int port) throws IOException {
    this.server = HttpServer.create(new InetSocketAddress(ip, port), 0);
  }

  public HttpServer getServer(){
    return this.server;
  }

}
