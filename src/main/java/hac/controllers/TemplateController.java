package hac.controllers;

/**
 * להוסיף חיפוש
 * העלאת תמונה לאדמין
 * שמירת תמונות במסד נתונים
 *
 */

import hac.repo.entities.Design;
import hac.repo.entities.Image;
import hac.repo.repositories.DesignRepository;
import hac.repo.repositories.ImageRepository;
import hac.util.ImageProcessor;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Controller
public class TemplateController {
    @Autowired
    private DesignRepository designRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ImageProcessor imageProcessor;

    @PostConstruct
    public void initialize() {
        imageProcessor.saveImageFileNamesToRepository();
    }

    @GetMapping("/")
    public String showHome() {
        return "home";
    }

    @GetMapping("/templates")
    public String showTemplates(Model model) {
        List<Image> images = imageRepository.findAll();
        List<String> templates = new ArrayList<>();

        for (Image image : images) {
            templates.add(image.getPath());
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
    public String saveTemplate(@Valid Design design, Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            // User is authenticated
            Object principal = auth.getPrincipal();

            if (principal instanceof UserDetails) {
                design.setOwner(((UserDetails) principal).getUsername());
                designRepository.save(design);
                }
            }
        model.addAttribute("design", design);  //??

        return "save-design";
    }

}
