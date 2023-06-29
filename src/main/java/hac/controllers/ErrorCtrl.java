package hac.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
    public class ErrorCtrl implements ErrorController {
    /**
     * @return the error page
     */
    @RequestMapping("/error")
    public String handleError() {
        return "error";
    }
}
