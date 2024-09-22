package com.revshop.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

    @GetMapping("/unauthorizedAccess")
    public String unauthorizedAccess() {
        return "unauthorizedAccess"; // This corresponds to unauthorizedAccess.jsp in /WEB-INF/views/
    }
}
