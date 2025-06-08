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

@WebServlet(name = "PatientAddServlet", urlPatterns = {"/PatientAddServlet"})
public class PatientAddServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        try {
            String patientName = request.getParameter("patient_name");
            String gender = request.getParameter("gender");
            String contactInformation = request.getParameter("contact_information");
            String address = request.getParameter("address");
            String dateofBirth = request.getParameter("date_of_birth");
            
            try(Connection conn = databaseconnection.getConnection()) {
                // Check if patient exists
                String patientCheck = "SELECT patient_name FROM tbl_patient where patient_name = ?";
                PreparedStatement rst = conn.prepareStatement(patientCheck);
                rst.setString(1, patientName);
                ResultSet rs = rst.executeQuery();
                
                if (rs.next()) {
                    response.sendRedirect("PatientDisplayServlet?alert=error&message=Patient+already+exists+in+the+Database");
                    return;
                } else {
                    String sql = "INSERT INTO tbl_patient (patient_name, date_of_birth, gender, contact_information, address) VALUES (?,?,?,?,?)";
                    PreparedStatement pst = conn.prepareStatement(sql);

                    pst.setString(1, patientName);
                    pst.setString(2, dateofBirth);
                    pst.setString(3, gender);
                    pst.setString(4, contactInformation);
                    pst.setString(5, address);

                    int result = pst.executeUpdate();

                    //INSERT LOG ACTION
                    HttpSession session = request.getSession();
                    int id = (int)session.getAttribute("user_id"); 
                    String user = (String)session.getAttribute("username");
                    String logsql = "Insert into tbl_logs (user_id, action, date_time) values (?,?,NOW())";
                    String message = user + " Added a Patient Record.";
                    PreparedStatement logpst = conn.prepareStatement(logsql);
                    logpst.setInt(1, id);
                    logpst.setString(2,message);
                    logpst.executeUpdate();

                    response.sendRedirect("PatientDisplayServlet?alert=success&message=Record+Added+Successfully");
                }
            } catch (Exception e) {
                System.out.println("Database error");
                e.printStackTrace();
                response.sendRedirect("PatientDisplayServlet?alert=error&message=Database+error+occurred.+Please+try+again");
            }
        } catch (Exception e) {
            System.out.println("Servlet error");
            e.printStackTrace();
            response.sendRedirect("PatientDisplayServlet?alert=error&message=System+error+occurred.+Please+try+again");
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