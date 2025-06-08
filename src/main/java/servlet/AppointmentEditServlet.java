package servlet;
import dbase.databaseconnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "AppointmentEditServlet", urlPatterns = {"/AppointmentEditServlet"})
public class AppointmentEditServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        try (PrintWriter out = response.getWriter()) {
            String id = request.getParameter("recordID");
            String selectsql = null;
            PreparedStatement pst = null;
            ArrayList <String[]> records = new ArrayList();
            
            int appointmentID = -1;
            try {
                appointmentID = Integer.parseInt(id);
            } catch (NumberFormatException e) {
                System.out.println("Invalid recordID input");
                response.sendRedirect("AppointmentDisplayServlet?alert=error&message=Invalid+Appointment+ID");
                return;
            }
            
            try (Connection conn = databaseconnection.getConnection();
                PreparedStatement rst = conn.prepareStatement("SELECT appointment_id FROM tbl_appointment WHERE appointment_id = ?");) {
               rst.setInt(1, appointmentID);
               try (ResultSet rs = rst.executeQuery()) {
                   if (rs.next()) {
                       //IF APPOINTMENT EXISTS IN THE DATABASE
                       selectsql = "Select * from tbl_appointment where appointment_id = ?";
                       pst = conn.prepareStatement(selectsql);
                       pst.setInt(1, appointmentID);
                       
                        //will receive the data from the executed select query
                        ResultSet rs1 = pst.executeQuery();
                        while (rs1.next()) {
                            //insert data only in a new arraylist
                            records.add(new String[]{
                                rs1.getString("appointment_id"),
                                rs1.getString("patient_id"),
                                rs1.getString("dentist_id"),
                                rs1.getString("appointment_date"),
                                rs1.getString("purpose_of_appointment"),
                            });
                        }
                        if(records.size() > 0){
                            System.out.println("Array has items");
                        }
                        request.setAttribute("records",records);
                        request.getRequestDispatcher("appointmentEdit.jsp").forward(request, response);
            
                    } else { //IF APPOINTMENT DOESN'T EXIST IN THE DATABASE
                        System.out.println("ERROR: Appointment ID Does Not Exist in the database");
                        response.sendRedirect("AppointmentDisplayServlet?alert=error&message=Appointment+does+not+exist+in+the+Database");
                        return;
                    }
                }
            }
            catch (Exception e) {
                System.out.println("error pst");
                e.printStackTrace();
                response.sendRedirect("AppointmentDisplayServlet?alert=error&message=Database+error");
            }
        }
        catch (Exception e) {
            System.out.println("error eh");
            e.printStackTrace();
            response.sendRedirect("AppointmentDisplayServlet?alert=error&message=System+error");
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

