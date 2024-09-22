<%@ page import="java.util.List"%>
<%@ page import="com.revshop.dto.CartItemDTO"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Shopping Cart</title>
<jsp:include page="includes/commoncss.jsp" />
<style>
.quantity-control {
	display: flex;
	align-items: center;
}

.quantity-control .btn-group-vertical {
	display: flex;
	flex-direction: column;
	margin-left: 2px;
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

</style>
</head>
<body class="product-cart checkout-cart blog">
	<jsp:include page="includes/header.jsp" />

	<!-- Main Content -->
	<div class="main-content" id="cart">
		<div id="wrapper-site">
			<nav class="breadcrumb-bg">
				<div class="container no-index">
					<div class="breadcrumb">
						<ol>
							<li><a href="#"> <span>Home</span></a></li>
							<li><a href="#"> <span>Cart</span></a></li>
						</ol>
					</div>
				</div>
			</nav>
			<div class="container">
				<div class="row">
					<div id="content-wrapper"
						class="col-xs-12 col-sm-12 col-md-12 col-lg-12 onecol">
						<section id="main">
							<div class="cart-grid row">
								<div class="col-md-9 col-xs-12 check-info">
									<h1 class="title-page">Shopping Cart</h1>
									<div class="cart-container">
										<div class="cart-overview js-cart">
											<ul class="cart-items">
												<c:forEach var="item" items="${cartItems}">
													<li class="cart-item">
														<div class="product-line-grid row justify-content-between">
															<!-- Product Image -->
															<div class="product-line-grid-left col-md-2">
																<span class="product-image media-middle"> <a
																	href="/product/${item.productId}"> <img
																		class="img-fluid" src="${item.productImage}"
																		alt="${item.productName}">
																</a>
																</span>
															</div>
															<!-- Product Details -->
															<div class="product-line-grid-body col-md-6">
																<div class="product-line-info">
																	<a class="label"
																		href="/product/${item.productId}">${item.productName}</a>
																</div>
																<div class="product-line-info product-price">
																	<span class="value">RS
																		${item.productPrice}</span>
																</div>
																<div class="product-line-info">
																	<span class="label-atrr">Quantity:</span> <span
																		class="value">${item.quantity}</span>
																</div>
																<div class="product-line-info">
																	<span class="label-atrr">Discount:</span> <span
																		class="value">${item.productDiscount}%</span>
																</div>
															</div>
															<!-- Total and Actions -->
															<div
																class="product-line-grid-right text-center product-line-actions col-md-4">
																<div class="row">
																	<div class="col-md-5 col qty">
																		<div class="label">Qty:</div>
																		<div class="quantity">
																			<form action="/cart/update" method="post">
																				<input type="hidden" name="productId"
																					value="${item.productId}" />
																				<div class="quantity-control">
																					<!-- Quantity box -->
																					<input type="text" name="qty"
																						value="${item.quantity}" class="form-control"
																						readonly />
																					<div class="btn-group-vertical">
																						<!-- Increase button -->
																						<button class="btn btn-touchspin" type="submit"
																							name="action" value="increase">+</button>
																						<!-- Decrease button -->
																						<button class="btn btn-touchspin" type="submit"
																							name="action" value="decrease">-</button>
																					</div>
																				</div>
																			</form>

																		</div>
																	</div>
																	<div class="col-md-5 col price">
																		<div class="label">Total:</div>
																		<div class="product-price total">
																			RS
																			<!-- Total after discount -->
																			<c:set var="discountedPrice"
																				value="${item.productPrice * (1 - (item.productDiscount / 100))}" />
																			${((item.quantity * discountedPrice)* 100.0).intValue() / 100.0}
																		</div>
																	</div>
																	<div class="col-md-2 col text-xs-right align-self-end">
																		<div class="cart-line-product-actions">
																			<form action="/cart/remove" method="post">
																				<input type="hidden" name="cartId"
																					value="${item.cartId}" />
																				<button type="submit"
																					class="btn btn-link text-danger">
																					<i class="fa fa-trash-o" aria-hidden="true"></i>
																				</button>
																			</form>
																		</div>
																	</div>
																</div>
															</div>
														</div>
													</li>
												</c:forEach>
												<c:if test="${empty cartItems}">
													<li class="cart-item">
														<div class="alert alert-warning">Your cart is empty.</div>
													</li>
												</c:if>
											</ul>
										</div>
									</div>
								</div>
								<!-- Cart Summary -->
								<div class="cart-grid-right col-xs-12 col-lg-3">
									<div class="cart-summary">
										<div class="cart-detailed-totals">
											<div class="cart-summary-products">
												<div class="summary-label">There are ${cartItems != null ? cartItems.size() : 0}
													item(s) in your cart</div>
											</div>
											<div class="cart-summary-line" id="cart-subtotal-products">
												<span class="label js-subtotal">Total products:</span> <span
													class="value">RS ${cartTotal}</span>
											</div>
											<div class="cart-summary-line" id="cart-subtotal-shipping">
												<span class="label">Total Shipping:</span> <span
													class="value">Free</span>
											</div>
											<div class="cart-summary-line cart-total">
												<span class="label">Total:</span> <span class="value">RS
													<!-- Cart total with discount --> <c:set
														var="cartTotalDiscounted" value="0" /> <c:forEach
														var="item" items="${cartItems}">
														<c:set var="itemTotal"
															value="${item.quantity * (item.productPrice * (1 - (item.productDiscount / 100)))}" />
														<c:set var="cartTotalDiscounted"
															value="${cartTotalDiscounted + itemTotal}" />
													</c:forEach> ${(cartTotalDiscounted* 100.0).intValue() / 100.0} (After discount)
												</span>
											
									</div>
								</div>

								<!-- Continue to Checkout Button -->
								<form action="/checkout/view" method="get">
									<button type="submit" class="checkout-button">Continue
										to Checkout</button>
								</form>
							
							</div>
							</div>
						
                                <a href="${pageContext.request.contextPath}/home" class="continue btn btn-primary pull-xs-right">Continue Shopping</a>
                            
                            
				</section>
			
		</div>
	</div>
	</div>
	</div>

	<jsp:include page="includes/footer.jsp" />
</body>
</html>
