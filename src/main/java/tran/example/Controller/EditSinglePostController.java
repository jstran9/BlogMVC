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
 * Servlet implementation class EditSinglePost
 */
public class EditSinglePostController extends HttpServlet {
	private static final long serialVersionUID = 4L;
	
	private DataSource ds;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditSinglePostController() {
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
		String postContent = request.getParameter("content");
		
		int postID = Integer.parseInt(request.getParameter("blogID"));
		if(action.equals("doEditPost")) {
			Connection dbConn = null;	
			try {
				dbConn = ds.getConnection();
				BlogDAO checkOldPost = new BlogDAO(dbConn);
				// verify that the person editing the post is the author.
				//Blog oldPost = checkOldPost.getaBlog(postID);
				
				HttpSession verifyUser = request.getSession(false);
				String authorToValidate = "";
				
				if(verifyUser != null) {
					authorToValidate = verifyUser.getAttribute("userName") != null ? (String)verifyUser.getAttribute("userName") : "" ;
				}
				
				if(checkOldPost.isAuthorOfPost(authorToValidate, postID)) {
					if(checkOldPost.checkForChanges(postID, postContent)) {
						Blog newBlog = checkOldPost.getaBlog(postID);
						request.setAttribute("editedContents", newBlog);
						request.getRequestDispatcher("/GetSinglePostController?action=showSinglePost&blogID=" + postID).forward(request, response);
					}
					else {
						request.setAttribute("infoMessage", "you didn't change the contents of the post");
						request.getRequestDispatcher("/GetPostsController?action=viewPosts").forward(request, response);
					}
				}
				else {
					request.setAttribute("infoMessage", "Only the author of the post can modify this post.");
					request.getRequestDispatcher("/GetPostsController?action=viewPosts").forward(request, response);
				}
				
			}
			catch(SQLException e) {
				e.printStackTrace();
			}
			finally {
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
