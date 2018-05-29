package bliznyuk.stanislav.usereditor.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
//@JsonIgnoreProperties(value = {"createdAt", "updatedAt", "hibernateLazyInitializer", "handler"}, allowGetters = true)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String surname;

    @NotBlank
    private String name;

    @NotBlank
    private String secondName;

    @NotBlank
    private String birthday;

    @JsonIgnore
    @ManyToMany
    private Set<User> friends = new HashSet<User>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public void addFriend(User user){
        this.friends.add(user);
    }

    public void deleteFriend(User user){
        this.friends.remove(user);
    }

    public Set<User> getFriends() {return friends;}

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}
