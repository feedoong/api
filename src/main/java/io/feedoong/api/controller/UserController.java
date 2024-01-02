package io.feedoong.api.controller;

import io.feedoong.api.controller.response.Response;
import io.feedoong.api.service.UserService;
import io.feedoong.api.service.dto.GoogleLoginInfoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;

    @PostMapping("/v2/users/login/google")
    public Response<GoogleLoginInfoDTO> login(@RequestParam String accessToken) {
        GoogleLoginInfoDTO googleLoginInfoDTO = userService.googleLogin(accessToken);
        return new Response<>(googleLoginInfoDTO);
    }
}
