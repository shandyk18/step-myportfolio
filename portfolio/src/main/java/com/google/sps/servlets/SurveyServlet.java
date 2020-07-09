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

/** Servlet that stores and displays votes for a color */
@WebServlet("/survey")
public class SurveyServlet extends HttpServlet {

  private DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

  @Override
  public void init() {
      String[] colorArray = new String[]{"Blue", "Red", "Orange", "Yellow", "Green", "Violet"};

      for (String color : colorArray) {
        colorEntity(color);
      }
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Query query = new Query("Survey");
    PreparedQuery results = datastore.prepare(query);
    Map<String, Long> colorVotes = new HashMap<>();

    for (Entity entity : results.asIterable()) {
      String color = (String) entity.getProperty("color");
      long count = (long) entity.getProperty("count");

      colorVotes.put(color, count);
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

    for (Entity entity : results.asIterable()) {
      if (color.equals((String) entity.getProperty("color"))) {
        entity.setProperty("count", ((long) entity.getProperty("count")) + 1);
        datastore.put(entity);
        break;
      }
    }

    response.sendRedirect("/chart.html");
  }

  // initializes an Entity object for a given color
  private void colorEntity(String color) {
    Entity colorEntity = new Entity("Survey");
    colorEntity.setProperty("color", color);
    colorEntity.setProperty("count", 0);

    datastore.put(colorEntity);
  }
}

