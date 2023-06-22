package hac.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;


/*
לשאול איזה קבצים בידיוק צריך לבוטסראפ
איך חוזרים לאותו דף מהלוגין

 */
@Controller
public class AdminController {

    @GetMapping("/admin")
    public String showAdmin() {

        return "admin";
    }

    @GetMapping("/admin/search")
    public String handleSearch(@RequestParam String searchType, @RequestParam String searchTerm, Model model) {
        // handle your search logic here...
        model.addAttribute("searchType", searchType);
        model.addAttribute("searchTerm", searchTerm);
        return "results";
    }

}
