package com.mettre.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
public class SecurityController {

    @Autowired
    private IUserService userService;

    @GetMapping(value = "/list")
    @PreAuthorize("hasPermission('user', 'read') or hasRole('ROLE_ADMINISTRATOR')")
    public List<?> getUserList(@RequestParam(value = "text", defaultValue = "") String text,
                               @RequestParam(value = "page", defaultValue = "0") int page,
                               @RequestParam(value = "size", defaultValue = "20") int size) {
        return userService.list(text, page, size);
    }
}
