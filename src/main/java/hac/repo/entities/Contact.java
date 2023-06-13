package hac.repo.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

import java.io.Serializable;

@Entity
public class Contact implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Contact name is mandatory")
    private String contactName;

    @NotEmpty(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    private String contactEmail;


    public Contact(String contactName, String contactEmail) {
        this.contactName = contactName;
        this.contactEmail = contactEmail;
    }
    public Contact() {
    }

    // getters and setters
}

