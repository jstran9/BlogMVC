<!DOCTYPE html>

<html>

<head> 
	<title>Registration Page</title>
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

 	<div class="container">
	  <form class="form-signin" action = "<%= response.encodeURL(request.getContextPath() + "/RegisterController") %>" method="post">
        <input type ="hidden" name ="action" value="checkRegistration"/>
		<h2 class="form-signin-heading">Register below!</h2>
		<%
			if(request.getAttribute("userName") != null && !((String) request.getAttribute("userName")).equals("") ) {
				String enteredName = (String) request.getAttribute("userName");
		%>

			<input type="text" name="userName" class="form-control" placeholder=<%=enteredName%> required>
		<%
			}
			else {
		%>
			<input type="text" name="userName" class="form-control" placeholder="Enter user name" required>
		<%
			}
		%>
		
       <input type="password" name="password" class="form-control" placeholder="password" required>
       <input type="password" name="validatePassword" class="form-control" placeholder="enter password again" required>

       <button class="btn btn-lg btn-primary btn-block" type="submit">Register user!</button>
   	 	<%
	   		String error = (String) request.getAttribute("validationmessage");
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