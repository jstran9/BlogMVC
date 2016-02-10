<%@ page import="tran.example.Model.Blog"%>
<!DOCTYPE html">
<html>
<head>
	<%@ include file = "styleInclude.jsp"%>
	<title>Your selected post</title>
</head>
<body>
	<div class="blog-masthead">
      <div class="container">
        <nav class="blog-nav">
          <a class="blog-nav-item active" href = "<%= response.encodeURL(request.getContextPath() + "/") %>">Home!</a>
          <a class="blog-nav-item active" href="<%= response.encodeURL(request.getContextPath() + "/GetPostsController?action=viewPosts") %>">View all posts!</a> <br/>
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
	        <%
	        	Blog thePost = (Blog) request.getAttribute("postContents");
	        	if(thePost != null) {
	        %>
		        	<div class="row">
	         			<div class="col-sm-8 blog-main">
	         				<div class="blog-post">
	           					<h2 class="blog-post-title"><%=thePost.getTitle()%></h2>
	           					<p class="blog-post-meta"> Written by <%=thePost.getAuthor()%> at <%=thePost.printDateInFormat(thePost.getDateCreated())%> </p>
	           					<%
           							if(!(thePost.getDateCreated().equals(thePost.getDateModified()))) {
           						%>
           								<p class="blog-post-meta"> Last modified by <%=thePost.getAuthor()%> at <%=thePost.printDateInFormat(thePost.getDateModified())%> </p>
           						<%
           							}
           						%>
	           					<pre><code><%=thePost.getContent()%></code></pre>
	           					<%
	      							Boolean isAuthor = (Boolean) request.getAttribute("isAuthorOfPost");
	      							if(isAuthor != null && isAuthor) {
	      						%>
	      							<h2><a class="extra-options" href = "<%= response.encodeURL(request.getContextPath() + "/DeleteSinglePostController?action=deletePost&blogID=" + thePost.getPostID())%>"> Delete This Post </a> </h2>
		    						<h2><a class="extra-options-two" href = "<%= response.encodeURL(request.getContextPath() + "/ShowEditSinglePostController?action=showEditPost&blogID=" + thePost.getPostID())%>"> Edit This Post </a> </h2>
	      						<% 
							      	}
							    %>
	           				</div>	
	         			</div>
	         		</div>
	        <%
	        	}
	        	else {
	        %>
	        		<h2 class="blog-post-title"> There is no post with this ID. </h2>
	      	<%
	      		}
	      	%>
	      </div>
	    
	</div>	
</body>
</html>