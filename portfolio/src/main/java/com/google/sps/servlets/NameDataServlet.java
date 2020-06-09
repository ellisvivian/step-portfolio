package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.sps.data.Constants;

@WebServlet("/name-data")
public class NameDataServlet extends HttpServlet {

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {    
    UserService userService = UserServiceFactory.getUserService();
    
    String name = request.getParameter(Constants.NAME_PARAM);
    String id = userService.getCurrentUser().getUserId();

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Entity userEntity = new Entity(Constants.USER_ENTITY_PARAM);
    userEntity.setProperty(Constants.ID_PARAM, id);
    userEntity.setProperty(Constants.NAME_PARAM, name);
    datastore.put(userEntity);

    response.sendRedirect("/index.html");
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();

    out.println("<p>Enter your name here: </p>");
    out.println("<form method=\"POST\" action=\"/name-data\">");
    out.println("<input name=\"name\"/>");
    out.println("<br/>");
    out.println("<button>Submit</button>");
    out.println("</form>");
  }
}
