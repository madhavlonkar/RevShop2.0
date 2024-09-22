<%@ page import="java.util.List"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page import="com.revshop.master.ReviewMaster"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Product Details</title>
<jsp:include page="includes/commoncss.jsp" />
<style>
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

@
-webkit-keyframes fadein {
	from {bottom: 0;
	opacity: 0;
}

to {
	bottom: 30px;
	opacity: 1;
}

}
@
keyframes fadein {
	from {bottom: 0;
	opacity: 0;
}

to {
	bottom: 30px;
	opacity: 1;
}

}
@
-webkit-keyframes fadeout {
	from {bottom: 30px;
	opacity: 1;
}

to {
	bottom: 0;
	opacity: 0;
}

}
@
keyframes fadeout {
	from {bottom: 30px;
	opacity: 1;
}

to {
	bottom: 0;
	opacity: 0;
}

}
.logo {
	font-family: Arial, sans-serif;
	font-weight: 750;
	font-size: 24px;
	color: #000000;
}
</style>
</head>

<body id="product-detail">

	<jsp:include page="includes/header.jsp" />

	<!-- main content -->
	<div class="main-content">
		<div id="wrapper-site">
			<div id="content-wrapper">
				<div id="main">
					<div class="page-home">

						<!-- breadcrumb -->
						<nav class="breadcrumb-bg">
							<div class="container no-index">
								<div class="breadcrumb">
									<ol>
										<li><a href="/home"> <span>Home</span></a></li>
										<li><a href="#"> <span>Product Details</span></a></li>
										<!-- Display the selected category -->
									</ol>
								</div>
							</div>
						</nav>
						<div class="container">
							<div class="content">
								<div class="row">
									<div
										class="sidebar-3 sidebar-collection col-lg-3 col-md-3 col-sm-4">

										<!-- category -->
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
													<a class="cateItem" href="/home?category=Home Appliances">Home
														Appliances</a>
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
									<div class="col-sm-8 col-lg-9 col-md-9">
										<div class="main-product-detail">
											<h2>${product.productName}</h2>
											<div class="product-single row">
												<div class="product-detail col-xs-12 col-md-5 col-sm-5">
													<div class="page-content" id="content">
														<div class="images-container">
															<div class="js-qv-mask mask tab-content border">
																<div id="item1" class="tab-pane fade active in show">
																	<img src="${product.productImage}" alt="Product Image">
																</div>
															</div>
														</div>
													</div>
												</div>
												<div class="product-info col-xs-12 col-md-7 col-sm-7">
													<div class="detail-description">
														<div class="price-del">
															<span class="price" style="color: black;"> RS
																${Math.floor(product.productPrice - (product.productPrice * product.productDiscount / 100))}
															</span> <span class="original-price"
																style="text-decoration: line-through; color: red; margin-left: 10px;">
																RS ${Math.floor(product.productPrice)} </span> <span
																class="discount-percentage"
																style="color: green; font-weight: bold; margin-left: 10px;">
																(${Math.floor(product.productDiscount)}% OFF) </span>
														</div>
														<p class="description">${product.productDescription}</p>

														<div class="has-border cart-area">
															<div class="product-quantity">
																<div class="qty">
																	<div class="input-group">
																		<span class="add"> <c:choose>
																				<c:when test="${user != null}">
																					<!-- User is logged in -->
																					
																					<form action="/favorites/add" method="post"
																						class="addToWishlist" style="display: inline;">
																						<input type="hidden" name="productId"
																							value="${product.productId}">
																						<button class="btn btn-primary add-to-cart add-item" type="submit">
																							<i class="fa fa-heart" aria-hidden="true"></i>
																						</button>
																					</form>

																					<form action="/cart/add" method="post"
																						style="display: inline;">
																						<input type="hidden" name="productId"
																							value="${product.productId}"> <input
																							type="hidden" name="userId"
																							value="${user.userId}">
																						<button
																							class="btn btn-primary add-to-cart add-item"
																							type="submit">
																							<i class="fa fa-shopping-cart" aria-hidden="true"></i>
																							<span>Add to cart</span>
																						</button>
																					</form>
																				</c:when>
																				<c:otherwise>
																					<!-- User is not logged in -->
																					<a class="addToWishlist"
																						href="LoginAndRegistration/user-login.jsp"> <i
																						class="fa fa-heart" aria-hidden="true"></i>
																					</a>
																					<form action="LoginAndRegistration/user-login.jsp"
																						method="post" style="display: inline;">
																						<input type="hidden" name="productId"
																							value="${product.productId}">
																						<button
																							class="btn btn-primary add-to-cart add-item"
																							type="submit">
																							<i class="fa fa-shopping-cart" aria-hidden="true"></i>
																							<span>Add to cart</span>
																						</button>
																					</form>
																				</c:otherwise>
																			</c:choose>
																		</span>
																	</div>
																</div>
															</div>
															<div class="clearfix"></div>
															<p class="product-minimal-quantity"></p>
														</div>

														<div class="d-flex2 has-border">
															<div class="btn-group">
																<a href="#" id="share-button"> <i
																	class="zmdi zmdi-share"></i> <span>Share</span>
																</a> <a href="#" id="print-button" class="print"> <i
																	class="zmdi zmdi-print"></i> <span>Print</span>
																</a>
																<div class="rating-comment d-flex">
																	<div class="review-description d-flex">
																		<span>REVIEW :</span>
																		<div class="rating">
																			<div class="star-content">
																				<div class="star"></div>
																				<div class="star"></div>
																				<div class="star"></div>
																				<div class="star"></div>
																				<div class="star"></div>
																			</div>
																		</div>
																	</div>
																</div>
															</div>
														</div>

														<div class="d-flex2 has-border">
															<span class="float-left"> <span class="availb">&nbsp
																	Availability:</span> <span class="check"> <i
																	class="fa fa-check-square-o" aria-hidden="true"></i> <span
																	style="color: ${product.productStock > 0 ? 'green' : 'red'};">
																		${product.productStock > 0 ? 'IN STOCK' : 'OUT OF STOCK'}
																</span>
															</span>
															</span> <br>
														</div>

														<!-- Confirmation Message -->
														<p id="confirmation-message"
															style="color: green; display: none; margin-top: 10px;">Link
															copied to clipboard!</p>

														<!-- Toast Notification -->
														<div id="toast" class="toast">Link copied to
															clipboard</div>

														<div class="content">
															<p>
																Categories: <span class="content2">
																	${product.productCategory} </span>
															</p>
															<p>
																Tags: <span class="content2">
																	${product.productTags} </span>
															</p>
														</div>
													</div>
												</div>
											</div>

											<div class="review" id="review-section">
												<ul class="nav nav-tabs">
													<li class="active"><a data-toggle="tab" href="#review">Reviews
													</a></li>
												</ul>

												<div class="tab-content">
													<div id="review" class="tab-pane fade active show">
														<div class="spr-form">
															<div class="user-comment">
																<c:choose>
																	<c:when test="${not empty reviews}">
																		<c:forEach var="review" items="${reviews}">
																			<div class="spr-review">
																				<div class="spr-review-header">
																					<span class="spr-review-header-byline"> <strong>${review.user.firstName}</strong>
																						- <fmt:formatDate value="${review.reviewDate}"
																							pattern="dd MMM yyyy" />

																					</span>
																					<div class="rating">
																						<div class="star-content">
																							<c:forEach begin="1" end="5" var="star">
																								<span
																									style="color: ${star <= review.rating ? 'gold' : 'gray'};">&#9733;</span>
																							</c:forEach>
																						</div>
																					</div>
																				</div>
																				<div class="spr-review-content">
																					<p class="spr-review-content-body">${review.reviewContent}</p>
																				</div>
																			</div>
																		</c:forEach>
																	</c:when>
																	<c:otherwise>
																		<p>No reviews yet. Be the first to write a review!</p>
																	</c:otherwise>
																</c:choose>
															</div>
														</div>
														<c:choose>
															<c:when test="${hasPurchased}">
																<!-- Show the review form if the user has purchased the product -->
																<form id="write-review-section" method="post"
																	action="/product/${product.productId}/review"
																	class="new-review-form">
																	<input type="hidden" name="product_id"
																		value="${product.productId}">
																	<h3 class="spr-form-title">Write a review</h3>

																	<fieldset>
																		<div class="spr-form-review-rating">
																			<label class="spr-form-label">Your Rating</label>
																			<fieldset class="ratings">
																				<input type="radio" id="star5" name="rating"
																					value="5" /> <label class="full" for="star5"
																					title="Awesome - 5 stars"></label> <input
																					type="radio" id="star4" name="rating" value="4" />
																				<label class="full" for="star4"
																					title="Pretty good - 4 stars"></label> <input
																					type="radio" id="star3" name="rating" value="3" />
																				<label class="full" for="star3"
																					title="Meh - 3 stars"></label> <input type="radio"
																					id="star2" name="rating" value="2" /> <label
																					class="full" for="star2"
																					title="Kinda bad - 2 stars"></label> <input
																					type="radio" id="star1" name="rating" value="1" />
																				<label class="full" for="star1"
																					title="Sucks big time - 1 star"></label>
																			</fieldset>
																		</div>
																	</fieldset>

																	<fieldset>
																		<div class="spr-form-review-body">
																			<div class="spr-form-input">
																				<textarea name="reviewContent"
																					class="spr-form-input-textarea" rows="10"
																					placeholder="Write your comments here"></textarea>
																			</div>
																		</div>
																	</fieldset>

																	<div class="submit">
																		<input type="submit" name="addComment"
																			id="submitComment" class="btn btn-default"
																			value="Submit Review">
																	</div>
																</form>
															</c:when>

															<c:otherwise>
																<!-- Show a message prompting the user to purchase the product first -->
																<p>You need to purchase this product before leaving
																	a review.</p>
															</c:otherwise>
														</c:choose>



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

	<!-- back to top -->
	<div class="back-to-top">
		<a href="#"> <i class="fa fa-long-arrow-up"></i></a>
	</div>

	<script>
		document
				.getElementById('share-button')
				.addEventListener(
						'click',
						function(event) {
							event.preventDefault();

							// Copy the current URL to the clipboard
							navigator.clipboard
									.writeText(window.location.href)
									.then(
											function() {
												// Show the confirmation message
												var confirmationMessage = document
														.getElementById('confirmation-message');
												confirmationMessage.style.display = 'block';

												// Hide the message after a few seconds
												setTimeout(
														function() {
															confirmationMessage.style.display = 'none';
														}, 1000);
											},
											function(err) {
												console
														.error(
																'Could not copy text: ',
																err);
											});
						});

		document.getElementById('print-button').addEventListener('click',
				function(event) {
					event.preventDefault();
					// Trigger the print dialog
					window.print();
				});
	</script>

	<script>
		document
				.getElementById('submitComment')
				.addEventListener(
						'click',
						function(event) {
							// Prevent the form submission to validate
							event.preventDefault();

							// Validate Rating
							var ratingChecked = false;
							var ratingElements = document
									.getElementsByName('rating');
							for (var i = 0; i < ratingElements.length; i++) {
								if (ratingElements[i].checked) {
									ratingChecked = true;
									break;
								}
							}

							if (!ratingChecked) {
								alert("Please select a rating.");
								return false;
							}

							// Validate Name

							// Validate Email

							// Validate Review
							var review = document
									.querySelector('textarea[name="reviewContent"]').value
									.trim();
							if (review === "") {
								alert("Please write your review.");
								return false;
							}

							// If all validations pass, submit the form
							document.getElementById('write-review-section')
									.submit();
						});
	</script>

	<jsp:include page="includes/footer.jsp" />
</body>
</html>
