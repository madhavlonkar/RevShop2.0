package com.revshop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.revshop.master.FavoriteMaster;
import com.revshop.master.LoginMaster;
import com.revshop.master.ProductMaster;
import com.revshop.master.UserMaster;
import com.revshop.service.FavoriteService;
import com.revshop.service.LoginService;
import com.revshop.service.ProductService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/favorites")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private LoginService loginService;

    @Autowired
    private ProductService productService;

    @PostMapping("/add")
    public String addProductToFavorites(@RequestParam("productId") int productId, Model model) {
        LoginMaster loggedInUser = loginService.getLoggedInUserDetails();
        if (loggedInUser == null) {
            model.addAttribute("errorMessage", "User is not logged in.");
            return "error-page";
        }

        UserMaster user = loggedInUser.getUserId();
        ProductMaster product = productService.getProductById(productId);

        if (product == null) {
            model.addAttribute("errorMessage", "Product not found.");
            return "error-page";
        }

        if (favoriteService.isProductInWishlist(user.getUserId(), productId)) {
            model.addAttribute("message", "Product already in wishlist.");
        } else {
            favoriteService.addProductToFavorites(user.getUserId(), productId);
            model.addAttribute("message", "Product added to wishlist.");
        }

        return "redirect:/favorites/list";
    }

    @GetMapping("/list")
    public String getFavoritesByUser(HttpServletRequest request, Model model) {
        // Get the logged-in user from the session
        LoginMaster loggedInUser = loginService.getLoggedInUserDetails();
        if (loggedInUser == null) {
            model.addAttribute("errorMessage", "User is not logged in.");
            return "error-page";
        }

        // Get the JSESSIONID from the current session to send it along with the REST request
        String sessionId = request.getSession().getId();

        // URL of the REST API to get the user's favorites
        String url = "http://localhost:8081/api/favorites/list";
        RestTemplate restTemplate = new RestTemplate();

        // Set up headers, including the session ID
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Cookie", "JSESSIONID=" + sessionId);  // Include session ID as a cookie

        // Create the HttpEntity object with headers
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Make the REST call and handle the response
        try {
            ResponseEntity<List<FavoriteMaster>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<FavoriteMaster>>() {}
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                List<FavoriteMaster> favorites = response.getBody();
                if (favorites != null && !favorites.isEmpty()) {
                    model.addAttribute("favorites", favorites);  // Add favorites to the model
                } else {
                    model.addAttribute("message", "No favorite products found.");
                }
            } else {
                model.addAttribute("message", "No favorite products found.");
            }
        } catch (RestClientException e) {
            // Handle any exceptions that occur during the REST call
            model.addAttribute("errorMessage", "Error fetching data: " + e.getMessage());
        }

        return "favorites-view";  // Return the view name to display the favorites
    }



    @PostMapping("/remove")
    public String removeProductFromFavorites(@RequestParam("productId") int productId, Model model) {
        LoginMaster loggedInUser = loginService.getLoggedInUserDetails();
        if (loggedInUser == null) {
            model.addAttribute("errorMessage", "User is not logged in.");
            return "error-page";
        }

        UserMaster user = loggedInUser.getUserId();
        favoriteService.removeProductFromFavorites(user.getUserId(), productId);

        return "redirect:/favorites/list";
    }

    @PostMapping("/move-to-cart")
    public String moveProductToCart(@RequestParam int productId, Model model) {
        LoginMaster loggedInUser = loginService.getLoggedInUserDetails();
        if (loggedInUser == null) {
            model.addAttribute("errorMessage", "User is not logged in.");
            return "error-page";
        }

        UserMaster user = loggedInUser.getUserId();
        if (!favoriteService.isProductInWishlist(user.getUserId(), productId)) {
            model.addAttribute("errorMessage", "Product not in wishlist.");
            return "error-page";
        } else {
            favoriteService.moveProductToCart(user.getUserId(), productId);
            return "redirect:/cart/view";
        }
    }

    @GetMapping("/exists")
    public ResponseEntity<Boolean> isProductInWishlist(@RequestParam("productId") int productId) {
        LoginMaster loggedInUser = loginService.getLoggedInUserDetails();
        if (loggedInUser == null) {
            return new ResponseEntity<>(false, HttpStatus.OK);
        }

        UserMaster user = loggedInUser.getUserId();
        boolean exists = favoriteService.isProductInWishlist(user.getUserId(), productId);
        return new ResponseEntity<>(exists, HttpStatus.OK);
    }
}
