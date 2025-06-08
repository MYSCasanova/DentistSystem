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
import javax.servlet.http.HttpSession;
import model.SecurityServlet;


@WebServlet(name = "UsersEditServlet", urlPatterns = {"/UsersEditServlet"})
public class UsersEditServlet extends HttpServlet
{

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("text/html;charset=UTF-8");
        
        try (PrintWriter out = response.getWriter())
        {
            String email = request.getParameter("email");
            String selectsql = null;
            PreparedStatement pst = null;
            ArrayList <String[]> records = new ArrayList();
            
            try (Connection conn = databaseconnection.getConnection();
                PreparedStatement rst = conn.prepareStatement("SELECT EMAIL FROM tbl_user WHERE EMAIL = ?");)
            {
                rst.setString(1, email);
                
                try (ResultSet rs = rst.executeQuery()) {
                    if (rs.next()) {
                        //IF EMAIL EXISTS IN THE DATABASE
                        System.out.println("old_email exists");
                        selectsql = "Select * from tbl_user where EMAIL = ?";
                        pst = conn.prepareStatement(selectsql);
                        pst.setString(1, email);

                        //will receive the data from the executed select query
                        ResultSet rs1 = pst.executeQuery();
                        while (rs1.next()) {                            
                            //insert data only in a new arraylist
                            records.add(new String[]{
                                rs1.getString("user_id"),
                                rs1.getString("full_name"),
                                rs1.getString("role"),
                                rs1.getString("username"),
                                rs1.getString("password"),
                                rs1.getString("email"),
                            });
                        }
                        if(records.size() > 0){
                            System.out.println("Array has items");
                        }
                        request.setAttribute("records",records);
                        request.getRequestDispatcher("usersEdit.jsp").forward(request, response);
                    } else { //CHECK IF EMAIL EXISTS IN THE DATABASE
                        System.out.println("ERROR: EMAIL " + email + " Does Not Exist in the database");
                        response.sendRedirect("UsersDisplayServlet?alert=error&message=Email+does+not+exist+in+the+Database");
                        return;
                    }
                }
               
            }
            catch (Exception e)
            {
                System.out.println("error pst");
                e.printStackTrace();
            }
        }
        catch (Exception e)
        {
            System.out.println("error eh");
            e.printStackTrace();
        }
    }

    private void showFailureAlert(PrintWriter out) {
        out.print("<html><head><title>Error!</title></head><body>");
        out.print("<script type='text/javascript'>");
        out.print("alert('Email does not exist in the Database! Please Try Again.'); window.location.href = 'UsersDisplayServlet';");
        out.print("</script></body></html>");
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