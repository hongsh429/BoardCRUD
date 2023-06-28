package firstweek.board.controller;


import firstweek.board.dto.LoginRequestDto;
import firstweek.board.dto.SignupRequestDto;
import firstweek.board.entity.User;
import firstweek.board.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @PostMapping("/user/signup")
    public Map<String, Object> signup(@Validated @RequestBody SignupRequestDto dto, BindingResult bindingResult) {
        Map<String, Object> map = new HashMap<>();
        if (bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                String field = fieldError.getField();
                String rejectedValue = String.valueOf(fieldError.getRejectedValue());
                String defaultMessage = fieldError.getDefaultMessage();
                String code = fieldError.getCode();
                log.info("code [{}] field : {}, Message : {}, prior value: {}", code, field, defaultMessage, rejectedValue);
            }
            return null;
        }

        return userService.signup(dto);
    }

    @PostMapping("/user/login")
    public Map<String, Object> login(HttpServletResponse response,
                      @Validated @RequestBody LoginRequestDto dto,
                      BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                String field = fieldError.getField();
                String rejectedValue = String.valueOf(fieldError.getRejectedValue());
                String defaultMessage = fieldError.getDefaultMessage();
                String code = fieldError.getCode();
                log.info("code [{}] field : {}, Message : {}, prior value: {}", code, field, defaultMessage, rejectedValue);
            }
            return null;
        }
        return userService.login(dto, response);
    }
}
