package yoonleeverse.onlinecourseback.user.entity;

import com.netflix.dgs.codegen.generated.types.SignUpInput;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import yoonleeverse.onlinecourseback.common.entity.BaseTimeEntity;

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

    @Lob @Basic(fetch = FetchType.EAGER)
    private String avatar;

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

    public void verifyEmail() {
        this.verified = true;
        this.verifyCode = "";
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
