<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.List"%>
<%@ page import="tran.example.Model.Blog"%>
<!DOCTYPE html>
<html>
<head>
	<title>Posts Written</title>
	<%@ include file = "styleInclude.jsp"%>
</head>
<body>

    <div class="blog-masthead">
      <div class="container">
        <nav class="blog-nav">
        	<a class="blog-nav-item active" href = "<%= response.encodeURL(request.getContextPath() + "/") %>">Home!</a>
        </nav>
      </div>
    </div>

    <%
    	String user = (String) session.getAttribute("userName");
    	if(user != null && !(user.equals(""))) {
    		String logoutUrl = "/LogoutController?action=doLogout";
    %>
    	<div class = "userName">
    		Hello, <%=user%> (<a href = "<%= response.encodeURL(request.getContextPath() + logoutUrl)%>">Logout</a>)
    	</div>
    <% 
    	}
    	else {
    		String loginURL = "/LoginController?action=doLogin";
    		String registerURL = "/RegisterController?action=showRegistration";
    %>
 		<div class = "userName">
    		<a href = "<%=response.encodeURL(request.getContextPath() + loginURL)%>">Login</a>
    		|
    		<a href = "<%=response.encodeURL(request.getContextPath() + registerURL)%>">Register</a>
    	</div>
 	<% 
    	}
 	%>

	<div class = "container">

		 <div class="blog-header">
	        <h1 class="blog-title">Todd's Java MVC Blog.</h1>
	        <p class="lead blog-description">This uses bootstrap styling and Java as the server-side programming language.</p>
         	<%
		   		String error = (String) request.getAttribute("infoMessage");
		   		if(error != null && !error.equals("")) {
   	 		%>
   	 			<br/>
    			<div class = "form-control" id = "infoField"><%=error%></div>
	      	<%
	      		}
	      	%>
	      </div>
		
	     <div class="row">

         	<div class="col-sm-8 blog-main">
				<%
					@SuppressWarnings("unchecked")
					List<Blog> blogs = (ArrayList<Blog>) request.getAttribute("dbResults");
					if(blogs != null) {
						Blog blogObject = new Blog();
						String urlHelper = "/GetSinglePostController?action=showSinglePost&blogID=";
						for(int i = 0; i < blogs.size(); i++) {
							blogObject = blogs.get(i);
				%>
						<div class="blog-post">
           					<h2 class="blog-post-title"> 
           						<a href = "<%= response.encodeURL(request.getContextPath() + urlHelper + blogObject.getPostID())%>"> <%=blogObject.getTitle() %> </a>
           					</h2>
           					<p class="blog-post-meta"> Written by <%=blogObject.getAuthor()%> at <%=blogObject.printDateInFormat(blogObject.getDateCreated())%> </p>
           					<%
           						if(!(blogObject.getDateCreated().equals(blogObject.getDateModified()))) {
           					%>
           							<p class="blog-post-meta"> Last modified by <%=blogObject.getAuthor()%> at <%=blogObject.printDateInFormat(blogObject.getDateModified())%> </p>
           					<%
           						}
           					%>
           					<pre><code><%=blogObject.getContent()%></code></pre>
           				</div>	
           				<hr>
				<%
						}
					}
					else {
				%>
						<pre><code>Blog has no posts :(.</code></pre>
				<%
					}
				%>

			</div> <!-- end blog-main -->
		</div> <!-- end row class -->	
	</div> <!-- end container -->

</body>
</html>