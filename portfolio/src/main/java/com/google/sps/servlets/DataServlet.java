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
import com.google.gson.Gson;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;


/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

  private final ArrayList<String> comments = new ArrayList<String>();

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Get the input from the form.
    String fName = getParameter(request, "first-name", "");
    String lName = getParameter(request, "last-name", "");
    String comment = getParameter(request, "comment", "");

    Entity commentEntity = new Entity("Comment");
    commentEntity.setProperty("first-name", fName);
    commentEntity.setProperty("last-name", lName);
    commentEntity.setProperty("comment",comment);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(commentEntity);

    response.sendRedirect("/index.html");

    // // Add the input to the storage of inputs.
    // comments.add(fName);
    // comments.add(lName);
    // comments.add(comment);

    // // Direct the user back to the main page.
    // response.setContentType("text/html");
    // response.getWriter().println("Your comment has been submitted. Press the back button to return to the main page.");
  }

  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // response.setContentType("text/html;");
    // response.getWriter().println("Hello [Your Name]!");

    // Convert the messages to JSON.
    String json = convertToJsonUsingGson(comments);

    // Send the JSON as the response.
    response.setContentType("application/json");
    response.getWriter().println(json);
  }

  /**
   * Converts a ArrayList<String> instance into a JSON string using the Gson library.
   */
  private String convertToJsonUsingGson(ArrayList<String> list) {
    Gson gson = new Gson();
    String json = gson.toJson(list);
    return json;
  }

  /**
   * @return the request parameter, or the default value if the parameter
   *         was not specified by the client
   */
  private String getParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    if (value == null) {
      return defaultValue;
    }
    return value;
  }
}
