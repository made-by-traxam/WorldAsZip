package de.tr808axm.worldaszip;

import de.tr808axm.worldaszip.command.WorldAsZipCommandExecutor;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by Maximilian on 16.04.2016.
 */
public class Main extends JavaPlugin {
    List<String> filesListInDir;

    @Override
    public void onDisable() {
        super.onDisable();
        getLogger().info("Disabled!");
    }

    @Override
    public void onEnable() {
        super.onEnable();
        registerCommands();
        if(!getDataFolder().exists()) getDataFolder().mkdirs();
        getLogger().info("Enabled!");
    }

    private void registerCommands() {
        getCommand("worldaszip").setExecutor(new WorldAsZipCommandExecutor(this));
    }

    public boolean build(World w) {
        getLogger().info("Building zip...");
        try {
            File dir = w.getWorldFolder();
            filesListInDir = new ArrayList<String>();
            populateFilesList(dir);

            FileOutputStream fos = new FileOutputStream(new File(getDataFolder(), w.getName() + ".zip"));
            ZipOutputStream zos = new ZipOutputStream(fos);
            for(String filePath : filesListInDir) {
                getLogger().info("Zipping " + filePath);
                ZipEntry ze = new ZipEntry(filePath.substring(dir.getAbsolutePath().length()+1, filePath.length()));
                zos.putNextEntry(ze);
                FileInputStream fis = new FileInputStream(filePath);
                byte[] buffer = new byte[1024];
                int len;
                while((len = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, len);
                }
                zos.closeEntry();
                fis.close();
            }
            zos.close();
            fos.close();
            getLogger().info("Successfully built zip!");
            return true;
        } catch (IOException e) {
            getLogger().severe("Could not zip world: " + e.getClass().getSimpleName());
            e.printStackTrace();
            return false;
        }
    }

    public void populateFilesList(File dir) {
        File[] files = dir.listFiles();
        for(File f : files) {
            if(f.isFile()) filesListInDir.add(f.getAbsolutePath());
            else populateFilesList(f);
        }
    }
}
