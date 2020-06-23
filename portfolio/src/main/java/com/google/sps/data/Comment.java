package com.google.sps.data;

import java.util.Date;
import java.util.List;

/** A user comment on the page. */
public final class Comment {

  private final long id;
  private final String name;
  private final String commentText;
  private final long timeStamp;
  private final String dateTime;
  private final List<String> likes;
  private final String userId; 

  public Comment(long id, String name, String commentText, 
      long timeStamp, String dateTime, List<String> likes, String userId) {
    this.id = id;
    this.name = name;
    this.commentText = commentText;
    this.timeStamp = timeStamp;
    this.dateTime = dateTime;
    this.likes = likes;
    this.userId = userId;
  }
}
