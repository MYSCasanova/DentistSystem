package servlet;
import dbase.databaseconnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "AppointmentAddServlet", urlPatterns = {"/AppointmentAddServlet"})
public class AppointmentAddServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        try (PrintWriter out = response.getWriter()) {
            String patientID = request.getParameter("patientID");
            String dentistName = request.getParameter("dentistName");
            String appDate = request.getParameter("appDate");
            // Replace the 'T' with a space
            appDate = appDate.replace("T", " ");
            // Add seconds ":00" at the end
            appDate = appDate + ":00";
            String purposeofApp = request.getParameter("purposeofApp");
            PreparedStatement pst = null, rst = null, dentistresult = null;
            
            try(Connection conn = databaseconnection.getConnection()) {
                // Check if patient exists in patient table
                String patientCheck = "SELECT patient_id FROM tbl_patient where patient_id = ?";
                rst = conn.prepareStatement(patientCheck);
                rst.setString(1, patientID);
                ResultSet rs = rst.executeQuery();

                if (rs.next()) {
                    // GET DENTIST ID OF SELECTED DENTIST
                    String getID = "SELECT dentist_id from tbl_dentist WHERE dentist_name = ?";
                    dentistresult = conn.prepareStatement(getID);
                    dentistresult.setString(1, dentistName);
                    rs = dentistresult.executeQuery();
                    int dentistId = -1;
    
                    if (rs.next()) {
                        dentistId = rs.getInt("dentist_id");
                    } else {
                        // Handle case: name not found
                        response.sendRedirect("AppointmentDisplayServlet?alert=error&message=Dentist+not+found");
                        return;
                    }
                    
                    String sql = "INSERT INTO tbl_appointment (patient_id, dentist_id, appointment_date, purpose_of_appointment) VALUES (?,?,?,?)";
                    pst = conn.prepareStatement(sql);
                    pst.setString(1, patientID);
                    pst.setInt(2, dentistId);
                    pst.setString(3, appDate);
                    pst.setString(4, purposeofApp);

                    int result = pst.executeUpdate();
                    
                    if (result > 0) {
                        //INSERT LOG ACTION
                        HttpSession session = request.getSession();
                        int id = (int)session.getAttribute("user_id"); 
                        String user = (String)session.getAttribute("username");
                        String logsql = "Insert into tbl_logs (user_id, action, date_time) values (?,?,NOW())";
                        String message = user + " Added an Appointment Record.";
                        PreparedStatement logpst = conn.prepareStatement(logsql);
                        logpst.setInt(1, id);
                        logpst.setString(2,message);
                        logpst.executeUpdate();
                        
                        response.sendRedirect("AppointmentDisplayServlet?alert=success&message=Record+Added+Successfully");
                    } else {
                        response.sendRedirect("AppointmentDisplayServlet?alert=error&message=Failed+to+add+record");
                    }
                } else {
                    response.sendRedirect("AppointmentDisplayServlet?alert=error&message=Patient+ID+does+not+exist");
                }
            } 
            catch (Exception e) {
                System.out.println("Error in database operation");
                e.printStackTrace();
                response.sendRedirect("AppointmentDisplayServlet?alert=error&message=Database+error:+Please+try+again");
            }
        }
        catch (Exception e) {
            System.out.println("Error in servlet");
            e.printStackTrace();
            response.sendRedirect("AppointmentDisplayServlet?alert=error&message=Unexpected+error");
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




