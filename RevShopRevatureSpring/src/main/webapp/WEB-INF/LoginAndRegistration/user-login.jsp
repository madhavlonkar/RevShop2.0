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
						<h1 class="text-center title-page">Log In</h1>
						<div class="login-form">
							<div id="error-message"
								style="color: red; font-weight: bold; text-align: center;">
								<%
								if (request.getParameter("error") != null) {
								%>
								<p style="color: red;">Invalid email or password. Please try
									again.</p>
								<%
								}
								%>
							</div>
							<br>
							<form id="customer-form"
								action="${pageContext.request.contextPath}/login" method="POST">
								<div class="form-group no-gutters">
									<input class="form-control" name="email" type="email"
										placeholder="Email" required>
								</div>
								<div class="form-group no-gutters">
									<input class="form-control" name="password" type="password"
										placeholder="Password" required>
								</div>
								<div class="clearfix text-center">
									<button class="btn btn-primary" type="submit">Sign in</button>
									<div class="forgot-password text-center"
										style="margin-top: 15px;">
										<a href="${pageContext.request.contextPath}/forgot-password">Forgot
											your password?</a>
									</div>
									<br> <a href="/oauth2/authorization/google"
										class="btn btn-primary"
										style="background-color: white; color: #4285F4; border: 2px solid #4285F4; padding: 10px 20px; border-radius: 5px; font-weight: bold; display: inline-flex; align-items: center; text-decoration: none;">
										<i class="fab fa-google" style="margin-right: 8px;"></i> Login
										with Google
									</a>
								</div>
							</form>


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
