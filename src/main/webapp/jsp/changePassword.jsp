<!DOCTYPE html>
<html>
<head> 
	<title>Change password</title>
	<%@ include file = "styleInclude.jsp"%>
</head>

<body class = "body-background">

	<div class="blog-masthead">
		<div class="container">
	    	<nav class="blog-nav">
	    		<a class="blog-nav-item active" href = "<%= response.encodeURL(request.getContextPath() + "/") %>">Home!</a>
	        </nav>
	    </div>
	</div>
	
	<%
    	String username = (String) session.getAttribute("userName");
    	if(username != null && !(username.equals(""))) {
    		String logoutUrl = "/LogoutController?action=doLogout";
    %>
    	<div class = "userName">
    		Hello, <%=username%> (<a href = "<%= response.encodeURL(request.getContextPath() + logoutUrl)%>">Logout</a>)
    	</div>
    <% 
    	}  	
    %>

 	<div class="container">
	  <form class="form-signin" action = "<%= response.encodeURL(request.getContextPath() + "/ChangeUserPasswordController") %>" method="post">
        <input type ="hidden" name ="action" value="doChangePassword"/>
        <%
        	String user = (String) session.getAttribute("userName");
        	if(user != null) {
        %>
       		<h2 class="form-signin-heading">Hello, <%=user%></h2>
        <% 
        	}
        %>
		<h2 class="form-signin-heading">Change Your Password Below</h2>
        <input type="password" name="password" class="form-control" placeholder="your password" required>
        <input type="password" name="passwordVerify" class="form-control" placeholder="password again" required>
        <button class="btn btn-lg btn-primary btn-block" type="submit">Change Password!</button>
   	 	<%
	   		String error = (String) request.getAttribute("validationMessage");
	   		if(error != null && !error.equals("")) {
   	 	%>
   	 			<br/>
    			<div class = "form-control" id = "infoField"><%=error%></div>
      	<%
      		}
      	%>
      </form>
	  
    </div> <!-- /container -->
	

</body>



</html>