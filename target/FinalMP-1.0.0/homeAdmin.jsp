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
        <title>Admin Page</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
        <link rel="stylesheet" href="css/styles.css">
        
        <style>
            :root {
                --primary-color: #4e73df;
                --secondary-color: #1cc88a;
                --danger-color: #e74a3b;
                --warning-color: #f6c23e;
                --info-color: #36b9cc;
            }
            
            body {
                font-family: 'Poppins', sans-serif;
                background-color: #f8f9fc;
            }
            
            .nav-link {
                display: block;
                color: #003366;
                font-weight: 500;
                padding: 0.5rem 1rem;
                margin-bottom: 0.25rem;
                text-decoration: none;
                border-radius: 8px;
                transition: all 0.3s ease;
                position: relative;
            }

            .nav-link:hover {
                background-color: #e0edff;
                color: #001f3f;
                transform: translateX(5px);
                box-shadow: 3px 3px 8px rgba(0, 0, 0, 0.1);
            }

            .nav-link:hover::before {
                content: '';
                position: absolute;
                left: -5px;
                top: 0;
                height: 100%;
                width: 5px;
                background-color: #1e3bb8;
                border-radius: 8px 0 0 8px;
            }

            .nav-link.active {
                background-color: #e0edff;
                color: #001f3f;
                box-shadow: inset 3px 0 0 #1e3bb8;
                font-weight: 600;
            }
            
            .welcome-section {
                background-color: #e6f2ff; /* light blue */
                padding: 1.5rem;
                border-radius: 12px;
                margin-top: 1.5rem;
                box-shadow: 0 0 10px rgba(0, 64, 128, 0.05);
            }
            
            .stat-card {
                border-radius: 0.35rem;
                box-shadow: 0 0.15rem 1.75rem 0 rgba(58, 59, 69, 0.1);
                transition: transform 0.3s, box-shadow 0.3s;
                border-left: 0.25rem solid;
                height: 100%;
            }
            
            .stat-card:hover {
                transform: translateY(-5px);
                box-shadow: 0 0.5rem 1.5rem 0 rgba(58, 59, 69, 0.2);
            }
            
            .stat-card-primary {
                border-left-color: var(--primary-color);
            }
            
            .stat-card-success {
                border-left-color: var(--secondary-color);
            }
            
            .stat-card-danger {
                border-left-color: var(--danger-color);
            }
            
            .stat-card-warning {
                border-left-color: var(--warning-color);
            }
            
            .stat-icon {
                font-size: 2rem;
                opacity: 0.3;
            }
            
            .stat-value {
                font-size: 1.5rem;
                font-weight: 700;
            }
            
            .stat-title {
                font-size: 0.9rem;
                text-transform: uppercase;
                font-weight: 600;
                color: #5a5c69;
            }
            
            .section-title {
                color: #4e73df;
                font-weight: 600;
                border-bottom: 2px solid #eaecf4;
                padding-bottom: 0.5rem;
                margin-bottom: 1.5rem;
            }
            
            .report-section {
                background-color: white;
                padding: 1.5rem;
                border-radius: 0.35rem;
                box-shadow: 0 0.15rem 1.75rem 0 rgba(58, 59, 69, 0.15);
                margin-top: 1.5rem;
            }
        </style>
        
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
                            <h1><i class="fas fa-user-shield me-2"></i>Admin Dashboard</h1>
                            <hr class="my-3"> <!-- Added divider line here -->
                            <h2>WELCOME, <%= session.getAttribute("username").toString().toUpperCase() %>!</h2>
                            <p class="mb-0">Select a link on the left to navigate.</p>
                        </div>
                        
                        <!-- Weekly Report Section -->
                        <div class="report-section">
                            <h3 class="section-title"><i class="fas fa-calendar-week me-2"></i>Weekly Report</h3>
                            
                            <div class="row g-4">
                                <!-- Appointments Card -->
                                <div class="col-md-6 col-lg-3">
                                    <div class="stat-card stat-card-primary p-3">
                                        <div class="d-flex justify-content-between align-items-center">
                                            <div>
                                                <div class="stat-value">24</div>
                                                <div class="stat-title">Appointments</div>
                                            </div>
                                            <div>
                                                <i class="fas fa-calendar-check stat-icon text-primary"></i>
                                            </div>
                                        </div>
                                        <div class="mt-2 text-end">
                                            <small class="text-muted">+5 from last week</small>
                                        </div>
                                    </div>
                                </div>
                                
                                <!-- Patients Card -->
                                <div class="col-md-6 col-lg-3">
                                    <div class="stat-card stat-card-success p-3">
                                        <div class="d-flex justify-content-between align-items-center">
                                            <div>
                                                <div class="stat-value">18</div>
                                                <div class="stat-title">Patients</div>
                                            </div>
                                            <div>
                                                <i class="fas fa-user-injured stat-icon text-success"></i>
                                            </div>
                                        </div>
                                        <div class="mt-2 text-end">
                                            <small class="text-muted">+3 from last week</small>
                                        </div>
                                    </div>
                                </div>
                                
                                <!-- Treatments Card -->
                                <div class="col-md-6 col-lg-3">
                                    <div class="stat-card stat-card-warning p-3">
                                        <div class="d-flex justify-content-between align-items-center">
                                            <div>
                                                <div class="stat-value">32</div>
                                                <div class="stat-title">Treatments</div>
                                            </div>
                                            <div>
                                                <i class="fas fa-teeth stat-icon text-warning"></i>
                                            </div>
                                        </div>
                                        <div class="mt-2 text-end">
                                            <small class="text-muted">+8 from last week</small>
                                        </div>
                                    </div>
                                </div>
                                
                                <!-- Revenue Card -->
                                <div class="col-md-6 col-lg-3">
                                    <div class="stat-card stat-card-danger p-3">
                                        <div class="d-flex justify-content-between align-items-center">
                                            <div>
                                                <div class="stat-value">$4,850</div>
                                                <div class="stat-title">Revenue</div>
                                            </div>
                                            <div>
                                                <i class="fas fa-dollar-sign stat-icon text-danger"></i>
                                            </div>
                                        </div>
                                        <div class="mt-2 text-end">
                                            <small class="text-muted">+12% from last week</small>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            
                            <div class="row mt-4 g-4">
                                <!-- Busiest Day -->
                                <div class="col-md-6">
                                    <div class="stat-card p-3 h-100">
                                        <h5 class="d-flex align-items-center">
                                            <i class="fas fa-chart-line text-info me-2"></i> Busiest Day
                                        </h5>
                                        <div class="mt-3">
                                            <h4 class="text-center">Wednesday</h4>
                                            <p class="text-center text-muted mb-1">8 appointments</p>
                                            <div class="progress mt-2">
                                                <div class="progress-bar bg-info" role="progressbar" style="width: 75%" aria-valuenow="75" aria-valuemin="0" aria-valuemax="100"></div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                
                                <!-- Most Common Treatment -->
                                <div class="col-md-6">
                                    <div class="stat-card p-3 h-100">
                                        <h5 class="d-flex align-items-center">
                                            <i class="fas fa-tooth text-primary me-2"></i> Most Common Treatment
                                        </h5>
                                        <div class="mt-3">
                                            <h4 class="text-center">Dental Cleaning</h4>
                                            <p class="text-center text-muted mb-1">12 procedures</p>
                                            <div class="progress mt-2">
                                                <div class="progress-bar bg-primary" role="progressbar" style="width: 65%" aria-valuenow="65" aria-valuemin="0" aria-valuemax="100"></div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            
                            <!-- Recent Activity -->
                            <div class="mt-4">
                                <h5 class="d-flex align-items-center">
                                    <i class="fas fa-history me-2"></i> Recent Activity
                                </h5>
                                <ul class="list-group mt-2">
                                    <li class="list-group-item d-flex justify-content-between align-items-center">
                                        <span>Completed treatment for Monica Geller</span>
                                        <small class="text-muted">2 hours ago</small>
                                    </li>
                                    <li class="list-group-item d-flex justify-content-between align-items-center">
                                        <span>Scheduled follow-up for Katniss Everdeen</span>
                                        <small class="text-muted">Yesterday</small>
                                    </li>
                                    <li class="list-group-item d-flex justify-content-between align-items-center">
                                        <span>New patient registration: Joey Tribbiani</span>
                                        <small class="text-muted">2 days ago</small>
                                    </li>
                                </ul>
                            </div>
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
                        <img src="https://scontent-mnl1-2.xx.fbcdn.net/v/t39.30808-6/502993446_4256921274536326_6031397182378396540_n.jpg?_nc_cat=100&ccb=1-7&_nc_sid=127cfc&_nc_eui2=AeGgCkr_7xAxrpB1DkG7Ey09kOEtTjV-8LOQ4S1ONX7ws2Xq6cYJ9_KxgWbf6_PVsfkxHRgC2BXbXt0g0vf28h3u&_nc_ohc=NnAQR0l1Yl8Q7kNvwEe63sx&_nc_oc=AdkS6aQJKJOWXUbeLjMIDU9UmYJhHRH7gDrqvnNrYiB0h-CLoNT1I6dOCIHbrQlEmGQ&_nc_zt=23&_nc_ht=scontent-mnl1-2.xx&_nc_gid=UohYbcXiPConiMFucYuvtQ&oh=00_AfKxfLdQP2iawBElQKVvgkUlnaJw4oi_Q55KNGcoFzh9lw&oe=68406359" 
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