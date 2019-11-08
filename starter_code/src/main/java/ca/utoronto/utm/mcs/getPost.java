package ca.utoronto.utm.mcs;

import java.io.IOException;

import javax.inject.Inject;

import com.mongodb.DBObject;
import dagger.ObjectGraph;
import org.bson.Document;
import org.bson.types.*;
import org.json.*;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCursor;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class getPost implements HttpHandler{

	//@Inject MongoClient client;
	@Inject MongoDB mongodb;
	@Inject Post post;
	@Inject postConvertor convertor;
	MongoDatabase database;
	MongoCollection collection;
	private static MongoClient client;
	private static String databaseName = "csc301a2";
	private static String collectionName = "posts";
	private Map getresponse;

	public getPost() {
		ObjectGraph objectGraph = ObjectGraph.create(new DaggerModule(new Config()));
		post = objectGraph.get(Post.class);
		mongodb  = objectGraph.get(MongoDB.class);
		convertor = objectGraph.get(postConvertor.class);
		// TODO Auto-generated constructor stub
		//this.database = client.getDatabase(databaseName);
		//this.collection = database.getCollection(collectionName);
	}

	@Override
	public void handle(HttpExchange r) throws IOException {
		// TODO Auto-generated method stub
		if (r.getRequestMethod().equals("GET")) {
			try {
				handleGet(r);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public void handleGet(HttpExchange r) throws IOException, JSONException {
		System.out.println("handleGet is called");
		String body = Utils.convert(r.getRequestBody());
	    JSONObject deserialized = new JSONObject(body);
	    //MongoCursor result;
	    if (deserialized.has("_id")) {
	    	System.out.println("id found");
	    	//r.sendResponseHeaders(400, -1);
	    	String id = deserialized.getString("_id");
	    	getID(id);
			JSONObject response = new JSONObject(getresponse);
			byte[] result = response.toString().getBytes();

			r.sendResponseHeaders(200, result.length);
			OutputStream os = r.getResponseBody();
			os.write(result);
			os.close();
	    }
	    else if(deserialized.has("title")){
		    String title = deserialized.getString("title");
		    getTitle(title);
	    }
	    else {
	    	r.sendResponseHeaders(400, -1);
	    }






	}
	
	public void getID(String id) {
		System.out.println("getID called");
		MongoCollection collection = mongodb.getClient().getDatabase("csc301a2").getCollection("posts");
		BasicDBObject want_id = new BasicDBObject();
		want_id.put("_id", new ObjectId(id));
		System.out.println(want_id);
		System.out.println(want_id.getClass());
		System.out.println("query finished");
		//Document myDoc = this.collection.find(eq("_id", id));
		//DBObject set = (DBObject) collection.find(want_id);
		MongoCursor<Document> cursor = collection.find(want_id).iterator();
		System.out.println("set is " + cursor.toString());
		Document resultpost = cursor.next();
		//System.out.println("document is " + resultpost.toString());
		System.out.println(cursor);
		//System.out.println(cursor.next());
		getresponse = resultpost;
		System.out.println("response got");
		//post = convertor.toPost(resultpost);
		//MongoCursor wanted_doc = (MongoCursor) this.collection.find(want_id);
		//System.out.println(mapversion);
		//return mapversion;
	}
	
	public MongoCursor getTitle(String title) {
		BasicDBObject want_title = new BasicDBObject();
		
		return null;
		
	}

}
