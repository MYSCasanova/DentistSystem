package servlet;
import dbase.databaseconnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.SecurityServlet;

@WebServlet(name = "DentistAddServlet", urlPatterns = {"/DentistAddServlet"})
public class DentistAddServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        try (PrintWriter out = response.getWriter()) {
            // Make sure these parameter names match your form:
            String fullname = request.getParameter("fullname"); // This is used for both user full_name and dentist_name
            String contactInformation = request.getParameter("contact_information");
            String specialization = request.getParameter("specialization");
            String email = request.getParameter("email");
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String role = "dentist"; // Fixed role
            
            //ENCRYPT PASSWORD
            String encryptedPassword = SecurityServlet.encrypt(password);
            
        try (Connection conn = databaseconnection.getConnection()) {
            // Check if email exists
            String nameCheck = "SELECT * FROM tbl_user WHERE full_name = ?";
            try (PreparedStatement rst = conn.prepareStatement(nameCheck)) {
                rst.setString(1, fullname);
                try (ResultSet rs = rst.executeQuery()) {
                    if (rs.next()) {
                        response.sendRedirect("DentistDisplayServlet?alert=error&message=Name+already+exists+in+the+Database");
                        return;
                    }
                }
            }

            // Start transaction
            conn.setAutoCommit(false);

            try {
                // Insert into tbl_user
                String sqlUser = "INSERT INTO tbl_user (full_name, role, username, password, email) VALUES (?,?,?,?,?)";
                try (PreparedStatement pstUser = conn.prepareStatement(sqlUser)) {
                    pstUser.setString(1, fullname);
                    pstUser.setString(2, role);
                    pstUser.setString(3, username);
                    pstUser.setString(4, encryptedPassword);
                    pstUser.setString(5, email);
                    pstUser.executeUpdate();
                }

                // Insert into DENTIST (using the same fullname as dentist_name)
                String sqlDentist = "INSERT INTO tbl_dentist (dentist_name, specialization, contact_information) VALUES (?,?,?)";
                try (PreparedStatement pstDentist = conn.prepareStatement(sqlDentist)) {
                    pstDentist.setString(1, fullname);
                    pstDentist.setString(2, specialization);
                    pstDentist.setString(3, contactInformation);
                    pstDentist.executeUpdate();
                }

                //INSERT LOG ACTION (NEW ADD)
                HttpSession session = request.getSession();
                int id = (int)session.getAttribute("user_id");
                String user = (String)session.getAttribute("username");
                String logsql = "Insert into tbl_logs (user_id, action, date_time) values (?,?,NOW())";
                String message = user + " Added a new Dentist Record."; // Fixed the action message
                PreparedStatement logpst = conn.prepareStatement(logsql);
                logpst.setInt(1, id);
                logpst.setString(2,message);
                logpst.executeUpdate();

                conn.commit();
                response.sendRedirect("DentistDisplayServlet?alert=success&message=Record+Added+Successfully");

            } catch (Exception e) {
                conn.rollback();
                System.out.println("Transaction failed");
                e.printStackTrace();
                response.sendRedirect("DentistDisplayServlet?alert=error&message=Error+adding+record.+Please+try+again");
            } finally {
                conn.setAutoCommit(true);
            }

        } catch (Exception e) {
            System.out.println("Database connection error");
            e.printStackTrace();
            response.sendRedirect("DentistDisplayServlet?alert=error&message=Database+error+occurred.+Please+try+again");
        }
        }
    }

    private void showAlert(PrintWriter out, String message, String redirectPage) {
        out.print("<html><head><title>Message</title></head><body>");
        out.print("<script type='text/javascript'>");
        out.print("alert('" + message + "');");
        out.print("window.location.href = '" + redirectPage + "';");
        out.print("</script></body></html>");
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