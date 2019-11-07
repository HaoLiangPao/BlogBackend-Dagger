package ca.utoronto.utm.mcs;

import java.io.IOException;

import javax.inject.Inject;
import org.bson.*;
import org.bson.types.*;
import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCursor;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.util.ArrayList;
import java.util.List;

public class getPost implements HttpHandler{

	@Inject MongoClient client;
	MongoDatabase database;
	MongoCollection collection;
	private static String databaseName = "csc301a2";
	private static String collectionName = "posts";

	public getPost() {
		// TODO Auto-generated constructor stub
		this.database = client.getDatabase(databaseName);
		this.collection = database.getCollection(collectionName);
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
		String body = Util.convert(r.getRequestBody());
	    JSONObject deserialized = new JSONObject(body);
	    MongoCursor result;
	    if (deserialized.has("_id")) {
	    	//r.sendResponseHeaders(400, -1);
	    	String id = deserialized.getString("_id");
	    	result = getID(id);
	    }
	    else if(deserialized.has("title")){
		    String title = deserialized.getString("title");
		    getTitle(title);
	    }
	    else {
	    	r.sendResponseHeaders(400, -1);
	    }
	}
	
	public MongoCursor getID(String id) {
		BasicDBObject want_id = new BasicDBObject();
		want_id.put("_id", new ObjectId(id));
		//Document myDoc = this.collection.find(eq("_id", id));
		//MongoCursor cursor = (MongoCursor) this.collection.find(want_id);
		MongoCursor wanted_doc = (MongoCursor) this.collection.find(want_id);
		return wanted_doc;
	}
	
	public MongoCursor getTitle(String title) {
		BasicDBObject want_title = new BasicDBObject();
		
		return null;
		
	}

}
