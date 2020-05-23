package com.whiterabbit.dto;

import java.util.ArrayList;
import java.util.List;


public class InputDataLot {
    private List<InputData> inputDatas;
    private String source;

    public InputDataLot() {
        this.inputDatas = new ArrayList<>();
    }

    public List<InputData> getInputDatas() {
        return inputDatas;
    }

    public void setInputDatas(List<InputData> inputDatas) {
        this.inputDatas = inputDatas;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
