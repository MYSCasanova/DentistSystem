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

@WebServlet(name = "AppointmentUpdateServlet", urlPatterns = {"/AppointmentUpdateServlet"})
public class AppointmentUpdateServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        String appDate = request.getParameter("appDate");
        String purpose = request.getParameter("purpose");
        int appointmentID = Integer.parseInt(request.getParameter("appointment_id"));


        try (Connection conn = databaseconnection.getConnection()) {
            String sql = "UPDATE tbl_appointment SET appointment_date = ?, purpose_of_appointment = ? WHERE appointment_id = ?";
            try (PreparedStatement pst = conn.prepareStatement(sql)) {
                pst.setString(1, appDate);
                pst.setString(2, purpose);
                pst.setInt(3, appointmentID);

                int rowsUpdated = pst.executeUpdate();
                System.out.println("Rows updated: " + rowsUpdated);

                //INSERT LOG ACTION
                HttpSession session = request.getSession();
                int id = (int)session.getAttribute("user_id"); 
                String user = (String)session.getAttribute("username");
                String logsql = "Insert into tbl_logs (user_id, action, date_time) values (?,?,NOW())";
                String message = user + " Edited an Appointment Record."; //SPECIFY WHO'S THE USER
                PreparedStatement logpst = conn.prepareStatement(logsql);
                logpst.setInt(1, id);
                logpst.setString(2,message);
                logpst.executeUpdate();

                // Redirect with success message
                response.sendRedirect("AppointmentDisplayServlet?alert=success&message=Record+Edited+Successfully");
            }
        } catch (Exception e) {
            System.err.println("Error updating appointment: " + e.getMessage());
            e.printStackTrace();

            try (PrintWriter out = response.getWriter()) {
                out.print("<html><head><title>Error</title></head><body>");
                out.print("<h3 style='color:red;'>Failed to update record.</h3>");
                out.print("<p>" + e.getMessage() + "</p>");
                out.print("<a href='AppointmentDisplayServlet'>Back to Appointment List</a>");
                out.print("</body></html>");
            }
        }
    }

    private void showSuccessAlert(PrintWriter out) {
        out.print("<html><head><title>Edited Successfully!</title></head><body>");
        out.print("<script type='text/javascript'>");
        out.print("alert('Record Edited Successfully!'); window.location.href = 'AppointmentDisplayServlet';");
        out.print("</script></body></html>");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Optional: redirect GET to users list or show error
        response.sendRedirect("AppointmentDisplayServlet");
    }

    @Override
    public String getServletInfo() {
        return "Servlet to update appointment information in the database.";
    }
}


