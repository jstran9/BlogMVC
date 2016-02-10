<!DOCTYPE html>
<html>
<head>
	<%@ include file = "styleInclude.jsp"%>
<title>Create A Post</title>
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
    %>

    <div class="container">

      <div class="blog-header">
        <h1 class="blog-title">Create Post</h1>
        <p class="lead blog-description">Fill in the information below to create your post!</p>
      </div>

      <div class="row">

        <div class="col-sm-8 blog-main">

          <div class="blog-post">
		  
			<form action = "<%= response.encodeURL(request.getContextPath() + "/CreateSinglePostController") %>" method="post">
			
				<input type ="hidden" name ="action" value="createPost"/>
				
				<label>Subject</label> <br/>
				<input type = "text" name = "title" maxlength = "50"/>  <br/> <br/>
				<label>Post Content:</label> <br/>
				
				<textarea rows = "8" cols = "80" name = "content"> </textarea> <br/> <br/>
				
				<button type="submit">Create Post</button>
			
			</form>
		  
          </div><!-- /.blog-post -->

        </div><!-- /.blog-main -->

      </div><!-- /.row -->

    </div><!-- /.container -->

</body>
</html>