<%@ page import="java.util.*" %>
<%@ page import="model.SecurityServlet" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Edit Dentist</title>
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
            <h1 class="mb-4"><i class="fas fa-user-md me-2"></i>Edit Dentist Record</h1>

            <%
              ArrayList<String[]> records = (ArrayList<String[]>) request.getAttribute("records");
              if (records != null && records.size() > 0) {
                  String[] dentist = records.get(0);
            %>

            <form action="DentistUpdateServlet" method="post">
                <div class="mb-3">
                    <label for="recordUserID" class="form-label">User ID</label>
                    <input type="text" class="form-control" id="recordUserID" name="user_id" disabled value="<%= dentist[0] %>">
                </div>
                
                <div class="mb-3">
                    <label for="fullname" class="form-label">Full Name</label>
                    <input type="text" class="form-control" name="fullname" id="fullname" value="<%= dentist[1] %>">
                </div>

                <div class="mb-3">
                    <label for="contact_information" class="form-label">Contact Information</label>
                    <input type="text" class="form-control" name="contact_information" id="contact_information" value="<%= dentist[2] %>">
                </div>

                <div class="mb-3">
                    <label for="specialization" class="form-label">Specialization</label>
                    <input type="text" class="form-control" name="specialization" id="specialization" value="<%= dentist[3] %>">
                </div>

                <div class="mb-3">
                    <label for="email" class="form-label">Email</label>
                    <input type="email" class="form-control" name="email" id="email" value="<%= dentist[4] %>">
                </div>
                
                <div class="mb-3">
                    <label for="username" class="form-label">Username</label>
                    <input type="text" class="form-control" name="username" id="username" value="<%= dentist[5] %>">
                </div>
                
                <div class="mb-3">
                    <label for="password" class="form-label">Password</label>
                    <input type="text" class="form-control" name="password" id="password" value="<%= SecurityServlet.decrypt(dentist[6]) %>">
                </div>

                <div class="mb-3">
                    <label for="recordRole" class="form-label">Role</label>
                    <input type="text" class="form-control" name="recordRole" id="recordRole" value="<%= dentist[7] %>" disabled>
                </div>
                
                <div class="mb-3">
                    <input type="hidden" class="form-control" name="old_name" id="old_name" value="<%= dentist[1] %>">
                </div>

                <div class="d-flex justify-content-end mt-4">
                    <a href="DentistDisplayServlet" class="btn btn-secondary me-2">Cancel</a>
                    <button type="submit" class="btn btn-primary">Save Changes</button>
                </div>
            </form>

            <%
              } else {
            %>
            <div class="alert alert-warning">No dentist data found.</div>
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

                showAlert(alertType, title, alertMessage, 'DentistDisplayServlet');

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