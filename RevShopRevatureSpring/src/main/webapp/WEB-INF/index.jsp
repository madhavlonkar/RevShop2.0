<%@ page import="java.util.List"%>
<%@ page import="jakarta.servlet.http.HttpSession"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Home Page</title>
<jsp:include page="includes/commoncss.jsp" />

<style>
/* Add this to remove border and outline from buttons */
button.add-to-Wishlist, button.add-to-cart {
    border: none;
    background: none;
    outline: none;
    cursor: pointer;
}

button.add-to-Wishlist:focus, button.add-to-cart:focus {
    outline: none;
    box-shadow: none; /* Remove any focus outline or shadow */
}

button.add-to-Wishlist:hover, button.add-to-cart:hover {
    color: #ff0000; /* Example hover effect for better UX (optional) */
}

/* Existing styling */
.toast {
    visibility: hidden;
    min-width: 250px;
    margin-left: -125px;
    background-color: #333;
    color: #fff;
    text-align: center;
    border-radius: 2px;
    padding: 16px;
    position: fixed;
    z-index: 1;
    left: 50%;
    bottom: 30px;
    font-size: 17px;
}

.toast.show {
    visibility: visible;
    -webkit-animation: fadein 0.5s, fadeout 0.5s 2.5s;
    animation: fadein 0.5s, fadeout 0.5s 2.5s;
}

@-webkit-keyframes fadein {
    from {bottom: 0; opacity: 0;}
    to {bottom: 30px; opacity: 1;}
}

@keyframes fadein {
    from {bottom: 0; opacity: 0;}
    to {bottom: 30px; opacity: 1;}
}

@-webkit-keyframes fadeout {
    from {bottom: 30px; opacity: 1;}
    to {bottom: 0; opacity: 0;}
}

@keyframes fadeout {
    from {bottom: 30px; opacity: 1;}
    to {bottom: 0; opacity: 0;}
}

.product-container .thumbnail-container {
    width: 100%;
    height: 200px;
    overflow: hidden;
    display: flex;
    align-items: center;
    justify-content: center;
    border: 1px solid #ddd;
    background-color: #f8f8f8;
}

.product-container .thumbnail-container img {
    max-width: 80%;
    max-height: 100%;
    width: auto;
    height: auto;
    display: block;
    margin: 0 auto;
}

.product-miniature {
    min-height: 350px;
    margin-bottom: 20px;
}

.img-fluid {
    max-width: 50%;
}

.logo {
    font-family: Arial, sans-serif;
    font-weight: 750;
    font-size: 24px;
    color: #000000;
}
</style>


</head>

<body id="product-sidebar-left" class="product-grid-sidebar-left">
	<div id="toast" class="toast"></div>

	<!-- Include Header -->
	<jsp:include page="includes/header.jsp" />

	<!-- main content -->
	<div class="main-content">
		<div id="wrapper-site">
			<div id="content-wrapper" class="full-width">
				<div id="main">
					<div class="page-home">
						<!-- breadcrumb -->
						<nav class="breadcrumb-bg">
							<div class="container no-index">
								<div class="breadcrumb">
									<ol>
										<li><a href="/home"> <span>Home</span></a></li>
										<li><a href="#"> <span>${selectedCategory}</span></a></li>
									</ol>
								</div>
							</div>
						</nav>

						<div class="container">
							<div class="content">
								<div class="row">
									<div
										class="sidebar-3 sidebar-collection col-lg-3 col-md-4 col-sm-4">
										<!-- category menu -->
										<div class="sidebar-block">
											<div class="title-block">Categories</div>
											<div class="block-content">
												<div class="cateTitle hasSubCategory open level1">
													<a class="cateItem" href="/home?category=Electronics">Electronics</a>
												</div>
												<div class="cateTitle hasSubCategory open level1">
													<a class="cateItem" href="/home?category=Fashion">Fashion</a>
												</div>
												<div class="cateTitle hasSubCategory open level1">
													<a class="cateItem" href="/home?category=HomeAppliances">Home Appliances</a>
												</div>
												<div class="cateTitle hasSubCategory open level1">
													<a class="cateItem" href="/home?category=Books">Books</a>
												</div>
												<div class="cateTitle hasSubCategory open level1">
													<a class="cateItem" href="/home?category=Sports">Sports</a>
												</div>
											</div>
										</div>
									</div>

									<div class="col-sm-8 col-lg-9 col-md-8 product-container">
										<h1>All Products</h1>
										<div class="js-product-list-top first nav-top">
											<div class="d-flex justify-content-around row">
												<div class="col col-xs-12">
													<ul class="nav nav-tabs">
														<li><a href="#grid" data-toggle="tab"
															class="active show fa fa-th-large"></a></li>
													</ul>
												</div>
											</div>
										</div>

										<div class="tab-content product-items">
											<div id="grid" class="related tab-pane fade in active show">
												<div class="row">
													<!-- Loop through products using JSTL -->
													<c:forEach var="product" items="${products}">
														<div class="item text-center col-md-4">
															<div
																class="product-miniature js-product-miniature item-one first-item">
																<div class="thumbnail-container border">
																	<a href="/product/${product.productId}"> <img
																		class="img-fluid" src="${product.productImage}"
																		alt="${product.productName}">
																	</a>
																</div>
																<div class="product-description">
																	<div class="product-groups">
																		<div class="product-title">
																			<a href="/product/${product.productId}">${product.productName}</a>
																		</div>
																		
																		<div class="detail-description" style="text-align: center;">
                                                                            <div class="price-del" style="display: flex; flex-direction: column; align-items: center;">
                                                                                <!-- Discounted Price -->
                                                                                <span class="price discounted-price" style="font-size: 15px; color: black; font-weight: bold;">
                                                                                    RS. ${Math.floor(product.productPrice - (product.productPrice * product.productDiscount / 100))}
                                                                                </span>

                                                                                <!-- Original Price with strikethrough -->
                                                                                <span class="price original-price" style="font-size: 12px; color: red; text-decoration: line-through; margin-top: 5px;">
                                                                                    RS. ${Math.floor(product.productPrice)} 
                                                                                </span>
                                                                            </div>
                                                                            
                                                                            <br> <br>
                                                                            <div class="product-buttons d-flex justify-content-center">
                                                                                <c:if test="${user != null}">
                                                                                    <form action="/cart/add" method="post" class="formAddToCart">
                                                                                        <input type="hidden" name="productId" value="${product.productId}">
                                                                                        <button class="add-to-cart" type="submit">
                                                                                            <i class="fa fa-shopping-cart" aria-hidden="true"></i>
                                                                                        </button>
                                                                                    </form>
                                                                                    <form action="/favorites/add" method="post" class="addToWishlist">
                                                                                        <input type="hidden" name="productId" value="${product.productId}">
                                                                                        <button class="add-to-Wishlist" type="submit">
                                                                                            <i class="fa fa-heart" aria-hidden="true"></i>
                                                                                        </button>
                                                                                    </form>
                                                                                </c:if>
                                                                                <c:if test="${user == null}">
                                                                                    <a class="addToWishlist" href="LoginAndRegistration/user-login.jsp" data-rel="${product.productId}">
                                                                                        <i class="fa fa-heart" aria-hidden="true"></i>
                                                                                    </a>
                                                                                </c:if>
                                                                                <a href="/product/${product.productId}" class="quick-view hidden-sm-down">
                                                                                    <i class="fa fa-eye" aria-hidden="true"></i>
                                                                                </a>
                                                                            </div>
                                                                        </div>
																		
																	</div>
																	
																</div>
															</div>
														</div>
													</c:forEach>
												</div>
											</div>
										</div>

										<!-- pagination -->
										<div class="pagination">
											<div class="js-product-list-top">
												<div class="d-flex justify-content-around row">
													<div class="showing col col-xs-12">
														<span>SHOWING 1-3 OF 3 ITEM(S)</span>
													</div>
													<div class="page-list col col-xs-12">
														<ul>
															<li><a rel="prev" href="#"
																class="previous disabled js-search-link"> Previous </a></li>
															<li class="current active"><a rel="nofollow"
																href="#" class="disabled js-search-link"> 1 </a></li>
															<li><a rel="nofollow" href="#"
																class="disabled js-search-link"> 2 </a></li>
															<li><a rel="nofollow" href="#"
																class="disabled js-search-link"> 3 </a></li>
															<li><a rel="next" href="#"
																class="next disabled js-search-link"> Next </a></li>
														</ul>
													</div>
												</div>
											</div>
										</div>

									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<!-- Include Footer -->


	<!-- back to top -->
	<div class="back-to-top">
		<a href="#"> <i class="fa fa-long-arrow-up"></i></a>
	</div>

	<jsp:include page="includes/footer.jsp" />
</body>
</html>
