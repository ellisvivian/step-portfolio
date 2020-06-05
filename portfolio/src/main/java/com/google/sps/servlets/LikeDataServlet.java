package com.google.sps.servlets;

import java.io.IOException;
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
import com.google.sps.data.Constants;

/** Servlet responsible for liking comments. */
@WebServlet("/like-data")
public class LikeDataServlet extends HttpServlet {
  
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    long likes = Long.parseLong(request.getParameter(Constants.LIKES_PARAM));
    long id = Long.parseLong(request.getParameter(Constants.ID_PARAM));
  
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Query query = new Query(Constants.ENTITY_PARAM);
    PreparedQuery results = datastore.prepare(query);

    for (Entity entity : results.asIterable()) {
      if (id == entity.getKey().getId()) {
        entity.setProperty(Constants.LIKES_PARAM, likes);
        datastore.put(entity);
        break;
      }
    }
  }  
}
