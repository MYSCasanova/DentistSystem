<%@ page import = "java.util.*"%>
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
    <title>Logs</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="css/styles.css">
    <style>
        /* Additional styles for table responsiveness */
        .table-container {
            width: 100%;
            overflow-x: auto;
        }
        .table {
            min-width: 100%;
            white-space: nowrap;
        }
        .table th, .table td {
            padding: 12px 15px;
            vertical-align: middle;
        }
        .table th {
            position: sticky;
            top: 0;
            background-color: #003366;
            color: white;
        }
        .sort-button {
            background: none;
            border: none;
            color: white;
            padding: 0;
            font-weight: 600;
        }
        .sort-button:hover {
            color: #d9ecff;
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
                        }
                    %>
                </div>
            <div class="col-md-9 col-12">
                <!-- Content area -->
                <div class="p-3 border rounded">
                    <!-- Page-specific content gets loaded here when you navigate -->
                    <div class="welcome-section mb-3">
                        <h1 class="mb-0">LOGS</h1>
                    </div>
                    
                    <div class="action-bar mb-3">
                        <div class="action-buttons">
                            <!-- No buttons needed for logs -->
                        </div>

                        <form method="get" action="LogsDisplayServlet" class="search-form">
                            <div class="input-group">
                                <input type="text" name="search" class="form-control" placeholder="Search...">
                                <button type="submit" name="sub" class="btn btn-success">
                                    <i class="fas fa-search"></i>
                                </button>
                            </div>
                        </form>
                    </div>
                    
                    <div class="table-container">
                        <%
                            //arraylist that will receive the request attribute records from the Display Servlet
                            ArrayList <String[]> records = (ArrayList <String[]>) request.getAttribute("records");
                        %>

                        <table class="table table-hover table-striped">
                            <thead>
                                <tr>
                                    <th>
                                        <%
                                            String currentSortField = request.getParameter("sortField");
                                            String currentSortOrder = request.getParameter("sortOrder");
                                            boolean isThisColumnSorted = "l.log_id".equals(currentSortField);
                                            String newSortOrder = "asc".equals(currentSortOrder) && isThisColumnSorted ? "desc" : "asc";
                                            String caretClass = isThisColumnSorted ? ("asc".equals(currentSortOrder) ? "fa-caret-up" : "fa-caret-down") : "fa-caret-down";
                                        %>

                                        <form action="LogsDisplayServlet" method="get" style="display: inline;">
                                            <input type="hidden" name="sortField" value="l.log_id">
                                            <input type="hidden" name="sortOrder" value="<%= newSortOrder %>">
                                            <button type="submit" class="sort-button">
                                                Log ID <i class="fa <%= caretClass %>" aria-hidden="true"></i>
                                            </button>
                                        </form>
                                    </th>
                                    <th>
                                        <%
                                            currentSortField = request.getParameter("sortField");
                                            currentSortOrder = request.getParameter("sortOrder");
                                            isThisColumnSorted = "u.full_name".equals(currentSortField);
                                            newSortOrder = "asc".equals(currentSortOrder) && isThisColumnSorted ? "desc" : "asc";
                                            caretClass = isThisColumnSorted ? ("asc".equals(currentSortOrder) ? "fa-caret-up" : "fa-caret-down") : "fa-caret-down";
                                        %>

                                        <form action="LogsDisplayServlet" method="get" style="display: inline;">
                                            <input type="hidden" name="sortField" value="u.full_name">
                                            <input type="hidden" name="sortOrder" value="<%= newSortOrder %>">
                                            <button type="submit" class="sort-button">
                                                Full Name <i class="fa <%= caretClass %>" aria-hidden="true"></i>
                                            </button>
                                        </form>
                                    </th>
                                    <th>
                                        <%
                                            currentSortField = request.getParameter("sortField");
                                            currentSortOrder = request.getParameter("sortOrder");
                                            isThisColumnSorted = "u.role".equals(currentSortField);
                                            newSortOrder = "asc".equals(currentSortOrder) && isThisColumnSorted ? "desc" : "asc";
                                            caretClass = isThisColumnSorted ? ("asc".equals(currentSortOrder) ? "fa-caret-up" : "fa-caret-down") : "fa-caret-down";
                                        %>

                                        <form action="LogsDisplayServlet" method="get" style="display: inline;">
                                            <input type="hidden" name="sortField" value="u.role">
                                            <input type="hidden" name="sortOrder" value="<%= newSortOrder %>">
                                            <button type="submit" class="sort-button">
                                                Role <i class="fa <%= caretClass %>" aria-hidden="true"></i>
                                            </button>
                                        </form>
                                    </th>
                                    <th>
                                        <%
                                            currentSortField = request.getParameter("sortField");
                                            currentSortOrder = request.getParameter("sortOrder");
                                            isThisColumnSorted = "l.action".equals(currentSortField);
                                            newSortOrder = "asc".equals(currentSortOrder) && isThisColumnSorted ? "desc" : "asc";
                                            caretClass = isThisColumnSorted ? ("asc".equals(currentSortOrder) ? "fa-caret-up" : "fa-caret-down") : "fa-caret-down";
                                        %>

                                        <form action="LogsDisplayServlet" method="get" style="display: inline;">
                                            <input type="hidden" name="sortField" value="l.action">
                                            <input type="hidden" name="sortOrder" value="<%= newSortOrder %>">
                                            <button type="submit" class="sort-button">
                                                Action <i class="fa <%= caretClass %>" aria-hidden="true"></i>
                                            </button>
                                        </form>
                                    </th>
                                    <th>
                                        <%
                                            currentSortField = request.getParameter("sortField");
                                            currentSortOrder = request.getParameter("sortOrder");
                                            isThisColumnSorted = "l.date_time".equals(currentSortField);
                                            newSortOrder = "asc".equals(currentSortOrder) && isThisColumnSorted ? "desc" : "asc";
                                            caretClass = isThisColumnSorted ? ("asc".equals(currentSortOrder) ? "fa-caret-up" : "fa-caret-down") : "fa-caret-down";
                                        %>

                                        <form action="LogsDisplayServlet" method="get" style="display: inline;">
                                            <input type="hidden" name="sortField" value="l.date_time">
                                            <input type="hidden" name="sortOrder" value="<%= newSortOrder %>">
                                            <button type="submit" class="sort-button">
                                                Date and Time <i class="fa <%= caretClass %>" aria-hidden="true"></i>
                                            </button>
                                        </form>
                                    </th>
                                </tr>
                            </thead>
                            <tbody>
                                <%
                                    for(String[] field:records){
                                %>
                                <tr>
                                    <td><%= field[0] %></td>
                                    <td><%= field[1] %></td>
                                    <td><%= field[2] %></td>
                                    <td><%= field[3] %></td>
                                    <td><%= field[4] %></td>
                                </tr>
                                <%
                                    }
                                %>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
      
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
  </body>
</html>