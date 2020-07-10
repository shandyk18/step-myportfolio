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

/** Servlet that stores and displays votes for a pet */
@WebServlet("/pet-survey")
public class PetSurveyServlet extends HttpServlet {

  private DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
  private String[] petArray = new String[] {"Dog", "Cat", "Bird", "Fish", "Other", "None"};

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Map<String, Integer> petVotes = new HashMap<>();

    for (String pet : petArray) {
      // query by current pet
      Filter petFilter = new FilterPredicate("pet", FilterOperator.EQUAL, pet);
      Query query = new Query("PetSurvey").setFilter(petFilter);

      petVotes.put(pet, datastore.prepare(query).countEntities());
    }

    response.setContentType("application/json");
    Gson gson = new Gson();
    String json = gson.toJson(petVotes);
    response.getWriter().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String pet = request.getParameter("pet");
    Query query = new Query("PetSurvey");
    PreparedQuery results = datastore.prepare(query);

    // captures the vote by user
    Entity newVote = new Entity("PetSurvey");
    newVote.setProperty("pet", pet);
    datastore.put(newVote);

    response.sendRedirect("/chart.html");
  }
}
