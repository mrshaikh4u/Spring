package com.rs4u.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

	@RequestMapping(value = { "/", "/home" })
	public String home() {
		return "home";
	}

	@RequestMapping("/secure")
	public @ResponseBody Principal user(Principal principal) {
		return principal;
	}
}
