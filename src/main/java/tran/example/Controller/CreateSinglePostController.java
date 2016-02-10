package tran.example.Controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

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
 * Servlet implementation class CreateSinglePost
 */
public class CreateSinglePostController extends HttpServlet {
	private static final long serialVersionUID = 2L;
	
	private DataSource ds;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateSinglePostController() {
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
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String action = request.getParameter("action");
		HttpSession session = request.getSession(false);
		if(action.equals("createPost") && session != null) {
			
			String title  = request.getParameter("title");
			String content = request.getParameter("content");
			
			String author = (String) session.getAttribute("userName");
			
			Connection dbConn = null;
			
			int createdBlogId = -1;
			try {
				dbConn = ds.getConnection();
				BlogDAO createBlog = new BlogDAO(dbConn);
				createBlog.addBlog(title, content, author);
				createdBlogId = createBlog.getCreatedBlogID();
				Blog postToDisplay = createBlog.getaBlog(createdBlogId);
				String forwardURL = "/GetSinglePostController?action=showSinglePost";
				request.setAttribute("postContents", postToDisplay);
				request.getRequestDispatcher(forwardURL).forward(request, response);
			}
			catch(SQLException e) {
				request.setAttribute("infoMessage", "An error occured and a post could not be created, try again.");
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
			request.setAttribute("infoMessage", "You must be logged in then you can create a post.");
			request.getRequestDispatcher("/GetPostsController?action=viewPosts").forward(request, response);
		}
		
	}

}
