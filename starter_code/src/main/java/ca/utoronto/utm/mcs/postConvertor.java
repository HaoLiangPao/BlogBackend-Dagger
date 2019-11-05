package ca.utoronto.utm.mcs;

import ca.utoronto.utm.mcs.Post;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import com.sun.xml.internal.fastinfoset.util.StringArray;
import org.bson.types.ObjectId;

public class postConvertor {
  // convert Person Object to MongoDB DBObject
  // take special note of converting id String to ObjectId

  public static DBObject toDBObject(Post p){

    BasicDBObjectBuilder builder = BasicDBObjectBuilder.start()
        .append("title", p.getTitle()).append("author", p.getAuthor())
        .append("content", p.getContent()).append("tages", p.getTags());
    if (p.getId() != null)
      builder = builder.append("_id", new ObjectId(p.getId()));
    return builder.get();
  }

  // convert DBObject Object to Person
  // take special note of converting ObjectId to String
  public static Post toPost(DBObject doc) {
    Post p = new Post();
    p.setTitle((String) doc.get("title"));
    p.setAuthor((String) doc.get("author"));
    p.setContent((String) doc.get("content"));
    p.setTags((String[]) doc.get("tags"));
    ObjectId id = (ObjectId) doc.get("_id");
    p.setId(id.toString());
    return p;
  }
}
