<%@ page import="java.util.List"%>
<%@ page import="com.revshop.master.CartMaster"%>
<%@ page import="com.revshop.master.UserMaster"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <style>
        .logo { font-family: Arial, sans-serif; font-weight: 750; font-size: 24px; color: #000000; }
        .section-divider { margin: 20px 0; font-weight: bold; text-align: center; color: #333; }
        .product-summary { margin: 20px 0; padding: 10px; border: 1px solid #ddd; border-radius: 5px; }
        .product-summary h4 { font-weight: bold; margin-bottom: 10px; }
        .product-item { display: flex; justify-content: space-between; margin-bottom: 10px; }
        .product-item span { display: inline-block; margin-right: 10px; }
        .product-total { font-weight: bold; }
        .checkout-container { display: flex; justify-content: space-between; }
        .checkout-form { flex: 0 0 65%; margin-right: 20px; }
        .order-summary { flex: 0 0 30%; }
        .checkout-form form { text-align: left; }
        .checkout-form .form-group { text-align: left; margin-bottom: 15px; }
        .checkout-form .form-control { width: 100%; text-align: left; margin: 0; padding: 10px; box-sizing: border-box; }
        .checkout-form .btn-primary { width: 100%; padding: 10px; font-size: 16px; font-weight: bold; }
        .product-item { display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px; }
        .product-item span { flex: 1; text-align: left; }
        .product-item span:nth-child(2) { text-align: center; flex: 0 0 50px; }
        .product-item span:nth-child(3) { text-align: right; flex: 0 0 100px; }
    </style>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Checkout</title>
	<jsp:include page="includes/commoncss.jsp" />
</head>
<body class="user-login blog">
    <jsp:include page="includes/header.jsp" />
    <div class="main-content">
        <div class="wrap-banner">
            <nav class="breadcrumb-bg">
                <div class="container no-index">
                    <div class="breadcrumb">
                        <ol>
                            <li><span>Home</span></li>
                            <li><span>Checkout</span></li>
                        </ol>
                    </div>
                </div>
            </nav>
        </div>
        <div id="wrapper-site">
            <div>
                <div class="row">
                    <div id="content-wrapper">
                        <div id="main">
                            <div id="content" class="page-content">
                                <div class="checkout-form">
                                    <h1 class="text-center title-page">Checkout</h1>
                                    <div id="error-message" style="color: red; font-weight: bold; text-align: center;"></div>
                                    <br>
                                    <c:if test="${empty cartItems}">
                                        <div class="alert alert-warning">You have no items in your cart.</div>
                                    </c:if>
                                    <c:if test="${!empty cartItems}">
                                        <div class="order-summary">
                                            <div class="section-divider">Order Summary</div>
                                            <div class="product-summary">
                                                <h4>Products</h4>
                                                <c:forEach var="item" items="${cartItems}">
                                                    <div class="product-item">
                                                        <span>${item.product.productName}</span>
                                                        <span>X ${item.quantity}</span>
                                                        <span class="product-total">Rs. ${(item.product.productPrice - (item.product.productPrice * item.product.productDiscount / 100.0)) * item.quantity}</span>
                                                    </div>
                                                </c:forEach>
                                                <div class="product-item">
                                                    <strong>Total Amount:</strong>
                                                    <strong class="product-total">Rs. ${cartTotal}</strong>
                                                </div>
                                            </div>
                                        </div>
                                        <form id="checkout-form" action="/checkout/confirm" method="POST">
                                            <!-- Personal Details Section -->
                                            <div class="section-divider">Personal Details</div>
                                            <div class="row">
                                                <div class="form-group col-md-6">
                                                    <input class="form-control" name="firstName" type="text" placeholder="First Name" value="${userDetails.firstName}" readonly>
                                                </div>
                                                <div class="form-group col-md-6">
                                                    <input class="form-control" name="lastName" type="text" placeholder="Last Name" value="${userDetails.lastName}" readonly>
                                                </div>
                                            </div>
                                            <div class="row">
                                                <div class="form-group col-md-6">
                                                    <input class="form-control" name="mobile" type="text" placeholder="Mobile" value="${userDetails.mobile}" readonly>
                                                </div>
                                                <div class="form-group col-md-6">
                                                    <input class="form-control" name="email" type="email" placeholder="Email" value="${userDetails.email}" readonly>
                                                </div>
                                            </div>
                                            <!-- Shipping Address Section -->
                                            <div class="section-divider">Shipping Address</div>
                                            <div class="row">
                                                <div class="form-group col-md-6">
                                                    <input class="form-control" name="address" type="text" placeholder="Address" required>
                                                </div>
                                                <div class="form-group col-md-6">
                                                    <input class="form-control" name="city" type="text" placeholder="City" required>
                                                </div>
                                            </div>
                                            <div class="row">
                                                <div class="form-group col-md-6">
                                                    <input class="form-control" name="state" type="text" placeholder="State" required>
                                                </div>
                                                <div class="form-group col-md-6">
                                                    <input class="form-control" name="zip" type="text" placeholder="Zip Code" required>
                                                </div>
                                            </div>
                                            <input type="hidden" name="payment_id" id="payment_id" value="">
                                            <div class="clearfix text-center">
                                                <button type="button" class="btn btn-primary" onclick="payNow()">Pay Now</button>
                                            </div>
                                        </form>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <jsp:include page="includes/footer.jsp" />

    <!-- Razorpay integration -->
    <script src="https://checkout.razorpay.com/v1/checkout.js"></script>
    <script>
    function validateCheckoutForm() {
        const address = document.querySelector('input[name="address"]').value.trim();
        const city = document.querySelector('input[name="city"]').value.trim();
        const state = document.querySelector('input[name="state"]').value.trim();
        const zip = document.querySelector('input[name="zip"]').value.trim();
        
        if (address === "" || city === "" || state === "" || zip === "") {
            alert("Please fill out all fields.");
            return false;
        }

        return true;
    }

    function payNow() {
        if (!validateCheckoutForm()) return;
        
        var amountInPaise = Math.round(${cartTotal * 100});
        var options = {
            "key": "rzp_test_wquKp1Dkyy2Nck",
            "amount": amountInPaise,
            "currency": "INR",
            "name": "Rev Shop",
            "description": "Test Transaction",
            "image": "https://example.com/your_logo",
            "handler": function (response) {
                alert("Payment successful!");
                document.getElementById('payment_id').value = response.razorpay_payment_id;
                document.getElementById('checkout-form').submit();
            },
            "prefill": {
                "name": "${userDetails.firstName} ${userDetails.lastName}",
                "email": "${userDetails.email}",
                "contact": "${userDetails.mobile}"
            },
            "theme": {
                "color": "#3399cc"
            }
        };

        var rzp1 = new Razorpay(options);
        rzp1.open();
    }
    </script>
</body>
</html>
