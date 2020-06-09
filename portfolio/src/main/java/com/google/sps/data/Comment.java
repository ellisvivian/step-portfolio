package com.google.sps.data;
import java.util.Date;

/** A user comment on the page. */
public final class Comment {

  private final String firstName;
  private final String lastName;
  private final String commentText;
  private final long timeStamp;
  private final String dateTime;
  private final long id;
  private long likes; 

  public Comment(String firstName, String lastName, String commentText, 
                 long timeStamp, String dateTime, long id, long likes) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.commentText = commentText;
    this.timeStamp = timeStamp;
    this.dateTime = dateTime;
    this.id = id;
    this.likes = likes;
  }
}
