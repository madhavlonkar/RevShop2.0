package com.revshop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CartItemDTO {
	private int cartId;
	private int productId; // For linking to the product page
	private String productName; // For displaying the product name
	private double productPrice; // For calculating total price
	private String productImage; // For displaying the product image
	private double productDiscount; // For calculating the discount
	private int quantity; // For displaying and updating the quantity
	private String status; // For displaying the cart item status
}
