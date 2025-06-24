package project.doblog.domain.user;

import jakarta.persistence.*;
import lombok.*;

@ToString
@Entity
@Getter
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_email", "user_login_type"})
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_email")
    private String email;

    @Column(name = "user_password")
    private String password;

    @Column(name = "user_nickname")
    private String nickname;

    @Column(name = "user_image")
    private String image;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role")
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_login_type")
    private LoginType loginType;

    @Column(name = "user_social_id")
    private String socialId;

    @Builder
    public User(String email, String password, String nickname, String image,
                Role role, LoginType loginType, String socialId) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.image = image;
        this.role = role;
        this.loginType = loginType;
        this.socialId = socialId;
    }
}
