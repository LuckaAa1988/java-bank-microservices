package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
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

    @GetMapping
    public Mono<String> indexPage(ServerWebExchange exchange,
                                  Model model) {
        var principal = exchange.getPrincipal()
                .map(Principal::getName).switchIfEmpty(Mono.just("Test")); //TODO
        var rates = viewClient.getRates().collectList();
        var user = principal.flatMap(viewClient::getUser);
        var accounts = principal.map(viewClient::getAccounts);
        return Mono.zip(rates, user, accounts)
                .doOnNext(tuple -> {
                    model.addAttribute("currencies", tuple.getT1());
                    model.addAttribute("user", tuple.getT2());
                    model.addAttribute("accounts", tuple.getT3());
                }).thenReturn("index");
    }
}
