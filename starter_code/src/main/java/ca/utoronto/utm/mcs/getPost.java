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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.List;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCursor;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class getPost {

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
	    	getID(id, r);
			System.out.println("after get, getresponse is " + getresponse.toString());
	    	Map[] response_lis = {getresponse};
			//System.out.println(response_lis);
			//String response_str = response_lis.toString();
			//String response_str1 = "{" + response_str + "}";
			//JSONObject response = new JSONObject(getresponse);
			JSONArray responselist = new JSONArray(Arrays.asList(response_lis));
			//String response = "{" + response_lis.toString() + "}";
			System.out.println("response is " + responselist.toString());
			String response_str = "{" + responselist.toString() + "}";
			byte[] result = response_str.getBytes();

			r.sendResponseHeaders(200, result.length);
			OutputStream os = r.getResponseBody();
			os.write(result);
			os.close();
	    }
	    else if(deserialized.has("title")){
		    String title = deserialized.getString("title");
		    JSONArray responselist = new JSONArray(getTitle(title));
			System.out.println("response is " + responselist.toString());
			String response_str = "{" + responselist.toString() + "}";
			byte[] result = response_str.getBytes();
			r.sendResponseHeaders(200, result.length);
			OutputStream os = r.getResponseBody();
			os.write(result);
			os.close();

	    }
	    else {
	    	r.sendResponseHeaders(400, -1);
	    }
	}
	
	public void getID(String id, HttpExchange r) throws IOException {
		try{
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
			if (cursor.hasNext()){
				Document resultpost = cursor.next();
				System.out.println(cursor);
				getresponse = resultpost;
				System.out.println("response got");
			}
			else {
				//when object id is not existing int he database.
				r.sendResponseHeaders(404, -1);
			}
		}
		catch (Exception e){
			//when object id is string but not parsable, return 400 as BAD REQUEST
			r.sendResponseHeaders(400, -1);
		}


	}
	
	public ArrayList getTitle(String title) {
		MongoCollection collection = mongodb.getClient().getDatabase("csc301a2").getCollection("posts");
		BasicDBObject regexQuery = new BasicDBObject();
		regexQuery.put("title", new BasicDBObject("$regex", ".*" + title + "*."));
		MongoCursor<Document> cursor = collection.find(regexQuery).iterator();
		ArrayList<Document> documents = new ArrayList<Document>();
		Document nextDoc;
		while (cursor.hasNext()){
			nextDoc = cursor.next();
			System.out.println(nextDoc);
			documents.add(nextDoc);
		}
		System.out.println("loop done");
		return documents;
		
	}

}
