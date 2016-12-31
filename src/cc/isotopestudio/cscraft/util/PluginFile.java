/*
 * Copyright (c) 2016. ISOTOPE Studio
 */

package cc.isotopestudio.cscraft.util;

import com.google.common.io.Files;
import org.apache.commons.lang.Validate;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;

public class PluginFile extends YamlConfiguration {

    private final File file;
    private final String defaults;
    private final JavaPlugin plugin;

    /**
     * Creates new PluginFile, with defaults
     *
     * @param plugin       - Your plugin
     * @param fileName     - Name of the file
     * @param defaultsName - Name of the defaults
     */
    public PluginFile(JavaPlugin plugin, String fileName, String defaultsName) {
        this.plugin = plugin;
        this.defaults = defaultsName;
        this.file = new File(plugin.getDataFolder(), fileName);
        reload();
    }

    public PluginFile(JavaPlugin plugin, String fileName) {
        this.plugin = plugin;
        this.defaults = null;
        this.file = new File(plugin.getDataFolder(), fileName);
        reload();
    }

    private boolean editable = true;

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    /**
     * Reload configuration
     */
    public void reload() {

        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
                if (defaults != null) {
                    OutputStream os = new FileOutputStream(file);
                    int bytesRead = 0;
                    byte[] buffer = new byte[8192];
                    InputStream resource = plugin.getResource(defaults);
                    while ((bytesRead = resource.read(buffer, 0, 8192)) != -1) {
                        os.write(buffer, 0, bytesRead);
                    }
                    os.close();
                    resource.close();
                    load(file);
                }
            } catch (IOException e) {
                e.printStackTrace();
                plugin.getLogger().severe("Error while creating file " + file.getName());
            } catch (InvalidConfigurationException e) {
                e.printStackTrace();
            }
        } else try {
            load(file);
        } catch (IOException | InvalidConfigurationException exception) {
            exception.printStackTrace();
            plugin.getLogger().severe("Error while loading file " + file.getName());
        }
    }

    /**
     * Save configuration
     */
    public void save() {
        if (editable)
            try {
                options().indent(2);
                save(file);
            } catch (IOException exception) {
                exception.printStackTrace();
                plugin.getLogger().severe("Error while saving file " + file.getName());
            }
    }

    @Override
    public void save(File file) throws IOException {
        Validate.notNull(file, "File cannot be null");
        Files.createParentDirs(file);
        String data = this.saveToString();
        Writer writer = new OutputStreamWriter(new FileOutputStream(file));
        try {
            writer.write(data);
        } finally {
            writer.close();
        }
    }

    @Override
    public void load(File file) throws IOException, InvalidConfigurationException {
        Validate.notNull(file, "File cannot be null");
        super.load(new FileInputStream(file));
    }

}