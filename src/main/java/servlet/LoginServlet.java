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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.SecurityServlet;
import model.VerifyRecaptcha;


/**
 *
 * @author MYSC
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {

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
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String full_name = null;
            
            // get reCAPTCHA request param
		String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
		System.out.println(gRecaptchaResponse);
		boolean verify = VerifyRecaptcha.verify(gRecaptchaResponse);
            
            
            //CREATE SESSION
            HttpSession session = request.getSession();
            
            //SET USERNAME INSIDE THE SESSION
            session.setAttribute("username", username);
            

            PreparedStatement pst = null;
            Connection conn = null;
            
            if (username == null || username.trim().isEmpty() ||
                password == null || password.trim().isEmpty()) {
                request.setAttribute("message", "invalid");
                request.getRequestDispatcher("login.jsp").forward(request, response);
                return;
            }

            if (!verify) {
                System.out.println("Missed Captcha");
                request.setAttribute("message", "Invalid");
                request.getRequestDispatcher("login.jsp").forward(request, response);
                return;
            }


            try {
                conn = databaseconnection. getConnection();

                //removed "password = ?"
                String sql = "SELECT * FROM tbl_user WHERE username = ?";
                pst = conn.prepareStatement(sql);
                pst.setString(1, username);
                System.out.println(username);

                //will receive the data from the executed select query
                ResultSet rs = pst.executeQuery();

                if(rs.next()) {
                    // Get the encrypted password from database
                    String encryptedPassword = rs.getString("password");
                    System.out.println("Encrypted Password: "+encryptedPassword);

                    // Decrypt password
                    String decryptedPassword = SecurityServlet.decrypt(encryptedPassword);

                    //if input password = decrypted password
                    if(password.equals(decryptedPassword)){
                        System.out.println("Valid login");

                        //get role field
                        String role = rs.getString("role");
                        System.out.println(role);

                        //get full name set session
                        full_name = rs.getString("full_name");
                        session.setAttribute("full_name", full_name);

                        //GET ID FOR LOGS
                        int id = rs.getInt("user_id");
                        String logsql = "Insert into tbl_logs (user_id, action, date_time) values (?,?,NOW())";
                        String message = username + " Logged in."; //SPECIFY WHO'S THE USER
                        PreparedStatement logpst = conn.prepareStatement(logsql);
                        logpst.setInt(1, id);
                        logpst.setString(2, message);
                        logpst.executeUpdate();
                        System.out.println("Role: " + role);

                        //CREATE SESSION FOR ID
                        session.setAttribute("user_id", id);
                        session.setAttribute("role", role);

                        if (role.equals("Admin")) {
                            response.sendRedirect("homeAdmin.jsp");
                        } else if (role.equals("Employee")) {
                            response.sendRedirect("homeEmployee.jsp");
                        } else if (role.equals("Dentist")) {
                            response.sendRedirect("homeDentist.jsp");
                        }
                    } else { //UPDATED
                    System.out.println("Invalid username/pass");
                    request.setAttribute("message", "Invalid");
                    request.getRequestDispatcher("login.jsp").forward(request, response);
                    }   

                }else{
                    System.out.println("Invalid username/pass");
                    request.setAttribute("message", "Invalid");
                    request.getRequestDispatcher("login.jsp").forward(request, response);

                }
            }catch (SQLException e){
                System.out.println("SQL EXCEPTION");
                e.printStackTrace();
            }
            
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
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(LoginServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(LoginServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
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



