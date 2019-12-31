package dev.arielalvesdutra.prs.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@EqualsAndHashCode(of = "id")
@ToString
public class Category {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter @Accessors(chain = true)
    private Long id;

    @Getter @Setter @Accessors(chain = true)
    private String name;

    @Getter @Setter @Accessors(chain = true)
    private String description;

    @Getter @Setter @Accessors(chain = true)
    private Instant createdAt = Instant.now();

    @JsonIgnore
    @OneToMany(mappedBy = "category", orphanRemoval = true, cascade = CascadeType.ALL)
    @Getter @Setter @Accessors(chain = true)
    private Set<Post> posts = new HashSet<>();
}
