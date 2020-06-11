package com.google.sps.servlets;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.datastore.*;
import com.google.sps.data.Constants;
import java.io.IOException;
import java.lang.StringBuilder;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/login-data")
public class LoginDataServlet extends HttpServlet {


  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("text/html");

    UserService userService = UserServiceFactory.getUserService();
    Boolean loginStatus = userService.isUserLoggedIn();
    StringBuilder jsonBuilder = new StringBuilder("{");
    jsonBuilder.append("\"");
    jsonBuilder.append(Constants.LOGIN_STATUS_PARAM);
    jsonBuilder.append("\": \"");
    jsonBuilder.append(loginStatus);
    jsonBuilder.append("\", ");

    if (loginStatus) {
      String logoutUrl = userService.createLogoutURL("/");
      String userEmail = userService.getCurrentUser().getEmail();
      String userId = userService.getCurrentUser().getUserId();
      String userName = getUserName(userId);
      buildJson(jsonBuilder, Constants.LOGOUT_URL_PARAM, logoutUrl);
      buildJson(jsonBuilder, Constants.USER_EMAIL_PARAM, userEmail);
      buildJson(jsonBuilder, Constants.USER_ID_PARAM, userId);
      buildJson(jsonBuilder, Constants.USER_NAME_PARAM, userName);
    } else {
      String loginUrl = userService.createLoginURL("/");
      buildJson(jsonBuilder, Constants.LOGIN_URL_PARAM, loginUrl);
    }

    jsonBuilder.delete(jsonBuilder.length() - 2, jsonBuilder.length());
    jsonBuilder.append("}");

    System.out.println("pre-string JSON: " + jsonBuilder);

    String json = jsonBuilder.toString();

    System.out.println("post-string JSON: " + json);

    response.setContentType("application/json");
    response.getWriter().println(json);
  }

  /** Returns the nickname of the user or the empty string if they do not yet have one. */
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

  /** Adds a String parameter and String value in the building of the specified JSON StringBuilder. */
  private void buildJson(StringBuilder builder, String paramName, String paramValue) {
    builder.append("\"");
    builder.append(paramName);
    builder.append("\": \"");
    builder.append(paramValue);
    builder.append("\", ");
  } 
}
