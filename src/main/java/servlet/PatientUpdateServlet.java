package servlet;

import dbase.databaseconnection;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.SecurityServlet;


@WebServlet(name = "PatientUpdateServlet", urlPatterns = {"/PatientUpdateServlet"})
public class PatientUpdateServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        String patientID = request.getParameter("patient_id");
        String patientName = request.getParameter("patient_name");
        String dateofBirth = request.getParameter("date_of_birth");
        String gender = request.getParameter("gender");
        String contactInformation = request.getParameter("contact_information");
        String address = request.getParameter("address");
        
        try (Connection conn = databaseconnection.getConnection()) {
            String sql = "UPDATE tbl_patient SET patient_name = ?, date_of_birth = ?, gender = ?, contact_information = ?, address = ? WHERE patient_id = ?";
            
            
            //INSERT LOG ACTION
            HttpSession session = request.getSession();
            int id = (int)session.getAttribute("user_id"); 
            String user = (String)session.getAttribute("username");
            String logsql = "Insert into tbl_logs (user_id, action, date_time) values (?,?,NOW())";
            String message = user + " Edited a Patient Record."; //SPECIFY WHO'S THE USER
            PreparedStatement logpst = conn.prepareStatement(logsql);
            logpst.setInt(1, id);
            logpst.setString(2,message);
            logpst.executeUpdate();
            
            
            try (PreparedStatement pst = conn.prepareStatement(sql)) {
                pst.setString(1, patientName);
                pst.setString(2, dateofBirth);
                pst.setString(3, gender); //insert encrypted password
                pst.setString(4, contactInformation);
                pst.setString(5, address);
                pst.setString(6, patientID);

                int rowsUpdated = pst.executeUpdate();
                System.out.println("Rows updated: " + rowsUpdated);
                response.sendRedirect("PatientDisplayServlet?alert=success&message=Record+Edited+Successfully");

                // Optional: you can use JavaScript if you want an alert popup
                showSuccessAlert(response.getWriter());

                // Or use this instead for redirect without alert:
                // response.sendRedirect("UsersDisplayServlet");
            }
        } catch (Exception e) {
            System.err.println("Error updating user: " + e.getMessage());
            e.printStackTrace();

            try (PrintWriter out = response.getWriter()) {
                out.print("<html><head><title>Error</title></head><body>");
                out.print("<h3 style='color:red;'>Failed to update record.</h3>");
                out.print("<p>" + e.getMessage() + "</p>");
                out.print("<a href='PatientDisplayServlet'>Back to Patients List</a>");
                out.print("</body></html>");
            }
        }
    }

    private void showSuccessAlert(PrintWriter out) {
        out.print("<html><head><title>Edited Successfully!</title></head><body>");
        out.print("<script type='text/javascript'>");
        out.print("alert('Record Edited Successfully!'); window.location.href = 'PatientDisplayServlet';");
        out.print("</script></body></html>");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Optional: redirect GET to users list or show error
        response.sendRedirect("PatientDisplayServlet");
    }

    @Override
    public String getServletInfo() {
        return "Servlet to update user information in the database.";
    }
}
