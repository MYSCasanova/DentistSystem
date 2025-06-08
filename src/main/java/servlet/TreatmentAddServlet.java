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

@WebServlet(name = "TreatmentAddServlet", urlPatterns = {"/TreatmentAddServlet"})
public class TreatmentAddServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        try {
            String patientID = request.getParameter("patientID");
            String dentistName = request.getParameter("dentistName");
            String treatType = request.getParameter("treatmentType");
            String treatDetails = request.getParameter("treatmentDetails");
            String treatDate = request.getParameter("treatmentDate");
            // Format the datetime
            treatDate = treatDate.replace("T", " ") + ":00";
            
            try(Connection conn = databaseconnection.getConnection()) {
                // Check if patient exists in patient table
                String patientCheck = "SELECT patient_id FROM tbl_patient where patient_id = ?";
                PreparedStatement rst = conn.prepareStatement(patientCheck);
                rst.setString(1, patientID);
                ResultSet rs = rst.executeQuery();

                if (!rs.next()) {
                    response.sendRedirect("TreatmentDisplayServlet?alert=error&message=Patient+ID+does+not+exist+in+the+Database");
                    return;
                }
                
                // GET DENTIST ID OF SELECTED DENTIST
                String getID = "SELECT dentist_id from tbl_dentist WHERE dentist_name = ?";
                PreparedStatement dentistresult = conn.prepareStatement(getID);
                dentistresult.setString(1, dentistName);
                rs = dentistresult.executeQuery();
                
                if (!rs.next()) {
                    response.sendRedirect("TreatmentDisplayServlet?alert=error&message=Dentist+not+found+" + 
                                         dentistName.replace(" ", "+"));
                    return;
                }
                
                int dentistId = rs.getInt("dentist_id");
                
                // Insert treatment record
                String sql = "INSERT INTO tbl_treatment (patient_id, dentist_id, treatment_type, treatment_details, treatment_date) VALUES (?,?,?,?,?)";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, patientID);
                pst.setInt(2, dentistId);
                pst.setString(3, treatType);
                pst.setString(4, treatDetails);
                pst.setString(5, treatDate);

                int result = pst.executeUpdate();
                
                //INSERT LOG ACTION
                HttpSession session = request.getSession();
                int id = (int)session.getAttribute("user_id"); 
                String user = (String)session.getAttribute("username");
                String logsql = "Insert into tbl_logs (user_id, action, date_time) values (?,?,NOW())";
                String message = user + " Added a Treatment Record.";
                PreparedStatement logpst = conn.prepareStatement(logsql);
                logpst.setInt(1, id);
                logpst.setString(2,message);
                logpst.executeUpdate();
                
                response.sendRedirect("TreatmentDisplayServlet?alert=success&message=Record+Added+Successfully");
                
            } catch (Exception e) {
                System.out.println("Database error");
                e.printStackTrace();
                response.sendRedirect("TreatmentDisplayServlet?alert=error&message=Database+error+occurred.+Please+try+again");
            }
        } catch (Exception e) {
            System.out.println("Servlet error");
            e.printStackTrace();
            response.sendRedirect("TreatmentDisplayServlet?alert=error&message=System+error+occurred.+Please+try+again");
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




