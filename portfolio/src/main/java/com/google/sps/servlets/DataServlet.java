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

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

  int maxComments = -1;

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String fName = getParameter(request, "first-name", "");
    String lName = getParameter(request, "last-name", "");
    String text = getParameter(request, "comment-text", "");
    long time = System.currentTimeMillis();

    String max = getParameter(request, "max-comments", "");
    if (!(max.equals(""))) {
      maxComments = Integer.parseInt(max);
    }

    if (!(fName.equals("") && lName.equals("") && text.equals(""))) {
      Entity commentEntity = new Entity("Comment");
      commentEntity.setProperty("first-name", fName);
      commentEntity.setProperty("last-name", lName);
      commentEntity.setProperty("comment-text", text);
      commentEntity.setProperty("time-stamp", time);

      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      datastore.put(commentEntity);
    }

    response.sendRedirect("/index.html");
  }

  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Query query = new Query("Comment").addSort("time-stamp", SortDirection.DESCENDING);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);

    List<Comment> comments = new ArrayList<>();
    int count = 0;
    for (Entity entity: results.asIterable()) {
      if (count == maxComments) {
        break;
      }

      String fName = (String) entity.getProperty("first-name");
      String lName = (String) entity.getProperty("last-name");
      String text = (String) entity.getProperty("comment-text");
      long time = (long) entity.getProperty("time-stamp");

      Comment comment = new Comment(fName, lName, text, time);
      comments.add(comment);

      count ++;
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
