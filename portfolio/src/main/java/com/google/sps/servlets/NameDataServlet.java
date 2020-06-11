package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.sps.data.Constants;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/name-data")
public class NameDataServlet extends HttpServlet {

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {    
    UserService userService = UserServiceFactory.getUserService();
    
    String name = request.getParameter(Constants.NAME_PARAM);
    String userId = userService.getCurrentUser().getUserId();

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    Query query = new Query(Constants.USER_ENTITY_PARAM);
    PreparedQuery results = datastore.prepare(query);

    for (Entity entity : results.asIterable()) {
      if (userId.equals((String) entity.getProperty(Constants.ID_PARAM))) {
        long entityId = entity.getKey().getId();
        Key key = KeyFactory.createKey(Constants.USER_ENTITY_PARAM, entityId);
        datastore.delete(key);
      }
    }

    Entity userEntity = new Entity(Constants.USER_ENTITY_PARAM);
    userEntity.setProperty(Constants.ID_PARAM, userId);
    userEntity.setProperty(Constants.NAME_PARAM, name);
    datastore.put(userEntity);

    response.sendRedirect("/index.html");
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();
    String name = getUserName(userService.getCurrentUser().getUserId());

    response.setContentType("text/html");
    response.getWriter().println(name);
  }

  /** Returns the nickname of the user or an empty string if they do not yet have one. */
  private String getUserName(String id) {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Query query = new Query(Constants.USER_ENTITY_PARAM);
    PreparedQuery results = datastore.prepare(query);
    String name = "";
    for (Entity entity : results.asIterable()) {
      if (id.equals((String) entity.getProperty(Constants.ID_PARAM))) {
        name = (String) entity.getProperty(Constants.NAME_PARAM);
      }
    }
    return name;
  }
}
