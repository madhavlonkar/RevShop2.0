<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
<%@ page import="com.revshop.master.LoginMaster"%>

<header>
	<div class="header-top d-xs-none">
		<div class="container">
			<div class="row">
				<!-- logo -->
				<div class="col-sm-2 col-md-2 d-flex align-items-center">
					<div id="logo">
						<a href="/home?category=Electronics"> <span class="logo">RevShop</span>
						</a>
					</div>
				</div>

				<!-- menu -->
				<div
					class="col-sm-5 col-md-5 align-items-center justify-content-center navbar-expand-md main-menu">
					<div class="menu navbar collapse navbar-collapse">
						<ul class="menu-top navbar-nav">
							<li><a href="/home?category=Electronics" class="parent">Home</a></li>
							<li><a href="/home?category=Electronics" class="parent">Categories</a>
								<div class="dropdown-menu">
									<ul>
										<li class="item"><a href="/home?category=Electronics"
											title="Electronics">Electronics</a></li>
										<li class="item"><a href="/home?category=Fashion"
											title="Fashion">Fashion</a></li>
										<li class="item"><a href="/home?category=HomeAppliances"
											title="Home Appliances">Home Appliances</a></li>
										<li class="item"><a href="/home?category=Books"
											title="Books">Books</a></li>
										<li class="item"><a href="/home?category=Sports"
											title="Sports">Sports</a></li>
									</ul>
								</div></li>
							<li><a href="/orders/myOrders" class="parent">Order
									History</a></li>
							<li><a href="/favorites/list" class="parent">Favorites</a></li>
						</ul>
					</div>
				</div>

				<!-- search and account -->
				<div
					class="col-sm-5 col-md-5 d-flex align-items-center justify-content-end"
					id="search_widget">
					<!-- Search Bar -->
					<form method="get" action="/home">
						<input type="text" name="s" value="" placeholder="Search"
							class="ui-autocomplete-input" autocomplete="off">
						<button type="submit">
							<i class="fa fa-search"></i>
						</button>
					</form>

					<!-- Account Info -->
					<div id="block_myaccount_infos" class="hidden-sm-down dropdown">
						<div class="myaccount-title">
							<a href="#acount" data-toggle="collapse" class="acount"> <i
								class="fa fa-user" aria-hidden="true"></i> <span>Account</span>
								<i class="fa fa-angle-down" aria-hidden="true"></i>
							</a>
						</div>
						<div id="acount" class="collapse">
							<div class="account-list-content">
								<!-- If the user is authenticated -->
								<sec:authorize access="isAuthenticated()">
									<div>
										<a class="login" href="/account" rel="nofollow"
											title="My Account"> <i class="fa fa-cog"></i> <span>My
												Account</span>
										</a>
									</div>
									<div>
										<a class="check-out" href="/checkout/view" rel="nofollow"
											title="Checkout"> <i class="fa fa-check"
											aria-hidden="true"></i> <span>Checkout</span>
										</a>
									</div>

									<!-- Seller Dashboard Link (for sellers only) -->
									<sec:authorize access="hasRole('seller')">
										<div>
											<a class="seller-dashboard" href="/dashboard" rel="nofollow"
												title="Seller Dashboard"> <i
												class="fa fa-tachometer-alt"></i> <span>Seller
													Dashboard</span>
											</a>
										</div>
									</sec:authorize>

									<div>
										<a href="/logout" title="Log Out"> <i
											class="fa fa-sign-out"></i> <span>Log Out</span>
										</a>
									</div>
								</sec:authorize>

								<!-- If the user is not authenticated -->
								<sec:authorize access="!isAuthenticated()">
									<div>
										<a class="login" href="/login" rel="nofollow"
											title="Log in to your account"> <i class="fa fa-sign-in"></i>
											<span>Sign in</span>
										</a>
									</div>
									<div>
										<a class="register" href="/register" rel="nofollow"
											title="Register Account"> <i class="fa fa-user"></i> <span>Register
												Account</span>
										</a>
									</div>
								</sec:authorize>
							</div>
						</div>
					</div>

					<!-- Cart Info -->
					<sec:authorize access="isAuthenticated()">
						<div class="desktop_cart">
							<div class="blockcart block-cart cart-preview tiva-toggle">
								<div class="header-cart tiva-toggle-btn">
									<span class="cart-products-count">1</span> <a href="/cart/view">
										<i class="fa fa-shopping-cart" aria-hidden="true"></i>
									</a>
								</div>
							</div>
						</div>
					</sec:authorize>
					<sec:authorize access="!isAuthenticated()">
						<div class="desktop_cart">
							<div class="blockcart block-cart cart-preview tiva-toggle">
								<div class="header-cart tiva-toggle-btn">
									<span class="cart-products-count">1</span> <a href="/login">
										<i class="fa fa-shopping-cart" aria-hidden="true"></i>
									</a>
								</div>
							</div>
						</div>
					</sec:authorize>
				</div>
			</div>
		</div>
	</div>
</header>
