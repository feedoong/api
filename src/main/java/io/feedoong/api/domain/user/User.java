package io.feedoong.api.domain.user;

import io.feedoong.api.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Table(name = "users")
@Entity
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "profile_image_url", length = 2000)
    private String profileImageUrl;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    public static User of(String name, String email, String profileImageUrl, String username) {
        return User.builder()
                .name(name)
                .email(email)
                .profileImageUrl(profileImageUrl)
                .username(username)
                .build();
    }

    public Boolean isDeactivated() {
        return this.deletedAt == null;
    }

    public void reactivate() {
        this.deletedAt = null;
    }
}
