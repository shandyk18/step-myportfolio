package com.google.sps.servlets;

import com.google.sps.data.LoginStatus;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/login")
public class AuthServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("text/html");

    UserService userService = UserServiceFactory.getUserService();
    if (userService.isUserLoggedIn()) {
      String urlToRedirectToAfterUserLogsOut = "/index.jsp";
      String logoutUrl = userService.createLogoutURL(urlToRedirectToAfterUserLogsOut);

      setStatus(response, userService.isUserLoggedIn(), logoutUrl);
    } else {
      String urlToRedirectToAfterUserLogsIn = "/index.jsp";
      String loginUrl = userService.createLoginURL(urlToRedirectToAfterUserLogsIn);

      setStatus(response, userService.isUserLoggedIn(), loginUrl);
    }
  }

  private void setStatus(HttpServletResponse response, Boolean status, String url) {
      LoginStatus ls = new LoginStatus(status, url);
      String json = new Gson().toJson(ls);
      try {
        response.getWriter().println(json);
      } catch (Exception e) {
        System.out.println(e);
      }
  }
}
