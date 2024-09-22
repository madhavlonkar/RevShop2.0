<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Edit Product</title>
<jsp:include page="../includes/commoncss.jsp" />
<style>
body {
	font-family: 'Montserrat', sans-serif;
	background-color: #fff;
	color: #333;
}

.container {
	display: flex;
	justify-content: space-between;
	max-width: 1200px;
	margin: 0 auto;
	padding: 20px;
	align-items: flex-start; /* Align items to the top */
}

.form-section {
	width: 60%; /* Take up 60% of the width */
	margin-right: 20px; /* Add some space between the form and the image */
}

h2 {
	font-weight: 600;
	margin-bottom: 10px;
	color: #d35f8d;
	font-size: 28px;
}

p {
	margin-bottom: 20px;
	color: #666;
}

.form-row {
	display: flex;
	justify-content: space-between;
}

.form-group {
	width: 48%;
	margin-bottom: 15px;
}

.form-group label {
	display: block;
	margin-bottom: 5px;
	font-weight: 600;
	color: #333;
	font-size: 16px;
}

.form-group input, .form-group select, .form-group textarea {
	width: 100%;
	padding: 12px;
	border: 1px solid #ddd;
	border-radius: 4px;
	font-size: 14px;
	background-color: #f9f9f9;
	transition: all 0.3s ease-in-out;
}

.form-group select {
	-webkit-appearance: none;
	-moz-appearance: none;
	appearance: none;
	background-image:
		url('data:image/svg+xml;charset=US-ASCII,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 4 5"><path fill="#666" d="M2 0L0 2h4zm0 5L0 3h4z"/></svg>');
	background-repeat: no-repeat;
	background-position: right 12px top 50%;
	background-size: 10px;
}

.form-group select:hover, .form-group input:focus, .form-group textarea:focus
	{
	border-color: #d35f8d;
	background-color: #fff;
	box-shadow: 0 0 5px rgba(211, 95, 141, 0.5);
}

.form-group textarea {
	resize: vertical;
	height: 120px;
}

.checkout-btn {
	display: block;
	width: 100%;
	padding: 15px;
	background-color: #d35f8d;
	color: #fff;
	font-size: 18px;
	text-align: center;
	border: none;
	border-radius: 4px;
	cursor: pointer;
	margin-top: 20px;
	transition: background-color 0.3s;
}

.checkout-btn:hover {
	background-color: #b54c6d;
}

.back-btn {
	display: inline-block;
	margin-top: 15px;
	color: #d35f8d;
	font-size: 16px;
	text-decoration: none;
}

.back-btn:hover {
	text-decoration: underline;
}

.cart-section {
	width: 35%; /* Set the width of the image preview section */
	padding-left: 20px;
	display: flex;
	flex-direction: column;
	align-items: center;
}

.cart-section h3 {
	font-size: 22px;
	color: #d35f8d;
	font-weight: 600;
	margin-bottom: 15px;
}

#previewImage {
	width: 100%;
	height: auto;
	border-radius: 8px;
	border: 2px solid #ddd;
	box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1);
	padding: 10px;
	background-color: #f9f9f9;
}
</style>
</head>
<body id="product-sidebar-left" class="product-grid-sidebar-left">

	<jsp:include page="../includes/header.jsp" />

	<div class="main-content">
		<div id="wrapper-site">
			<div id="content-wrapper" class="full-width">
				<div id="main">
					<div class="page-home">
						<nav class="breadcrumb-bg">
							<div class="container no-index">
								<div class="breadcrumb">
									<ol>
										<li><a href="#"><span>Welcome</span></a></li>
										<li><a href="#"><span>Edit Product</span></a></li>
									</ol>
								</div>
							</div>
						</nav>
						<div class="container">
							<div class="form-section">
								<h2>Edit Product</h2>
								<p>Fill in the details to update the product in the system</p>
								<form
									action="${pageContext.request.contextPath}/products/update"
									method="POST" enctype="multipart/form-data">
									<input type="hidden" name="productId"
										value="${product.productId}" /> <input type="hidden"
										name="sellerId" value="${seller.userId}" />

									<!-- Product Name and Category Fields -->
									<div class="form-row">
										<!-- Product Name -->
										<div class="form-group">
											<label for="productName">Product Name *</label> <input
												type="text" id="productName" name="productName" required
												value="${product.productName}">
										</div>
										<!-- Product Category -->
										<div class="form-group">
											<label for="productCategory">Category *</label> <select
												id="productCategory" name="productCategory" required>
												<option value="" disabled selected>Select Type</option>
												<option value="electronics"
													${product.productCategory == 'electronics' ? 'selected' : ''}>Electronics</option>
												<option value="fashion"
													${product.productCategory == 'fashion' ? 'selected' : ''}>Fashion</option>
												<option value="HomeAppliances"
													${product.productCategory == 'HomeAppliances' ? 'selected' : ''}>Home
													Appliances</option>
												<option value="Books"
													${product.productCategory == 'Books' ? 'selected' : ''}>Books</option>
												<option value="Sports"
													${product.productCategory == 'Sports' ? 'selected' : ''}>Sports</option>
											</select>
										</div>
									</div>

									<!-- Product Price and Discount Fields -->
									<div class="form-row">
										<!-- Product Price -->
										<div class="form-group">
											<label for="productPrice">Price *</label> <input
												type="number" id="productPrice" name="productPrice"
												step="0.01" required value="${product.productPrice}">
										</div>
										<!-- Product Discount -->
										<div class="form-group">
											<label for="productDiscount">Discount</label> <input
												type="number" id="productDiscount" name="productDiscount"
												step="0.01" value="${product.productDiscount}">
										</div>
									</div>

									<!-- Product Stock and Brand Fields -->
									<div class="form-row">
										<div class="form-group">
											<label for="productStock">Stock Quantity *</label> <input
												type="number" id="productStock" name="productStock" required
												value="${product.productStock}">
										</div>
										<div class="form-group">
											<label for="productBrand">Brand *</label> <input type="text"
												id="productBrand" name="productBrand" required
												value="${product.productBrand}">
										</div>
									</div>

									<!-- Product Description -->
									<div class="form-group">
										<label for="productDescription">Product Description *</label>
										<textarea id="productDescription" name="productDescription"
											required>${product.productDescription}</textarea>
									</div>

									<!-- Product Status -->
									<div class="form-row">
										<div class="form-group">
											<label for="productStatus">Status *</label> <select
												id="productStatus" name="productStatus" required>
												<option value="In_Stock"
													${product.productStatus == 'In_Stock' ? 'selected' : ''}>In
													Stock</option>
												<option value="Out_of_Stock"
													${product.productStatus == 'Out_of_Stock' ? 'selected' : ''}>Out
													of Stock</option>
											</select>
										</div>

										<!-- Product Image -->
										<div class="form-group">
											<label for="productImages">Product Image *</label> <input
												type="file" id="productImages" name="productImages"
												accept="image/*">
										</div>
									</div>
									<!-- Threshold -->
									<div class="form-row">
										<div class="form-group">
											<label for="threshold">Threshold</label> <input type="number"
												id="threshold" name="threshold" value="${product.threshold}">
										</div>
										<div class="form-group">
											<label for="productTags">Product Tags (comma
												separated)</label> <input type="text" id="productTags"
												name="productTags" value="${product.productTags}">
										</div>
									</div>
									<!-- Hidden field to store the current image URL -->
									<input type="hidden" name="currentImageUrl"
										id="currentImageUrl" value="${product.productImage}" />

									<!-- Submit button -->
									<button type="submit" class="checkout-btn btn btn-primary">Update
										Product</button>
								</form>
								<a href="/dashboard" class="back-btn">Back to
									Dashboard</a>
							</div>

							<!-- Image preview on the right side -->
							<div class="cart-section">
								<h3>PREVIEW IMAGE</h3>
								<img id="previewImage" src="${product.productImage}"
									alt="No image uploaded">
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<jsp:include page="../includes/footer.jsp" />

	<script>
		const productImageInput = document.getElementById('productImages');
		const previewImage = document.getElementById('previewImage');
		const currentImageUrlInput = document.getElementById('currentImageUrl');

		// Show current image in preview section on load
		previewImage.src = currentImageUrlInput.value;

		productImageInput.addEventListener('change', function(e) {
			const file = e.target.files[0];
			const reader = new FileReader();
			reader.onload = function() {
				previewImage.src = reader.result; // New image preview
			};
			if (file) {
				reader.readAsDataURL(file);
			}
		});

		// On form submission, keep current image if no new image is uploaded
		document.querySelector('form').addEventListener('submit', function(e) {
			if (!productImageInput.files.length) {
				const input = document.createElement('input');
				input.type = 'hidden';
				input.name = 'productImage';
				input.value = currentImageUrlInput.value; // Keep the current image URL
				this.appendChild(input);
			}
		});
	</script>
	
	<script>
// Function to validate form fields
function validateForm() {
    let isValid = true;

    // Get form fields
    const productName = document.getElementById('productName').value.trim();
    const productCategory = document.getElementById('productCategory').value;
    const productPrice = document.getElementById('productPrice').value;
    const productStock = document.getElementById('productStock').value;
    const productBrand = document.getElementById('productBrand').value.trim();
    const productImageInput = document.getElementById('productImages');
    const currentImageUrl = document.getElementById('currentImageUrl').value;

    // Reset previous error messages
    document.querySelectorAll('.error-message').forEach(function(el) {
        el.remove();
    });

    // Validate Product Name
    if (productName === "") {
        showErrorMessage('productName', 'Product Name is required');
        isValid = false;
    }

    // Validate Product Category
    if (productCategory === "") {
        showErrorMessage('productCategory', 'Product Category is required');
        isValid = false;
    }

    // Validate Product Price (must be a positive number)
    if (productPrice === "" || isNaN(productPrice) || parseFloat(productPrice) <= 0) {
        showErrorMessage('productPrice', 'Please enter a valid positive price');
        isValid = false;
    }

    // Validate Product Stock (must be a non-negative integer)
    if (productStock === "" || isNaN(productStock) || parseInt(productStock) < 0) {
        showErrorMessage('productStock', 'Please enter a valid stock quantity');
        isValid = false;
    }

    // Validate Product Brand
    if (productBrand === "") {
        showErrorMessage('productBrand', 'Product Brand is required');
        isValid = false;
    }

    // Validate Product Image (if no current image and no new image uploaded)
    if (!currentImageUrl && !productImageInput.files.length) {
        showErrorMessage('productImages', 'Please upload a product image');
        isValid = false;
    }

    return isValid;
}

// Function to display error messages
function showErrorMessage(fieldId, message) {
    const field = document.getElementById(fieldId);
    const error = document.createElement('div');
    error.className = 'error-message';
    error.style.color = 'red';
    error.style.marginTop = '5px';
    error.textContent = message;
    field.parentElement.appendChild(error);
}

// Attach the validation function to the form's submit event
document.querySelector('form').addEventListener('submit', function(e) {
    if (!validateForm()) {
        e.preventDefault(); // Prevent form submission if validation fails
    }
});
</script>
	

</body>
</html>
