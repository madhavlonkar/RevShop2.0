<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Login</title>
<jsp:include page="../includes/commoncss.jsp" />
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
						<h1 class="text-center title-page">Forgot Password</h1>
						<div class="login-form">
							
							<br>
							<form id="forgot-password-form" action="${pageContext.request.contextPath}/forgot-password" method="POST">
								<div class="form-group no-gutters">
									<input class="form-control" name="email" type="email"
										placeholder="Enter your email" required>
								</div>
								<div class="clearfix text-center">
									<button class="btn btn-primary" type="submit">Send Reset Link</button>
								</div>
							</form>

							<div id="success-message" style="color: green; text-align: center;">
								<%
								if (request.getParameter("success") != null) {
								%>
								<p style="color: green;">If the email exists, a reset link has been sent to your inbox.</p>
								<%
								}
								%>
							</div>

							<div id="error-message" style="color: red; text-align: center;">
								<%
								if (request.getParameter("error") != null) {
								%>
								<p style="color: red;">An error occurred. Please try again later.</p>
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
