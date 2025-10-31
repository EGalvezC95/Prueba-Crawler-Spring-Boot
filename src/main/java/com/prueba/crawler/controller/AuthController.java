package com.prueba.crawler.controller;

import com.prueba.crawler.service.UsuarioService;
import com.prueba.crawler.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {
    @Autowired
    private UsuarioService userService;

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            Model model) {
        if (error != null) model.addAttribute("errorMsg", "Credenciales inválidas");
        if (logout != null) model.addAttribute("msg", "Sesión cerrada");
        return "login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new Usuario());
        return "register";
    }

    @PostMapping("/register")
    public String registerSubmit(@ModelAttribute("user") Usuario user, Model model) {
        try {
            userService.registerNewUser(user.getUsername(), user.getPassword());
            return "redirect:/login?registered";
        } catch (Exception ex) {
            model.addAttribute("errorMsg", ex.getMessage());
            return "register";
        }
    }
}
