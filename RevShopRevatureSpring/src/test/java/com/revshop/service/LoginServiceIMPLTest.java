package com.revshop.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.revshop.dao.LoginDAO;
import com.revshop.exceptionHandler.EmailAlreadyExistsException;
import com.revshop.exceptionHandler.UsernameAlreadyExistsException;
import com.revshop.master.LoginMaster;
import com.revshop.master.UserMaster;
import com.revshop.service.impl.LoginServiceIMPL;

public class LoginServiceIMPLTest {

    @Mock
    private LoginDAO loginDAO;

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private LoginServiceIMPL loginService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser_Success() {
        LoginMaster user = new LoginMaster();
        user.setUserName("testUser");
        user.setEmail("test@example.com");
        user.setPassword("password");

        when(loginDAO.existsByUsername(user.getUserName())).thenReturn(false);
        when(loginDAO.existsByEmail(user.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");

        UserMaster savedUser = new UserMaster();
        savedUser.setEmail(user.getEmail());
        when(userService.saveUser(any(UserMaster.class))).thenReturn(savedUser);

        when(loginDAO.insert(user)).thenReturn(true);

        boolean result = loginService.registerUser(user);

        assertTrue(result);
        verify(passwordEncoder, times(1)).encode(user.getPassword());
        verify(loginDAO, times(1)).insert(user);
    }

    @Test
    void testRegisterUser_UsernameAlreadyExists() {
        LoginMaster user = new LoginMaster();
        user.setUserName("existingUser");
        user.setEmail("test@example.com");

        when(loginDAO.existsByUsername(user.getUserName())).thenReturn(true);

        assertThrows(UsernameAlreadyExistsException.class, () -> loginService.registerUser(user));
    }

    @Test
    void testRegisterUser_EmailAlreadyExists() {
        LoginMaster user = new LoginMaster();
        user.setUserName("testUser");
        user.setEmail("existing@example.com");

        when(loginDAO.existsByEmail(user.getEmail())).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class, () -> loginService.registerUser(user));
    }

    @Test
    void testGetAllUser_Success() {
        List<LoginMaster> users = List.of(new LoginMaster(), new LoginMaster());
        when(loginDAO.reterieveAll()).thenReturn(users);

        List<LoginMaster> result = loginService.getAllUser();

        assertEquals(2, result.size());
    }

    @Test
    void testDeleteUser_Success() {
        int userId = 1;
        loginService.deleteUser(userId);
        verify(loginDAO, times(1)).delete(userId);
    }

    @Test
    void testExistsByUsername_Success() {
        String username = "testUser";
        when(loginDAO.existsByUsername(username)).thenReturn(true);

        boolean result = loginService.existsByUsername(username);

        assertTrue(result);
    }

    @Test
    void testExistsByEmail_Success() {
        String email = "test@example.com";
        when(loginDAO.existsByEmail(email)).thenReturn(true);

        boolean result = loginService.existsByEmail(email);

        assertTrue(result);
    }

    @Test
    void testGetLoggedInUserDetails_OAuth2User() {
        OAuth2User oAuth2User = mock(OAuth2User.class);
        when(oAuth2User.getAttributes()).thenReturn(Map.of("email", "oauth2user@example.com"));

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(oAuth2User);
        SecurityContextHolder.setContext(securityContext);

        LoginMaster expectedUser = new LoginMaster();
        expectedUser.setEmail("oauth2user@example.com");
        when(loginDAO.findByEmail("oauth2user@example.com")).thenReturn(expectedUser);

        LoginMaster result = loginService.getLoggedInUserDetails();

        assertEquals(expectedUser.getEmail(), result.getEmail());
    }

    @Test
    void testGetLoggedInUserDetails_CustomUser() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("customuser@example.com");
        SecurityContextHolder.setContext(securityContext);

        LoginMaster expectedUser = new LoginMaster();
        expectedUser.setEmail("customuser@example.com");
        when(loginDAO.findByEmail("customuser@example.com")).thenReturn(expectedUser);

        LoginMaster result = loginService.getLoggedInUserDetails();

        assertEquals(expectedUser.getEmail(), result.getEmail());
    }

    @Test
    void testIsFirstLogin_True() {
        String email = "test@example.com";
        LoginMaster user = new LoginMaster();
        user.setFirstLogin(true);
        when(loginDAO.findByEmail(email)).thenReturn(user);

        boolean result = loginService.isFirstLogin(email);

        assertTrue(result);
    }

    @Test
    void testIsFirstLogin_False() {
        String email = "test@example.com";
        LoginMaster user = new LoginMaster();
        user.setFirstLogin(false);
        when(loginDAO.findByEmail(email)).thenReturn(user);

        boolean result = loginService.isFirstLogin(email);

        assertFalse(result);
    }

    @Test
    void testFindByEmail_Success() {
        String email = "test@example.com";
        LoginMaster expectedUser = new LoginMaster();
        expectedUser.setEmail(email);
        when(loginDAO.findByEmail(email)).thenReturn(expectedUser);

        LoginMaster result = loginService.findByEmail(email);

        assertEquals(expectedUser.getEmail(), result.getEmail());
    }
}
