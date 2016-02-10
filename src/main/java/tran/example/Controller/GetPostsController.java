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
import javax.sql.DataSource;

import tran.example.DAO.BlogDAO;
import tran.example.Model.Blog;

/**
 * Servlet implementation class GetPostsController
 */
public class GetPostsController extends HttpServlet {
	private static final long serialVersionUID = 5L;
	
	private DataSource ds;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetPostsController() {
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
		if(action.equals("viewPosts")) {
			Connection dbConn = null;
			try {
				dbConn = ds.getConnection();
				List<Blog> thePosts = new ArrayList<Blog>();
				
				BlogDAO retrievePosts = new BlogDAO(dbConn);
				thePosts = retrievePosts.getBlogs();
				
				request.setAttribute("dbResults", thePosts);
				request.getRequestDispatcher("/jsp/posts.jsp").forward(request, response);
			}
			catch(SQLException e) {
				request.setAttribute("infoMessage", "an error has occured retrieving the posts, perhaps the database is down, try again later.");
				request.getRequestDispatcher("/jsp/posts.jsp").forward(request, response);
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
			// go back to the home page.
			request.getRequestDispatcher("/").forward(request, response);
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String action = request.getParameter("action");
		if(action.equals("viewPosts")) {
			Connection dbConn = null;
			try {
				dbConn = ds.getConnection();
				List<Blog> thePosts = new ArrayList<Blog>();
				
				BlogDAO retrievePosts = new BlogDAO(dbConn);
				thePosts = retrievePosts.getBlogs();
				
				request.setAttribute("dbResults", thePosts);
				request.getRequestDispatcher("/jsp/posts.jsp").forward(request, response);
			}
			catch(SQLException e) {
				request.setAttribute("infoMessage", "an error has occured retrieving the posts, perhaps the database is down, try again later.");
				request.getRequestDispatcher("/GetPostsController?action=viewPosts").forward(request, response);
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
			// go back to the home page.
			request.getRequestDispatcher("/").forward(request, response);
		}
	}
}
