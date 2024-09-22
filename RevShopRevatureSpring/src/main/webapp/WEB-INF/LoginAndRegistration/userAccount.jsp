<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Account</title>
	<jsp:include page="../includes/commoncss.jsp" />
    <style>
        .logo {
            font-family: Arial, sans-serif; /* Or use a similar font */
            font-weight: 750; /* Extra bold */
            font-size: 24px; /* Adjust size as needed */
            color: #000000; /* Black color */
        }
    </style>
</head>

<body class="product-cart checkout-cart blog">
    <jsp:include page="../includes/header.jsp" />

    <!-- main content -->
    <div class="main-content">
        <div class="wrap-banner">

            <!-- breadcrumb -->
            <nav class="breadcrumb-bg">
                <div class="container no-index">
                    <div class="breadcrumb">
                        <ol>
                            <li><a href="#"> <span>Home</span></a></li>
                            <li><a href="#"> <span>Account</span></a></li>
                            <!-- Display the selected category -->
                        </ol>
                    </div>
                </div>
            </nav>

            <div class="acount head-acount">
                <div class="container">
                    <div id="main">
                        <h1 class="title-page">My Account</h1>
                        <div class="content" id="block-history">
                            <table class="std table">
                                <tbody>
                                    <!-- Check if userDetails is available in the model -->
                                    <c:choose>
                                        <c:when test="${not empty userDetails}">
                                            <tr>
                                                <th class="first_item">My Name:</th>
                                                <td>${userDetails.firstName} ${userDetails.lastName}</td>
                                            </tr>
                                            <tr>
                                                <th class="first_item">Email:</th>
                                                <td>${userDetails.email}</td>
                                            </tr>
                                            <tr>
                                                <th class="first_item">Address:</th>
                                                <td>${userDetails.billingAddress}</td>
                                            </tr>
                                            <tr>
                                                <th class="first_item">Postal/Zip Code:</th>
                                                <td>${userDetails.pincode}</td>
                                            </tr>
                                            <tr>
                                                <th class="first_item">Phone:</th>
                                                <td>${userDetails.mobile}</td>
                                            </tr>
                                            <tr>
                                                <th class="first_item">Gender:</th>
                                                <td>${userDetails.gender}</td>
                                            </tr>
                                        </c:when>
                                        <c:otherwise>
                                            <tr>
                                                <td colspan="2">No user details available.</td>
                                            </tr>
                                        </c:otherwise>
                                    </c:choose>
                                </tbody>
                            </table>
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
