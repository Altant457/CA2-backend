package dtos;

import entities.Anime;

public class AnimeDTO {
    private final Integer id;
    private final String name;
    private final String startDate;
    private final String endDate;
    private final String status;
    private final String posterURL;
    private final String synopsis;

    public AnimeDTO(Integer id, String name, String startDate,String endDate, String status, String posterURL, String synopsis) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.posterURL = posterURL;
        this.synopsis = synopsis;
    }

    public AnimeDTO(Anime anime) {
        this.id = anime.getId();
        this.name = anime.getName();
        this.startDate = anime.getStartDate();
        this.endDate = anime.getEndDate();
        this.status = anime.getStatus();
        this.posterURL = anime.getPosterURL();
        this.synopsis = anime.getSynopsis();
    }
}
