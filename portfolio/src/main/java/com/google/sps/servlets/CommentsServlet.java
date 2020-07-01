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
import java.util.List;
import java.util.ArrayList;
import com.google.gson.Gson;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;

/** Servlet that returns comments on index.html.*/
@WebServlet("/comments")
public class CommentsServlet extends HttpServlet {

  private DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Query query = new Query("Task").addSort("timestamp", SortDirection.DESCENDING);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);

    String maxString = request.getParameter("max-comments");
    // on default, show 5 comments
    int maxComments = maxString == null ? 5 : Integer.parseInt(maxString);
    int counter = 0;

    List<String> commentHistory = new ArrayList<>();
    for (Entity entity : results.asIterable()) {
      if (counter == maxComments) {
          break;
      }

      String comment = (String) entity.getProperty("comment");
      commentHistory.add(comment);
      counter++;
    }

    response.setContentType("application/json");
    String json = new Gson().toJson(commentHistory);
    response.getWriter().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Get the input from the form.
    String newComment = request.getParameter("comment-input");
    long timestamp = System.currentTimeMillis();

    Entity taskEntity = new Entity("Task");
    taskEntity.setProperty("timestamp", timestamp);
    taskEntity.setProperty("comment", newComment);

    datastore.put(taskEntity);

    // Redirect back to the HTML page.
    response.sendRedirect("/index.html");
  }
}
