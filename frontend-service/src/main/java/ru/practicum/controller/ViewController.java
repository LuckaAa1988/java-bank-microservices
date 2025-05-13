package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import ru.practicum.client.ViewClient;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ViewController {

    private final ViewClient viewClient;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/registration")
    public String registration() {
        return "signup";
    }

    @GetMapping("/index")
    public Mono<String> indexPage(@AuthenticationPrincipal Jwt jwt,
                                  Model model) {
        String username = jwt.getClaim("preferred_username");
        var rates = viewClient.getRates().collectList();
        var user = viewClient.getUser(username);
        var accounts = viewClient.getAccounts(username).collectList();
        return Mono.zip(rates, user, accounts)
                .doOnNext(tuple -> {
                    model.addAttribute("currencies", tuple.getT1());
                    model.addAttribute("user", tuple.getT2());
                    model.addAttribute("accounts", tuple.getT3());
                    model.addAttribute("jwt", jwt.getTokenValue());
                }).thenReturn("index");
    }
}
