package hac.controllers;

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
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class UserController {
    private static final int DEFAULT_NEW_ID = -1;
    private static final String NO_CONTACTS_MESSAGE = "No contacts found.";
    private static final String NO_DESIGNS_MESSAGE = "No designs found.";
    private static final String CONTACT_DELETED_MESSAGE = "Contact deleted successfully.";
    private static final String CONTACT_SAVED_MESSAGE = "Contact saved successfully.";
    private static final String INVITATION_DELETED_MESSAGE = "Invitation deleted successfully.";
    private static final String NOT_OWNER_MESSAGE = "forbidden! You are not the owner!";
    private static final String WAS_DELETED = "Was deleted. Not refresh the page.";

    @Autowired
    private DesignRepository designRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ImageProcessor imageProcessor;

    @Autowired
    private ContactRepository contactRepository;

    /**
     * initialize the database with the images for the backgrounds
     */
    @PostConstruct
    public void initialize() {
        if(imageRepository.findAll().isEmpty())
            imageProcessor.saveImageFileNamesToRepository();
    }

    /**
     * @param model the model to add attributes
     * @return home page
     */
    @GetMapping("/")
    public String showHome(Model model) {
        getBackgrounds(model);
        return "user/home";
    }

    /**
     * @return error page
     */
    @PostMapping("/designs")
    public String postDesigns(){
        return "error";
    }

    /**
     * @param model the model to add attributes
     * @return page with background images
     */
    @GetMapping("/designs")
    public String showDesigns(Model model) {
        getBackgrounds(model);
        return "designs";
    }

    /**
     * @return error page
     */
    @GetMapping("/shared/edit-invitation")
        public String postEditInvitation(){
        return "error";
    }

    /**
     * Allow to edit an invitation
     * @param imgPath the path of the background image
     * @param model the model to add attributes
     * @return page with option to edit
     */
    @PostMapping("/shared/edit-invitation")
    public String selectInvitation(@RequestParam("imgPath") String imgPath, Model model) {
        model.addAttribute("imgDesign", imgPath);
        model.addAttribute("designId", DEFAULT_NEW_ID);
        return "user/edit-invitation";
    }

    /**
     * @return error page
     */
    @GetMapping("/shared/save")
    public String getSaveInvitation(){
        return "error";
    }

    /**
     * Save the invitation in the db. If this is old the db update.
     * @param freeText the text in the invitation
     * @param id id of the design- if this is a new design, the id is -1.
     * @param imgPath the path of the background image
     * @param model the model to add attributes
     * @return page which show the design that saved in the database
     */
    @PostMapping("/shared/save")
    public String saveInvitation(@RequestParam String freeText, @RequestParam("designId") Long id, @RequestParam("imgDesign")String imgPath, Model model) {
        Design design= new Design();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            Object principal = auth.getPrincipal();
            if (principal instanceof UserDetails) {
                List<Image> images = imageRepository.findByImagePath(imgPath);
                if (id != DEFAULT_NEW_ID) {
                    design = designRepository.findById(id).get();
                    design.setFreeText(freeText);
                }
                else
                    design = new Design(freeText, ((UserDetails) principal).getUsername(), images.get(0));
            designRepository.save(design);
            }
        }
        model.addAttribute("design", design);
        return "user/show-design";
    }

    /**
     * @return error page
     */
    @GetMapping("/search-designs")
    public String getSearchDesigns(){
        return "error";
    }

    /**
     * Allow to search images by their category
     * @param search the search input
     * @param model the model to add attributes
     * @return page with the images
     */
    @PostMapping("/search-designs")
    public String searchDesigns(@RequestParam("search") String search, Model model) {
        List<String> imgDesigns = imageRepository.findByCategory(search).stream()
                .map(Image::getPath)
                .collect(Collectors.toList());
        System.out.println(imgDesigns);
        model.addAttribute("imgDesigns", imgDesigns);
        return "designs";
    }

    /**
     * @return error page
     */
    @PostMapping("/shared/my-account")
    public String postMyAccount(){
        return "error";
    }

    /**
     * Get the account page
     * @param model the model to add attributes
     * @return account page with designs and contacts
     */
    @GetMapping("/shared/my-account")
    public String showMyAccount(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            // User is authenticated
            Object principal = auth.getPrincipal();
            if (principal instanceof UserDetails) {
                List<Contact> contacts = contactRepository.findByOwner(((UserDetails) principal).getUsername());
                if(contacts.isEmpty())
                    model.addAttribute("errorContact", NO_CONTACTS_MESSAGE);
                model.addAttribute("contacts", contacts);
                List<Design> designs = designRepository.findByUser(((UserDetails) principal).getUsername());
                if(designs.isEmpty())
                    model.addAttribute("errorDesign", NO_DESIGNS_MESSAGE);
                model.addAttribute("designs", designs);
            }
        }
        return "user/my-account";
    }

    /**
     * @return error page
     */
    @GetMapping("/shared/add-contact")
    public String getAddContact(){
        return "error";
    }

    /**
     * Add a new contact or old which edit.
     * @param name the name of the contact
     * @param email the email of the contact
     * @param contactId the id of the contact that in the db- if it old that edited. If it is a new contact, the id is -1.
     * @param model the model to add attributes
     * @return the account page
     */
    @PostMapping("/shared/add-contact")
    public String addUser(@RequestParam("contactName") String name, @RequestParam("emailContact") String email, @RequestParam("contactId") Long contactId, Model model) {
        name = name.trim();
        Contact contact;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            Object principal = auth.getPrincipal();
            if (principal instanceof UserDetails) {
                if (contactId != DEFAULT_NEW_ID) {
                    contact = contactRepository.findById(contactId).get();
                    contact.setContactName(name);
                    contact.setContactEmail(email);
                }
                else
                    contact = new Contact(((UserDetails) principal).getUsername(), name, email);
                contactRepository.save(contact);
            }
        }
        update(model);
        model.addAttribute("successMessage", CONTACT_SAVED_MESSAGE);
        return showMyAccount(model);
    }

    /**
     * @return error page
     */
    @PostMapping("/shared/delete-contact")
    public String postDeleteContact(@RequestParam String id){
        return "error";
    }

    /**
     * Delete a contact from the database
     * @param contactId the id of the contact in the database
     * @param model the model to add attributes
     * @return the account page
     */
    @GetMapping("/shared/delete-contact")
    public String deleteContact(@RequestParam("deleteId") Long contactId, Model model) {
        Optional<Contact> contact = contactRepository.findById(contactId);
        if(contact.isEmpty())
            model.addAttribute("errorMessage", WAS_DELETED);
        else if(validateAccess(contact.get().getOwner())) {
            contactRepository.deleteById(contactId);
            model.addAttribute("successMessage", CONTACT_DELETED_MESSAGE);
        }
        else
            model.addAttribute("errorMessage", NOT_OWNER_MESSAGE);
        update(model);
        return showMyAccount(model);
    }

    /**
     * @return error page
     */
    @PostMapping("/shared/edit-contact")
    public String postEditContact(@RequestParam String editId){
        return "error";
    }

    /**
     * Allow to edit a contact
     * @param contactId the id of the contact in the database
     * @param model the model to add attributes
     * @return the account page
     */
    @GetMapping("/shared/edit-contact")
    public String editContact(@RequestParam("editId") Long contactId, Model model) {
        String owner = contactRepository.findById(contactId).get().getOwner();
        if(validateAccess(owner))
            model.addAttribute("contactToEdit", contactRepository.findById(contactId).get());
        else
            model.addAttribute("errorMessage", NOT_OWNER_MESSAGE);
        update(model);
        return showMyAccount(model);
    }

    /**
     * @return error page
     */
    @PostMapping("/shared/delete-design")
    public String postDeleteDesign(@RequestParam("deleteId") String id){
        return "error";
    }

    /**
     * Delete an invitation
     * @param designId invitation id in the db
     * @param model the model to add attributes
     * @return the account page
     */
    @GetMapping("/shared/delete-design")
    public String deleteDesign(@RequestParam("deleteId") Long designId, Model model) {
        Optional<Design> design = designRepository.findById(designId);
        if(design.isEmpty())
            model.addAttribute("errorMessage", WAS_DELETED);
        else if(validateAccess(design.get().getOwner())) {
            designRepository.deleteById(designId);
            model.addAttribute("successMessage", INVITATION_DELETED_MESSAGE);
        }
        else
            model.addAttribute("errorMessage", NOT_OWNER_MESSAGE);
        update(model);
        return showMyAccount(model);
    }

    /**
     * Allow to edit an invitation
     * @param designId the id of the invitation that in the db
     * @param model the model to add attributes
     * @return a page to edit the invitation
     */
    @GetMapping("/shared/edit-design")
    public String editDesign(@RequestParam("editId") Long designId, Model model) {
        String owner = designRepository.findById(designId).get().getOwner();
        if(validateAccess(owner)) {
            Design design = designRepository.findById(designId).get();
            model.addAttribute("imgDesign", design.getImgDesign().getPath());
            model.addAttribute("text", design.getFreeText());
            model.addAttribute("designId", designId);
        }
        else {
            model.addAttribute("errorMessage", NOT_OWNER_MESSAGE);
            update(model);
            return showMyAccount(model);
        }
        return "user/edit-invitation";
    }

    /**
     * @return error page
     */
    @GetMapping("/shared/style")
    public String getChangeFont(){
        return "error";
    }

    /**
     * Get the backgrounds images paths from the database
     * @param model the model to add attributes
     */
    public void getBackgrounds(Model model){
        List<Image> images = imageRepository.findAll();
        List<String> imgDesigns = new ArrayList<>();

        for (Image image : images) {
            imgDesigns.add(image.getPath());
        }
        model.addAttribute("imgDesigns", imgDesigns);
    }

    /**
     * Update the account page
     * @param model the model to add attributes
     */
    public void update(Model model){
        List<Contact> contacts = contactRepository.findByOwner(SecurityContextHolder.getContext().getAuthentication().getName());
        List<Design> designs = designRepository.findByUser(SecurityContextHolder.getContext().getAuthentication().getName());
        model.addAttribute("contacts", contacts);
        model.addAttribute("designs", designs);
    }

    public boolean validateAccess(String objectCreator){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            Object principal = auth.getPrincipal();
            if (principal instanceof UserDetails) {
                return objectCreator.equals(((UserDetails) principal).getUsername());
            }
        }
        return false;
    }
}
