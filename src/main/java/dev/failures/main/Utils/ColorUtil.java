package dev.failures.main.Utils;

import net.md_5.bungee.api.ChatColor;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorUtil {
    public static String colorize(String message){
        Pattern hexPattern = Pattern.compile("&#([A-Fa-f0-9]){6}");

        Matcher matcher = hexPattern.matcher(message);
        while (matcher.find()) {
            String color = message.substring(matcher.start()+1,matcher.end());
            message = message.replace("&"+color, ChatColor.of(color) + "");
            matcher = hexPattern.matcher(message);
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }
    public static List<String> colorize(List<String> lore) {
        List<String> newLore = new ArrayList<>();
        for(String l : lore) {
            newLore.add(colorize(l));
        }
        return newLore;
    }

    public static String capitalize(String str) {
        if(str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static String withSuffix(long count) {
        if (count < 1000) return String.format("%,.0f",(double)count);
        int exp = (int) (Math.log(count) / Math.log(1000));
        DecimalFormat format = new DecimalFormat("0.##");
        return String.format("%s%c",
                format.format(count/Math.pow(1000,exp)),
                "kMBTGE".charAt(exp-1));
    }
    public static String withSuffix(Double count) {
        if (count < 1000) return withCommas(count);
        int exp = (int) (Math.log(count) / Math.log(1000));
        DecimalFormat format = new DecimalFormat("0.##");
        return String.format("%s%c",
                format.format(count/Math.pow(1000,exp)),
                "kMBTGE".charAt(exp-1));
    }

    public static String withCommas(Double count) {
        return String.format("%,.0f",count);
    }
}

