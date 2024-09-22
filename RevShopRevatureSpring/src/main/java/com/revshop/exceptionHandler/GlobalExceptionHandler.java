package com.revshop.exceptionHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception ex, Model model) {
        logger.error("General exception occurred: ", ex); // Log the exception for troubleshooting
        model.addAttribute("errorMessage", "An error occurred: " + ex.getMessage());
        return "error"; // Display a generic error page
    }

    @ExceptionHandler(UserNotFoundException.class)
    public String handleUserNotFoundException(UserNotFoundException ex, RedirectAttributes redirectAttributes) {
        logger.warn("User not found: {}", ex.getMessage()); // Log the warning
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        return "redirect:/getUsers"; // Redirect to user list with error message
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public String handleEmailAlreadyExistsException(EmailAlreadyExistsException ex, RedirectAttributes redirectAttributes) {
        logger.info("Email already exists: {}", ex.getMessage()); // Log the info
        redirectAttributes.addFlashAttribute("RegistererrorMessage", ex.getMessage());
        return "redirect:/register"; // Redirect back to the registration page
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public String handleUsernameAlreadyExistsException(UsernameAlreadyExistsException ex, RedirectAttributes redirectAttributes) {
        logger.info("Username already exists: {}", ex.getMessage()); // Log the info
        redirectAttributes.addFlashAttribute("RegistererrorMessage", ex.getMessage());
        return "redirect:/register"; // Redirect back to the registration page
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public void handleNoResourceFoundException(NoResourceFoundException ex) {
//        logger.warn("Static resource not found: {}", ex.getMessage()); // Log the warning but do nothing
    }
}
