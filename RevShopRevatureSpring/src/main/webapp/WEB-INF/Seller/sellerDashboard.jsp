<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.revshop.master.ProductMaster"%>
<%@ page import="java.util.List"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Seller Dashboard</title>
<jsp:include page="../includes/commoncss.jsp" />
<style>
.logo {
	font-family: Arial, sans-serif;
	font-weight: 750;
	font-size: 24px;
	color: #000000;
}
/* Add more custom styles if necessary */
</style>
</head>
<body class="product-cart checkout-cart blog">

	<jsp:include page="../includes/header.jsp" />

	<!-- main content -->
	<div class="main-content" id="cart">
		<div id="wrapper-site">
			<!-- breadcrumb -->
			<nav class="breadcrumb-bg">
				<div class="container no-index">
					<div class="breadcrumb">
						<ol>
							<li><a href="#"> <span>Welcome</span></a></li>
							<li><a href="#"> <span>Dashboard</span></a></li>
						</ol>
					</div>
				</div>
			</nav>
			<div class="container">
				<div class="row">
					<!-- Sidebar -->
					<div
						class="sidebar-3 sidebar-collection col-lg-3 col-md-4 col-sm-4">
						<!-- Category Menu -->
						<br> <br> <br>
						<div class="sidebar-block">
							<div class="title-block">Categories</div>
							<div class="block-content">
								<!-- Updated category links to pass the selected category for the seller -->
								<div class="cateTitle hasSubCategory open level1">
									<a class="cateItem" href="dashboard?category=Electronics">Electronics</a>
								</div>
								<div class="cateTitle hasSubCategory open level1">
									<a class="cateItem" href="dashboard?category=Fashion">Fashion</a>
								</div>
								<div class="cateTitle hasSubCategory open level1">
									<a class="cateItem" href="dashboard?category=HomeAppliances">Home
										Appliances</a>
								</div>
								<div class="cateTitle hasSubCategory open level1">
									<a class="cateItem" href="dashboard?category=Books">Books</a>
								</div>
								<div class="cateTitle hasSubCategory open level1">
									<a class="cateItem" href="dashboard?category=Sports">Sports</a>
								</div>
							</div>
						</div>
					</div>

					<!-- Main Content -->
					<div id="content-wrapper" class="col-lg-9 col-md-8 col-sm-8">
						<section id="main">
							<!-- Low Stock Alerts (if any) -->
							<c:if test="${not empty lowStockProducts}">
								<div class="low-stock-alert">
									<strong
										style="color: #856404; background-color: #ffc107; padding: 5px; border-radius: 3px;">
										Low Stock Alert! </strong> <span
										style="color: #856404; background-color: #ffc107; padding: 5px; border-radius: 3px;">
										The following products are below the threshold: </span>
									<ul style="color: red; padding-left: 20px;">
										<br>
										<c:forEach var="product" items="${lowStockProducts}">
											<li>${product.productName}- Current Stock:
												${product.productStock} (Threshold: ${product.threshold})</li>
										</c:forEach>
									</ul>
								</div>
							</c:if>

							<!-- Product List -->
							<div class="cart-grid row">
								<div class="col-md-12 col-xs-12 check-info">
									<h1 class="title-page">Product List</h1>
									<!-- Search form updated to work for the seller's dashboard -->
									<form method="get" action="dashboard" class="form-inline mb-3">
										<input type="hidden" name="category"
											value="${selectedCategory}"> <input type="text"
											name="s" class="form-control mr-2"
											placeholder="Search products">
										<button type="submit" class="btn btn-primary">Search</button>
										<a href="/products/add" class="btn btn-primary ml-2">Add
											Product</a>
									</form>

									<div class="cart-container">
										<div class="cart-overview js-cart">
											<ul class="cart-items">
												<c:forEach var="product" items="${products}">
													<li class="cart-item" id="product-${product.productId}">
														<div class="product-line-grid row justify-content-between">
															<!-- Product left content: image -->
															<div class="product-line-grid-left col-md-2">
																<span class="product-image media-middle"> <a
																	href="/product/${product.productId}"> <img
																		class="img-fluid" src="${product.productImage}"
																		alt="${product.productName}">
																</a>
																</span>
															</div>
															<div class="product-line-grid-body col-md-6">
																<div class="product-line-info">
																	<a class="label" href="/product/${product.productId}">${product.productName}</a>
																</div>
																<div class="product-line-info product-price">
																	<span class="value">₹${product.productPrice}</span>
																</div>
																<div class="product-line-info">
																	<span class="label-atrr">Brand:</span> <span
																		class="value">${product.productBrand}</span>
																</div>
																<div class="product-line-info">
																	<span class="label-atrr">Discount:</span> <span
																		class="value">${product.productDiscount} %</span>
																</div>
															</div>
															<div
																class="product-line-grid-right text-center product-line-actions col-md-4">
																<div class="row">
																	<div class="col-md-5 col qty">
																		<div class="label">Qty:</div>
																		<div class="quantity">
																			<input type="text" name="qty"
																				value="${product.productStock}"
																				class="input-group form-control">
																		</div>
																	</div>
																	<div class="col-md-5 col price">
																		<div class="label">Edit:</div>
																		<div class="product-price total">
																			<a class="edit-cart-item"
																				href="${pageContext.request.contextPath}/products/edit/${product.productId}">
																				<i class="fa fa-pencil" aria-hidden="true"></i>
																			</a>
																		</div>
																	</div>
																	<div class="col-md-2 col text-xs-right align-self-end">
																		<div class="cart-line-product-actions">
																			<form
																				action="${pageContext.request.contextPath}/products/delete/${product.productId}"
																				method="post" style="display: inline;">
																				<button type="submit"
																					class="btn btn-link remove-from-cart">
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
											</ul>
										</div>
									</div>
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
		<a href="#"><i class="fa fa-long-arrow-up"></i></a>
	</div>

	<!-- Vendor JS -->
	<jsp:include page="../includes/footer.jsp" />
</body>
</html>
