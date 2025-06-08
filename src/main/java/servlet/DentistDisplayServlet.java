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

@WebServlet(name = "DentistDisplayServlet", urlPatterns = {"/DentistDisplayServlet"})
public class DentistDisplayServlet extends HttpServlet {

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
            
             // Select * from DENTIST where 
            String selectsql,sortsql;
            String sortField = request.getParameter("sortField"); //NEW
            String sortOrder = request.getParameter("sortOrder"); //NEW
            
            //INSERT LOG ACTION
            session = request.getSession();
            int id = (int)session.getAttribute("user_id"); 
            String user = (String)session.getAttribute("username");
            String logsql = "Insert into tbl_logs (user_id, action, date_time) values (?,?,NOW())";
            String message = user + " Displayed Dentist Record."; //SPECIFY WHO'S THE USER
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
                selectsql = "Select * from tbl_dentist where dentist_id LIKE ? or dentist_name LIKE ? or specialization LIKE ? or contact_information LIKE ?";
                
                pst = conn.prepareStatement(selectsql);
                String searchFormat = "%" + searchInput + "%"; //Separate this kasi hindi pwede idiretso sa selectsql na string
                pst.setString(1, searchFormat); //dentist_id
                pst.setString(2, searchFormat); //dentist_name
                pst.setString(3,searchFormat); //specialization
                pst.setString(4, searchFormat); //contact_information
            }
            else if (sortField != null && sortOrder != null) 
            {
                sortsql = "Select * from tbl_dentist order by " + sortField + " " + sortOrder;
                pst = conn.prepareStatement(sortsql);
                System.out.println("Sort Successful");
            }
            else 
            {
                //if search input is empty, display only
                selectsql = "Select * from tbl_dentist";
                pst = conn.prepareStatement(selectsql);
            }

            //will receive the data from the executed select query
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                //insert data only in a new arraylist
                records.add(new String[]{
                    rs.getString("dentist_id"),
                    rs.getString("dentist_name"),
                    rs.getString("specialization"),
                    rs.getString("contact_information"),

                });
            }
            
            if(records.size() > 0){
                System.out.println("Array has items");
            }
            request.setAttribute("records",records);
            request.getRequestDispatcher("dentistTable.jsp").forward(request, response);
            
        } catch (Exception e) {
            System.out.println("Error");
            e.printStackTrace();
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