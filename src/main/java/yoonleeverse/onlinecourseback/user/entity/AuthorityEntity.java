package yoonleeverse.onlinecourseback.user.entity;
import org.springframework.security.core.GrantedAuthority;

public enum AuthorityEntity implements GrantedAuthority {
    ROLE_ADMIN, ROLE_USER;

    public String getAuthority() {
        return name();
    }
}
