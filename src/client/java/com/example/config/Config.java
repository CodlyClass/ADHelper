package com.example.config;

import com.google.gson.annotations.Expose;

public class Config {
    @Expose int detectRadius = 16;

    public int getDetectRadius() {
        return detectRadius;
    }

    public void setDetectRadius(int detectRadius) {
        this.detectRadius = detectRadius;
    }
}
