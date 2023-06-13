package hac.repo.entities;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;

import jakarta.validation.constraints.Email; // FIX!!!!
import jakarta.validation.constraints.NotEmpty; // FIX!!!!
import jakarta.validation.constraints.PositiveOrZero; // FIX!!!!

/**
 * a purchase is a record of a user buying a product. You should not need to edit this file
 * but if you feel like you need to, please get in touch with the teacher.
 */
@Entity
public class User implements Serializable {
    @Id
    @NotEmpty(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    private String email;

    @NotEmpty(message = "User name is mandatory")
    private String userName;

    // The User's contacts
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_email")
    private List<Contact> contacts;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_email")
    private List<Design> designs;

    public User(String userName, String email) {
        this.userName = userName;
        this.email = email;
    }

    public User() {}

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public List<Design> getDesigns() {
        return designs;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public void setDesigns(List<Design> designs) {
        this.designs = designs;
    }
}
