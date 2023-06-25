package hac.util;

public class UserDesignCount {
    private String username;
    private Long designCount;

    public UserDesignCount(String username, Long designCount) {
        this.username = username;
        this.designCount = designCount;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getDesignCount() {
        return designCount;
    }

    public void setDesignCount(Long designCount) {
        this.designCount = designCount;
    }
// Getters and setters

}
