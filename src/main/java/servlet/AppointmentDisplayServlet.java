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
@WebServlet(name = "AppointmentDisplayServlet", urlPatterns = {"/AppointmentDisplayServlet"})
public class AppointmentDisplayServlet extends HttpServlet {

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
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
        response.sendRedirect("login.jsp"); 
        return; 
        }
        
        try(Connection conn = databaseconnection.getConnection()) {
            String search = request.getParameter("search");
            String sortField = request.getParameter("sortField"); //NEW
            String sortOrder = request.getParameter("sortOrder"); //NEW
            
            String selectsql, sortsql;
            if (search != null && !search.trim().isEmpty()) {
                search = "%" + search + "%";
                //selectsql = "SELECT * FROM tbl_appointment WHERE appointment_id LIKE ? OR patient_id LIKE ? OR dentist_id LIKE ? OR purpose_of_appointment LIKE ? ORDER BY appointment_date DESC";
                selectsql = "SELECT a.appointment_id, p.patient_name, d.dentist_name, d.specialization, a.appointment_date, a.purpose_of_appointment FROM tbl_appointment a JOIN tbl_patient p ON a.patient_id = p.patient_id JOIN tbl_dentist d ON a.dentist_id = d.dentist_id WHERE a.appointment_id LIKE ? OR p.patient_name LIKE ? OR d.dentist_name LIKE ? OR d.specialization LIKE ? OR a.appointment_date LIKE ? OR a.purpose_of_appointment LIKE ? ORDER BY a.appointment_date DESC";
                pst = conn.prepareStatement(selectsql);
                pst.setString(1, search);
                pst.setString(2, search);
                pst.setString(3, search);
                pst.setString(4, search);
                pst.setString(5, search);
                pst.setString(6, search);
            } 
            else if (sortField != null && sortOrder != null) 
            {
                sortsql = "SELECT a.appointment_id, p.patient_name, d.dentist_name, d.specialization, a.appointment_date, a.purpose_of_appointment FROM tbl_appointment a JOIN tbl_patient p ON a.patient_id = p.patient_id JOIN tbl_dentist d ON a.dentist_id = d.dentist_id order by " + sortField + " " + sortOrder;
                pst = conn.prepareStatement(sortsql);
                System.out.println("Sort Successful");
            }
            else {
                selectsql = "SELECT a.appointment_id, p.patient_name, d.dentist_name, d.specialization, a.appointment_date, a.purpose_of_appointment FROM tbl_appointment a JOIN tbl_patient p ON a.patient_id = p.patient_id JOIN tbl_dentist d ON a.dentist_id = d.dentist_id order by a.appointment_date desc";
                pst = conn.prepareStatement(selectsql);
            }
            
            //will receive the data from the executed select query
            ResultSet rs = pst.executeQuery();
            //UNCOMMENT THIS AFTER TESTING
            //INSERT LOG ACTION
            session = request.getSession();
            int id = (int)session.getAttribute("user_id"); 
            String user = (String)session.getAttribute("username");
            String logsql = "Insert into tbl_logs (user_id, action, date_time) values (?,?,NOW())";
            String message = user + " Displayed Appointment Records."; //SPECIFY WHO'S THE USER
            PreparedStatement logpst = conn.prepareStatement(logsql);
            logpst.setInt(1, id);
            logpst.setString(2,message);
            logpst.executeUpdate();
            
            while (rs.next()) {
                //insert data only in a new arraylist
                records.add(new String[]{
                    rs.getString("a.appointment_id"),
                    rs.getString("p.patient_name"),
                    rs.getString("d.dentist_name"),
                    rs.getString("d.specialization"),
                    rs.getString("a.appointment_date"),
                    rs.getString("a.purpose_of_appointment"),
                });
            }
            
            rs.close();
            pst.close();
            
            if(records.size() > 0){
                System.out.println("Array has items");
            }
            
            //Display dentists in appointmentTable.jsp
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
            request.getRequestDispatcher("appointmentTable.jsp").forward(request, response);
            
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
