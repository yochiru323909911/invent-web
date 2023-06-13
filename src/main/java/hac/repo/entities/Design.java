package hac.repo.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

import java.io.Serializable;

@Entity
    public class Design implements Serializable {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String dateText;
        private String locationText;
        private String nameText;
        private String freeText;
        private String templateImage;
        public Design() {
        }

        public Design(String dateText, String locationText, String nameText, String freeText, String templateImage) {
            this.dateText = dateText;
            this.locationText = locationText;
            this.nameText = nameText;
            this.freeText = freeText;
            this.templateImage = templateImage;
        }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDateText() {
        return dateText;
    }

    public void setDateText(String dateText) {
        this.dateText = dateText;
    }

    public String getLocationText() {
        return locationText;
    }

    public void setLocationText(String locationText) {
        this.locationText = locationText;
    }

    public String getNameText() {
        return nameText;
    }

    public void setNameText(String nameText) {
        this.nameText = nameText;
    }

    public String getFreeText() {
        return freeText;
    }

    public void setFreeText(String freeText) {
        this.freeText = freeText;
    }

    public String getTemplateImage() {
        return templateImage;
    }

    public void setTemplateImage(String templateImage) {
        this.templateImage = templateImage;
    }

}
