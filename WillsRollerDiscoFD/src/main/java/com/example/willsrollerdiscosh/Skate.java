package com.example.willsrollerdiscosh;

public class Skate {
    private String skateSize;
    private Integer skateAmount;

    public Skate(String skateSize, Integer skateAmount){
        this.skateSize = skateSize;
        this.skateAmount = skateAmount;
    }

    public String getSkateSize(){
        return skateSize;
    }

    public Integer getSkateAmount(){
        return skateAmount;
    }

    public void setSkateSize(String skateSize) {
        this.skateSize = skateSize;
    }

    public void setSkateAmount(Integer skateAmount) {
        this.skateAmount = skateAmount;
    }
}
