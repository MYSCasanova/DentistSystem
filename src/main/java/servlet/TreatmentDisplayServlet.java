/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import dbase.databaseconnection;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author alexandrakiana
 */
@WebServlet(name = "TreatmentDisplayServlet", urlPatterns = {"/TreatmentDisplayServlet"})
public class TreatmentDisplayServlet extends HttpServlet {

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
            String search = request.getParameter("search");
            String selectsql, sortsql;
            
            //UNCOMMENT THIS AFTER TESTING
            //INSERT LOG ACTION
            session = request.getSession();
            int id = (int)session.getAttribute("user_id"); 
            String user = (String)session.getAttribute("username");
            String logsql = "Insert into tbl_logs (user_id, action, date_time) values (?,?,NOW())";
            String message = user + " Displayed Treatment Records."; //SPECIFY WHO'S THE USER
            String sortField = request.getParameter("sortField"); //NEW
            String sortOrder = request.getParameter("sortOrder");

            PreparedStatement logpst = conn.prepareStatement(logsql);
            logpst.setInt(1, id);
            logpst.setString(2,message);
            logpst.executeUpdate();
            if (search != null && !search.trim().isEmpty()) {
                search = "%" + search + "%";
                //selectsql = "SELECT * FROM tbl_treatment WHERE record_id LIKE ? OR patient_id LIKE ? OR dentist_id LIKE ? OR treatment_type LIKE ? OR treatment_details LIKE ? OR treatment_date LIKE ?";
                selectsql = "SELECT t.record_id, p.patient_name, d.dentist_name, d.specialization, t.treatment_type, t.treatment_details, t.treatment_date "
                        + "FROM tbl_treatment t "
                        + "JOIN tbl_patient p ON t.patient_id = p.patient_id "
                        + "JOIN tbl_dentist d ON t.dentist_id = d.dentist_id where t.record_id LIKE ? or p.patient_name LIKE ? or "
                         + "d.dentist_name LIKE ? or d.specialization LIKE ? or t.treatment_type LIKE ? or t.treatment_details LIKE ? or t.treatment_date LIKE ? or t.treatment_date LIKE ?"
                        + "order by t.treatment_date desc;";
                
                pst = conn.prepareStatement(selectsql);
                String searchFormat = "%" + searchInput + "%"; //Separate this kasi hindi pwede idiretso sa selectsql na string
                pst.setString(1, searchFormat);
                pst.setString(2, searchFormat);
                pst.setString(3, searchFormat);
                pst.setString(4, searchFormat);
                pst.setString(5, searchFormat);
                pst.setString(6, searchFormat);
                pst.setString(7, searchFormat);
                pst.setString(8, searchFormat);
            } 
            else if (sortField != null && sortOrder != null) 
            {
                sortsql = "SELECT t.record_id, p.patient_name, d.dentist_name, d.specialization, t.treatment_type, t.treatment_details, t.treatment_date "
                        + "FROM tbl_treatment t "
                        + "JOIN tbl_patient p ON t.patient_id = p.patient_id "
                        + "JOIN tbl_dentist d ON t.dentist_id = d.dentist_id "
                        + "order by " + sortField + " " + sortOrder;
                pst = conn.prepareStatement(sortsql);
                System.out.println("Sort Successful");
            }
            else {
                selectsql = "SELECT t.record_id, p.patient_name, d.dentist_name, d.specialization, t.treatment_type, t.treatment_details, t.treatment_date "
                        + "FROM tbl_treatment t "
                        + "JOIN tbl_patient p ON t.patient_id = p.patient_id "
                        + "JOIN tbl_dentist d ON t.dentist_id = d.dentist_id "
                        + "order by t.treatment_date desc;";
                
                pst = conn.prepareStatement(selectsql);
            }
            
            //will receive the data from the executed select query
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                //insert data only in a new arraylist
                records.add(new String[]{
                    rs.getString("t.record_id"),
                    rs.getString("p.patient_name"),
                    rs.getString("d.dentist_name"),
                    rs.getString("d.specialization"),
                    rs.getString("t.treatment_type"),
                    rs.getString("t.treatment_details"),
                    rs.getString("t.treatment_date")
                });
            }
            
            rs.close();
            pst.close();
            
            if(records.size() > 0){
                System.out.println("Array has items");
            }
            
            //Display dentists in treatmentTable.jsp
            ArrayList<String> dentists = new ArrayList<>();
            String dentistSql = "SELECT dentist_name FROM tbl_dentist";
            pst = conn.prepareStatement(dentistSql); // reuse pst
            ResultSet rsDentists = pst.executeQuery();

            while (rsDentists.next()) {
                dentists.add(rsDentists.getString("dentist_name"));
            }
            rsDentists.close();
            pst.close();
            
            request.setAttribute("records", records);
            request.setAttribute("dentists", dentists); // ✅ Send to JSP

            // Forward to JSP
            request.getRequestDispatcher("treatmentTable.jsp").forward(request, response);
            
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