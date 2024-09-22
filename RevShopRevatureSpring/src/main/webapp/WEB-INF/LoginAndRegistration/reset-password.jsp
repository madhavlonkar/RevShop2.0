<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Reset Password</title>
<jsp:include page="../includes/commoncss.jsp" />

<script>
// Function to validate the password
function validatePassword() {
    const password = document.querySelector('input[name="password"]').value;
    const confirmPassword = document.querySelector('input[name="confirmPassword"]').value;

    // Password validation: must be at least 6 characters long, include uppercase, lowercase, number, and special character
    const passwordPattern = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{6,}$/;

    // Check if password matches the required pattern
    if (!passwordPattern.test(password)) {
        alert("Password must be at least 6 characters long, and include an uppercase letter, a lowercase letter, a number, and a special character.");
        return false;
    }

    // Check if passwords match
    if (password !== confirmPassword) {
        alert("Passwords do not match. Please confirm your password.");
        return false;
    }

    return true; // Allow form submission if validation passes
}
</script>

</head>
<body class="user-login blog">
	<jsp:include page="../includes/header.jsp" />
	<!-- main content -->
	<div class="main-content">
		<div class="wrap-banner">
			<!-- breadcrumb -->
			<nav class="breadcrumb-bg">
				<div class="container no-index">
					<div class="breadcrumb">
						<ol>
							<li><span>Home</span></li>
							<li><span>Login</span></li>
						</ol>
					</div>
				</div>
			</nav>
		</div>
		<!-- main -->
		<div id="wrapper-site">
			<div id="content-wrapper" class="full-width">
				<div id="main">
					<div class="container">
						<h1 class="text-center title-page">Reset Password</h1>
						<div class="login-form">
							<br>
							<form id="reset-password-form"
								action="${pageContext.request.contextPath}/reset-password"
								method="POST" onsubmit="return validatePassword()">
								<!-- Hidden field for token -->
								<input type="hidden" name="token" value="${token}" />

								<div class="form-group no-gutters">
									<input class="form-control" name="password" type="password"
										placeholder="Enter your new password" required>
								</div>
								<div class="form-group no-gutters">
									<input class="form-control" name="confirmPassword"
										type="password" placeholder="Confirm your new password"
										required>
								</div>
								<div class="clearfix text-center">
									<button class="btn btn-primary" type="submit">Reset Password</button>
								</div>
							</form>

							<div id="error-message" style="color: red; text-align: center;">
								<%
								if (request.getParameter("error") != null) {
								%>
								<p style="color: red;">An error occurred. Please try again later.</p>
								<%
								}
								%>
							</div>

							<div id="success-message" style="color: green; text-align: center;">
								<%
								if (request.getParameter("resetSuccess") != null) {
								%>
								<p style="color: green;">Password reset successfully. You can now log in with your new password.</p>
								<%
								}
								%>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- footer -->
	<jsp:include page="../includes/footer.jsp" />
</body>
</html>
