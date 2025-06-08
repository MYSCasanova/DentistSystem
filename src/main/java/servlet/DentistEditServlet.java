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

@WebServlet(name = "DentistEditServlet", urlPatterns = {"/DentistEditServlet"})
public class DentistEditServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        try {
            String fullname = request.getParameter("fullname");
            ArrayList<String[]> records = new ArrayList();
            
            try (Connection conn = databaseconnection.getConnection()) {
                // Check if email exists in tbl_user with dentist role
                String userCheck = "SELECT *\n" +
                "From tbl_dentist A\n" +
                "LEFT JOIN tbl_user B ON A.dentist_name = B.full_name\n" +
                "WHERE A.dentist_name = ? AND B.full_name = ? AND B.role = 'Dentist';";
                
                try (PreparedStatement pstUser = conn.prepareStatement(userCheck)) {
                    pstUser.setString(1, fullname);
                    pstUser.setString(2, fullname);
                    
                    try (ResultSet rsUser = pstUser.executeQuery()) {
                        if (rsUser.next()) {
                            // Get dentist details
                            String dentistQuery = "SELECT *\n" +
                            "From tbl_dentist A\n" +
                            "LEFT JOIN tbl_user B ON A.dentist_name = B.full_name\n" +
                            "WHERE A.dentist_name = ? AND B.full_name = ? AND B.role = 'Dentist';";
                            
                            try (PreparedStatement pstDentist = conn.prepareStatement(dentistQuery)) {
                                pstDentist.setString(1, fullname);
                                pstDentist.setString(2, fullname);
                                
                                ResultSet rs1 = pstDentist.executeQuery();
                                while(rs1.next()){
                                    records.add(new String[]{
                                        rs1.getString("user_id"),
                                        rs1.getString("full_name"),
                                        rs1.getString("contact_information"),
                                        rs1.getString("specialization"),
                                        rs1.getString("email"),
                                        rs1.getString("username"),
                                        rs1.getString("password"),
                                        rs1.getString("role")
                                    });
                                }
                                
                                request.setAttribute("records", records);
                                request.getRequestDispatcher("dentistEdit.jsp").forward(request, response);
                            }
                        } else {
                            response.sendRedirect("DentistDisplayServlet?alert=error&message=Dentist+does+not+exist+or+is+not+a+dentist");
                            return;
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Database error occurred");
                e.printStackTrace();
                response.sendRedirect("DentistDisplayServlet?alert=error&message=A+database+error+occurred.+Please+try+again.");
                return;
            }
        } catch (Exception e) {
            System.out.println("Servlet error occurred");
            e.printStackTrace();
            response.sendRedirect("DentistDisplayServlet?alert=error&message=An+unexpected+error+occurred.+Please+try+again.");
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


