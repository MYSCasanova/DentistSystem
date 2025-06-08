/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import dbase.databaseconnection;
import java.sql.*;
import java.util.ArrayList;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "PatientDisplayServlet", urlPatterns = {"/PatientDisplayServlet"})
public class PatientDisplayServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PreparedStatement pst = null;
        ArrayList <String[]> records = new ArrayList();
        
        //search input
        String searchInput = request.getParameter("search"); //name of input type is search
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
        response.sendRedirect("login.jsp"); 
        return; 
        }
        
        try(Connection conn = databaseconnection.getConnection()) {
            
             // Select * from PATIENT where 
            String selectsql, sortsql;
            
            //INSERT LOG ACTION
            session = request.getSession();
            int id = (int)session.getAttribute("user_id"); 
            String user = (String)session.getAttribute("username");
            String logsql = "Insert into tbl_logs (user_id, action, date_time) values (?,?,NOW())";
            String message = user + " Displayed Patient Record."; //SPECIFY WHO'S THE USER
            String sortField = request.getParameter("sortField"); //NEW
            String sortOrder = request.getParameter("sortOrder"); //NEW
            
            PreparedStatement logpst = conn.prepareStatement(logsql);
            logpst.setInt(1, id);
            logpst.setString(2,message);
            logpst.executeUpdate();
            
            //if search input is not empty
            if (searchInput != null && !searchInput.trim().isEmpty())
            {
                
                //Select * from tbl_accountdetails_mysc where fname LIKE % userInput
                //selectsql = "Select * from tbl_accountdetails_mysc where fname LIKE ?"; //use like para kahit letter pa lang naffilter out na
                //Now to make it flexible 
                selectsql = "Select * from tbl_patient where patient_id LIKE ? or patient_name LIKE ? or date_of_birth LIKE ? or gender LIKE ? or contact_information LIKE ? or address LIKE ?";
                
                pst = conn.prepareStatement(selectsql);
                String searchFormat = "%" + searchInput + "%"; //Separate this kasi hindi pwede idiretso sa selectsql na string
                pst.setString(1, searchFormat); //patient_id
                pst.setString(2, searchFormat); //patient_name
                pst.setString(3,searchFormat); //date_of_birth
                pst.setString(4, searchFormat); //gender
                pst.setString(5, searchFormat); //contact_information
                pst.setString(6, searchFormat); //address
            }
            else if ("address".equals(sortField)) {
                // Fetch without SQL sorting if sorting by address
                selectsql = "Select * from tbl_patient";
                pst = conn.prepareStatement(selectsql);
            }
            
            else if (sortField != null && sortOrder != null) 
            {
                sortsql = "Select * from tbl_patient order by " + sortField + " " + sortOrder;
                pst = conn.prepareStatement(sortsql);
                System.out.println("Sort Successful");
            }
            
            else 
            {
                //if search input is empty, display only
                selectsql = "Select * from tbl_patient"; //Select * from tbl_patient
                pst = conn.prepareStatement(selectsql);
            }

            //will receive the data from the executed select query
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                //insert data only in a new arraylist
                records.add(new String[]{
                    rs.getString("patient_id"),
                    rs.getString("patient_name"),
                    rs.getString("date_of_birth"),
                    rs.getString("gender"),
                    rs.getString("contact_information"),
                    rs.getString("address"),

                });
            }
            if ("address".equals(sortField) && records.size() > 0) {
                final String order = sortOrder != null ? sortOrder.toLowerCase() : "asc";

                records.sort((a, b) -> {
                String addr1 = a[5];
                String addr2 = b[5];

                boolean isDist1 = addr1 != null && addr1.toLowerCase().startsWith("district ");
                boolean isDist2 = addr2 != null && addr2.toLowerCase().startsWith("district ");

                int comp;

                if (isDist1 && isDist2) {
                    int d1 = extractDistrictNumber(addr1);
                    int d2 = extractDistrictNumber(addr2);
                    comp = Integer.compare(d1, d2);
                } else if (isDist1) {
                    comp = 1;
                } else if (isDist2) {
                    comp = -1;
                } else {
                    comp = addr1.compareTo(addr2);
                }

                return "desc".equals(order) ? -comp : comp;
            });

            }

            request.setAttribute("records",records);
            request.getRequestDispatcher("patientTable.jsp").forward(request, response);
            
        } catch (Exception e) {
            System.out.println("Error");
            e.printStackTrace();
        }   
        
    }
    private int extractDistrictNumber(String address) {
        if (address != null && address.startsWith("District ")) {
            try {
                return Integer.parseInt(address.substring(9).trim());
            } catch (Exception e) {
                return -1;
            }
        }
        return -1;
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


