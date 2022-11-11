package dtos;

import entities.Role;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link entities.Role} entity
 */
public class RoleDTO implements Serializable {
    @NotNull
    private final String roleName;

    public RoleDTO(Role role) {
        this.roleName = role.getRoleName();
    }

    public String getRoleName() {
        return roleName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleDTO entity = (RoleDTO) o;
        return Objects.equals(this.roleName, entity.roleName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roleName);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "roleName = " + roleName + ")";
    }
}