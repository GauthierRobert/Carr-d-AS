package lhc.com.dtos.embedded;

import java.io.Serializable;

import lombok.Data;


@Data
public class DataName implements Serializable {

    private String dataName_1;

    private String dataName_2;

    private String dataName_3;

    private String dataName_4;

    private String dataName_5;

    public DataName() {
    }

    public DataName(String dataName_1, String dataName_2, String dataName_3, String dataName_4, String dataName_5) {
        this.dataName_1 = dataName_1;
        this.dataName_2 = dataName_2;
        this.dataName_3 = dataName_3;
        this.dataName_4 = dataName_4;
        this.dataName_5 = dataName_5;
    }

    public static DataName dataName(String dataName_1, String dataName_2, String dataName_3, String dataName_4, String dataName_5){
        return new DataName(dataName_1, dataName_2, dataName_3, dataName_4, dataName_5);
    }


}
