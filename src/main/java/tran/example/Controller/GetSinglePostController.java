package tran.example.Controller;

import java.util.List;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import tran.example.DAO.BlogDAO;
import tran.example.Model.Blog;

/**
 * Servlet implementation class GetSinglePost
 */
public class GetSinglePostController extends HttpServlet {
	private static final long serialVersionUID = 7L;
	
	private DataSource ds;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetSinglePostController() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public void init(ServletConfig config) throws ServletException {
    	// TODO Auto-generated method stub
    	try {
			InitialContext cxt = new InitialContext();
			ds = (DataSource) cxt.lookup("java:/comp/env/jdbc/MySQLDS");
		}
		catch(NamingException e) {
			e.printStackTrace();
		}
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// when a user clicks on the title of a post and provides an ID.
		String action = request.getParameter("action");
		
		if(action.equals("showSinglePost")) {
			if(request.getParameter("blogID") != null) {
				int postID = Integer.parseInt(request.getParameter("blogID"));
				
				Connection dbConn = null;
				try {
					dbConn = ds.getConnection();
					BlogDAO queryForPost = new BlogDAO(dbConn);
					Blog getSinglePost = queryForPost.getaBlog(postID);
					HttpSession currentSession = request.getSession(false);
					boolean isAuthorOfPost = false;
					
					if(currentSession != null) {
						String authorOfPost = (String) currentSession.getAttribute("userName");
						isAuthorOfPost = queryForPost.isAuthorOfPost(authorOfPost, postID);
					}
					
					request.setAttribute("postContents", getSinglePost);
					request.setAttribute("isAuthorOfPost", isAuthorOfPost);
					request.getRequestDispatcher("/jsp/singlepost.jsp").forward(request, response);
				}
				catch(SQLException e) {
					request.setAttribute("validationmessage", "An error occured, try again.");
					request.getRequestDispatcher("/").forward(request, response);
				}
				finally {
					if(dbConn != null) {
						try {
							dbConn.close();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
			else {
				request.setAttribute("validationmessage", "You are going to a page that does not exist, try again.");
				request.getRequestDispatcher("/GetPostsController?action=viewPosts").forward(request, response);
			}
		}
		else {
			request.setAttribute("infoMessage", "Page doesn't exist!");
			request.getRequestDispatcher("/GetPostsController?action=viewPosts").forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// comes from when a user has created a new post is redirected to this freshly created post.
		String action = request.getParameter("action");
		
		if(action.equals("showSinglePost")) {
			Blog newBlog = (Blog) request.getAttribute("editedContents");
			if(newBlog != null) {
				request.setAttribute("postContents", newBlog);
				
				Connection dbConn = null;
				try {
					dbConn = ds.getConnection();
					BlogDAO queryForPost = new BlogDAO(dbConn);
					
					HttpSession currentSession = request.getSession(false);
					boolean isAuthorOfPost = false;
					
					if(currentSession != null) {
						String authorOfPost = (String) currentSession.getAttribute("userName");
						isAuthorOfPost = queryForPost.isAuthorOfPost(authorOfPost, newBlog.getPostID());
					}
					
					request.setAttribute("isAuthorOfPost", isAuthorOfPost);
					request.getRequestDispatcher("/jsp/singlepost.jsp").forward(request, response);
				}
				catch(SQLException e) {
					// just go home because some weird error occurred.
					request.getRequestDispatcher("/").forward(request, response);
				}
				finally {
					if(dbConn != null) {
						try {
							dbConn.close();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				
			}
			else {
				// just go home because some weird error occurred.
				request.getRequestDispatcher("/").forward(request, response);
			}
		}
		else {
			request.setAttribute("userName", "");
			request.setAttribute("password", "");
			request.setAttribute("validationmessage", "you have chosen to go to the incorrect page.");
			Connection dbConn = null;
			try {
				dbConn = ds.getConnection();
				List<Blog> thePosts = new ArrayList<Blog>();
				
				BlogDAO retrievePosts = new BlogDAO(dbConn);
				thePosts = retrievePosts.getBlogs();
				
				request.setAttribute("dbResults", thePosts);
				request.getRequestDispatcher("/GetPostsController?action=viewPosts").forward(request, response);
			}
			catch(SQLException e) {
				request.getRequestDispatcher("/").forward(request, response);
			}
			finally {
				if(dbConn != null) {
					try {
						dbConn.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

}
