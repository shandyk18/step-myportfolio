<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<% BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
   String uploadUrl = blobstoreService.createUploadUrl("/comments"); %>

<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Shandy Kim's Portfolio</title>
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Maven+Pro:wght@500&display=swap">
    <link rel="stylesheet" href="style.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.min.js"></script>
  </head>
  <body onload="onLoad()">
    <div id="content" class="container-fluid">
      <div class="container-fluid p-3 my-3 bg-dark text-white">
        <nav class="navbar navbar-expand-sm bg-dark justify-content-flex-start">
          <h1 class="navbar-brand">Shandy Kim's Portfolio</h1>
          <ul class="navbar-nav">
            <li class="nav-item">
              <a class="nav-link" href="/index.jsp">Home</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="/projects.html">Projects</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="/pet.html">Pet: Luci</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="/pnw.html">PNW</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="/chart.html">Polls</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="https://www.linkedin.com/in/shandy-kim-63242b168" target="_blank">LinkedIn</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="https://www.github.com/shandyk18" target="_blank">GitHub</a>
            </li>
          </ul>
        </nav>
      </div>
      <div class="row">
        <div class="col-2">
        </div>
        <div class="col-8">
          <div id="overview" class="container">
            <div class="text">
              <h3>Overview</h3>
            </div>
            <br>
            <div class="text">
              <p>My name is Shandy Kim, and I'm a rising junior at the University of Washington (Seattle) studying computer science. I grew up in Tacoma, WA but currently live in Snoqualmie, WA.
                My talents include sleeping late for no reason (and regretting it the next morning) and playing the ukulele. Some of my hobbies include drawing, reading, listening to podcasts, 
                and walking my dog (especially during quarantine).
              </p>
              <p>I got interested in computer science through game development. In high school, I thought that game development was a way for me to combine both programming and 
                art. I am no longer trying to pursue a career in game development, but I am exploring other areas in computer science, such as systems. I hope to learn a lot about web dev
                from STEP!
              </p>
              <p>Here are fun facts about me:</p>
              <div class="container-fluid">
                <div class="container" id="fact-container">
                </div>
                <br>
                <button class="button" onclick="showFact()">next</button>
              </div>
            </div>
            <br>
            <figure class="figure">
              <img src="images/me.jpg" class="figure-img img-fluid rounded" alt="Picture of me">
              <figcaption class="figure-caption text">During my study abroad trip to Japan last summer, I visited the Itsukushima Shrine on Miyajima.</figcaption>
            </figure>
            <br><br>
          </div>
          <div class="container">
            <div class="text">
              <form id="comment-form" method="POST" enctype="multipart/form-data" action="<%= uploadUrl %>">
                <label for="name">Name: </label>
                <textarea name="name-input" id="text-name" rows="1"></textarea>
                <textarea name="comment-input" id="text-comment" rows="4"></textarea>
                <br>
                <input type="file" name="image">
                <br><br>
                <input type="submit" class="button"/>
              </form>
              <p id="comment-login-form"><a id="comment-login-link" href="">Login</a> to write a comment</p>
            </div>
            <br>
            <div class="text">
              <label for="max-comments">Number of Comments: </label>
              <select onchange="refreshComments(this.value)" name="max-comments" id="max-comments">
                <option value=""></option>
                <option value="1">1</option>
                <option value="5">5</option>
                <option value="10">10</option>
                <option value="20">20</option>
              </select>   
            </div>
            <br>
            <div id="history"></div>
            <br>
            <button type="button" class="button" onclick="deleteComments()">Delete</button>
          </div>
        </div>
      </div>
      <footer class="page-footer font-xsmall">
        <div class="container-fluid text-center text-md-left p-3 my-3 bg-dark text-white">
          <div class="row">
            <hr class="clearfix w-100 d-md-none pb-3">
            <div class="col-md-3 mb-md-0 mb-3">
              <h5 class="text-uppercase">Contact</h5>
              <a class="nav-link" href="mailto:shandyk@google.com">shandyk@google.com</a>  
            </div>
          </div>
        </div>
      </footer>
    </div>
  </body>
  <script src="script.js"></script>
</html>