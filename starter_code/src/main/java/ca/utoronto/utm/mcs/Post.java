package ca.utoronto.utm.mcs;

import com.sun.xml.internal.fastinfoset.util.StringArray;

public class Post {

  private String id;

  private String title;

  private String author;

  private String content;

  private StringArray tags;

  public String getTitle(){ return title; }

  public void setTitle(String title) { this.title = title; }

  public String getAuthor(){ return author; }

  public void setAuthor(String author) { this.author = author; }

  public String getContent() { return content; }

  public void setContent(String content) { this.content = content; }

  public StringArray getTags() { return tags; }

  public void setTags(StringArray tags) { this.tags = tags; }

  public String getId() { return id; }

  public void setId(String id) { this.id = id; }
}