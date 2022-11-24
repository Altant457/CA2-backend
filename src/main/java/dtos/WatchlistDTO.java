package dtos;

import entities.Anime;
import entities.Watchlist;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link entities.Watchlist} entity
 */
public class WatchlistDTO implements Serializable {
    private final Set<AnimeDTO> animeList = new LinkedHashSet<>();

    public WatchlistDTO(Watchlist watchlist) {
        watchlist.getAnimeList().forEach(anime -> animeList.add(new AnimeDTO(anime)));
    }

    public Set<AnimeDTO> getAnimeList() {
        return animeList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WatchlistDTO entity = (WatchlistDTO) o;
        return Objects.equals(this.animeList, entity.animeList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(animeList);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "animeList = " + animeList + ")";
    }
}