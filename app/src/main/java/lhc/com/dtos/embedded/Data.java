package lhc.com.dtos.embedded;

import java.io.Serializable;

@lombok.Data
public class Data implements Serializable {

    private Integer data_1;

    private Integer data_2;

    private Integer data_3;

    private Integer data_4;

    private Integer data_5;

    public Data() {
    }

    public Data(Integer data_1, Integer data_2, Integer data_3, Integer data_4, Integer data_5) {
        this.data_1 = data_1;
        this.data_2 = data_2;
        this.data_3 = data_3;
        this.data_4 = data_4;
        this.data_5 = data_5;
    }

    public static Data data(Integer data_1, Integer data_2, Integer data_3, Integer data_4, Integer data_5){
        return new Data(data_1, data_2, data_3, data_4, data_5);
    }
}
