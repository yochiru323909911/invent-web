package hac.Beans;

public class UserSession {
    private String email;

    public UserSession(String email) {
        this.email = email;
    }

    public UserSession() {
        this.email = "";
    }

    public void setUser(String email) {
        this.email = email;
    }
}
