package sample.data.jpa.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.List;


@Getter
@Setter
@Entity
@NoArgsConstructor
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@Table(name = "users")
public abstract class User implements UserDetails {

    public User(String name, String email, String password){
        this.name = name;
        this.email = email;
        this.password = password;
    }
    public User(Long id, String email, String name){
        this.name = name;
        this.email = email;
        this.id = id;
    }

  // An autogenerated id (unique for each user in the db)
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  protected long id;

 // @Column(nullable = false, unique = true)
  protected String email;

  //@Column(nullable = false)
  protected String name;

  //@Column(nullable = false)
  protected String password;

  @OneToMany
  protected List<Appointment> appointments;


}