package hac.controllers;

/**
 *!!!!!!!!!!!!!!!!!!!!!!! לשנות הגדרות ושמות של טבלאות של טבלאות
 *
 * --צריך להוסיף דף חשבון אישי--
 * לערוך אנשי קשר
 *
 *
 * העלאת תמונה לאדמין*
 * לעצב דף בית
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
import jakarta.validation.constraints.Email;
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
        System.out.println("paths: ");
        System.out.println(imgDesigns);
        model.addAttribute("imgDesigns", imgDesigns);
        return "templates";
    }


    @PostMapping("/shared/edit-template")
    public String selectTemplate(@RequestParam("imgDesignId") String imgPath, Model model) {
        model.addAttribute("imgDesign", imgPath);
        model.addAttribute("designId", -1);
        List<String> fonts = List.of("Arial", "Verdana", "Helvetica", "Times New Roman", "Courier New");
        model.addAttribute("fonts", fonts);
        model.addAttribute("fontStyle", "Arial");

        return "edit-template";
    }

    @PostMapping("/shared/save")
    public String saveTemplate(@RequestParam("freeText") String freeText, @RequestParam("imgDesign") String imgDesign,
                               @RequestParam("designId") Long designId, Model model) {
        Design design= new Design();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            // User is authenticated
            Object principal = auth.getPrincipal();

            if (principal instanceof UserDetails) {
                List<Image> images = imageRepository.findByImagePath(imgDesign);
                if (designId != -1) {
                    design = designRepository.findById(designId).get();
                    design.setFreeText(freeText);
                } else
                    design = new Design(freeText, ((UserDetails) principal).getUsername(), images.get(0));

                designRepository.save(design);
            }
        }
        model.addAttribute("design", design);
        return "show-design";
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
                    model.addAttribute("errorContact", "No contacts found");
                model.addAttribute("contacts", contacts);

                List<Design> designs = designRepository.findByUser(((UserDetails) principal).getUsername());
                if(designs.isEmpty())
                    model.addAttribute("errorDesign", "No designs found");
                model.addAttribute("designs", designs);
            }
        }
        return "my-account";
    }

    @PostMapping("/shared/add-contact")
    public String addUser(@RequestParam("contactName") String name, @RequestParam("emailContact") String email, Model model) {
        String owner=SecurityContextHolder.getContext().getAuthentication().getName();
        Contact newContact = new Contact(owner, name, email);
        contactRepository.save(newContact);

        update(model);
        model.addAttribute("successContact", "Contact added successfully");

        return "my-account";
    }

    @GetMapping("/shared/delete-contact/{id}")
    public String deleteContact(@PathVariable("id") Long contactId, Model model) {
        contactRepository.deleteById(contactId);
        update(model);
        model.addAttribute("successContact", "Contact deleted successfully");

        return "my-account";
    }

    @GetMapping("/shared/delete-design/{id}")
    public String deleteDesign(@PathVariable("id") Long designId, Model model) {
        designRepository.deleteById(designId);
        update(model);
        model.addAttribute("successDesign", "Design deleted successfully");

        return "my-account";
    }

    @GetMapping("/shared/edit-design")
    public String editDesign(@RequestParam("designId") Long designId, Model model) {
        Design design = designRepository.findById(designId).get();

        model.addAttribute("imgDesign", design.getImgDesign().getPath());
        model.addAttribute("text", design.getFreeText());
        model.addAttribute("designId", designId);
        List<String> fonts = List.of("Arial", "Verdana", "Helvetica", "Times New Roman", "Courier New");
        model.addAttribute("fonts", fonts);
        model.addAttribute("fontStyle", "Arial");
        return "edit-template";
    }

    @GetMapping("/shared/style/{fontStyle}")
    public String changeFont(@PathVariable("fontStyle") String fontStyle, Model model) {
        model.addAttribute("fontStyle", fontStyle);
        return "edit-template";
    }


    public void update(Model model){
        List<Contact> contacts = contactRepository.findByOwner(SecurityContextHolder.getContext().getAuthentication().getName());
        List<Design> designs = designRepository.findByUser(SecurityContextHolder.getContext().getAuthentication().getName());

        model.addAttribute("contacts", contacts);
        model.addAttribute("designs", designs);
    }
}
