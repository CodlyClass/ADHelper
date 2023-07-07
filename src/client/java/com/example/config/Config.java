package com.example.config;

import com.google.gson.annotations.Expose;

public class Config {
    @Expose int detectRadius = 16;
    @Expose String targetBlockName = "minecraft:ancient_debris";
    @Expose boolean isOn = true;

    public int getDetectRadius() {
        return detectRadius;
    }

    public void setDetectRadius(int detectRadius) {
        this.detectRadius = detectRadius;
    }

    public String getTargetBlockName() {
        return targetBlockName;
    }

    public void setTargetBlockName(String targetBlock) {
        this.targetBlockName = targetBlock;
    }

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean on) {
        isOn = on;
    }
}
