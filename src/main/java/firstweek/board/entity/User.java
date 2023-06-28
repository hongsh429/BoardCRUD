package firstweek.board.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "users",
        uniqueConstraints = {@UniqueConstraint(name = "uk-username", columnNames = "username")})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRoleEnum role;

    public User(String username, String encodePassword, UserRoleEnum auth) {
        this.username = username;
        this.password = encodePassword;
        this.role = auth;
    }
}
