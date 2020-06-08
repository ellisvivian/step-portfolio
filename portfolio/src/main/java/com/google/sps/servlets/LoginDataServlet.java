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

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("text/html");

    UserService userService = UserServiceFactory.getUserService();
    // if (userService.isUserLoggedIn()) {
    //   String userEmail = userService.getCurrentUser().getEmail();
    //   String logoutUrl = userService.createLogoutURL("/");

    //   response.getWriter().println("<p>Hi " + userEmail + ", it's great to have you here!</p>");
    //   response.getWriter().println("<p>Logout <a href=\"" + logoutUrl + "\">here</a>.</p>");
    // } else {
    //   String loginUrl = userService.createLoginURL("/");

    //   response.getWriter().println("<p>I don't seem to know you yet. Let's change that.</p>");
    //   response.getWriter().println("<p>Login <a href=\"" + loginUrl + "\">here</a>.</p>");
    // }
    Boolean status = userService.isUserLoggedIn();
    response.getWriter().println(status);
  }
}
