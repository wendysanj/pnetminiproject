package com.java.proj.pnet;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class PnetController {

		@GetMapping("/greeting")
	public String addIndex (@RequestParam(name="name", 
		required=false, 
		defaultValue="World") 	
		String name, Model model) {
		
		model.addAttribute("name", name);
		return "index";
	}

 
	 
	
	
}
