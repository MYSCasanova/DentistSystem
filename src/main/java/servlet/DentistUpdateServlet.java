package servlet;

import dbase.databaseconnection;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "DentistUpdateServlet", urlPatterns = {"/DentistUpdateServlet"})
public class DentistUpdateServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        String fullname = request.getParameter("fullname");
        String contactInformation = request.getParameter("contact_information");
        String specialization = request.getParameter("specialization");
        String email = request.getParameter("email");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String old_name = request.getParameter("old_name");
        String role = "dentist"; // Fixed role

        try (Connection conn = databaseconnection.getConnection()) {
            conn.setAutoCommit(false);
            
            try {
                // Update tbl_user
                String sqlUser = "UPDATE tbl_user SET full_name=?, username=?, password=?, email=? WHERE full_name=?";
                try (PreparedStatement pstUser = conn.prepareStatement(sqlUser)) {
                    pstUser.setString(1, fullname);
                    pstUser.setString(2, username);
                    pstUser.setString(3, password);
                    pstUser.setString(4, email);
                    pstUser.setString(5, old_name);
                    pstUser.executeUpdate();
                }

                // Update DENTIST table
                String sqlDentist = "UPDATE tbl_dentist SET dentist_name=?, specialization=?, contact_information=? WHERE dentist_name = ?";
                try (PreparedStatement pstDentist = conn.prepareStatement(sqlDentist)) {
                    pstDentist.setString(1, fullname);
                    pstDentist.setString(2, specialization);
                    pstDentist.setString(3, contactInformation);
                    pstDentist.setString(4, old_name);
                    pstDentist.executeUpdate();
                }
                
                //INSERT LOG ACTION
                HttpSession session = request.getSession();
                int id = (int)session.getAttribute("user_id"); 
                String user = (String)session.getAttribute("username");
                String logsql = "Insert into tbl_logs (user_id, action, date_time) values (?,?,NOW())";
                String message = user + " Updated Dentist Record: " + fullname; // More specific log message
                PreparedStatement logpst = conn.prepareStatement(logsql);
                logpst.setInt(1, id);
                logpst.setString(2, message);
                logpst.executeUpdate();
                
                conn.commit();
                response.sendRedirect("DentistDisplayServlet?alert=success&message=Dentist+Updated+Successfully");
            } catch (Exception e) {
                conn.rollback();
                response.sendRedirect("DentistDisplayServlet?alert=error&message=Error+updating+dentist:+" + e.getMessage().replace(" ", "+"));
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (Exception e) {
            response.sendRedirect("DentistDisplayServlet?alert=error&message=Database+error:+" + e.getMessage().replace(" ", "+"));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("DentistDisplayServlet");
    }

    @Override
    public String getServletInfo() {
        return "Servlet for updating dentist records";
    }
}