package me.aglerr.donations.managers;

import me.aglerr.mclibs.libs.CustomConfig;

public class ConfigManager {

    public static CustomConfig CONFIG;
    public static CustomConfig PRODUCT;
    public static CustomConfig DATA;

    public static void initialize(){
        CONFIG = new CustomConfig("config.yml", null);
        PRODUCT = new CustomConfig("product.yml", null);
        DATA = new CustomConfig("data.yml", null);
    }

}
