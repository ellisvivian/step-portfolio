package com.google.sps.data;
import java.util.Date;

/** A user comment on the page. */
public final class Comment {

  private final String name;
  private final String commentText;
  private final long timeStamp;
  private final String dateTime;
  private long likes; 

  public Comment(String name, String commentText, 
                 long timeStamp, String dateTime, long likes) {
    this.name = name;
    this.commentText = commentText;
    this.timeStamp = timeStamp;
    this.dateTime = dateTime;
    this.likes = likes;
  }
}
