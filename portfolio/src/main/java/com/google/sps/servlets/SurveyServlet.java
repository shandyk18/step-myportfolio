package com.google.sps.servlets;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
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
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

/** Servlet that stores and displays votes for a color */
@WebServlet("/survey")
public class SurveyServlet extends HttpServlet {

  private DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
  private String[] colorArray = new String[] {"Red", "Orange", "Yellow", "Green", "Blue", "Violet"};

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Map<String, Integer> colorVotes = new HashMap<>();

    for (String color : colorArray) {
      // query by current color
      Filter colorFilter = new FilterPredicate("color", FilterOperator.EQUAL, color);
      Query query = new Query("Survey").setFilter(colorFilter);

      colorVotes.put(color, datastore.prepare(query).countEntities());
    }

    response.setContentType("application/json");
    Gson gson = new Gson();
    String json = gson.toJson(colorVotes);
    response.getWriter().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String color = request.getParameter("color");
    Query query = new Query("Survey");
    PreparedQuery results = datastore.prepare(query);

    // captures the vote by user
    Entity newVote = new Entity("Survey");
    newVote.setProperty("color", color);
    datastore.put(newVote);

    response.sendRedirect("/chart.html");
  }
}
