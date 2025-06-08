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
    <title>Treatment</title>
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
                    String role = (String) session.getAttribute("role");
                    System.out.println(role);
                    //ADMIN
                    if ("Admin".equals(role)) {
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
                } else if ("Employee".equals(role)) {
                %>
                <nav class="nav flex-column bg-light p-3 rounded shadow-sm">
                    <a class="nav-link active" aria-current="page" href="homeEmployee.jsp">Home</a>
                    <a class="nav-link" href="PatientDisplayServlet">Patients</a>
                    <a class="nav-link" href="AppointmentDisplayServlet">Appointments</a>
                    <a class="nav-link" href="LogoutServlet">Log Out</a>
                </nav>

                <%
                } else if ("Dentist".equals(role)) {
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
                        <h1 class="mb-0">TREATMENTS</h1>
                    </div>
                    
                    <div class="action-bar mb-3">
                        <div class="action-buttons">
                            <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#addRecordModal">
                                <i class="fas fa-plus"></i> Add Record
                            </button>
                            <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#editRecordModal">
                                <i class="fas fa-edit"></i> Edit Record
                            </button>
                        </div>

                        <form method="get" action="TreatmentDisplayServlet" class="search-form">
                            <div class="input-group">
                                <input type="text" name="search" class="form-control" placeholder="Search...">
                                <button type="submit" name="sub" class="btn btn-success">
                                    <i class="fas fa-search"></i>
                                </button>
                            </div>
                        </form>
                    </div>
                    
                    <!-- Add Record Modal -->
                    <div class="modal fade" id="addRecordModal" tabindex="-1" aria-labelledby="addRecordModalLabel" aria-hidden="true">
                        <div class="modal-dialog modal-dialog-centered">
                            <div class="modal-content">
                                <div class="modal-header bg-primary text-white">
                                    <h5 class="modal-title" id="addRecordModalLabel">
                                        <i class="fas fa-plus-circle me-2"></i>Add New Treatment
                                    </h5>
                                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                                </div>

                                <form action="TreatmentAddServlet" method="post">
                                    <div class="modal-body">
                                        <!-- PATIENT ID -->
                                        <div class="mb-3">
                                            <label for="recordID" class="form-label fw-semibold">Patient ID</label>
                                            <div class="input-group">
                                                <span class="input-group-text bg-light">
                                                    <i class="fas fa-user text-primary"></i>
                                                </span>
                                                <input type="text" class="form-control" id="recordID" name="patientID" required>
                                            </div>
                                        </div>

                                        <!-- DENTIST -->
                                        <div class="mb-3">
                                            <label for="recordDentistID" class="form-label fw-semibold">Dentist</label>
                                            <div class="input-group">
                                                <span class="input-group-text bg-light">
                                                    <i class="fas fa-user-md text-primary"></i>
                                                </span>
                                                <select class="form-select" id="recordDentistID" name="dentistName" required>
                                                    <option value="" disabled selected>Select dentist</option>
                                                    <%
                                                        ArrayList<String> dentists = (ArrayList<String>) request.getAttribute("dentists");
                                                        if (dentists != null) {
                                                            for (String d : dentists) {
                                                    %>
                                                    <option value="<%= d%>"><%= d%></option>
                                                    <%
                                                            }
                                                        }
                                                    %>
                                                </select>
                                            </div>
                                        </div>

                                        <!-- TREATMENT TYPE -->
                                        <div class="mb-3">
                                            <label for="recordType" class="form-label fw-semibold">Treatment Type</label>
                                            <div class="input-group">
                                                <span class="input-group-text bg-light">
                                                    <i class="fas fa-notes-medical text-primary"></i>
                                                </span>
                                                <input type="text" class="form-control" id="recordType" name="treatmentType" required>
                                            </div>
                                        </div>

                                        <!-- TREATMENT DETAILS -->
                                        <div class="mb-3">
                                            <label for="recordDetails" class="form-label fw-semibold">Treatment Details</label>
                                            <div class="input-group">
                                                <span class="input-group-text bg-light">
                                                    <i class="fas fa-info-circle text-primary"></i>
                                                </span>
                                                <input type="text" class="form-control" id="recordDetails" name="treatmentDetails" required>
                                            </div>
                                        </div>

                                        <!-- TREATMENT DATE -->
                                        <div class="mb-3">
                                            <label for="recordTreatDate" class="form-label fw-semibold">Treatment Date</label>
                                            <div class="input-group">
                                                <span class="input-group-text bg-light">
                                                    <i class="fas fa-calendar-alt text-primary"></i>
                                                </span>
                                                <input type="datetime-local" class="form-control" id="recordTreatDate" name="treatmentDate" required>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="modal-footer border-top-0">
                                        <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">
                                            <i class="fas fa-times me-1"></i> Cancel
                                        </button>
                                        <button type="submit" class="btn btn-primary">
                                            <i class="fas fa-save me-1"></i> Add Treatment
                                        </button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                    
                    
                    <%--
                    <!-- Add Record Modal -->
                    <div class="modal fade" id="addRecordModal" tabindex="-1" aria-labelledby="addRecordModalLabel" aria-hidden="true">
                        <div class="modal-dialog">
                            <form action="TreatmentAddServlet" method="post"> 
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h5 class="modal-title" id="addRecordModalLabel">Add New Record</h5>
                                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                    </div>

                                    <div class="modal-body">
                                        <!-- PATIENT ID -->
                                        <div class="mb-3">
                                            <label for="recordID" class="form-label">Patient ID</label>
                                            <input type="text" class="form-control" id="recordID" name="patientID" required>
                                        </div>
                                        
                                        <!-- DENTIST -->
                                        <div class="mb-3">
                                            <label for="recordDentistID" class="form-label">Dentist</label>
                                            <select class="form-select" id="recordDentistID" name="dentistName" required>
                                                <option value="" disabled selected>Select dentist</option>
                                                <%
                                                    ArrayList<String> dentists = (ArrayList<String>) request.getAttribute("dentists");
                                                    if (dentists != null) {
                                                        for (String d : dentists) {
                                                %>
                                                <option value="<%= d%>"><%= d%></option>
                                                <%
                                                        }
                                                    }
                                                %>
                                            </select>
                                        </div>
                                            
                                        <!-- TREATMENT TYPE -->
                                        <div class="mb-3">
                                            <label for="recordType" class="form-label">Treatment Type</label>
                                            <input type="text" class="form-control" id="recordType" name="treatmentType" required>
                                        </div>
                                        
                                        <!-- TREATMENT DETAILS -->
                                        <div class="mb-3">
                                            <label for="recordDetails" class="form-label">Treatment Details</label>
                                            <input type="text" class="form-control" id="recordDetails" name="treatmentDetails" required>
                                        </div>
                                        
                                        <!-- TREATMENT DATE -->
                                        <div class="mb-3">
                                            <label for="recordAppDate" class="form-label">Treatment Date</label>
                                            <input type="datetime-local" class="form-control" id="recordTreatDate" name="treatmentDate" required>
                                        </div>
                                    </div>

                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                        <button type="submit" class="btn btn-success">Save Record</button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div> --%>
                    
                                            
                    <%--                        
                    <!-- Edit Record Modal -->
                    <div class="modal fade" id="editRecordModal" tabindex="-1" aria-labelledby="editRecordModalLabel" aria-hidden="true">
                        <div class="modal-dialog">
                            <form action="TreatmentEditServlet" method="post">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h5 class="modal-title" id="editRecordModalLabel">Search the treatment ID of the treatment record you want to edit</h5>
                                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                    </div>

                                    <div class="modal-body">
                                        <div class="mb-3">
                                            <label for="recordID" class="form-label">Treatment ID</label>
                                            <input type="text" class="form-control" id="recordID" name="recordID" required>
                                        </div>
                                    </div>

                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                        <button type="submit" class="btn btn-success">Search</button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                    --%>
                    
                    <!-- Edit Record Modal -->
                    <div class="modal fade" id="editRecordModal" tabindex="-1" aria-labelledby="editRecordModalLabel" aria-hidden="true">
                        <div class="modal-dialog modal-dialog-centered">
                            <div class="modal-content">
                                <div class="modal-header bg-primary text-white">
                                    <h5 class="modal-title" id="editRecordModalLabel">
                                        <i class="fas fa-search me-2"></i>Search Treatment to Edit
                                    </h5>
                                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                                </div>

                                <form action="TreatmentEditServlet" method="post">
                                    <div class="modal-body">
                                        <div class="mb-3">
                                            <label for="recordID" class="form-label fw-semibold">Treatment Record ID</label>
                                            <div class="input-group">
                                                <span class="input-group-text bg-light">
                                                    <i class="fas fa-id-card text-primary"></i>
                                                </span>
                                                <input type="text" class="form-control" id="recordID" name="recordID" 
                                                       placeholder="Enter treatment record ID" required
                                                       pattern="[0-9]+" title="Please enter a valid numeric ID">
                                            </div>
                                            <div class="form-text text-muted">
                                                Enter the ID of the treatment record you want to edit
                                            </div>
                                        </div>
                                    </div>

                                    <div class="modal-footer border-top-0">
                                        <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">
                                            <i class="fas fa-times me-1"></i> Cancel
                                        </button>
                                        <button type="submit" class="btn btn-primary">
                                            <i class="fas fa-search me-1"></i> Search Treatment
                                        </button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                                            
                    <div class="table-container">
                        <%
                            ArrayList <String[]> records = (ArrayList <String[]>) request.getAttribute("records");
                        %>

                        <table class="table table-hover table-striped">
                            <thead>
                                <tr>
                                    <th>
                                        <%
                                            String currentSortField = request.getParameter("sortField");
                                            String currentSortOrder = request.getParameter("sortOrder");
                                            boolean isThisColumnSorted = "t.record_id".equals(currentSortField);
                                            String newSortOrder = "asc".equals(currentSortOrder) && isThisColumnSorted ? "desc" : "asc";
                                            String caretClass = isThisColumnSorted ? ("asc".equals(currentSortOrder) ? "fa-caret-up" : "fa-caret-down") : "fa-caret-down";
                                        %>

                                        <form action="TreatmentDisplayServlet" method="get" style="display: inline;">
                                            <input type="hidden" name="sortField" value="t.record_id">
                                            <input type="hidden" name="sortOrder" value="<%= newSortOrder %>">
                                            <button type="submit" class="sort-button">
                                                Record ID <i class="fa <%= caretClass %>" aria-hidden="true"></i>
                                            </button>
                                        </form>
                                    </th>
                                    <th>
                                        <%
                                            currentSortField = request.getParameter("sortField");
                                            currentSortOrder = request.getParameter("sortOrder");
                                            isThisColumnSorted = "p.patient_name".equals(currentSortField);
                                            newSortOrder = "asc".equals(currentSortOrder) && isThisColumnSorted ? "desc" : "asc";
                                            caretClass = isThisColumnSorted ? ("asc".equals(currentSortOrder) ? "fa-caret-up" : "fa-caret-down") : "fa-caret-down";
                                        %>

                                        <form action="TreatmentDisplayServlet" method="get" style="display: inline;">
                                            <input type="hidden" name="sortField" value="p.patient_name">
                                            <input type="hidden" name="sortOrder" value="<%= newSortOrder %>">
                                            <button type="submit" class="sort-button">
                                                Patient Name <i class="fa <%= caretClass %>" aria-hidden="true"></i>
                                            </button>
                                        </form>
                                    </th>
                                    <th>
                                        <%
                                            currentSortField = request.getParameter("sortField");
                                            currentSortOrder = request.getParameter("sortOrder");
                                            isThisColumnSorted = "d.dentist_name".equals(currentSortField);
                                            newSortOrder = "asc".equals(currentSortOrder) && isThisColumnSorted ? "desc" : "asc";
                                            caretClass = isThisColumnSorted ? ("asc".equals(currentSortOrder) ? "fa-caret-up" : "fa-caret-down") : "fa-caret-down";
                                        %>

                                        <form action="TreatmentDisplayServlet" method="get" style="display: inline;">
                                            <input type="hidden" name="sortField" value="d.dentist_name">
                                            <input type="hidden" name="sortOrder" value="<%= newSortOrder %>">
                                            <button type="submit" class="sort-button">
                                                Dentist Name <i class="fa <%= caretClass %>" aria-hidden="true"></i>
                                            </button>
                                        </form>
                                    </th>
                                    <th>
                                        <%
                                            currentSortField = request.getParameter("sortField");
                                            currentSortOrder = request.getParameter("sortOrder");
                                            isThisColumnSorted = "d.specialization".equals(currentSortField);
                                            newSortOrder = "asc".equals(currentSortOrder) && isThisColumnSorted ? "desc" : "asc";
                                            caretClass = isThisColumnSorted ? ("asc".equals(currentSortOrder) ? "fa-caret-up" : "fa-caret-down") : "fa-caret-down";
                                        %>

                                        <form action="TreatmentDisplayServlet" method="get" style="display: inline;">
                                            <input type="hidden" name="sortField" value="d.specialization">
                                            <input type="hidden" name="sortOrder" value="<%= newSortOrder %>">
                                            <button type="submit" class="sort-button">
                                                Dentist Specialization <i class="fa <%= caretClass %>" aria-hidden="true"></i>
                                            </button>
                                        </form>
                                    </th>
                                    <th>
                                        <%
                                            currentSortField = request.getParameter("sortField");
                                            currentSortOrder = request.getParameter("sortOrder");
                                            isThisColumnSorted = "t.treatment_type".equals(currentSortField);
                                            newSortOrder = "asc".equals(currentSortOrder) && isThisColumnSorted ? "desc" : "asc";
                                            caretClass = isThisColumnSorted ? ("asc".equals(currentSortOrder) ? "fa-caret-up" : "fa-caret-down") : "fa-caret-down";
                                        %>

                                        <form action="TreatmentDisplayServlet" method="get" style="display: inline;">
                                            <input type="hidden" name="sortField" value="t.treatment_type">
                                            <input type="hidden" name="sortOrder" value="<%= newSortOrder %>">
                                            <button type="submit" class="sort-button">
                                                Treatment Type <i class="fa <%= caretClass %>" aria-hidden="true"></i>
                                            </button>
                                        </form>
                                    </th>
                                    <th>
                                        <%
                                            currentSortField = request.getParameter("sortField");
                                            currentSortOrder = request.getParameter("sortOrder");
                                            isThisColumnSorted = "t.treatment_details".equals(currentSortField);
                                            newSortOrder = "asc".equals(currentSortOrder) && isThisColumnSorted ? "desc" : "asc";
                                            caretClass = isThisColumnSorted ? ("asc".equals(currentSortOrder) ? "fa-caret-up" : "fa-caret-down") : "fa-caret-down";
                                        %>

                                        <form action="TreatmentDisplayServlet" method="get" style="display: inline;">
                                            <input type="hidden" name="sortField" value="t.treatment_details">
                                            <input type="hidden" name="sortOrder" value="<%= newSortOrder %>">
                                            <button type="submit" class="sort-button">
                                                Treatment Details <i class="fa <%= caretClass %>" aria-hidden="true"></i>
                                            </button>
                                        </form>
                                    </th>
                                    <th>
                                        <%
                                            currentSortField = request.getParameter("sortField");
                                            currentSortOrder = request.getParameter("sortOrder");
                                            isThisColumnSorted = "t.treatment_date".equals(currentSortField);
                                            newSortOrder = "asc".equals(currentSortOrder) && isThisColumnSorted ? "desc" : "asc";
                                            caretClass = isThisColumnSorted ? ("asc".equals(currentSortOrder) ? "fa-caret-up" : "fa-caret-down") : "fa-caret-down";
                                        %>

                                        <form action="TreatmentDisplayServlet" method="get" style="display: inline;">
                                            <input type="hidden" name="sortField" value="t.treatment_date">
                                            <input type="hidden" name="sortOrder" value="<%= newSortOrder %>">
                                            <button type="submit" class="sort-button">
                                                Treatment Date <i class="fa <%= caretClass %>" aria-hidden="true"></i>
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
                                    <td><%= field[5]%></td>
                                    <td><%= field[6]%></td>
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
  
     
    <!-- Custom Alert Modal -->
    <div id="alertModalOverlay" class="alert-modal-overlay"></div>
    <div id="alertModal" class="alert-modal">
        <div class="alert-modal-icon">
            <i id="alertModalIcon" class="fas"></i>
        </div>
        <h3 id="alertModalTitle" class="alert-modal-title"></h3>
        <p id="alertModalMessage" class="alert-modal-message"></p>
        <button id="alertModalBtn" class="alert-modal-btn">OK</button>
    </div>
        <script>
        // Custom alert function
        function showAlert(type, title, message, redirectUrl) {
            const modal = document.getElementById('alertModal');
            const overlay = document.getElementById('alertModalOverlay');
            const icon = document.getElementById('alertModalIcon');
            const modalTitle = document.getElementById('alertModalTitle');
            const modalMessage = document.getElementById('alertModalMessage');
            const btn = document.getElementById('alertModalBtn');

            // Set modal content based on type
            if (type === 'success') {
                modal.classList.add('alert-modal-success');
                modal.classList.remove('alert-modal-error');
                icon.className = 'fas fa-check-circle';
            } else {
                modal.classList.add('alert-modal-error');
                modal.classList.remove('alert-modal-success');
                icon.className = 'fas fa-exclamation-circle';
            }

            modalTitle.textContent = title;
            modalMessage.textContent = message;

            // Show modal
            modal.style.display = 'block';
            overlay.style.display = 'block';

            // Button click handler
            btn.onclick = function() {
                modal.style.display = 'none';
                overlay.style.display = 'none';
                if (redirectUrl) {
                    window.location.href = redirectUrl;
                }
            };

            // Close when clicking overlay
            overlay.onclick = function() {
                modal.style.display = 'none';
                overlay.style.display = 'none';
                if (redirectUrl) {
                    window.location.href = redirectUrl;
                }
            };
        }

        // Function to check for URL parameters and show alerts
        function checkForAlerts() {
            const urlParams = new URLSearchParams(window.location.search);
            const alertType = urlParams.get('alert');
            const alertMessage = urlParams.get('message');

            if (alertType && alertMessage) {
                let title = '';
                if (alertType === 'success') {
                    title = 'Success!';
                } else {
                    title = 'Error!';
                }

                showAlert(alertType, title, alertMessage, 'TreatmentDisplayServlet');

                // Clean up URL
                const cleanUrl = window.location.pathname;
                window.history.replaceState({}, document.title, cleanUrl);
            }
        }

        // Initialize on page load
        document.addEventListener('DOMContentLoaded', checkForAlerts);
    </script>
  
  
  
  </body>
</html>