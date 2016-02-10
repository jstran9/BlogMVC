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

/**
 * Servlet implementation class DeleteSinglePost
 */
public class DeleteSinglePostController extends HttpServlet {
	private static final long serialVersionUID = 3L;
	
	private DataSource ds;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteSinglePostController() {
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
		HttpSession session = request.getSession(false);
		
		if(action.equals("deletePost") && session != null) {
			Connection dbConn = null;
			try {
				int blogID = Integer.parseInt(request.getParameter("blogID"));
				String authorOfPost = (String) session.getAttribute("userName");
				dbConn = ds.getConnection();
				BlogDAO deletePost = new BlogDAO(dbConn);
				int returnCodeValue = deletePost.removeBlog(blogID, authorOfPost);
				if(returnCodeValue > 0) {
					request.setAttribute("infoMessage", "Post deleted!");
					request.getRequestDispatcher("/GetPostsController?action=viewPosts").forward(request, response);
				}
				else {
					request.setAttribute("infoMessage", "The post could not be deleted.");
					request.getRequestDispatcher("/GetPostsController?action=viewPosts").forward(request, response);
				}
			}
			catch(SQLException e) {
				request.setAttribute("infoMessage", "an error occured, could not delete the post.");
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
			request.setAttribute("infoMessage", "you chose not to delete the post.");
			request.getRequestDispatcher("/GetPostsController?action=viewPosts").forward(request, response);
		}
	}

}
