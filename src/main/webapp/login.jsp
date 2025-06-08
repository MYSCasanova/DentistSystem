<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    response.setHeader("Cache-Control","no-cache, no-store, must-revalidate");
    response.setHeader("Pragma","no-cache");
    response.setDateHeader ("Expires", 0);
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login</title>
    <script src="https://www.google.com/recaptcha/enterprise.js" async defer></script>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">

    <style>
        body, html {
            height: 100%;
            margin: 0;
            font-family: 'Poppins', sans-serif;
        }

        .main-container {
            display: flex;
            height: 100vh;
            width: 100%;
        }

        .image-side {
            width: 50%;
            background: url('https://scontent.fmnl8-2.fna.fbcdn.net/v/t39.30808-6/502465234_4255150254713428_398441474377693993_n.jpg?_nc_cat=111&ccb=1-7&_nc_sid=127cfc&_nc_eui2=AeFRex7t6yyVZmOFrlxPBdg_8E84b6YjHQzwTzhvpiMdDHrtKxAAvIs0q1wEBar2uCrtHUHSUVkDZ9AkFpDuox5o&_nc_ohc=HbViuciHsi8Q7kNvwHi7_H5&_nc_oc=Adlcw2RoVvhc_UdO51jOtNiQ5CmWOIG5NyxmcTtm4RMpkHJE3Sqj7R33v7LnwV0HfHI&_nc_zt=23&_nc_ht=scontent.fmnl8-2.fna&_nc_gid=XFkaEVDt7B1RHc-IS-4Y9w&oh=00_AfJTkeySPpClEy2zOU9BeBq36SLpSclwGSwxnUD2Dsldhg&oe=683E0373') no-repeat center center;
            background-size: cover;
        }

        .form-side {
            width: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            /*background-color: #f5f5f5;*/
            background: url('https://scontent.fmnl8-4.fna.fbcdn.net/v/t39.30808-6/500767840_4255316378030149_7907497146272438524_n.jpg?_nc_cat=102&ccb=1-7&_nc_sid=127cfc&_nc_eui2=AeGRZCbevVmFaKu0JCQesBAKrW5_FOwn0mutbn8U7CfSa4D6dnXsY784OXsl128la_jPh7KJEK2WDdiUZ0xc5axR&_nc_ohc=FB5qyi-r8FoQ7kNvwH3PwTE&_nc_oc=AdlD03693IVaKzkjPNh-GxEshkpk-3YyvP8JbbHM_u3ZTF0jyzXK8RdmNXlmwJUgr4M&_nc_zt=23&_nc_ht=scontent.fmnl8-4.fna&_nc_gid=P40seso5u1paJReow2FV_w&oh=00_AfKeijaKqU3Cucpo-h28Y2ryVuwGtMHDkeOPiUyLojAyJA&oe=683E2037') no-repeat center center;
            background-size: cover;
            position: relative;
        }
        
        /* Add an overlay so text stays readable */
        .form-side::before {
            content: "";
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0, 47, 95, 0.6); /* dark blue overlay */
            z-index: 1;
        }

        .login-container {
            position: relative;
            z-index: 2; /* Put form on top of the overlay */
            background: transparent; /* ⬅️ This removes the colored background */
            border-radius: 12px;
            box-shadow: none; /* Optional: Remove drop shadow if you want it fully minimal */
            padding: 2.5rem;
            width: 100%;
            max-width: 400px;
            transition: all 0.3s ease;
            border: none; /* Optional: Remove border */
            color: white; /* Keeps text readable over dark overlay */
        }


        .login-container:hover {
            box-shadow: 0 12px 40px rgba(0, 0, 0, 0.35);
            transform: scale(1.02);
            border: 1px solid #dcefff;
            background: linear-gradient(135deg, #7aa9f9, #0051cc);
            
            /* updating the :hover state to avoid the form changing color:
                .login-container:hover {
                        transform: scale(1.02);
                    }
            */
        }

        .display-1 {
            font-size: 2.5rem;
            font-weight: 600;
            margin-bottom: 1.5rem;
            color: white;
        }

        .user {
            color: #bbe1ff;
            font-weight: 700;
        }

        .form-label {
            color: #cce0ff;
            font-weight: 500;
            margin-bottom: 0.5rem;
        }

        .form-control {
            border: 1px solid #bdd7ff;
            border-radius: 8px;
            padding: 0.75rem 1rem;
            transition: all 0.3s;
            background-color: rgba(255, 255, 255, 0.9);
            color: #004080;
        }

        .form-control:focus {
            border-color: #bbe1ff;
            box-shadow: 0 0 0 0.25rem rgba(187, 225, 255, 0.5);
            background-color: white;
            color: #004080;
        }

        .btn-login {
            background-color: #1e3bb8;
            border: none;
            border-radius: 8px;
            padding: 0.75rem;
            font-weight: 600;
            letter-spacing: 0.5px;
            transition: all 0.3s;
            color: white;
        }

        .btn-login:hover {
            background-color: #163488;
            transform: translateY(-2px);
        }

        .g-recaptcha {
            transform: scale(0.9);
            transform-origin: center;
        }

        .password-toggle-container {
            position: relative;
        }

        .password-toggle-wrapper {
            display: flex;
            align-items: center;
            position: relative;
        }

        .password-toggle-wrapper input {
            flex: 1;
            padding-right: 40px;
        }

        .toggle-password {
            position: absolute;
            right: 10px;
            font-size: 1.25rem;
            color: #cce0ff;
            cursor: pointer;
            transition: transform 0.3s ease, color 0.3s ease;
        }

        .toggle-password:hover {
            color: #99c2ff;
        }

        .toggle-password.animate {
            transform: scale(1.3) rotate(20deg);
        }


        @media (max-width: 768px) {
            .main-container {
                flex-direction: column;
            }

            .image-side {
                display: none;
            }

            .form-side {
                width: 100%;
            }
        }
    </style>
</head>
<body>
    <div class="main-container">
        <div class="image-side"></div>

        <div class="form-side">
            <div class="login-container">
                <h1 class="display-1 text-center mb-4">Welcome Back <span class="user">User</span></h1>
                <form action="LoginServlet" method="post">
                    <div class="mb-4">
                        <label for="username" class="form-label">Username</label>
                        <input type="text" class="form-control" id="username" name="username">
                    </div>
                    <div class="mb-4 password-toggle-container">
                        <label for="password" class="form-label">Password</label>
                        <div class="password-toggle-wrapper">
                            <input type="password" class="form-control" id="password" name="password">
                            <span class="toggle-password" id="togglePassword">👁️</span>
                        </div>
                    </div>
                    <div class="d-grid gap-2 mt-4">
                        <button type="submit" class="btn btn-login text-white">Login</button>
                    </div>
                    <div class="g-recaptcha d-grid gap-2 mt-4 mx-auto"
                        data-sitekey="6Ledee0qAAAAADQmwhNSUtFR7ePJeoti2HF7zlkV"
                        data-action="LOGIN">
                    </div>
                </form>
            </div>
        </div>
    </div>


</body>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>

    <!-- Password toggle script -->
    <script>
        const togglePassword = document.getElementById('togglePassword');
        const passwordInput = document.getElementById('password');

        togglePassword.addEventListener('click', () => {
            const isPassword = passwordInput.getAttribute('type') === 'password';
            passwordInput.setAttribute('type', isPassword ? 'text' : 'password');
            togglePassword.textContent = isPassword ? '🙈' : '👁️';
            
            // Add animation class
            togglePassword.classList.add('animate');
            setTimeout(() => {
                togglePassword.classList.remove('animate');
            }, 300);
        });
    </script>

<%
    String message = (String) request.getAttribute("message");
    if(message != null){
%>
    <script>
        Swal.fire({
            icon: "error",
            title: "Oops...",
            text: "Invalid or Empty Credentials!",
            footer: '<a href="#">Why do I have this issue?</a>'
        }).then(() => {
            if (typeof grecaptcha !== "undefined") {
                grecaptcha.reset();
            }
        });
    </script>
<%
    }
%>

</html>
