package hac.controllers;

/**
 *!!!!!!!!!!!!!!!!!!!!!!! לשנות ושמות של טבלאות
 *
 * --צריך להוסיף דף חשבון אישי--
 * לערוך אנשי קשר
 *לסדר את הקוד
 *
 * העלאת תמונה לאדמין*
 * לעצב דף בית
 */

import hac.repo.entities.Contact;
import hac.repo.entities.Design;
import hac.repo.entities.Image;
import hac.repo.repositories.ContactRepository;
import hac.repo.repositories.DesignRepository;
import hac.repo.repositories.ImageRepository;
import hac.util.ImageProcessor;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class UserController {

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
        return "user/home";
    }



    @PostMapping("/designs")
    public String postDesigns(){
        return "error";
    }

    @GetMapping("/designs")
    public String showDesigns(Model model) {
        List<Image> images = imageRepository.findAll();
        List<String> imgDesigns = new ArrayList<>();

        for (Image image : images) {
            imgDesigns.add(image.getPath());
        }

        model.addAttribute("imgDesigns", imgDesigns);
        return "designs";
    }


    @GetMapping("/shared/edit-invitation")
        public String postEditInvitation(){
        return "error";
    }
    @PostMapping("/shared/edit-invitation")
    public String selectInvitation(@RequestParam("imgPath") String imgPath, Model model) {
        model.addAttribute("imgDesign", imgPath);
        model.addAttribute("designId", -1);

        return "user/edit-invitation";
    }

    @GetMapping("/shared/save")
    public String getSaveInvitation(){
        return "error";
    }
    @PostMapping("/shared/save")
    public String saveInvitation(@RequestParam String freeText, @RequestParam("designId") Long id, @RequestParam("imgDesign")String imgPath, Model model) {
        Design design= new Design();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            Object principal = auth.getPrincipal();

            if (principal instanceof UserDetails) {
                List<Image> images = imageRepository.findByImagePath(imgPath);
                if (id != -1) {
                    design = designRepository.findById(id).get();
                    design.setFreeText(freeText);
                } else
                    design = new Design(freeText, ((UserDetails) principal).getUsername(), images.get(0));
            designRepository.save(design);
            }
        }
        model.addAttribute("design", design);
        return "user/show-design";
    }

    @GetMapping("/search-designs")
    public String getSearchDesigns(){
        return "error";
    }
    @PostMapping("/search-designs")
    public String searchDesigns(@RequestParam("search") String search, Model model) {
        List<String> imgDesigns = imageRepository.findByCategory(search).stream()
                .map(Image::getPath)
                .collect(Collectors.toList());

        model.addAttribute("imgDesigns", imgDesigns);
        return "designs";
    }

    @PostMapping("/shared/my-account")
    public String postMyAccount(){
        return "error";
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
        return "user/my-account";
    }

    @GetMapping("/shared/add-contact")
    public String getAddContact(){
        return "error";
    }
    @PostMapping("/shared/add-contact")
    public String addUser(@RequestParam("contactName") String name, @RequestParam("emailContact") String email, Model model) {
        String owner=SecurityContextHolder.getContext().getAuthentication().getName();
        Contact newContact = new Contact(owner, name, email);
        contactRepository.save(newContact);

        update(model);
        model.addAttribute("successContact", "Contact added successfully");

        return "user/my-account";
    }

    @PostMapping("/shared/delete-contact/{id}")
    public String postDeleteContact(@PathVariable String id){
        return "error";
    }
    @GetMapping("/shared/delete-contact/{id}")
    public String deleteContact(@PathVariable("id") Long contactId, Model model) {
        contactRepository.deleteById(contactId);
        update(model);
        model.addAttribute("successContact", "Contact deleted successfully");

        return "user/my-account";
    }

    @PostMapping("/shared/delete-design/{id}")
    public String postDeleteDesign(@PathVariable String id){
        return "error";
    }
    @GetMapping("/shared/delete-design/{id}")
    public String deleteDesign(@PathVariable("id") Long designId, Model model) {
        designRepository.deleteById(designId);
        update(model);
        model.addAttribute("successDesign", "Design deleted successfully");

        return "user/my-account";
    }

    @GetMapping("/shared/edit-design")
    public String editDesign(@RequestParam("designId") Long designId, Model model) {
        Design design = designRepository.findById(designId).get();
        model.addAttribute("imgDesign", design.getImgDesign().getPath());
        model.addAttribute("text", design.getFreeText());
        model.addAttribute("designId", designId);

        return "user/edit-invitation";
    }

    @GetMapping("/shared/style")
    public String getChangeFont(){
        return "error";
    }


    public void update(Model model){
        List<Contact> contacts = contactRepository.findByOwner(SecurityContextHolder.getContext().getAuthentication().getName());
        List<Design> designs = designRepository.findByUser(SecurityContextHolder.getContext().getAuthentication().getName());

        model.addAttribute("contacts", contacts);
        model.addAttribute("designs", designs);
    }
}
