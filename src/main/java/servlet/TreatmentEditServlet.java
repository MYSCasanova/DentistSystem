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

@WebServlet(name = "TreatmentEditServlet", urlPatterns = {"/TreatmentEditServlet"})
public class TreatmentEditServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        try {
            String id = request.getParameter("recordID");
            int treatmentID;
            
            try {
                treatmentID = Integer.parseInt(id);
            } catch (NumberFormatException e) {
                response.sendRedirect("TreatmentDisplayServlet?alert=error&message=Invalid+Treatment+ID+format");
                return;
            }
            
            ArrayList<String[]> records = new ArrayList<>();
            
            try (Connection conn = databaseconnection.getConnection()) {
                // First check if treatment exists
                try (PreparedStatement checkStmt = conn.prepareStatement(
                        "SELECT record_id FROM tbl_treatment WHERE record_id = ?")) {
                    
                    checkStmt.setInt(1, treatmentID);
                    
                    try (ResultSet rs = checkStmt.executeQuery()) {
                        if (rs.next()) {
                            // Treatment exists - get full details
                            try (PreparedStatement detailStmt = conn.prepareStatement(
                                    "SELECT * FROM tbl_treatment WHERE record_id = ?")) {
                                
                                detailStmt.setInt(1, treatmentID);
                                ResultSet rs1 = detailStmt.executeQuery();
                                
                                while (rs1.next()) {
                                    records.add(new String[]{
                                        rs1.getString("record_id"),
                                        rs1.getString("patient_id"),
                                        rs1.getString("dentist_id"),
                                        rs1.getString("treatment_type"),
                                        rs1.getString("treatment_details"),
                                        rs1.getString("treatment_date")
                                    });
                                }
                                
                                if (!records.isEmpty()) {
                                    request.setAttribute("records", records);
                                    request.getRequestDispatcher("treatmentEdit.jsp").forward(request, response);
                                    return;
                                } else {
                                    // This case shouldn't normally happen since we verified the treatment exists
                                    response.sendRedirect("TreatmentDisplayServlet?alert=error&message=Treatment+data+could+not+be+retrieved");
                                    return;
                                }
                            }
                        } else {
                            // Treatment doesn't exist
                            response.sendRedirect("TreatmentDisplayServlet?alert=error&message=Treatment+ID+"+treatmentID+"+does+not+exist");
                            return;
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Database error occurred");
                e.printStackTrace();
                response.sendRedirect("TreatmentDisplayServlet?alert=error&message=Database+error+occurred");
                return;
            }
        } catch (Exception e) {
            System.out.println("Servlet error occurred");
            e.printStackTrace();
            response.sendRedirect("TreatmentDisplayServlet?alert=error&message=An+unexpected+error+occurred");
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

