/** WILLS ROLLER DISCO - DISSERTATION PROJECT
 *  AUTHOR : EMILY FLETCHER
 *  STUDENT NUMBER: 18410839
 *  APPLICATION: WillsRollerDiscoFD
 *  FILE TITLE: Skate.java
 *  APPLICATION VERSION: 2.0
 *  DATE OF WRITING: 20/06/2023
 *
 *  PURPOSE:
 *      Setters for skate objects. Used for setting values,
 *   */
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
    
    public void setSkateAmount(Integer skateAmount) {
        this.skateAmount = skateAmount;
    }
}
