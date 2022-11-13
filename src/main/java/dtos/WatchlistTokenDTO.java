package dtos;

import entities.Anime;
import entities.Watchlist;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public class WatchlistTokenDTO {

    private final Set<AnimeDTO> animeList = new LinkedHashSet<>();
    private final String token;

    public WatchlistTokenDTO(Watchlist watchlist, String token) {
        for (Anime anime : watchlist.getAnimeList()) {
            animeList.add(new AnimeDTO(anime));
        }
        this.token = token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WatchlistTokenDTO that = (WatchlistTokenDTO) o;
        return animeList.equals(that.animeList) && token.equals(that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(animeList, token);
    }

    @Override
    public String toString() {
        return "WatchlistTokenDTO{" +
                "animeList=" + animeList +
                ", token='" + token + '\'' +
                '}';
    }
}
