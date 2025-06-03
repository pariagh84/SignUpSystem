package aut.ap.account;
import jakarta.persistence.*;

@Entity
@Table(name = "User")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Basic(optional = false)
    private String first_name;

    @Basic(optional = false)
    private String last_name;

    @Basic(optional = false)
    private Integer age;

    @Basic(optional = false)
    private String email;

    @Basic(optional = false)
    private String password;

    public User() {}

    public User(String firstName, String lastName, Integer age, String email, String password) {
        first_name = firstName;
        last_name = lastName;
        this.age = age;
        this.email = email;
        setPassword(password);
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
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
        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException("Password is weak");
        }
        this.password = password;
    }
}
