package entities;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "watchlist")
public class Watchlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @OneToOne
    @JoinColumn(name = "user_name", referencedColumnName = "userName")
    private User user;
    @ManyToMany
    @JoinTable(name = "watchlist_anime", joinColumns = {
            @JoinColumn(name = "watchList_id", referencedColumnName = "id")}, inverseJoinColumns = {
            @JoinColumn(name = "anime_id", referencedColumnName = "id")})
    private Set<Anime> animeList = new LinkedHashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Anime> getAnimeList() {
        return animeList;
    }

    public void setAnimeList(Set<Anime> animeList) {
        this.animeList = animeList;
    }

    public void addAnimeToList(Anime anime) {
        this.animeList.add(anime);
        anime.getWatchlists().add(this);
    }

    public void removeAnimeFromList(Anime anime) {
        this.animeList.remove(anime);
        anime.getWatchlists().remove(this);
    }

    @Override
    public String toString() {
        return "Watchlist{" +
                "id=" + id +
                ", animeList=" + animeList +
                '}';
    }
}