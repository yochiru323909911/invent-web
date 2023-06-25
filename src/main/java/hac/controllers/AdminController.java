package hac.controllers;

import hac.repo.entities.Design;
import hac.repo.repositories.DesignRepository;
import hac.util.UserDesignCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;


/*
לשאול איזה קבצים בידיוק צריך לבוטסראפ
איך חוזרים לאותו דף מהלוגין

 */
@Controller
public class AdminController {

    @Autowired
    private DesignRepository designRepository;

    @GetMapping("/admin")
    public String showAdmin() {

        return "admin";
    }

    @GetMapping("/admin/search")
    public String handleSearch(@RequestParam String searchType, @RequestParam String searchTerm, Model model) {
        List<Design> designsResults= new ArrayList<>();
        switch (searchType) {
            case "user" -> designsResults = designRepository.findByUser(searchTerm);

//            case "date":
//                List<Design> designsResults = designRepository.findByDate(searchTerm);
//                break;
            case "background" -> designsResults = designRepository.findByBackground(searchTerm);
            default -> {
                model.addAttribute("errors", "Invalid search term: " + searchTerm);
                return "admin";
            }
        }
        model.addAttribute("designsResults", designsResults);
        return "results";
    }

    @GetMapping("/admin/user-statistics")
    public String getUserStatistics(Model model) {
        List<UserDesignCount> userDesignCounts = designRepository.findTop3UsersWithMostDesigns();
        if (userDesignCounts.isEmpty()) {
            model.addAttribute("error", "No users found");
            return "admin";
        }
        model.addAttribute("userDesignCounts", userDesignCounts);
        return "user-statistics";
    }

}
