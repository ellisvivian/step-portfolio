package com.google.sps.servlets;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/login-data")
public class LoginDataServlet extends HttpServlet {
  public static final String LOGIN_STATUS_PARAM = "loginStatus";
  public static final String LOGOUT_URL_PARAM = "logoutUrl";
  public static final String USER_EMAIL_PARAM = "userEmail";
  public static final String LOGIN_URL_PARAM = "loginUrl";

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("text/html");

    UserService userService = UserServiceFactory.getUserService();
    Boolean loginStatus = userService.isUserLoggedIn();
    String json = "{";
    json += "\"" + LOGIN_STATUS_PARAM + "\": " + loginStatus + ", ";

    if (loginStatus) {
      String logoutUrl = userService.createLogoutURL("/");
      String userEmail = userService.getCurrentUser().getEmail();
      json += "\"" + LOGOUT_URL_PARAM + "\": \"" + logoutUrl + "\", ";
      json += "\"" + USER_EMAIL_PARAM + "\": \"" + userEmail + "\"}";
    } else {
      String loginUrl = userService.createLoginURL("/name-data");
      json += "\"" + LOGIN_URL_PARAM + "\": \"" + loginUrl + "\"}";
    }

    response.setContentType("application/json");
    response.getWriter().println(json);
  }
}
