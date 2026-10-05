package job_portat.demo.Entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String token;
    @Column(nullable = false)
    private Date expire;
    @Column(nullable = false)
    private boolean revocation;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    public RefreshToken() {
    }

    public RefreshToken(String token, Date expire, boolean revocation) {
        this.token = token;
        this.expire = expire;
        this.revocation = revocation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getExpire() {
        return expire;
    }

    public void setExpire(Date expire) {
        this.expire = expire;
    }

    public boolean isRevocation() {
        return revocation;
    }

    public void setRevocation(boolean revocation) {
        this.revocation = revocation;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
