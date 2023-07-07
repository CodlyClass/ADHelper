package com.example.config;

import com.example.ADFinder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ConfigManager {
    private Config config;
    private ADFinder adFinder;
    private Gson gson;
    private File configFile;
    private Executor executor = Executors.newSingleThreadExecutor();

    public ConfigManager(ADFinder adFinder){
        this.adFinder=adFinder;
        this.gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
        this.configFile = new File(FabricLoader.getInstance().getConfigDir().toFile(), "ADFinder.config");
        readConfig();
    }

    public void readConfig(){
        try {
            if (configFile.exists()) {
                String fileContents = FileUtils.readFileToString(configFile, Charset.defaultCharset());
                config = gson.fromJson(fileContents, Config.class);
            }else{
                //generate a new config file
                config = new Config();
                writeConfig(false);
            }
        } catch (IOException e) {
            e.printStackTrace();
            //generate a new config file
            config = new Config();
            writeConfig(false);
        }
    }

    public void writeConfig(boolean async) {
        Runnable task = () -> {
            try {
                if (config != null) {
                    String serialized = gson.toJson(config);
                    FileUtils.writeStringToFile(configFile, serialized, Charset.defaultCharset());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        if (async) executor.execute(task);
        else task.run();
    }
    public Config getConfig() {
        return config;
    }
}
