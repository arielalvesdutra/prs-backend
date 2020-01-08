package dev.arielalvesdutra.prs.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@EqualsAndHashCode(of = "id")
@ToString
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter  @Accessors(chain = true)
    private Long id;

    @Column(unique = true)
    @Getter @Setter @Accessors(chain = true)
    private String name;

    @Getter @Setter @Accessors(chain = true)
    private String description;

    @Getter @Setter @Accessors(chain = true)
    private Instant createdAt = Instant.now();

    @ManyToMany( fetch = FetchType.EAGER)
    @JoinTable(name = "role_permission",
        joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "permission_id", referencedColumnName = "id"))
    @Getter @Setter @Accessors(chain = true)
    private Set<Permission> permissions = new HashSet<>();

    @ManyToMany(mappedBy = "roles")
    @Getter @Setter @Accessors(chain = true)
    private Set<User> users = new HashSet<>();
}
