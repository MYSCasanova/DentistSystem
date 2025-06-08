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

@WebServlet(name = "TreatmentUpdateServlet", urlPatterns = {"/TreatmentUpdateServlet"})
public class TreatmentUpdateServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        String TreatmentType = request.getParameter("type");
        String TreatmentDetails = request.getParameter("details");
        String TreatmentDate = request.getParameter("date");
        
        int recordID = Integer.parseInt(request.getParameter("record_id"));

        

        try (Connection conn = databaseconnection.getConnection()) {
            String sql = "UPDATE tbl_treatment SET treatment_type = ?, treatment_details = ?, treatment_date = ? WHERE record_id = ?";
            //UNCOMMENT THIS AFTER TESTING
            //INSERT LOG ACTION
            HttpSession session = request.getSession();
            int id = (int)session.getAttribute("user_id"); 
            String user = (String)session.getAttribute("username");
            String logsql = "Insert into tbl_logs (user_id, action, date_time) values (?,?,NOW())";
            String message = user + " Updated a Treatment Record."; //SPECIFY WHO'S THE USER
            PreparedStatement logpst = conn.prepareStatement(logsql);
            logpst.setInt(1, id);
            logpst.setString(2,message);
            logpst.executeUpdate();
            
            try (PreparedStatement pst = conn.prepareStatement(sql)) {
                pst.setString(1, TreatmentType);
                pst.setString(2, TreatmentDetails);
                pst.setString(3, TreatmentDate);
                pst.setInt(4, recordID);

                int rowsUpdated = pst.executeUpdate();
                System.out.println("Rows updated: " + rowsUpdated);
                response.sendRedirect("TreatmentDisplayServlet?alert=success&message=Record+Edited+Successfully");

                // Optional: you can use JavaScript if you want an alert popup
                showSuccessAlert(response.getWriter());

                // Or use this instead for redirect without alert:
                // response.sendRedirect("UsersDisplayServlet");
            }
        } catch (Exception e) {
            System.err.println("Error updating treatment: " + e.getMessage());
            e.printStackTrace();

            try (PrintWriter out = response.getWriter()) {
                out.print("<html><head><title>Error</title></head><body>");
                out.print("<h3 style='color:red;'>Failed to update record.</h3>");
                out.print("<p>" + e.getMessage() + "</p>");
                out.print("<a href='TreatmentDisplayServlet'>Back to Treatment List</a>");
                out.print("</body></html>");
            }
        }
    }

    private void showSuccessAlert(PrintWriter out) {
        out.print("<html><head><title>Edited Successfully!</title></head><body>");
        out.print("<script type='text/javascript'>");
        out.print("alert('Record Edited Successfully!'); window.location.href = 'TreatmentDisplayServlet';");
        out.print("</script></body></html>");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Optional: redirect GET to users list or show error
        response.sendRedirect("TreatmentDisplayServlet");
    }

    @Override
    public String getServletInfo() {
        return "Servlet to update treatment information in the database.";
    }
}


