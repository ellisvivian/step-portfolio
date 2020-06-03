package com.google.sps.data;

/*
 * A user comment on the page.
 */
public final class Comment {

  private final String firstName;
  private final String lastName;
  private final String commentText;
  private final long timeStamp;

  public Comment(String firstName, String lastName, String commentText, long timeStamp) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.commentText = commentText;
    this.timeStamp = timeStamp;
  }
}