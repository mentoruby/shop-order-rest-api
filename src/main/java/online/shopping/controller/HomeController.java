package online.shopping.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// @RestController combines @Controller and @ResponseBody, simplify the need to annotate every request handling method
@RestController
public class HomeController {
	
	@RequestMapping(path="/")
    public String home() {
        return "<h1>Happy Shopping!<h1>";
    }
}
