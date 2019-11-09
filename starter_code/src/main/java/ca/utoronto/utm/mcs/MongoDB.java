package ca.utoronto.utm.mcs;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClientFactory;
import com.mongodb.client.MongoDatabase;

import java.util.Hashtable;


public class MongoDB implements AutoCloseable{

  private MongoClient client;
  private static String databaseName = "csc301a2";
  private static String collectionName = "posts";
  private static String mongoDBAddress = "mongodb://127.0.0.1:27017";
  private Hashtable environment = new Hashtable();

  public MongoDB(){
    this.environment.put("connectionString", mongoDBAddress);
    //create a MongoClient
    try{
      client = (MongoClient) new MongoClientFactory().getObjectInstance(
          null, null,null, environment);
    }
    catch (MongoException e){
      //Failed to create a MongoClient
      client = null;
    }
    MongoDatabase database = client.getDatabase(databaseName);
    //create a collection named posts if not exist
    if (database.getCollection(collectionName) == null) {
      database.createCollection(collectionName);
    }
    System.out.printf("Database '%s' is created with Collection '%s'...\n",
        databaseName, collectionName );
  }

  public MongoClient getClient(){
    return this.client;
  }

  @Override
  public void close() throws Exception {
    client.close();
  }
}
