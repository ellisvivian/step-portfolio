package com.google.sps.servlets;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.datastore.*;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.sps.data.Constants;

@WebServlet("/login-data")
public class LoginDataServlet extends HttpServlet {


  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("text/html");

    UserService userService = UserServiceFactory.getUserService();
    Boolean loginStatus = userService.isUserLoggedIn();
    String json = "{";
    json += "\"" + Constants.LOGIN_STATUS_PARAM + "\": " + loginStatus + ", ";

    if (loginStatus) {
      String logoutUrl = userService.createLogoutURL("/");
      String userEmail = userService.getCurrentUser().getEmail();
      String userId = userService.getCurrentUser().getUserId();
      String userName = getUserName(userId);
      json += "\"" + Constants.LOGOUT_URL_PARAM + "\": \"" + logoutUrl + "\", ";
      json += "\"" + Constants.USER_EMAIL_PARAM + "\": \"" + userEmail + "\", ";
      json += "\"" + Constants.USER_ID_PARAM + "\": \"" + userId + "\", ";
      json += "\"" + Constants.USER_NAME_PARAM + "\": \"" + userName + "\"}";
    } else {
      String loginUrl = userService.createLoginURL("/");
      json += "\"" + Constants.LOGIN_URL_PARAM + "\": \"" + loginUrl + "\"}";
    }

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
}
