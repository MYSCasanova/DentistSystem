<%@ page import = "java.util.*"%>
<%@ page import="javax.servlet.http.HttpSession" %>
<%@ page session="true" %>
<!-- need to import so we can use the arraylist -->
<%
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    session = request.getSession(false);
    if (session == null || session.getAttribute("username") == null) {
        response.sendRedirect("login.jsp"); 
        return; 
    }
%>
<!doctype html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Employee Page</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
        <link rel="stylesheet" href="css/styles.css">
    </head>
    <body>
        <div class="container-fluid mt-4">
            <div class="row">
                <div class="col-md-3 col-12">
                    <% 
                        String role = (String)session.getAttribute("role");
                        System.out.println(role);
                        //ADMIN
                        if("Admin".equals(role))
                        {
                            System.out.println("ADMIN CONNECTED");
                    %>
                    <nav class="nav flex-column bg-light p-3 rounded shadow-sm">
                        <a class="nav-link active" aria-current="page" href="homeAdmin.jsp">Home</a>
                        <a class="nav-link" href="UsersDisplayServlet">Users</a>
                        <a class="nav-link" href="DentistDisplayServlet">Dentists</a>
                        <a class="nav-link" href="PatientDisplayServlet">Patients</a>
                        <a class="nav-link" href="AppointmentDisplayServlet">Appointments</a>
                        <a class="nav-link" href="TreatmentDisplayServlet">Treatments</a>
                        <a class="nav-link" href="LogsDisplayServlet">Logs</a>
                        <a class="nav-link" href="LogoutServlet">Log Out</a>
                    </nav>
                    
                    <%
                        }else if("Employee".equals(role)){
                    %>
                    <nav class="nav flex-column bg-light p-3 rounded shadow-sm">
                        <a class="nav-link active" aria-current="page" href="homeEmployee.jsp">Home</a>
                        <a class="nav-link" href="PatientDisplayServlet">Patients</a>
                        <a class="nav-link" href="AppointmentDisplayServlet">Appointments</a>
                        <a class="nav-link" href="LogoutServlet">Log Out</a>
                    </nav>
                    
                    <%
                        }else if("Dentist".equals(role)){
                    %>
                    <nav class="nav flex-column bg-light p-3 rounded shadow-sm">
                        <a class="nav-link active" aria-current="page" href="homeDentist.jsp">Home</a>
                        <a class="nav-link" href="PatientDisplayServlet">Patients</a>
                        <a class="nav-link" href="AppointmentDisplayServlet">Appointments</a>
                        <a class="nav-link" href="TreatmentDisplayServlet">Treatments</a>
                        <a class="nav-link" href="LogoutServlet">Log Out</a>
                    </nav>
                    <%
                        }else { //NEED TANGGALIN SOON
                    %>
                    <nav class="nav flex-column bg-light p-3 rounded shadow-sm">
                        <a class="nav-link active" aria-current="page" href="homeAdmin.jsp">Home (BACKUP)</a>
                        <a class="nav-link" href="UsersDisplayServlet">Users</a>
                        <a class="nav-link" href="DentistDisplayServlet">Dentists</a>
                        <a class="nav-link" href="PatientDisplayServlet">Patients</a>
                        <a class="nav-link" href="AppointmentDisplayServlet">Appointments</a>
                        <a class="nav-link" href="TreatmentDisplayServlet">Treatments</a>
                        <a class="nav-link" href="LogsDisplayServlet">Logs</a>
                    </nav>
                    <%
                        }
                    %>
                </div>
                <div class="col-md-9 col-12">
                    <!-- Content area -->
                    <div class="p-3 border rounded">
                        <div class="welcome-section">
                            <h1><i class="fas fa-user-tie me-2"></i>Employee Dashboard</h1>
                            <hr class="my-3"> <!-- Added divider line here -->
                            <h2>WELCOME, <%= session.getAttribute("username").toString().toUpperCase() %>!</h2>
                            <p class="mb-0">Select a link on the left to navigate.</p>
                        </div>
                        
                        <!-- Image with click-to-enlarge functionality -->
                        <div class="mt-4" style="max-height: 500px; overflow-y: auto;">
                            <img src="https://scontent.fmnl4-6.fna.fbcdn.net/v/t39.30808-6/502847435_4256900654538388_3658067917631933225_n.jpg?_nc_cat=111&ccb=1-7&_nc_sid=127cfc&_nc_eui2=AeGSxzLZMY_lKM3mxc4HkCRFHrNR_T0ZCYAes1H9PRkJgJTVi-0qMq1mBR-8YaGspGCAVwH4SWl6D6nCyOQIpMZP&_nc_ohc=ra-jfX9It9AQ7kNvwFG-1fa&_nc_oc=Adk3OYYb5ze76jaLmDr3WhKEqkg18lUhCS59T99FcL1IjuKTycnnGraDP7Ui1O5KVPE&_nc_zt=23&_nc_ht=scontent.fmnl4-6.fna&_nc_gid=u8PenAorP1y8clTZhICc4A&oh=00_AfKRnBSK4cXutE1FfHUdGWgmBz3e6wkXAwm-PFxcQx0LpA&oe=684056B4" 
                                 alt="Dental Clinic Image" 
                                 class="img-fluid rounded"
                                 style="width: 100%; height: auto; cursor: pointer;"
                                 data-bs-toggle="modal" 
                                 data-bs-target="#imageModal">
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Image Modal -->
        <div class="modal fade" id="imageModal" tabindex="-1" aria-labelledby="imageModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="imageModalLabel">Dental Clinic</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body text-center">
                        <img src="https://scontent.fmnl4-6.fna.fbcdn.net/v/t39.30808-6/502847435_4256900654538388_3658067917631933225_n.jpg?_nc_cat=111&ccb=1-7&_nc_sid=127cfc&_nc_eui2=AeGSxzLZMY_lKM3mxc4HkCRFHrNR_T0ZCYAes1H9PRkJgJTVi-0qMq1mBR-8YaGspGCAVwH4SWl6D6nCyOQIpMZP&_nc_ohc=ra-jfX9It9AQ7kNvwFG-1fa&_nc_oc=Adk3OYYb5ze76jaLmDr3WhKEqkg18lUhCS59T99FcL1IjuKTycnnGraDP7Ui1O5KVPE&_nc_zt=23&_nc_ht=scontent.fmnl4-6.fna&_nc_gid=u8PenAorP1y8clTZhICc4A&oh=00_AfKRnBSK4cXutE1FfHUdGWgmBz3e6wkXAwm-PFxcQx0LpA&oe=684056B4" 
                             alt="Dental Clinic Image" 
                             class="img-fluid">
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
    </body>
</html>