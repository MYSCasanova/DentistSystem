package servlet;

import dbase.databaseconnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "PatientEditServlet", urlPatterns = {"/PatientEditServlet"})
public class PatientEditServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        try {
            int patientID = Integer.parseInt(request.getParameter("patient_id"));
            ArrayList<String[]> records = new ArrayList();
            
            try (Connection conn = databaseconnection.getConnection()) {
                // First check if patient exists
                try (PreparedStatement checkStmt = conn.prepareStatement(
                        "SELECT patient_id FROM tbl_patient WHERE patient_id = ?")) {
                    
                    checkStmt.setInt(1, patientID);
                    
                    try (ResultSet rs = checkStmt.executeQuery()) {
                        if (rs.next()) {
                            // Patient exists - get full details
                            try (PreparedStatement detailStmt = conn.prepareStatement(
                                    "SELECT * FROM tbl_patient WHERE patient_id = ?")) {
                                
                                detailStmt.setInt(1, patientID);
                                ResultSet rs1 = detailStmt.executeQuery();
                                
                                while (rs1.next()) {
                                    records.add(new String[]{
                                        rs1.getString("patient_id"),
                                        rs1.getString("patient_name"),
                                        rs1.getString("date_of_birth"),
                                        rs1.getString("gender"),
                                        rs1.getString("contact_information"),
                                        rs1.getString("address")
                                    });
                                }
                                
                                if (!records.isEmpty()) {
                                    request.setAttribute("records", records);
                                    request.getRequestDispatcher("patientEdit.jsp").forward(request, response);
                                    return;
                                } else {
                                    // This case shouldn't normally happen since we just verified the patient exists
                                    response.sendRedirect("PatientDisplayServlet?alert=error&message=Patient+data+could+not+be+retrieved");
                                    return;
                                }
                            }
                        } else {
                            // Patient doesn't exist
                            response.sendRedirect("PatientDisplayServlet?alert=error&message=Patient+ID+"+patientID+"+does+not+exist");
                            return;
                        }
                    }
                }
            } catch (NumberFormatException e) {
                response.sendRedirect("PatientDisplayServlet?alert=error&message=Invalid+Patient+ID+format");
                return;
            } catch (Exception e) {
                System.out.println("Database error occurred");
                e.printStackTrace();
                response.sendRedirect("PatientDisplayServlet?alert=error&message=Database+error+occurred");
                return;
            }
        } catch (Exception e) {
            System.out.println("Servlet error occurred");
            e.printStackTrace();
            response.sendRedirect("PatientDisplayServlet?alert=error&message=An+unexpected+error+occurred");
            return;
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