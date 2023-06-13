package hac.controllers;

/**
 * יש לוגין, ואנחנוצריכות את השם של המשתמש(אימייל) כדי לדעת ללכת
  האם צריך סשן רק בשביל לשמור את המזהה של המתשתמש
  איך ואיפה שמים את הפרטים מהלוגין בטבלה של היוזר
 מה הסינטקס של לגשת לטבלה של היוזר לחפש יוזר נוכחי ולהכניס לותך הרשימה של העיצובים שלו עיצוב חדש
 */

import hac.Beans.UserSession;
import hac.repo.entities.Design;
import hac.repo.entities.User;
import hac.repo.repositories.DesignRepository;
import hac.repo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class TemplateController {
    @Autowired
    private DesignRepository designRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    @Qualifier("sessionBeanUser")
    private UserSession userSession;

    @GetMapping("/")
    public String showHome() {
        return "home";
    }

    @GetMapping("/templates")
    public String showTemplates(Model model) {
        String[] templates = new String[12];
        for (int i = 1; i <= 12; i++) {
            templates[i-1] = i + ".jpg";
            System.out.println(templates[i-1]);
        }
        model.addAttribute("templates", templates);
        return "templates";
    }

    @GetMapping("/edit-template/{template}")
    public String selectTemplate(@PathVariable("template") String template, Model model) {
        model.addAttribute("template", "../images/"+template);
        return "edit-template";
    }

    @PostMapping("/save-design")
    public String saveTemplate(
            @RequestParam("template") String template,
            @RequestParam("date") String date,
            @RequestParam("name") String name,
            @RequestParam("location") String location,
            @RequestParam("freeText") String freeText,
            Model model
    ) {
        // Logic to save the template with the provided data
        // You can access the values: template, date, name, location, freeText
        // and perform the necessary operations (e.g., save to a database, generate a new image with the added text, etc.)
        // You can return a success message or redirect to another page

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            // User is authenticated
            Object principal = auth.getPrincipal();

            if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;

                // Fetch the user from the database
                Optional<User> optionalUser = userRepository.findByEmail(userDetails.getUsername());

                if(optionalUser.isPresent()){
                    User user = optionalUser.get();
                    Design design = new Design(template,date,name,location,freeText);
                    user.getDesigns().add(design);
                }
            }
        }

        return "save-design";
    }

}
