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
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.sps.data.Comment;
import com.google.sps.data.Constants;
import java.util.Date;
import java.util.Calendar;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/** Servlet that returns some example content. */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String text = getParameter(request, Constants.TEXT_PARAM, "");
    long time = System.currentTimeMillis();
    
    Date now = Calendar.getInstance().getTime();
    DateFormat dateFormat = new SimpleDateFormat("h:mm a E, MMM d, yyyy");
    dateFormat.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
    String date = dateFormat.format(now);

    UserService userService = UserServiceFactory.getUserService();
    String userId = userService.getCurrentUser().getUserId();

    Entity commentEntity = new Entity(Constants.COMMENT_ENTITY_PARAM);
    commentEntity.setProperty(Constants.TEXT_PARAM, text);
    commentEntity.setProperty(Constants.TIMESTAMP_PARAM, time);
    commentEntity.setProperty(Constants.DATETIME_PARAM, date);
    commentEntity.setProperty(Constants.LIKES_PARAM, 0);
    commentEntity.setProperty(Constants.USER_ID_PARAM, userId);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(commentEntity);

    response.sendRedirect("/index.html");
  }

  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Query query = new Query(Constants.COMMENT_ENTITY_PARAM).addSort(Constants.TIMESTAMP_PARAM, 
                                                            SortDirection.DESCENDING);
    Query userQuery = new Query(Constants.USER_ENTITY_PARAM);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);
    PreparedQuery userResults = datastore.prepare(userQuery);

    List<Comment> comments = new ArrayList<>();

    for (Entity entity : results.asIterable()) {
      long id = entity.getKey().getId();
      String text = (String) entity.getProperty(Constants.TEXT_PARAM);
      long time = (long) entity.getProperty(Constants.TIMESTAMP_PARAM);
      String date = (String) entity.getProperty(Constants.DATETIME_PARAM);
      long likes = (long) entity.getProperty(Constants.LIKES_PARAM);
      String userId = (String) entity.getProperty(Constants.USER_ID_PARAM);

      String name = "";
      for (Entity userEntity : userResults.asIterable()) {
        if (userId.equals((String) userEntity.getProperty(Constants.ID_PARAM))) {
          name = (String) userEntity.getProperty(Constants.NAME_PARAM);
          break;
        }
      }

      Comment comment = new Comment(id, name, text, time, date, likes);
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
  private String getParameter(HttpServletRequest request, String parameterName, String defaultValue) {
    String value = request.getParameter(parameterName);
    if (value == null) {
      return defaultValue;
    }
    return value;
  }
}
