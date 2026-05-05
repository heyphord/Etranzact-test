package com.gmfs.demo.controller;

import com.gmfs.demo.dto.ChangePinRequest;
import com.gmfs.demo.dto.CustomerLoginRequest;
import com.gmfs.demo.dto.LoginResponse;
import com.gmfs.demo.dto.SignupRequest;
import com.gmfs.demo.dto.SignupResponse;
import com.gmfs.demo.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final AuthService authService;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public SignupResponse signup(@Valid @RequestBody SignupRequest request) {
        AuthService.SignupResult result = authService.signup(
                request.getFirstName(),
                request.getLastName(),
                request.getDob(),
                request.getGhanacardNumber(),
                request.getPin()
        );
        return new SignupResponse(result.customerId(), result.primaryAccountId());
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody CustomerLoginRequest request) {
        AuthService.LoginResult result = authService.login(request.getGhanacardNumber(), request.getPin());
        return new LoginResponse(result.token(), result.customerId(), result.expiresAt());
    }

    @PatchMapping("/pin")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePin(
            @RequestHeader("X-Auth-Token") String authToken,
            @Valid @RequestBody ChangePinRequest request
    ) {
        authService.changePin(authToken, request.getCurrentPin(), request.getNewPin());
    }
}
