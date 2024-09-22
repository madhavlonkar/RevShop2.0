<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Order Placed Successfully</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #f8f9fa;
            font-family: Arial, sans-serif;
        }
        .thank-you-container {
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }
        .thank-you-box {
            background-color: #ffffff;
            border-radius: 10px;
            padding: 30px;
            text-align: center;
            box-shadow: 0px 0px 15px rgba(0, 0, 0, 0.1);
        }
        .thank-you-box h1 {
            color: #28a745;
            font-size: 2.5rem;
        }
        .thank-you-box p {
            font-size: 1.2rem;
            margin-top: 10px;
        }
        .checkmark {
            color: #28a745;
            font-size: 4rem;
        }
        .order-details-btn {
            margin-top: 20px;
            background-color: #28a745;
            color: #fff;
        }
        .order-details-btn:hover {
            background-color: #218838;
        }
    </style>
</head>
<body>
    <div class="thank-you-container">
        <div class="thank-you-box">
            <!-- Green checkmark symbol -->
            <div class="checkmark">
                &#10004;
            </div>
            <!-- Success message -->
            <h1>Thank You!</h1>
            <p>Your order has been placed successfully.</p>
            <p>We will notify you once your items are shipped.</p>
            <!-- Button to view order details -->
            <a href="/orders/myOrders" class="btn order-details-btn">View Order Details</a>
        </div>
    </div>

    <!-- Bootstrap JS for responsiveness (Optional) -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
