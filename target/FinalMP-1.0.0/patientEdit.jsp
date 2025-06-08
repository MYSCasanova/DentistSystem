<%@ page import="java.util.*" %>
<%@ page import="model.SecurityServlet" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Edit Patient</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="css/styles.css">
    <style>
        .form-container {
            background-color: #ffffff;
            border-radius: 12px;
            box-shadow: 0 0 12px rgba(0, 0, 0, 0.05);
            padding: 2rem;
            margin-top: 2rem;
        }
        .form-label {
            font-weight: 500;
            color: #003366;
        }
        .form-control, .form-select {
            border-radius: 8px;
            padding: 0.75rem 1rem;
            border: 1px solid #d9ecff;
        }
        .form-control:focus, .form-select:focus {
            border-color: #1e3bb8;
            box-shadow: 0 0 0 0.25rem rgba(30, 59, 184, 0.25);
        }
        .btn {
            border-radius: 8px;
            font-weight: 500;
            padding: 0.75rem 1.5rem;
        }
        .btn-primary {
            background-color: #1e3bb8;
            border-color: #1e3bb8;
        }
        .btn-primary:hover {
            background-color: #163488;
            border-color: #163488;
        }
        .btn-secondary {
            background-color: #6c757d;
            border-color: #6c757d;
        }
        .btn-secondary:hover {
            background-color: #5a6268;
            border-color: #545b62;
        }
        body {
            background-color: #f8f9fa;
        }
    </style>
  </head>
  <body>
    <div class="container mt-4">
        <div class="form-container mx-auto" style="max-width: 800px;">
            <h1 class="mb-4"><i class="fas fa-user-edit me-2"></i>Edit Patient Record</h1>

            <%
              ArrayList<String[]> records = (ArrayList<String[]>) request.getAttribute("records");
              if (records != null && records.size() > 0) {
                  String[] patient = records.get(0);
            %>

            <form action="PatientUpdateServlet" method="post">
                <div class="mb-3">
                    <label for="patientIDDisplay" class="form-label">Patient ID</label>
                    <input type="text" class="form-control" id="patientIDDisplay" disabled value="<%= patient[0] %>">
                </div>
                
                <input type="hidden" name="patient_id" id="patient_id" value="<%= patient[0] %>">
                
                <div class="mb-3">
                    <label for="patient_name" class="form-label">Patient Name</label>
                    <input type="text" class="form-control" name="patient_name" id="patient_name" value="<%= patient[1] %>">
                </div>

                <div class="mb-3">
                    <label for="recordBirthday" class="form-label">Date of Birth</label>
                    <input type="date" class="form-control" id="recordBirthday" name="date_of_birth" value="<%= patient[2] %>">
                </div>

                <div class="mb-3">
                    <label for="gender" class="form-label">Gender</label>
                    <select class="form-select" id="gender" name="gender" required>
                        <option value="" disabled selected>Select gender</option>
                        <option value="Male" <%= "Male".equals(patient[3]) ? "selected" : "" %>>Male</option>
                        <option value="Female" <%= "Female".equals(patient[3]) ? "selected" : "" %>>Female</option>
                        <option value="Other" <%= "Other".equals(patient[3]) ? "selected" : "" %>>Other</option>
                    </select>
                </div>

                <div class="mb-3">
                    <label for="contact_information" class="form-label">Contact Information</label>
                    <input type="text" class="form-control" name="contact_information" id="contact_information" value="<%= patient[4] %>">
                </div>
                
                <div class="mb-3">
                    <label for="address" class="form-label">Address</label>
                    <input type="text" class="form-control" name="address" id="address" value="<%= patient[5] %>">
                </div>

                <div class="d-flex justify-content-end mt-4">
                    <a href="PatientDisplayServlet" class="btn btn-secondary me-2">Cancel</a>
                    <button type="submit" class="btn btn-primary">Save Changes</button>
                </div>
            </form>

            <%
              } else {
            %>
            <div class="alert alert-warning">No patient data found.</div>
            <%
              }
            %>
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

                showAlert(alertType, title, alertMessage, 'PatientDisplayServlet');

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