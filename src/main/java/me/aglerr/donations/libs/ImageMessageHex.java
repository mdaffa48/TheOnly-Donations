package me.aglerr.donations.libs;

import com.muhammaddaffa.mdlib.utils.Common;
import com.muhammaddaffa.mdlib.utils.Logger;
import me.aglerr.donations.ConfigValue;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.util.ChatPaginator;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class ImageMessageHex {
    private final static char TRANSPARENT_CHAR = ' ';

    private String[] lines;

    public ImageMessageHex(BufferedImage image, int height, char imgChar) {
        Color[][] chatColors = toChatColorArray(image, height);
        lines = toImgMessage(chatColors, imgChar);
    }

    private Color[][] toChatColorArray(BufferedImage image, int height) {
        double ratio = (double) image.getHeight() / image.getWidth();
        int width = (int) (height / ratio);
        if (width > 10) width = 10;
        BufferedImage resized = resizeImage(image, width, height);

        Color[][] chatImg = new Color[resized.getWidth()][resized.getHeight()];
        for (int x = 0; x < resized.getWidth(); x++) {
            for (int y = 0; y < resized.getHeight(); y++) {
                int argb = resized.getRGB(x, y);
                Color color = new Color(argb, true);
                if (color.getAlpha() < 128) { // Transparency check
                    chatImg[x][y] = null;
                } else {
                    chatImg[x][y] = new Color(color.getRed(), color.getGreen(), color.getBlue());
                }
            }
        }
        return chatImg;
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int width, int height) {
        Image scaled = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(scaled, 0, 0, null);
        g2d.dispose();
        return resized;
        /*AffineTransform af = new AffineTransform();
        af.scale(
                width / (double) originalImage.getWidth(),
                height / (double) originalImage.getHeight());

        AffineTransformOp operation = new AffineTransformOp(af, Image.SCALE_SMOOTH);
        return operation.filter(originalImage, null);*/
    }

    private String[] toImgMessage(Color[][] colors, char imgchar) {
        lines = new String[colors[0].length];

        for (int y = 0; y < colors[0].length; y++) {
            StringBuilder line = new StringBuilder();
            for (int x = 0; x < colors.length; x++) {
                Color color = colors[x][y];
                // convert to minedown-styled color string
                if (color != null) {
                    line.append("&#")
                            .append(colorToHex(color))
                            .append(imgchar);
                } else {
                    line.append(TRANSPARENT_CHAR);
                }
            }
            lines[y] = Common.color(line.toString()) + ChatColor.RESET;
        }

        return lines;
    }

    private String colorToHex(Color c) {
        return String.format("%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());
    }

    public ImageMessageHex appendText(String... text) {
        for (int y = 0; y < lines.length; y++) {
            if (text.length > y) {
                lines[y] += " " + text[y];
            }
        }
        return this;
    }


    public void sendToPlayers() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.spigot().sendMessage(translateHex(ConfigValue.HEADER));
            for (String line : lines) {
                player.spigot().sendMessage(translateHex(line));
            }
            player.spigot().sendMessage(translateHex(ConfigValue.FOOTER));
        });
    }

    /**
     * Translates a hex color string (&#RRGGBB) into a properly formatted BaseComponent[]
     */
    private BaseComponent[] translateHex(String message) {
        return TextComponent.fromLegacyText(Common.color(message));
    }
}
