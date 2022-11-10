package dtos;

public class AnimeDTO {
    private final String id;
    private final String name;
    private final String year;
    private final String status;

    public AnimeDTO(String id, String name, String year, String status) {
        this.id = id;
        this.name = name;
        this.year = year;
        this.status = status;
    }
}
