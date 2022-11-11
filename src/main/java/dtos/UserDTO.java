package dtos;

import entities.Role;
import entities.User;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the {@link entities.User} entity
 */
public class UserDTO implements Serializable {
    @NotNull
    @Size(min = 1, max = 255)
    private final String userName;
    private final List<RoleDTO> roleList = new ArrayList<>();
    private final WatchlistDTO watchList;

    public UserDTO(User user) {
        this.userName = user.getUserName();
        for (Role role : user.getRoleList()) {
            roleList.add(new RoleDTO(role));
        }
        this.watchList = new WatchlistDTO(user.getWatchlist());
    }

    public String getUserName() {
        return userName;
    }

    public List<RoleDTO> getRoleList() {
        return roleList;
    }

    public WatchlistDTO getWatchList() {
        return watchList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO entity = (UserDTO) o;
        return Objects.equals(this.userName, entity.userName) &&
                Objects.equals(this.roleList, entity.roleList) &&
                Objects.equals(this.watchList, entity.watchList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, roleList, watchList);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "userName = " + userName + ", " +
                "roleList = " + roleList + ", " +
                "watchList = " + watchList + ")";
    }
}