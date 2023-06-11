package hac.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TemplateController {

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

    @GetMapping("/select-template/{template}")
    public String selectTemplate(@PathVariable("template") String template, Model model) {
        model.addAttribute("template", template);
        return "edit-template";
    }

    @PostMapping("/save-template")
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
        return "template-saved";
    }

}
