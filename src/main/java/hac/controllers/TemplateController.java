package hac.controllers;

/**
 *!!!!!!!!!!!!!!!!!!!!!!! לשנות הגדרות ושמות של טבלאות של טבלאות
 *==באדמין:==
 * חיפוש הזמנה לפי תאריך
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

import hac.repo.entities.Contact;
import hac.repo.entities.Design;
import hac.repo.entities.Image;
import hac.repo.repositories.ContactRepository;
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

    @Autowired
    private ContactRepository contactRepository;

    @PostConstruct
    public void initialize() {
        imageProcessor.saveImageFileNamesToRepository();
    }

    @GetMapping("/")
    public String showHome(Model model) {
        List<Image> images = imageRepository.findAll();
        List<String> imgDesigns = new ArrayList<>();

        for (Image image : images) {
            imgDesigns.add(image.getPath());
        }

        model.addAttribute("imgDesigns", imgDesigns);
        return "home";
    }

    @GetMapping("/templates")
    public String showTemplates(Model model) {
        List<Image> images = imageRepository.findAll();
        List<String> imgDesigns = new ArrayList<>();

        for (Image image : images) {
            imgDesigns.add(image.getPath());
        }

        model.addAttribute("imgDesigns", imgDesigns);
        return "templates";
    }


    @PostMapping("/shared/edit-template")
    public String selectTemplate(@RequestParam("imgDesignId") String imgPath, Model model) {

        model.addAttribute("imgDesign", imgPath);
        return "edit-template";
    }

    @PostMapping("/shared/save")
    public String saveTemplate(@RequestParam("freeText") String freeText, @RequestParam("imgDesign") String imgDesign, Model model) {
        Design design= new Design();
        System.out.println("in saveTemplate");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            // User is authenticated
            Object principal = auth.getPrincipal();

            if (principal instanceof UserDetails) {
                List<Image> images = imageRepository.findByImagePath(imgDesign);
                design = new Design(freeText, ((UserDetails) principal).getUsername(), images.get(0));
                designRepository.save(design);
            }
        }
        model.addAttribute("design", design);

        return "save";
    }

    @PostMapping("/search-templates")
    public String searchTemplates(@RequestParam("search") String search, Model model) {
        List<String> imgDesigns = imageRepository.findByCategory(search).stream()
                .map(Image::getPath)
                .collect(Collectors.toList());

        model.addAttribute("imgDesigns", imgDesigns);
        return "templates";
    }

    @PostMapping("/admin/upload")
    public String handleFileUpload(@RequestParam("imageFile") MultipartFile file) {
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

    @GetMapping("/shared/my-account")
    public String showMyAccount(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            // User is authenticated
            Object principal = auth.getPrincipal();

            if (principal instanceof UserDetails) {
                List<Contact> contacts = contactRepository.findByOwner(((UserDetails) principal).getUsername());
                if(contacts.isEmpty())
                    model.addAttribute("error", "No contacts found");
                model.addAttribute("contacts", contacts);
            }
        }
        return "my-account";
    }




}
