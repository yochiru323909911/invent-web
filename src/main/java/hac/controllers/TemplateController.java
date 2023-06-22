package hac.controllers;

/**
 *!!!!!!!!!!!!!!!!!!!!!!! לשנות הגדרות ושמות של טבלאות של טבלאות
 *==באדמין:==
 * חיפוש לפי יוזר
 * חיפוש לפי באקרונד- שיציג את כל ההזמנות שעוצבו עם תמונה זו
 * להציג יוזר שיש לו הכי הרבה דיזיין
 *חיפוש הזמנה לפי תאריך
 * להוסיף באקרונד מועדף
 *
 * --צריך להוסיף דף חשבון אישי--
 * שיש בו אפשרות למחוק ולערוך עיצובים קיימים
 * לערוך אנשי קשר
 *
 *
 * בסייבד דיזיין להוסיף חיפוש
 * העלאת תמונה לאדמין*
 * לעצב דף בית
 * דף של סייב דיזיין- לחלץ את העיצוב ולהראות אותו למשתמש
 * גט ופוסט לכל דבר!! טיפול בארור
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    public String showHome(Model model) {
        List<Image> images = imageRepository.findAll();
        List<String> templates = new ArrayList<>();

        for (Image image : images) {
            templates.add(image.getPath());
        }

        model.addAttribute("templates", templates);
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

    @PostMapping("/search-templates")
    public String searchTemplates(@RequestParam("search") String search, Model model) {
        List<String> templates = imageRepository.findByCategory(search).stream()
                .map(Image::getPath)
                .collect(Collectors.toList());

        model.addAttribute("templates", templates);
        return "templates";
    }


    @PostMapping("/shared/edit-template")
    public String selectTemplate(@RequestParam("template") String template, Model model) {
        model.addAttribute("template", template);
        return "edit-template";
    }

    @PostMapping("/shared/save")
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
        model.addAttribute("design", design);

        return "save";
    }

    @PostMapping("/admin/upload")
    public String handleFileUpload(@RequestParam("imageFile") MultipartFile file, Model model) {
        if (!file.isEmpty()) {
            System.out.println("file is not empty");
            try {
                // Save the file to a permanent location
                String filePath = "/images/party/" + file.getOriginalFilename();
                System.out.println("filepath");
                System.out.println(filePath);
                file.transferTo(new File(filePath));

                // Save the file path in the database
                Image fileEntity = new Image(filePath, "party");
                System.out.println("fileEntity");
                System.out.println(fileEntity);
                imageRepository.save(fileEntity);
            } catch (IOException e) {
                System.out.println("in exception");
                e.printStackTrace();
            }
            //return home

        }
        //return file is empty
//        List<Image> images = imageRepository.findAll();
//        List<String> templates = new ArrayList<>();
//
//        for (Image image : images) {
//            templates.add(image.getPath());
//        }
//
//        model.addAttribute("templates", templates);
        return "redirect:/login";
    }




}
