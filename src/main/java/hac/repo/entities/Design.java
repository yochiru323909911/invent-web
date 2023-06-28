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

    public Design(String freeText, String owner, Image imgDesign) {
        this.freeText = freeText;
        this.owner = owner;
        this.imgDesign = imgDesign;
        this.creationDate = LocalDate.now();
    }

    public Design(Design design) {
        this.freeText = design.freeText;
        this.owner = design.owner;
        this.imgDesign = design.imgDesign;
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

}