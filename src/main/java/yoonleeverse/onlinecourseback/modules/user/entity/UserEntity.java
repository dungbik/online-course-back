package yoonleeverse.onlinecourseback.modules.user.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import yoonleeverse.onlinecourseback.modules.common.entity.BaseTimeEntity;
import yoonleeverse.onlinecourseback.modules.file.entity.FileEntity;
import yoonleeverse.onlinecourseback.modules.user.types.UpdateUserInput;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class UserEntity extends BaseTimeEntity implements UserDetails {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String userId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    private Boolean emailAgreed;

    private String verifyCode;

    private Boolean verified;

    @Column(nullable = false)
    private String password;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "avatar_id")
    private FileEntity avatar;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(joinColumns = @JoinColumn(name = "id"))
    @Enumerated(EnumType.STRING)
    private Set<AuthorityEntity> authorities = new HashSet<>();

    @PrePersist
    void prePersist() {
        userId = UUID.randomUUID().toString();
        if (authorities.contains(AuthorityEntity.ROLE_ADMIN)) {
            verified = true;
        } else {
            verifyCode = UUID.randomUUID().toString();
            verified = false;
        }
    }

    public void updateEmail(String email) {
        this.email = email;
        this.verifyCode = UUID.randomUUID().toString();
        this.verified = false;
    }

    public void verifyEmail() {
        this.verified = true;
        this.verifyCode = "";
    }

    public void updateUser(UpdateUserInput input) {
        this.name = input.getName();
        this.emailAgreed = input.getEmailAgreed();
    }


    public void updateAvatar(FileEntity avatar) {
        this.avatar = avatar;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return verified;
    }

    @Override
    public boolean isAccountNonLocked() {
        return verified;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return verified;
    }

    @Override
    public boolean isEnabled() {
        return verified;
    }
}
