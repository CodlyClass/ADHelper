package com.example.gui;


import io.github.cottonmc.cotton.gui.GuiDescription;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;

public class SettingScreen extends CottonClientScreen {
    private final SettingGuiDescription settingGuiDescription;
    public SettingScreen(SettingGuiDescription description) {
        super(description);
        this.settingGuiDescription = description;
    }

    @Override
    public void tick() {
        if(settingGuiDescription.status == ScreenStatus.Closed){
            assert client != null;
            client.setScreen(null);
        }
    }
}