package hac.util;

public class DesignCount {
    private String searchTerm;
    private Long designCount;

    public DesignCount(String searchTerm, Long designCount) {
        this.searchTerm = searchTerm;
        this.designCount = designCount;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public Long getDesignCount() {
        return designCount;
    }

    public void setDesignCount(Long designCount) {
        this.designCount = designCount;
    }


}
