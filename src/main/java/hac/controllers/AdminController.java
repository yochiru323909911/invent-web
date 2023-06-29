package hac.controllers;

import hac.repo.entities.Design;
import hac.repo.repositories.DesignRepository;
import hac.util.DesignCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import java.time.LocalDate;
import java.util.List;


@Controller
@RequestMapping("/admin")
public class AdminController {
    private static final int TOP_STATISTICS_RESULTS = 3;   //For slice the ordered list to get the top
    private static final String INVALID_TYPE_MESSAGE = "Invalid search type";
    private static final String NO_RESULTS_MESSAGE = "No results found";
    private static final String DATE_ERROR_MESSAGE = "Invalid date input. required YYYY-MM-DD.";

    @Autowired
    private DesignRepository designRepository;

    /**
     * The admin home page
     * @return admin home page
     */
    @GetMapping("")
    public String showAdmin() {
        return "admin/admin";
    }

    /**
     * Searching handling
     * @param searchType search by the searchType
     * @param searchTerm search the searchTerm in the col of searchType in the db
     * @param model the model to add attributes
     * @return results search page or the home page with error
     */
    @GetMapping("/search")
    public String handleSearch(@RequestParam String searchType, @RequestParam String searchTerm, Model model) {
        List<Design> designsResults;
        try{
        switch (searchType) {
            case "user" -> designsResults = designRepository.findByUser(searchTerm);
            case "date" -> designsResults = designRepository.findByDate(LocalDate.parse(searchTerm));
            case "background" -> designsResults = designRepository.findByBackground(searchTerm);
            default -> {
                model.addAttribute("error", INVALID_TYPE_MESSAGE);
                return "admin/admin";
            }
        }
            if(designsResults.isEmpty()){
                model.addAttribute("error", NO_RESULTS_MESSAGE);
                return "admin/admin";
            }

        } catch (Exception e) {
            model.addAttribute("error", DATE_ERROR_MESSAGE);
            return "admin/admin";
        }
        model.addAttribute("designs", designsResults);
        return "results";
    }

    /**
     * Get the 3 users that have the most designs - ordered.
     * @param model the model to add attributes
     * @return user statistics page or home page with error message
     */
    @GetMapping("/user-statistics")
    public String getUserStatistics(Model model) {
        List<DesignCount> userDesignCounts = designRepository.findTop3UsersWithMostDesigns();
        userDesignCounts = userDesignCounts.subList(0, Math.min(userDesignCounts.size(), TOP_STATISTICS_RESULTS));
        if (userDesignCounts.isEmpty()) {
            model.addAttribute("error", NO_RESULTS_MESSAGE);
            return "admin/admin";
        }
        model.addAttribute("userDesignCounts", userDesignCounts);
        return "admin/user-statistics";
    }

    /**
     * Get the 3 background images that the users use the most - ordered.
     * @param model the model to add attributes
     * @return page which show the favorite backgrounds or the home page with a message
     */
    @GetMapping("/result-favorite-design")
    public String getFavoriteDesign(Model model) {
        List<DesignCount> favoriteDesign = designRepository.findTop3FavoriteDesigns();
        favoriteDesign = favoriteDesign.subList(0, Math.min(favoriteDesign.size(), TOP_STATISTICS_RESULTS));
        if (favoriteDesign.isEmpty()) {
            model.addAttribute("error", NO_RESULTS_MESSAGE);
            return "admin/admin";
        }
        model.addAttribute("favoriteDesigns", favoriteDesign);
        return "admin/result-favorite-design";
    }
}
