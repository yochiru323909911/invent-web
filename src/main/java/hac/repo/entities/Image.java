package hac.repo.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Collection;

@Entity
public class Image implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotEmpty
    private String path;

    @NotNull
    @NotEmpty
    private String category;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Image() {
    }



    public Image(String path, String category) {
        this.path = path;
        this.category = category;
    }



    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


}
