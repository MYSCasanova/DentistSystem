package servlet;
import dbase.databaseconnection;
import java.sql.*;
import java.util.ArrayList;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.SecurityServlet;



@WebServlet(name = "UsersAddServlet", urlPatterns = {"/UsersAddServlet"})
public class UsersAddServlet extends HttpServlet
{

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = response.getWriter())
        {
            String fullname = request.getParameter("fullname");
            String role = request.getParameter("role");
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String email = request.getParameter("email");
            PreparedStatement pst = null, rst = null;

            //ENCRYPT PASSWORD
            String encryptedPassword = SecurityServlet.encrypt(password);


            try(Connection conn = databaseconnection.getConnection())
            {

                // Check if email exists
                String emailCheck = "SELECT EMAIL FROM tbl_user where EMAIL = ?";
                rst = conn.prepareStatement(emailCheck);
                rst.setString(1, email);  // Set the email value here
                ResultSet rs = rst.executeQuery();
                // Retrieve the result
                if (rs.next()) {
                    String resultEmail = rs.getString("EMAIL");
                    System.out.println("ERROR: EMAIL " + resultEmail + " Exists in the database");
                    // Redirect with error message
                    response.sendRedirect("UsersDisplayServlet?alert=error&message=Email+already+exists+in+the+Database");
                    return;
                } else {
                    String sql = "INSERT INTO tbl_user (full_name, role, username, password, email) VALUES (?,?,?,?,?)";
                    pst = conn.prepareStatement(sql);

                    pst.setString(1, fullname);
                    pst.setString(2, role);
                    pst.setString(3, username);
                    pst.setString(4, encryptedPassword); //insert encrypted password
                    pst.setString(5, email);

                    int result = pst.executeUpdate();

                     //UNCOMMENT THIS AFTER TESTING
                    //INSERT LOG ACTION
                    HttpSession session = request.getSession();
                    int id = (int)session.getAttribute("user_id"); 
                    String user = (String)session.getAttribute("username");
                    String logsql = "Insert into tbl_logs (user_id, action, date_time) values (?,?,NOW())";
                    String message = user + " Added a User Record."; //SPECIFY WHO'S THE USER
                    PreparedStatement logpst = conn.prepareStatement(logsql);
                    logpst.setInt(1, id);
                    logpst.setString(2,message);
                    logpst.executeUpdate();

                    System.out.println(result);
                    System.out.println("Email Successfully Added");
                    // Redirect with success message
                    response.sendRedirect("UsersDisplayServlet?alert=success&message=Record+Added+Successfully");
                    return;
                }
            } 
            catch (Exception e)
            {
                System.out.println("error pst");
                e.printStackTrace();
                // You might want to add an error redirect here as well
                response.sendRedirect("UsersDisplayServlet?alert=error&message=Error+processing+request");
            }
        }
        catch (Exception e)
        {
            System.out.println("error eh");
            e.printStackTrace();
            // You might want to add an error redirect here as well
            response.sendRedirect("UsersDisplayServlet?alert=error&message=Unexpected+error");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}