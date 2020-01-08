package dev.arielalvesdutra.prs.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.Instant;

@Entity
@EqualsAndHashCode
@ToString
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter @Accessors(chain = true)
    private Long id;

    @Getter @Setter @Accessors(chain = true)
    private String title;

    @Getter @Setter @Accessors(chain = true)
    private String subtitle;

    @Column(columnDefinition = "TEXT")
    @Getter @Setter @Accessors(chain = true)
    private String body;

    @Getter @Setter @Accessors(chain = true)
    private Instant createdAt = Instant.now();

    @JsonIgnore
    @ManyToOne
    @Getter @Setter @Accessors(chain = true)
    private Category category;
}
