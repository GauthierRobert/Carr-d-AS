package lhc.com.dtos.embedded;

import lombok.Data;

@Data
public class SystemDataDto {

    private String reference;

    private String createdBy;

    private String modifiedBy;


    public SystemDataDto(String reference, String createdBy, String modifiedBy) {
        this.reference = reference;
        this.createdBy = createdBy;
        this.modifiedBy = modifiedBy;
    }

    public SystemDataDto() {
    }

    public static SystemDataDto systemDataDto(String reference, String createdBy){
        return new SystemDataDto(reference, createdBy, null);
    }

    public static SystemDataDto systemDataDto(){
        return new SystemDataDto();
    }

}
