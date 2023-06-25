package hac.repo.entities;
import hac.repo.repositories.ImageRepository;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

@Entity
    public class Design implements Serializable {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @NotNull
        private String owner;
        private String freeText;

        @ManyToOne
        @JoinColumn(name = "img_design_id")
        private Image imgDesign;

    public Image getImgDesign() {
        return imgDesign;
    }

    public void setImgDesign(Image imgDesign) {
        this.imgDesign = imgDesign;
    }

    public String getOwner() {
        return owner;
    }

    public Design() {
    }

    public Design(String freeText, String owner, Image imgDesign) {
        this.freeText = freeText;
        this.owner = owner;
        this.imgDesign = imgDesign;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }


    public String getFreeText() {
        return freeText;
    }

    public void setFreeText(String freeText) {
        this.freeText = freeText;
    }

    public Image getImg() {
        return imgDesign;
    }

    public void setImg(Image imgDesign) {
        this.imgDesign = imgDesign;
    }

}
