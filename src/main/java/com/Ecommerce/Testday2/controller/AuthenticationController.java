package com.Ecommerce.Testday2.controller;

import com.Ecommerce.Testday2.exception.InvalidCredentialsException;
import com.Ecommerce.Testday2.exception.UserAlreadyExistsException;
import com.Ecommerce.Testday2.model.User;
import com.Ecommerce.Testday2.service.AuthenticationService;
import com.Ecommerce.Testday2.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class AuthenticationController {

    private final UserService userService;
    private final AuthenticationService authenticationService;

    public AuthenticationController(UserService userService, AuthenticationService authenticationService){
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "users/login";
    }

    @GetMapping("/register")
    public String register(@NotNull Model model) {
        model.addAttribute("user", new User()); // Thêm một đối tượng User mới vào model
        return "users/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute @Valid User request, @NotNull BindingResult result, Model model) {
        if (result.hasErrors()) {
            var errors = result.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toArray(String[]::new);
            model.addAttribute("errors", errors);
            return "users/register";
        }

        try {
            authenticationService.register(request); // Save the user to the database
            return "redirect:/login"; // Redirect to login page after successful registration
        } catch (UserAlreadyExistsException e) {
            model.addAttribute("errorMessage", "Tài khoản đã tồn tại");
            return "users/register"; // Return to the "register" view if the user already exists
        } catch (Exception e) {
            model.addAttribute("errorMessage", "An error occurred while registering the user. Please try again.");
            return "users/register"; // Return to the "register" view if there's an error
        }
    }


}
