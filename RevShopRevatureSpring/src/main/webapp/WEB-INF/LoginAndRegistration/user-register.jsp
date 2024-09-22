<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Register</title>
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
							<li><span>Register</span></li>
						</ol>
					</div>
				</div>
			</nav>
		</div>
		<!-- main -->
		<div id="wrapper-site">
			<div class="container">
				<div class="row">
					<div id="content-wrapper"
						class="col-xs-12 col-sm-12 col-md-12 col-lg-12 onecol">
						<div id="main">
							<div id="content" class="page-content">
								<div class="register-form text-center">
									<h1 class="text-center title-page">Create Account</h1>
									<div id="error-message"
										style="color: red; font-weight: bold; text-align: center;">
										<%
										if (request.getAttribute("RegistererrorMessage") != null) {
										%>
										<p><%=request.getAttribute("RegistererrorMessage")%></p>
										<%
										}
										%>
									</div>

									<br>
									<form:form id="customer-form" class="js-customer-form"
										action="${pageContext.request.contextPath}/register"
										method="POST" modelAttribute="user"
										onsubmit="return validateForm()">

										<!-- Account Type Dropdown -->
										<!-- Username Input -->
										<div class="form-group">
											<div>
												<form:input path="userName" class="form-control"
													id="username" placeholder="Username" type="text"
													htmlEscape="false" />
											</div>
										</div>

										<!-- Email Input -->
										<div class="form-group">
											<div>
												<form:input path="email" class="form-control" id="email"
													placeholder="Email" type="email" htmlEscape="false" />
											</div>
										</div>

										<!-- Password Input -->
										<div class="form-group">
											<div>
												<div class="input-group js-parent-focus">
													<form:password path="password"
														class="form-control js-child-focus js-visible-password"
														id="password" placeholder="Password" htmlEscape="false" />
												</div>
											</div>
										</div>

										<!-- Submit Button -->
										<div class="clearfix">
											<div>
												<button class="btn btn-primary" data-link-action="sign-in"
													type="submit">Create Account</button>
											</div>
										</div>
										<a href="/oauth2/authorization/google" class="btn btn-primary"
											style="background-color: white; color: #4285F4; border: 2px solid #4285F4; padding: 10px 20px; border-radius: 5px; font-weight: bold; display: inline-flex; align-items: center; text-decoration: none;">
											<i class="fab fa-google" style="margin-right: 8px;"></i>
											Login with Google
										</a>

										<!-- Sign In Link -->
										<div class="mt-3">
											<p>
												Already have an account? <a href="user-login.html">Sign
													in</a>
											</p>
										</div>
									</form:form>


								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script>
		function validateForm() {
			const username = document.forms["customer-form"]["username"].value;
			const email = document.forms["customer-form"]["email"].value;
			const password = document.forms["customer-form"]["password"].value;
			const userType = document.forms["customer-form"]["userType"].value;

			// Username validation: must be at least 3 characters long and contain at least one letter
			const usernamePattern = /[a-zA-Z]/;
			if (username.length < 3 || !usernamePattern.test(username)) {
				alert("Username must be at least 3 characters long and contain at least one letter.");
				return false;
			}

			// Email validation
			const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
			if (!emailPattern.test(email)) {
				alert("Please enter a valid email address.");
				return false;
			}

			// Password validation: must be at least 6 characters long, include uppercase, lowercase, number, and special character
			const passwordPattern = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{6,}$/;
			if (!passwordPattern.test(password)) {
				alert("Password must be at least 6 characters long and include an uppercase letter, a number, and a special character.");
				return false;
			}

			return true; // If all validations pass
		}
	</script>


	<!-- footer -->
	<jsp:include page="../includes/footer.jsp" />

</body>
</html>