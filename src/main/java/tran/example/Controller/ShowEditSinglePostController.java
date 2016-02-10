package tran.example.Controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
 * Servlet implementation class ShowEditSinglePostController
 */
public class ShowEditSinglePostController extends HttpServlet {
	private static final long serialVersionUID = 11L;
	
	private DataSource ds;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ShowEditSinglePostController() {
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
		String action = request.getParameter("action");
		int blogID = Integer.parseInt(request.getParameter("blogID"));
		
		if(action.equals("showEditPost")) {
			Connection dbConn = null;
			try {
				dbConn = ds.getConnection();
				BlogDAO verifyAuthor = new BlogDAO(dbConn);
				HttpSession currentSession = request.getSession(false);
				if(currentSession != null) {
					String theAuthor = currentSession.getAttribute("userName") != null ? (String) currentSession.getAttribute("userName") : "";
					boolean canEditPost = verifyAuthor.isAuthorOfPost(theAuthor, blogID);
					if(canEditPost) {
						// must pass in the blog.
						Blog getBlog = verifyAuthor.getaBlog(blogID);
						request.setAttribute("blogToEdit", getBlog);
						request.getRequestDispatcher("/jsp/editPost.jsp").forward(request, response);
					}
					else {
						request.setAttribute("infoMessage", "You must be the author of a post to edit it..");
						List<Blog> thePosts = new ArrayList<Blog>();
						
						BlogDAO retrievePosts = new BlogDAO(dbConn);
						thePosts = retrievePosts.getBlogs();
						
						request.setAttribute("dbResults", thePosts);
						request.getRequestDispatcher("/GetSinglePostController?action=viewPosts").forward(request, response);
					}
				}
				else {
					request.setAttribute("infoMessage", "if you want to edit a post, make sure you are the logged in and the author of it.");
					List<Blog> thePosts = new ArrayList<Blog>();
					
					BlogDAO retrievePosts = new BlogDAO(dbConn);
					thePosts = retrievePosts.getBlogs();
					
					request.setAttribute("dbResults", thePosts);
					request.getRequestDispatcher("/GetSinglePostController?action=viewPosts").forward(request, response);
				}
			}
			catch(SQLException e) {
				request.setAttribute("infoMessage", "could not verify if you are the author of the post, try again");
				request.getRequestDispatcher("/jsp/posts.jsp").forward(request, response);
			}
			finally {
				if(dbConn != null) {
					try {
						dbConn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}
		else {
			request.getRequestDispatcher("/jsp/posts.jsp").forward(request, response);
		}
		
	}

}
