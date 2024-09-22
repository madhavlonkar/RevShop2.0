<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Orders</title>

    <jsp:include page="includes/commoncss.jsp" />

    <style>
        .logo {
            font-family: Arial, sans-serif;
            font-weight: 750;
            font-size: 24px;
            color: #000000;
        }
        .order-table img {
            max-width: 100%;
            height: auto;
        }
        .order-details {
            background-color: #f8f9fa;
            padding: 15px;
            border: 1px solid #ddd;
            margin-top: 10px;
            border-radius: 5px;
        }
        .toggle-orders-view {
            margin-bottom: 20px;
        }
        .toggle-orders-view .btn {
            margin-right: 10px;
        }
    </style>
</head>

<body class="product-cart checkout-cart blog">
    <jsp:include page="includes/header.jsp" />

    <div class="main-content" id="orders">
        <div id="wrapper-site">
            <nav class="breadcrumb-bg">
                <div class="container no-index">
                    <div class="breadcrumb">
                        <ol>
                            <li><a href="#"> <span>Home</span></a></li>
                            <li><a href="#"> <span>Orders</span></a></li>
                        </ol>
                    </div>
                </div>
            </nav>

            <div class="container">
                <div class="row">
                    <div id="content-wrapper" class="col-xs-12 col-sm-12 col-md-12 col-lg-12 onecol">
                        <section id="main">
                            <h1 class="title-page">My Orders</h1>

                            <div class="order-container">
                                <!-- Only show the toggle button if the user is a seller -->
                                <c:if test="${userDetails.role == 'ROLE_seller'}">
                                    <div class="toggle-orders-view">
                                        <c:choose>
                                            <c:when test="${view == 'personal'}">
                                                <a href="/orders/myOrders?view=seller" class="btn btn-secondary">View Seller Orders</a>
                                            </c:when>
                                            <c:otherwise>
                                                <a href="/orders/myOrders?view=personal" class="btn btn-secondary">View My Personal Orders</a>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </c:if>

                                <!-- Check if orders exist -->
                                <c:choose>
                                    <c:when test="${not empty orders}">
                                        <table class="table table-striped order-table">
                                            <thead>
                                                <tr>
                                                    <th>Image</th>
                                                    <th>Product Name</th>
                                                    <th>Price</th>
                                                    <th>Status</th>
                                                    <th>Action</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:forEach var="order" items="${orders}">
                                                    <tr>
                                                        <td style="width: 100px; text-align: center;">
                                                            <img src="${order.orderDetails[0].product.productImage}" alt="${order.orderDetails[0].product.productName}" style="width: 100px; height: auto;">
                                                        </td>
                                                        <td>${order.orderDetails[0].product.productName}</td>
                                                        <td>Rs. ${order.totalAmount}</td>
                                                        <td>
                                                            <c:choose>
                                                                <c:when test="${userDetails.role == 'ROLE_seller' && view != 'personal'}">
                                                                    <form method="post" action="/orders/updateStatus">
                                                                        <input type="hidden" name="orderId" value="${order.orderId}">
                                                                        <select name="status" onchange="this.form.submit()">
                                                                            <option value="To Be Shipped" ${order.orderStatus == 'To Be Shipped' ? 'selected' : ''}>To Be Shipped</option>
                                                                            <option value="In Transit" ${order.orderStatus == 'In Transit' ? 'selected' : ''}>In Transit</option>
                                                                            <option value="Delivered" ${order.orderStatus == 'Delivered' ? 'selected' : ''}>Delivered</option>
                                                                            <option value="Cancelled" ${order.orderStatus == 'Cancelled' ? 'selected' : ''}>Cancelled</option>
                                                                        </select>
                                                                    </form>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    ${order.orderStatus}
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </td>
                                                        <td>
                                                            <button class="btn btn-primary" type="button" onclick="toggleDetails(this, 'details${order.orderId}')">
                                                                Show Details
                                                            </button>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td colspan="5">
                                                            <div class="order-details" id="details${order.orderId}" style="display: none;">
                                                                <p><strong>Order ID:</strong> ${order.orderId}</p>
                                                                <p><strong>Transaction ID:</strong> ${order.transaction.paymentId}</p>
                                                                <p><strong>Shipping Address:</strong> ${order.shippingAddress}</p>
                                                                <p><strong>Quantity:</strong> ${order.orderDetails[0].quantity}</p>
                                                                <p><strong>Total Price:</strong> Rs. ${order.totalAmount}</p>
                                                            </div>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </tbody>
                                        </table>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="alert alert-warning">No orders found.</div>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </section>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="back-to-top">
        <a href="#"><i class="fa fa-long-arrow-up"></i></a>
    </div>

    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>

    <script>
        function toggleDetails(button, detailsId) {
            var detailsDiv = document.getElementById(detailsId);
            if (detailsDiv.style.display === "none" || detailsDiv.style.display === "") {
                detailsDiv.style.display = "block";
                button.textContent = "Hide Details";
            } else {
                detailsDiv.style.display = "none";
                button.textContent = "Show Details";
            }
        }
    </script>

    <jsp:include page="includes/footer.jsp" />
</body>
</html>
