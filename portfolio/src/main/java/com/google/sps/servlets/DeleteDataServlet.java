package com.google.sps.servlets;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.sps.data.Constants;

/** Servlet responsible for deleting comments. */
@WebServlet("/delete-data")
public class DeleteDataServlet extends HttpServlet {
  
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    if (request.getParameter(Constants.ID_PARAM) != null) {
      // delete specified comment
      long id = Long.parseLong(request.getParameter(Constants.ID_PARAM));
      Key commentKey = KeyFactory.createKey(Constants.COMMENT_ENTITY_PARAM, id);
      datastore.delete(commentKey);
      
    } else {
      // delete all comments
      Query query = new Query(Constants.COMMENT_ENTITY_PARAM);
      PreparedQuery results = datastore.prepare(query);

      for (Entity entity : results.asIterable()) {
        long id = entity.getKey().getId();
        Key commentKey = KeyFactory.createKey(Constants.COMMENT_ENTITY_PARAM, id);
        datastore.delete(commentKey);
      }
    }
  }
}
