package com.IAM.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    /**
     * Returns the authenticated user's roles.
     */
    @GetMapping("/roles")
    public List<String> getUserRoles(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }

    /**
     * Returns a custom message based on the highest role of the authenticated user.
     */
    @GetMapping("/welcome")
    public String getWelcomeMessage(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "Welcome, Guest!";
        }

        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        // Check roles without the "ROLE_" prefix
        if (roles.contains("ROLE_ecommerce-admin")) {
            return "Welcome, E-Commerce Admin!";
        } else if (roles.contains("ROLE_ecommerce-manager")) {
            return "Welcome, Manager!";
        } else if (roles.contains("ROLE_ecommerce-user")) {
            return "Welcome, User!";
        }

        return "Welcome, Unrecognized Role!";
    }

    /**
     * Returns the JWT claims for the authenticated user.
     */
    @GetMapping("/me")
    public String getUserDetails(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "No authenticated user.";
        }

        if (authentication.getPrincipal() instanceof Jwt) {
            Jwt jwt = (Jwt) authentication.getPrincipal();
            return "User details: " + jwt.getClaims().toString();
        }

        return "User details not available.";
    }

    /**
     * Checks if the authenticated user has the E-Commerce Admin role.
     */
    @GetMapping("/has-admin-role")
    public boolean hasAdminRole(Authentication authentication) {
        return hasRole(authentication, "ROLE_ecommerce-admin");
    }

    /**
     * Checks if the authenticated user has the Manager role.
     */
    @GetMapping("/has-manager-role")
    public boolean hasManagerRole(Authentication authentication) {
        return hasRole(authentication, "ROLE_ecommerce-manager");
    }

    /**
     * Checks if the authenticated user has the User role.
     */
    @GetMapping("/has-user-role")
    public boolean hasUserRole(Authentication authentication) {
        return hasRole(authentication, "ROLE_ecommerce-user");
    }

    /**
     * Helper method to check if a specific role exists in the authenticated user's authorities.
     */
    private boolean hasRole(Authentication authentication, String role) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role::equals);
    }
}
