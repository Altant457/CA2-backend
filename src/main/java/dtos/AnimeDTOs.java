package dtos;

import java.util.List;

public class AnimeDTOs {
    private final List<AnimeDTO> data;
    private final String token;

    public AnimeDTOs(List<AnimeDTO> data, String token) {
        this.data = data;
        this.token = token;
    }
}
