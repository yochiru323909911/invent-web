package hac.controllers;

import hac.repo.entities.Design;
import hac.repo.repositories.DesignRepository;
import hac.util.DesignCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Controller
public class AdminController {

    @Autowired
    private DesignRepository designRepository;

    @GetMapping("/admin")
    public String showAdmin() {

        return "admin/admin";
    }

    @GetMapping("/admin/search")
    public String handleSearch(@RequestParam String searchType, @RequestParam String searchTerm, Model model) {
        List<Design> designsResults= new ArrayList<>();
        try{
        switch (searchType) {
            case "user" -> designsResults = designRepository.findByUser(searchTerm);
            case "date" -> designsResults = designRepository.findByDate(LocalDate.parse(searchTerm));
            case "background" -> designsResults = designRepository.findByBackground(searchTerm);
            default -> {
                model.addAttribute("error", "Invalid search type: " + searchType);
                return "admin/admin";
            }
        }
            if(designsResults.isEmpty()){
                model.addAttribute("error", "No results found");
                return "admin/admin";
            }

        } catch (Exception e) {
            model.addAttribute("error", "Invalid date input: required YYYY-MM-DD.");
            return "admin/admin";
        }
        model.addAttribute("designs", designsResults);
        return "results";
    }

    @GetMapping("/admin/user-statistics")
    public String getUserStatistics(Model model) {
        List<DesignCount> userDesignCounts = designRepository.findTop3UsersWithMostDesigns();
        if (userDesignCounts.isEmpty()) {
            model.addAttribute("error", "No users found");
            return "admin/admin";
        }
        model.addAttribute("userDesignCounts", userDesignCounts);
        return "admin/user-statistics";
    }

    @GetMapping("/admin/result-favorite-design")
    public String getFavoriteDesign(Model model) {
        List<DesignCount> favoriteDesign = designRepository.findTop3FavoriteDesigns();
        if (favoriteDesign.isEmpty()) {
            model.addAttribute("error", "No favorite design found");
            return "admin/admin";
        }
        model.addAttribute("favoriteDesigns", favoriteDesign);
        return "admin/result-favorite-design";
    }

}
