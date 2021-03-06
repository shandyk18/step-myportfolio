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

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

  private List<String> messages;

  @Override
  public void init() {
    messages = new ArrayList<>();
    messages.add("First of all... how are you?");
    messages.add("The worst thing about prison was the dementors.");
    messages.add("If I dont have some cake soon, I might die.");
  }  

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String json = convertToJsonUsingGson(messages);

    response.setContentType("text/html;");
    response.getWriter().println(json);
  }

  /**
   * Converts a List instance into a JSON string using the Gson library.
  */
  private String convertToJsonUsingGson(List list) {
    Gson gson = new Gson();
    return gson.toJson(list);
  }
}

