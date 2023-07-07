package com.example.gui;

import com.example.ADFinder;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.Axis;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import java.util.Vector;
import java.util.function.Predicate;

interface CallBack{
    void saveSetting();
}
public class SettingGuiDescription extends LightweightGuiDescription {
    public ScreenStatus status = ScreenStatus.Running;
    private int gridSize = 2;
    private int gapHeigh = 20;
    private int gapWidth = 40;
    private int gridHeigh = 9;
    private int griWidth = 7;
    private int tmpY = 0;
    private WGridPanel root = new WGridPanel(gridSize);
    private final Vector<CallBack> saveUpList = new Vector<>();
    public SettingGuiDescription() {
        setupBasic();

        setupSwitch();

        setupDetectRadius();

        setupTargetBlock();

        addButton();

        root.validate(this);
    }
    public void setupBasic(){
        setRootPanel(root);
        root.setInsets(Insets.ROOT_PANEL);
        root.setGaps(gapWidth, gapHeigh);
        root.setSize(gridSize * griWidth, gridSize * gridHeigh);

        WLabel label = new WLabel(Text.literal("ADFinder Setting"), 0x3311FF);
        root.add(label, 0, tmpY);
        tmpY++;
    }
    public void setupSwitch(){
        WLabel label = new WLabel(Text.literal("Switch"), 0xFFFFFF);
        WToggleButton isOn =new WToggleButton();
        isOn.setToggle(ADFinder.configManager.getConfig().isOn());

        root.add(label, 0, tmpY);
        root.add(isOn, 3, tmpY,2,1);
        tmpY++;

        saveUpList.add(() -> ADFinder.configManager.getConfig().setOn(isOn.getToggle()));
    }
    public void setupDetectRadius(){
        WLabel label = new WLabel(Text.literal("Detect radius"), 0xFFFFFF);

        WSlider radiusRanger = new WSlider(1,32, Axis.HORIZONTAL);
        radiusRanger.setValue(ADFinder.configManager.getConfig().getDetectRadius());

        WDynamicLabel currentRadius = new WDynamicLabel(() -> (String.valueOf(radiusRanger.getValue())));
        currentRadius.setColor(0xFFFFFF,0xFFFFFF);

        root.add(label, 0, tmpY);
        root.add(radiusRanger, 3, tmpY,3,1);
        root.add(currentRadius, 6,tmpY);
        tmpY++;

        saveUpList.add(() -> ADFinder.configManager.getConfig().setDetectRadius(radiusRanger.getValue()));
    }
    public void setupTargetBlock(){
        WLabel label = new WLabel(Text.literal("Target block"), 0xFFFFFF);
        WTextField input = new WTextField();
        input.setMaxLength(64);
        input.setText(ADFinder.configManager.getConfig().getTargetBlockName());

        root.add(label, 0, tmpY);
        root.add(input, 3, tmpY,5,1);
        tmpY++;

        saveUpList.add(() -> ADFinder.configManager.getConfig().setTargetBlockName(input.getText()));
    }
    public void addButton(){
        WButton Apply = new WButton(Text.literal("Apply"));
        Apply.setOnClick(()->{
            for(CallBack option: saveUpList){
                option.saveSetting();
            }
            ADFinder.configManager.writeConfig(true);
            status = ScreenStatus.Closed;
        });
        WButton Cancel = new WButton(Text.literal("Cancel"));
        Cancel.setOnClick(()->{
            status = ScreenStatus.Closed;
        });

        root.add(Apply, 4, 8,2,1);
        root.add(Cancel, 6, 8,2,1);
    }
    @Override
    public void addPainters() {
//        if (this.rootPanel!=null && !fullscreen) {
//            this.rootPanel.setBackgroundPainter(BackgroundPainter.createColorful(0x00FF00));
//        }
    }
}