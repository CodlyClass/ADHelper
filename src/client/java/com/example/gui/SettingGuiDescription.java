package com.example.gui;

import com.example.ADFinder;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.Axis;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.minecraft.text.Text;

public class SettingGuiDescription extends LightweightGuiDescription {
    public ScreenStatus status = ScreenStatus.Running;
    private int gridSize = 2;
    private int gapHeigh = 20;
    private int gapWidth = 40;
    private int gridHeigh = 9;
    private int griWidth = 7;
    public SettingGuiDescription() {
        WGridPanel root = new WGridPanel(gridSize);
        setRootPanel(root);
        root.setInsets(Insets.ROOT_PANEL);
        root.setGaps(gapWidth, gapHeigh);
        root.setSize(gridSize * griWidth, gridSize * gridHeigh);


        WLabel label = new WLabel(Text.literal("Detect radius: "), 0xFFFFFF);

        WSlider radiusRanger = new WSlider(1,32, Axis.HORIZONTAL);
        radiusRanger.setValue(ADFinder.configManager.getConfig().getDetectRadius());

        WDynamicLabel currentRadius = new WDynamicLabel(() -> (String.valueOf(radiusRanger.getValue())));
        currentRadius.setColor(0xFFFFFF,0xFFFFFF);

        WButton Apply = new WButton(Text.literal("Apply"));
        Apply.setOnClick(()->{
            ADFinder.configManager.getConfig().setDetectRadius(radiusRanger.getValue());
            ADFinder.configManager.writeConfig(true);
            status = ScreenStatus.Closed;
        });
        WButton Cancel = new WButton(Text.literal("Cancel"));
        Cancel.setOnClick(()->{
            status = ScreenStatus.Closed;
        });

        root.add(label, 0, 0);
        root.add(radiusRanger, 3, 0,3,1);
        root.add(currentRadius, 6,0);
        root.add(Apply, 5, 8,2,1);
        root.add(Cancel, 6, 8,2,1);
        root.validate(this);
    }

    @Override
    public void addPainters() {
//        if (this.rootPanel!=null && !fullscreen) {
//            this.rootPanel.setBackgroundPainter(BackgroundPainter.createColorful(0x00FF00));
//        }
    }
}