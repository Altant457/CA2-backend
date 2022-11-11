package dtos;

public class AnimeDTO {
    private final String id;
    private final String name;
    private final String year;
    private final String status;
    private final String posterURL;

    public AnimeDTO(String id, String name, String year, String status, String posterURL) {
        this.id = id;
        this.name = name;
        this.year = year;
        this.status = status;
        this.posterURL = posterURL;
    }
}
