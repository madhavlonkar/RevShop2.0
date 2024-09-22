package com.revshop.controller.restcontroller;

import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.revshop.master.FavoriteMaster;
import com.revshop.master.LoginMaster;
import com.revshop.master.UserMaster;
import com.revshop.service.FavoriteService;
import com.revshop.service.LoginService;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteRestController {

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private LoginService loginService;

    @GetMapping(value = "/list", produces = "application/json")
    public ResponseEntity<List<FavoriteMaster>> getFavoritesByUser() {
        LoginMaster loggedInUser = loginService.getLoggedInUserDetails();
        if (loggedInUser == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        UserMaster user = loggedInUser.getUserId();
        List<FavoriteMaster> favorites = favoriteService.getFavoritesByUserId(user.getUserId());
        favorites.forEach(fav -> Hibernate.initialize(fav.getProduct()));

        if (favorites.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(favorites, HttpStatus.OK);
    }

}
