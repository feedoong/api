package io.feedoong.api.domain.refreshtoken;

import io.feedoong.api.domain.BaseEntity;
import io.feedoong.api.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Table(name = "refresh_tokens")
@Entity
public class RefreshToken extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String tokenValue;

    @Column(nullable = true, unique = true)
    private String previousTokenValue;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
