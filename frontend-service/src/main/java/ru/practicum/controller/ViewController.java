package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Mono;
import ru.practicum.client.ViewClient;

@Controller
@RequiredArgsConstructor
public class ViewController {

    private final ViewClient viewClient;

    @GetMapping("/greet")
    public String loginPage() {
        return "greet";
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
