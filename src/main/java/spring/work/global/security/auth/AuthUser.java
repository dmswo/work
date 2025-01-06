package spring.work.global.security.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import spring.work.user.constant.UserRole;

import java.util.*;

@Getter
@AllArgsConstructor
@Builder
public class AuthUser implements UserDetails {

    private final Map<String, Object> properties = new HashMap<>();

    private String userId;
    private String password;
    private String nickName;
    private UserRole userRole;

    public void setDefaultInfo(AuthUser user) {
        this.userId = user.userId;
        this.password = user.password;
        this.nickName = user.nickName;
        this.userRole = user.userRole;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collections = new ArrayList<>();
        collections.add(() -> {
            return userRole.name();
        });

        return collections;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.userId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
