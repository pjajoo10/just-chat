package com.just_chat.auth_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class TokenController {

    private final OAuth2AuthorizedClientService clients;

    public TokenController(OAuth2AuthorizedClientService clients) {
        this.clients = clients;
    }

    @GetMapping("/tokens")
    public ResponseEntity<?> tokens(
            @AuthenticationPrincipal OidcUser user,
            Authentication authentication) {

        if (user == null || authentication == null) {
            return ResponseEntity.status(401).body(Map.of("error", "not_authenticated"));
        }

        OAuth2AuthorizedClient client = clients.loadAuthorizedClient("google", authentication.getName());

        if (client == null) {
            return ResponseEntity.status(404).body(Map.of("error", "authorized_client_not_found"));
        }

        String idToken = user.getIdToken().getTokenValue();
        String accessToken = client.getAccessToken().getTokenValue();
        String refreshToken = client.getRefreshToken() == null
                ? null
                : client.getRefreshToken().getTokenValue();

        Map<String, Object> out = new LinkedHashMap<>();
        out.put("id_token", idToken);
        out.put("access_token", accessToken);
        if (refreshToken != null) {
            out.put("refresh_token", refreshToken); // only include if present
        }

        return ResponseEntity.ok(out);
    }
}
