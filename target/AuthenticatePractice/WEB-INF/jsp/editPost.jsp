<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.List"%>
<%@ page import="tran.example.Model.Blog"%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file = "styleInclude.jsp"%>
<title>Edit A Post</title>
</head>
<body>

	<div class="blog-masthead">
      <div class="container">
        <nav class="blog-nav">
          <a class="blog-nav-item active" href = "<%= response.encodeURL(request.getContextPath() + "/") %>">Home!</a>
          <a class="blog-nav-item active" href="<%= response.encodeURL(request.getContextPath() + "/GetPostsController?action=viewPosts") %>">View all posts!</a>
        	<%
				Blog blogToEditObject = (Blog) request.getAttribute("blogToEdit");
        		if(blogToEditObject != null) {
			%>
					<a class="blog-nav-item active" href="<%= response.encodeURL(request.getContextPath() + "/GetSinglePostController?action=showSinglePost&blogID=" + blogToEditObject.getPostID())%>">Go back to this post!</a>
			<%
        		}
			%>
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
        <h1 class="blog-title">Edit Post</h1>
        <p class="lead blog-description">Fill in the information below to edit your post!</p>
      </div>

      <div class="row">

        <div class="col-sm-8 blog-main">

          <div class="blog-post">
          
			<%
				Blog blogToEdit = (Blog) request.getAttribute("blogToEdit");
			%>
		  
			<form action = "<%= response.encodeURL(request.getContextPath() + "/EditSinglePostController") %>" method="post">
			
				<input type ="hidden" name ="action" value="doEditPost"/>			
				<%
					if (blogToEdit != null) {
				%>
					<input type ="hidden" name ="blogID" value="<%=blogToEdit.getPostID()%>"/>
					<h2 class="blog-post-title"> <%=blogToEdit.getTitle()%></h2> <br/>
					<label>Content:</label><br/>
					<textarea rows = "8" cols = "80" name = "content"><%=blogToEdit.getContent()%></textarea> <br/> <br/>
				<%
					}
					else {
						// error so just go back to the home page?
						response.sendRedirect(response.encodeURL(request.getContextPath() + "/"));
					}
				%>			
				<button type="submit">Edit Post</button>
			
			</form>
		  
          </div><!-- /.blog-post -->

        </div><!-- /.blog-main -->

      </div><!-- /.row -->

    </div><!-- /.container -->

</body>
</html>