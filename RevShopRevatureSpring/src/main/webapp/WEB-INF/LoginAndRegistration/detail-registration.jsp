<%@ page import="com.revshop.master.LoginMaster"%>
<!DOCTYPE html>
<html lang="en">
<head>
<style>
.section-divider {
	margin: 20px 0;
	font-weight: bold;
	text-align: center;
	color: #333;
}

.logo {
	font-family: Arial, sans-serif; /* Or use a similar font */
	font-weight: 750; /* Extra bold */
	font-size: 24px; /* Adjust size as needed */
	color: #000000; /* Black color */
}

/* Hide the company and banking details by default */
.company-details, .banking-details {
	display: none;
}
</style>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Register</title>
<jsp:include page="../includes/commoncss.jsp" />

<script>
	// Function to show/hide business fields based on selected role
	function toggleBusinessFields() {
		var role = document.getElementById("roleSelect").value;
		var companyDetails = document.querySelector(".company-details");
		var bankingDetails = document.querySelector(".banking-details");

		if (role === "seller") {
			// Show company and banking details if seller is selected
			companyDetails.style.display = "block";
			bankingDetails.style.display = "block";
		} else {
			// Hide company and banking details if buyer is selected
			companyDetails.style.display = "none";
			bankingDetails.style.display = "none";
		}
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
									<h1 class="text-center title-page">Details Registration</h1>
									<div id="error-message"
										style="color: red; font-weight: bold; text-align: center;">
										<%
										if (request.getAttribute("RegistererrorMessage") != null) {
										%>
										<%=request.getAttribute("RegistererrorMessage")%>
										<%
										}
										%>
									</div>
									<br>
									<form id="customer-form"
										action="/completeDetailRegistration"
										method="POST" onsubmit="return validateForm()">

										<!-- Email Section (display only, no input) -->
										<div class="row">
											<div class="form-group col-md-12">
												<label>Email:</label>
												<p>
													<strong>${user.email}</strong>
												</p>
												<input type="hidden" name="email" value="${user.email}">
											</div>
										</div>

										<!-- Role Selection Dropdown -->
										<div class="row">
											<div class="form-group col-md-12">
												<label for="roleSelect">Select Role:</label> <select
													id="roleSelect" name="role" class="form-control"
													onchange="toggleBusinessFields()">
													<option value="buyer"
														${user.role == 'buyer' ? 'selected' : ''}>Buyer</option>
													<option value="seller"
														${user.role == 'seller' ? 'selected' : ''}>Seller</option>
												</select>
											</div>
										</div>

										<!-- Personal Details Section -->
										<div class="section-divider">------------------------Personal
											Details-------------------------</div>
										<div class="row">
											<div class="form-group col-md-6">
												<input class="form-control" name="firstName" type="text"
													placeholder="First Name" value="${user.userId.firstName}">
											</div>
											<div class="form-group col-md-6">
												<input class="form-control" name="lastName" type="text"
													placeholder="Last Name" value="${user.userId.lastName}">
											</div>
										</div>
										<div class="row">
											<div class="form-group col-md-6">
												<select class="form-control" name="gender">
													<option value="" disabled selected>Select Gender</option>
													<option value="male"
														${user.userId.gender == 'male' ? 'selected' : ''}>Male</option>
													<option value="female"
														${user.userId.gender == 'female' ? 'selected' : ''}>Female</option>
													<option value="other"
														${user.userId.gender == 'other' ? 'selected' : ''}>Other</option>
												</select>
											</div>
											<div class="form-group col-md-6">
												<input class="form-control" name="mobile" type="text"
													placeholder="Mobile" value="${user.userId.mobile}">
											</div>
										</div>
										<div class="row">
											<div class="form-group col-md-6">
												<input class="form-control" name="pincode" type="text"
													placeholder="Pincode" value="${user.userId.pincode}">
											</div>
											<div class="form-group col-md-12">
												<textarea class="form-control" name="billingAddress"
													rows="4" placeholder="Billing Address"
													style="height: 100px;">${user.userId.billingAddress}</textarea>
											</div>
										</div>

										<div class="company-details ">
											<!-- Company Details Section -->
											<div class="section-divider">------------------------Company
												Details------------------------</div>
											<div class="row company-details">
												<div class="form-group col-md-6">
													<input class="form-control" name="companyName" type="text"
														placeholder="Company Name"
														value="${user.userId.companyName}">
												</div>
												<div class="form-group col-md-6">
													<input class="form-control" name="gstNumber" type="text"
														placeholder="GST Number" value="${user.userId.gstNumber}">
												</div>
											</div>
											<div class="row company-details">
												<div class="form-group col-md-6">
													<input class="form-control" name="websiteUrl" type="text"
														placeholder="Website URL"
														value="${user.userId.websiteUrl}">
												</div>
												<div class="form-group col-md-6">
													<input class="form-control" name="panNumber" type="text"
														placeholder="PAN Number" value="${user.userId.panNumber}">
												</div>
											</div>
										</div>
										<!-- Banking Details Section -->
										<div class="banking-details">
											<div class="section-divider">------------------------Banking
												Details-------------------------</div>
											<div class="row banking-details">
												<div class="form-group col-md-6">
													<input class="form-control" name="bankAccountNo"
														type="text" placeholder="Bank Account Number"
														value="${user.userId.bankAccountNo}">
												</div>
												<div class="form-group col-md-6">
													<input class="form-control" name="ifsc" type="text"
														placeholder="IFSC Code" value="${user.userId.ifsc}">
												</div>
											</div>
										</div>

										<div class="clearfix text-center">
											<button class="btn btn-primary" data-link-action="sign-in"
												type="submit">Submit Information</button>
										</div>
									</form>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- footer -->
	<jsp:include page="../includes/footer.jsp" />

	<script>
		// Automatically trigger the toggleBusinessFields function on page load to ensure the correct fields are shown/hidden
		document.addEventListener("DOMContentLoaded", function() {
			toggleBusinessFields();
		});
	</script>
	
	<script>
// Function to validate the form
function validateForm() {
    let isValid = true;

    // Get form fields
    const firstName = document.querySelector('input[name="firstName"]').value.trim();
    const lastName = document.querySelector('input[name="lastName"]').value.trim();
    const gender = document.querySelector('select[name="gender"]').value;
    const mobile = document.querySelector('input[name="mobile"]').value.trim();
    const pincode = document.querySelector('input[name="pincode"]').value.trim();
    const billingAddress = document.querySelector('textarea[name="billingAddress"]').value.trim();

    const role = document.getElementById("roleSelect").value;

    // Company details
    const companyName = document.querySelector('input[name="companyName"]').value.trim();
    const gstNumber = document.querySelector('input[name="gstNumber"]').value.trim();
    const panNumber = document.querySelector('input[name="panNumber"]').value.trim();

    // Banking details
    const bankAccountNo = document.querySelector('input[name="bankAccountNo"]').value.trim();
    const ifsc = document.querySelector('input[name="ifsc"]').value.trim();

    // Reset error messages
    document.getElementById("error-message").innerText = "";

    // Validate first name
    if (firstName === "") {
        document.getElementById("error-message").innerText += "First name is required.\n";
        isValid = false;
    }

    // Validate last name
    if (lastName === "") {
        document.getElementById("error-message").innerText += "Last name is required.\n";
        isValid = false;
    }

    // Validate gender
    if (gender === "") {
        document.getElementById("error-message").innerText += "Please select a gender.\n";
        isValid = false;
    }

    // Validate mobile number (assuming 10-digit number)
    const mobilePattern = /^[0-9]{10}$/;
    if (!mobilePattern.test(mobile)) {
        document.getElementById("error-message").innerText += "Please enter a valid 10-digit mobile number.\n";
        isValid = false;
    }

    // Validate pincode
    if (pincode === "" || pincode.length !== 6 || isNaN(pincode)) {
        document.getElementById("error-message").innerText += "Please enter a valid 6-digit pincode.\n";
        isValid = false;
    }

    // Validate billing address
    if (billingAddress === "") {
        document.getElementById("error-message").innerText += "Billing address is required.\n";
        isValid = false;
    }

    // Additional validation for sellers
    if (role === "seller") {
        if (companyName === "") {
            document.getElementById("error-message").innerText += "Company name is required for sellers.\n";
            isValid = false;
        }
        if (gstNumber === "" || gstNumber.length !== 15) {
            document.getElementById("error-message").innerText += "Please enter a valid 15-digit GST number.\n";
            isValid = false;
        }
        if (panNumber === "" || panNumber.length !== 10) {
            document.getElementById("error-message").innerText += "Please enter a valid 10-digit PAN number.\n";
            isValid = false;
        }
        if (bankAccountNo === "" || isNaN(bankAccountNo)) {
            document.getElementById("error-message").innerText += "Please enter a valid bank account number.\n";
            isValid = false;
        }
        if (ifsc === "" || ifsc.length !== 11) {
            document.getElementById("error-message").innerText += "Please enter a valid 11-character IFSC code.\n";
            isValid = false;
        }
    }

    // Return false to prevent form submission if any validation failed
    return isValid;
}
</script>
	
</body>
</html>
