<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.util.List"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Favorite Products</title>

    <!-- SEO and Social Sharing Meta Tags -->
    <meta name="keywords" content="Furniture, Decor, Interior">
    <meta name="description" content="Furnitica - Minimalist Furniture HTML Template">
    <meta property="og:title" content="Favorite Products - RevShop">
    <meta property="og:description" content="View and manage your favorite products on RevShop.">
    <meta property="og:image" content="${pageContext.request.contextPath}/img/logo.png">
    <meta property="og:url" content="${pageContext.request.contextPath}/favorites">
    <meta name="twitter:card" content="summary_large_image">
    
    <!-- Importing Fonts and FontAwesome -->
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;600|Poppins:400,500,600,700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    
    <!-- Importing CSS Files -->
    <base href="${pageContext.request.contextPath}/">
    <link rel="stylesheet" href="Static/libs/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="Static/libs/font-awesome/css/font-awesome.min.css">
    <link rel="stylesheet" href="Static/libs/owl-carousel/assets/owl.carousel.min.css">
    <link rel="stylesheet" type="text/css" href="Static/css/style.css">
    <link rel="stylesheet" type="text/css" href="Static/css/reponsive.css">
    <jsp:include page="includes/commoncss.jsp" />
    
    <!-- Additional CSS for layout -->
    <style>
            <!-- CSS for quantity control -->
    .quantity-control {
        display: flex;
        align-items: center;
    }

    .quantity-control .btn-group-vertical {
        display: flex;
        flex-direction: column;
        margin-left: 5px;
    }

    .quantity-control input[type="text"] {
        max-width: 50px;
        text-align: center;
    }

    .quantity-control button {
        padding: 5px 10px;
        font-size: 16px;
        line-height: 1;
    }

    /* Button styling */
    .checkout-button {
        background-color: #28a745;
        color: white;
        border: none;
        padding: 10px 20px;
        font-size: 16px;
        cursor: pointer;
        border-radius: 5px;
    }

    .checkout-button:hover {
        background-color: #218838;
    }

    /* Logo style */
    .logo {
        font-family: 'Montserrat', sans-serif;
        font-weight: 700;
        font-size: 24px;
        color: #000;
    }

    /* Adjustments for cart items */
    .cart-item {
        border-bottom: 1px solid #f0f0f0;
        padding: 15px 0;
    }

    .product-line-grid {
        display: flex;
        justify-content: space-between;
        align-items: center;
    }

    .product-line-grid-left .product-image img {
        width: 100%;
        max-width: 100px;
    }

    .product-line-info {
        font-size: 18px;
        font-weight: 600;
        color: #333;
    }

    .product-price .value {
        font-size: 16px;
        color: #28a745;
    }

    .cart-line-product-actions button {
        background: none;
        border: none;
        color: #333;
        font-size: 18px;
        cursor: pointer;
        padding: 0;
    }

    .cart-line-product-actions button:hover {
        color: #e74c3c;
    }

    /* Responsive adjustments */
    @media (max-width: 768px) {
        .product-line-grid {
            flex-direction: column;
        }

        .product-line-grid-left,
        .product-line-grid-body,
        .product-line-grid-right {
            width: 100%;
            margin-bottom: 10px;
        }

        .cart-item {
            padding: 10px 0;
        }
    }

    </style>
</head>

<body class="product-cart checkout-cart blog">
<%@ include file="/WEB-INF/includes/header.jsp" %>

<div class="main-content" id="cart">
    <div id="wrapper-site">
        <nav class="breadcrumb-bg">
            <div class="container no-index">
                <div class="breadcrumb">
                    <ol>
                        <li><a href="#"> <span>Home</span></a></li>
                        <li><a href="#"> <span>Favorites</span></a></li>
                    </ol>
                </div>
            </div>
        </nav>
        <div class="container">
            <div class="row">
                <div id="content-wrapper" class="col-xs-12 col-sm-12 col-md-12 col-lg-12 onecol">
                    <section id="main">
                        <div class="cart-grid row">
                            <div class="col-md-9 col-xs-12 check-info">
                                <h1 class="title-page">Favorite Products</h1>
                                <div class="cart-container">
                                    <div class="cart-overview js-cart">
                                        <ul class="cart-items">
                                            <!-- JSTL loop to iterate through the favorite items -->
                                            <c:choose>
                                                <c:when test="${not empty favorites}">
                                                    <c:forEach var="item" items="${favorites}">
                                                        <li class="cart-item">
                                                            <div class="product-line-grid row justify-content-between">
                                                                <!-- product image -->
                                                                <div class="product-line-grid-left col-md-2">
                                                                    <span class="product-image media-middle"> 
                                                                        <a href="${pageContext.request.contextPath}/product/${item.product.productId}">
                                                                            <img class="img-fluid" src="${item.product.productImage}" alt="Image of ${item.product.productName}">
                                                                        </a>
                                                                    </span>
                                                                </div>
                                                                <!-- product details -->
                                                                <div class="product-line-grid-body col-md-6">
                                                                    <div class="product-line-info">
                                                                        <a class="label" href="${pageContext.request.contextPath}/product/${item.product.productId}">
                                                                            ${item.product.productName}
                                                                        </a>
                                                                    </div>
                                                                    <div class="product-line-info product-price">
                                                                        <span class="value">RS ${item.product.productPrice}</span>
                                                                    </div>
                                                                    <div class="product-line-info product-price">
                                                                        <span class="value">RS ${item.product.productPrice * (1 - (item.product.productDiscount / 100))} (After Discount)</span>
                                                                    </div>
                                                                </div>
                                                                <!-- product actions -->
                                                                <div class="product-line-grid-right text-center product-line-actions col-md-4">
                                                                    <div class="row">
                                                                        <!-- remove from wishlist -->
                                                                        <div class="col-md-2 col text-xs-right align-self-end">
                                                                            <div class="cart-line-product-actions">
                                                                                <form action="${pageContext.request.contextPath}/favorites/remove" method="post">
                                                                                    <input type="hidden" name="productId" value="${item.product.productId}">
                                                                                    <button class="remove-from-wishlist" type="submit" aria-label="Remove ${item.product.productName} from wishlist">
                                                                                        <i class="fa fa-trash-o" aria-hidden="true"></i>
                                                                                    </button>
                                                                                </form>
                                                                            </div>
                                                                        </div>
                                                                        <!-- move to cart -->
                                                                        <div class="col-md-2 col text-xs-right align-self-end">
                                                                            <div class="cart-line-product-actions">
                                                                                <form action="${pageContext.request.contextPath}/favorites/move-to-cart" method="post">
                                                                                    <input type="hidden" name="productId" value="${item.product.productId}">
                                                                                    <button class="move-to-cart" type="submit" aria-label="Move ${item.product.productName} to cart">
                                                                                        <i class="fa fa-shopping-cart" aria-hidden="true"></i>
                                                                                    </button>
                                                                                </form>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </li>
                                                    </c:forEach>
                                                </c:when>
                                                <c:otherwise>
                                                    <li class="cart-item">
                                                        <div class="alert alert-warning">You have no favorite products.</div>
                                                    </li>
                                                </c:otherwise>
                                            </c:choose>
                                        </ul>
                                    </div>
                                </div>
                                <a href="${pageContext.request.contextPath}/home" class="continue btn btn-primary pull-xs-right">Continue Shopping</a>
                            </div>
                        </div>
                    </section>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- back to top -->
<div class="back-to-top">
    <a href="#"> <i class="fa fa-long-arrow-up"></i></a>
</div>

<%@ include file="/WEB-INF/includes/footer.jsp" %>
</body>
</html>
