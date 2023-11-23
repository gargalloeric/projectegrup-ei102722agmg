package es.uji.ei1027.clubesportiu.model;

public class UserLogin {
    private String email;

    private String password;

    @Override
    public String toString() {
        return "UserLogin{" +
                "name='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
