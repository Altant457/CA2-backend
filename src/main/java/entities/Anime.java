package entities;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "anime")
public class Anime {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "startDate", nullable = false)
    private String startDate;
    @Column(name = "endDate", nullable = false)
    private String endDate;
    @Column(name = "status", nullable = false)
    private String status;
    @Column(name = "posterURL", nullable = false)
    private String posterURL;
    @Column(name = "synopsis", nullable = false)
    private String synopsis;
    @ManyToMany(mappedBy = "animeList")
    private Set<Watchlist> watchlists = new LinkedHashSet<>();

    public Anime(){}

    public Anime(Integer id, String name, String startDate, String endDate, String status, String posterURL) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.posterURL = posterURL;
    }

    public Set<Watchlist> getWatchlists() {
        return watchlists;
    }

    public void setWatchlists(Set<Watchlist> watchlists) {
        this.watchlists = watchlists;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPosterURL() {
        return posterURL;
    }

    public void setPosterURL(String posterURL) {
        this.posterURL = posterURL;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Anime anime = (Anime) o;
        return id.equals(anime.id) && name.equals(anime.name) &&
                startDate.equals(anime.startDate) && endDate.equals(anime.endDate) &&
                status.equals(anime.status) && posterURL.equals(anime.posterURL) &&
                synopsis.equals(anime.synopsis) && watchlists.equals(anime.watchlists);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, startDate, endDate, status, posterURL, synopsis, watchlists);
    }
}
