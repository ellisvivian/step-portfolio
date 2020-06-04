// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.google.gson.Gson;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.sps.data.Comment;
import java.util.Date;
import java.util.Calendar;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {
  private static final String FIRST_NAME_PARAM = "first-name";
  private static final String LAST_NAME_PARAM = "last-name";
  private static final String TEXT_PARAM = "comment-text";
  private static final String TIMESTAMP_PARAM = "time-stamp";
  private static final String DATETIME_PARAM = "date-time";
  private static final String ENTITY_PARAM = "Comment";

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String fName = getParameter(request, FIRST_NAME_PARAM, "");
    String lName = getParameter(request, LAST_NAME_PARAM, "");
    String text = getParameter(request, TEXT_PARAM, "");
    long time = System.currentTimeMillis();
    
    Date now = Calendar.getInstance().getTime();
    DateFormat dateFormat = new SimpleDateFormat("h:mm a E, MMM d, yyyy");
    dateFormat.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
    String date = dateFormat.format(now);

    Entity commentEntity = new Entity(ENTITY_PARAM);
    commentEntity.setProperty(FIRST_NAME_PARAM, fName);
    commentEntity.setProperty(LAST_NAME_PARAM, lName);
    commentEntity.setProperty(TEXT_PARAM, text);
    commentEntity.setProperty(TIMESTAMP_PARAM, time);
    commentEntity.setProperty(DATETIME_PARAM, date);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(commentEntity);

    response.sendRedirect("/index.html");
  }

  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Query query = new Query(ENTITY_PARAM).addSort(TIMESTAMP_PARAM, SortDirection.DESCENDING);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);

    List<Comment> comments = new ArrayList<>();

    for (Entity entity: results.asIterable()) {
      String fName = (String) entity.getProperty(FIRST_NAME_PARAM);
      String lName = (String) entity.getProperty(LAST_NAME_PARAM);
      String text = (String) entity.getProperty(TEXT_PARAM);
      long time = (long) entity.getProperty(TIMESTAMP_PARAM);
      String date = (String) entity.getProperty(DATETIME_PARAM);

      Comment comment = new Comment(fName, lName, text, time, date);
      comments.add(comment);
    }

    Gson gson = new Gson();

    response.setContentType("application/json");
    response.getWriter().println(gson.toJson(comments));
  }

  /**
   * @return the request parameter, or the default value if the parameter
   *         was not specified by the client
   */
  private String getParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    if (value == null) {
      return defaultValue;
    }
    return value;
  }
}
