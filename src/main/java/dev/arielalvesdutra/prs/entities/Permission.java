package dev.arielalvesdutra.prs.entities;

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
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    @Accessors(chain = true)
    private Long id;

    @Column(unique = true)
    @Getter @Setter @Accessors(chain = true)
    private String name;

    @Getter @Setter @Accessors(chain = true)
    private String description;

    @Getter @Setter @Accessors(chain = true)
    private Instant createdAt = Instant.now();

    @ManyToMany(mappedBy = "permissions")
    @Getter @Setter @Accessors(chain = true)
    private Set<Role> roles = new HashSet<>();
}
