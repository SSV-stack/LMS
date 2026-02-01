package in.maven.ark.lms.auth.dto;

import in.maven.ark.lms.common.entity.User;
import java.util.Set;

public class LoginResponse {
    
    private String token;
    private Long userId;
    private String username;
    private String email;
    private Set<User.Role> roles;

    public LoginResponse() {}

    public LoginResponse(String token, Long userId, String username, String email, Set<User.Role> roles) {
        this.token = token;
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Set<User.Role> getRoles() { return roles; }
    public void setRoles(Set<User.Role> roles) { this.roles = roles; }
}
