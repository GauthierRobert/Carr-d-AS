package lhc.com.dtos;

import lombok.Data;

@Data
public class ImageCompetitionDto {


    private String asBase64;

    private String competition_ref;

    public ImageCompetitionDto(String asBase64, String competition_ref) {
        this.asBase64 = asBase64;
        this.competition_ref = competition_ref;
    }
}
