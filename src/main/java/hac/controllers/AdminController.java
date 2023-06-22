package hac.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

}
