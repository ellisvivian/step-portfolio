package com.google.sps.servlets;

import java.io.IOException;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

/** Servlet responsible for liking comments. */
@WebServlet("/like-data")
public class LikeDataServlet extends HttpServlet {
  
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    long commentId = Long.parseLong(request.getParameter(Constants.ID_PARAM));
    
    UserService userService = UserServiceFactory.getUserService();

    if (!userService.isUserLoggedIn()) {
      return;
    }   

    String userId = userService.getCurrentUser().getUserId();
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Query query = new Query(Constants.COMMENT_ENTITY_PARAM);
    PreparedQuery results = datastore.prepare(query);

    for (Entity entity : results.asIterable()) {
      if (commentId == entity.getKey().getId()) {
        List<String> likes = (List<String>) entity.getProperty(Constants.LIKES_PARAM);
        // unlike the comment if it has already been liked by the user
        if (likes.contains(userId)) {
          likes.remove(userId);
        // like the comment if it has not yet been liked by the user
        } else {
          likes.add(userId);
        }
        entity.setProperty(Constants.LIKES_PARAM, likes);
        datastore.put(entity);
        break;
      }
    }
  }  
}
