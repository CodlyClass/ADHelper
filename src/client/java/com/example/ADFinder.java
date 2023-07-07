package com.example;

import com.example.config.ConfigManager;
import com.example.gui.SettingGuiDescription;
import com.example.gui.SettingScreen;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.StonecutterBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import static java.lang.Math.max;

public class ADFinder implements ClientModInitializer {
    public static int status = 0;
    public static boolean toggle = false;
    public static ConfigManager configManager;
    private KeyBinding autofishGuiKey;
    @Override
    public void onInitializeClient() {
        configManager = new ConfigManager(this);
        ClientTickEvents.END_CLIENT_TICK.register(this::tick);
        autofishGuiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.ADFinder.open_gui", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_F12, "ADFinder"));
//        AttackBlockCallback.EVENT.register((player, world, hand, pos, direction) -> {
////            player.applyMovementInput(pos.toCenterPos(),0.5f);
//            return ActionResult.PASS;
//        });

    }
    public void tick(MinecraftClient client) {
        if(client.player!=null) {
            if (autofishGuiKey.wasPressed() && toggle == false) {
                MinecraftClient.getInstance().setScreen(new SettingScreen(new SettingGuiDescription()));
                toggle = true;
            }
            if (!autofishGuiKey.wasPressed())toggle = false;
            if(configManager.getConfig().isOn()){
                client.player.setOnFire(false);
                findAncientDebris(client);
            }
        }
    }

    public void findAncientDebris(MinecraftClient client) {
        PlayerEntity player = client.player;

        int radius = configManager.getConfig().getDetectRadius();
        String targetBlockName = configManager.getConfig().getTargetBlockName();

        assert player != null;
        Vec3d playerPos = player.getPos();
        playerPos = playerPos.add(0,1.15,0);

        double mn = 1145141919;
        Vec3d target = new Vec3d(0,0,0);

        for(int x = (int) (playerPos.getX()-radius); x<=playerPos.getX()+radius; x++){
            for(int y = (int) (player.getY()-radius); y<=playerPos.getY()+radius; y++){
                for(int z = (int) (player.getZ()-radius); z<=playerPos.getZ()+radius; z++){
                    BlockState state = player.getWorld().getBlockState(new BlockPos(x,y,z));
                    if(state.getBlock().toString().equals("Block{"+targetBlockName+"}")) {
                        if(status == 0) {
                            status = 1;
                            player.sendMessage(Text.of("ancient debris! at " + x + " " + y + " " + z));
                        }
                        Vec3d end = new Vec3d(x + 0.5f, y + 0.5f, z + 0.5f);
                        double dis = end.distanceTo(playerPos);
                        if(dis + 1 < mn){
                            mn = dis + 1;
                            target = new Vec3d(end.x,end.y,end.z);
                        }

                    }
                }
            }
        }
        if(mn!=1145141919){
            for (double i = 0; i <= mn; i += 0.11) {
                player.getWorld().addImportantParticle(new DustParticleEffect(new Vector3f(0, (float)mn/radius, (float)max(0,radius-mn)/radius), (float) (0.2f+1.2f*i/mn)),
                        (target.x-playerPos.x)*i/mn+playerPos.x,
                        (target.y-playerPos.y)*i/mn+playerPos.y,
                        (target.z-playerPos.z)*i/mn+playerPos.z,
                        0,
                        0,
                        0);
            }
        }else {
            if(status == 1){
                player.sendMessage(Text.of("ADFinder search done"));
            }
            status = 0;
        }

    }
}