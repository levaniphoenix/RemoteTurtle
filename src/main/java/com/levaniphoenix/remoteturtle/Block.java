package com.levaniphoenix.remoteturtle;

import javafx.scene.shape.Box;

public class Block extends Box {

    private String blockName = "undefined";

    public Block(float v, float v1, float v2) {
        super(v,v1,v2);
    }

    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }

    @Override
    public String toString() {
        return "Block{" +
                "blockName='" + blockName + '\'' +
                '}';
    }
}
