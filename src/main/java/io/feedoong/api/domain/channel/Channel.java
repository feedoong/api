package io.feedoong.api.domain.channel;

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
@Table(name = "channels")
@Entity
public class Channel extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "url", columnDefinition = "TEXT", unique = true, nullable = false)
    private String url;

    @Column(name = "feed_url", columnDefinition = "TEXT", unique = true, nullable = false)
    private String feedUrl;

    @Column(name = "image_url", columnDefinition = "TEXT")
    private String imageUrl;

    @Column(name = "last_modified")
    private String lastModified;

    @Column(name = "etag")
    private String etag;
}
