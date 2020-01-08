package dev.arielalvesdutra.prs.controllers.dto;

import dev.arielalvesdutra.prs.entities.User;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter @Setter @NoArgsConstructor @EqualsAndHashCode
public class RetrieveUserDTO {

    private Long id;
    private String name;
    private String email;

    public RetrieveUserDTO(User user) {
        setId(user.getId());
        setName(user.getName());
        setEmail(user.getEmail());
    }

    public static Page<RetrieveUserDTO> toPage(Page<User> usersPage) {
        return usersPage.map(RetrieveUserDTO::new);
    }
}
