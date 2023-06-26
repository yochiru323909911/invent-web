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

    private String owner;

    @NotEmpty(message = "Contact name is mandatory")
    private String contactName;

    @NotEmpty(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    private String contactEmail;


    public Contact(String owner, String contactName, String contactEmail) {
        this.owner=owner;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
    }
    public Contact() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }
}

