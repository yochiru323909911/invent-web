package hac.repo.entities;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
    public class Design implements Serializable {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @NotNull
        private String owner;

        private String freeText;

        private String fontStyle;

        private String fontColor;

        private String fontSize;


        @Column(name = "creation_date")
        private LocalDate creationDate;

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

    public Design(String freeText, String owner, Image imgDesign, String fontStyle, String fontColor, String fontSize) {
        this.freeText = freeText;
        this.owner = owner;
        this.imgDesign = imgDesign;
        this.fontStyle = fontStyle;
        this.fontColor = fontColor;
        this.fontSize = fontSize;
        this.creationDate = LocalDate.now();
    }

    public Design(Design design) {
        this.freeText = design.freeText;
        this.owner = design.owner;
        this.imgDesign = design.imgDesign;
        this.fontStyle = design.fontStyle;
        this.fontColor = design.fontColor;
        this.fontSize = design.fontSize;
        this.creationDate = LocalDate.now();
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

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public String getFreeText() {
        return freeText;
    }

    public void setFreeText(String freeText) {
        this.freeText = freeText;
    }

    public String getFontStyle() {
        return fontStyle;
    }

    public void setFontStyle(String fontStyle) {
        this.fontStyle = fontStyle;
    }

    public String getFontColor() {
        return fontColor;
    }

    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }

    public String getFontSize() {
        return fontSize;
    }

    public void setFontSize(String fontSize) {
        this.fontSize = fontSize;
    }


}
