package com.just_chat.auth_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
public class MeController {
    @GetMapping("/me")
    public Object me(@AuthenticationPrincipal OidcUser user) {
        return user.getClaims(); // email, sub, name, etc.
    }
}
