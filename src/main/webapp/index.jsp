<!DOCTYPE html>
<html>
<head>
	<title>Table Of Contents Page</title>
	<link rel="stylesheet" href = "<%= response.encodeURL(request.getContextPath() + "/css/home.css") %>">
</head>
<body>
<ol>
	<li><a href="<%= response.encodeURL(request.getContextPath() + "/GetPostsController?action=viewPosts") %>">View all posts!</a></li>
	<li><a href="<%= response.encodeURL(request.getContextPath() + "/LoginController?action=doLogin") %>">Click to Login!</a></li>
	<li><a href="<%= response.encodeURL(request.getContextPath() + "/RegisterController?action=showRegistration") %>">Sign up!</a></li>
	<li><a href="<%= response.encodeURL(request.getContextPath() + "/DisplayInsertPostController?action=createSinglePost") %>">Create A Post.</a> </li>
	<li><a href="<%= response.encodeURL(request.getContextPath() + "/DisplayChangeUserPasswordController?action=showChangePassword") %>">Change your password.</a> </li>
</ol> 
<br/>

</body>
</html>
