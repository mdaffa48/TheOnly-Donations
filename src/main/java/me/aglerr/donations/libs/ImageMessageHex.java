package me.aglerr.donations.libs;

import de.themoep.minedown.MineDown;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
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
                int rgb = resized.getRGB(x, y);
                chatImg[x][y] = new Color(rgb, true);
            }
        }
        return chatImg;
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int width, int height) {
        AffineTransform af = new AffineTransform();
        af.scale(
                width / (double) originalImage.getWidth(),
                height / (double) originalImage.getHeight());

        AffineTransformOp operation = new AffineTransformOp(af, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        return operation.filter(originalImage, null);
    }

    private String[] toImgMessage(Color[][] colors, char imgchar) {
        lines = new String[colors[0].length];

        for (int y = 0; y < colors[0].length; y++) {
            StringBuilder line = new StringBuilder();
            for (int x = 0; x < colors.length; x++) {
                Color color = colors[x][y];
                // convert to minedown-styled color string
                if (color != null) {
                    line.append("&")
                            .append(colorToHex(colors[x][y]))
                            .append("&")
                            .append(imgchar);
                }
                else {
                    line.append(TRANSPARENT_CHAR);
                }
            }
            lines[y] = line.toString() + ChatColor.RESET;
        }

        return lines;
    }

    private String colorToHex(Color c) {
        return String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());
    }

    public ImageMessageHex appendText(String... text) {
        for (int y = 0; y < lines.length; y++) {
            if (text.length > y) {
                lines[y] += " " + text[y];
            }
        }
        return this;
    }

    public ImageMessageHex appendCenteredText(String... text) {
        for (int y = 0; y < lines.length; y++) {
            if (text.length > y) {
                int len = ChatPaginator.AVERAGE_CHAT_PAGE_WIDTH - lines[y].length();
                lines[y] = lines[y] + centerText(text[y], len);
            } else {
                return this;
            }
        }
        return this;
    }

    public String centerText(String text, int lineLength) {
        char[] chars = text.toCharArray(); // Get a list of all characters in text
        boolean isBold = false;
        double length = 0;
        ChatColor pholder = null;
        for (int i = 0; i < chars.length; i++) { // Loop through all characters
            // Check if the character is a ColorCode..
            if (chars[i] == '&' && chars.length != (i + 1) && (pholder = ChatColor.getByChar(chars[i + 1])) != null) {
                if (pholder != ChatColor.UNDERLINE && pholder != ChatColor.ITALIC
                        && pholder != ChatColor.STRIKETHROUGH && pholder != ChatColor.MAGIC) {
                    isBold = (chars[i + 1] == 'l'); // Setting bold  to true or false, depending on if the ChatColor is Bold.
                    length--; // Removing one from the length, of the string, because we don't wanna count color codes.
                    i += isBold ? 1 : 0;
                }
            } else {
                // If the character is not a color code:
                length++; // Adding a space
                length += (isBold ? (chars[i] != ' ' ? 0.7555555555555556 : 0) : 0); // Adding 0.156 spaces if the character is bold.
            }
        }

        double spaces = (lineLength - length) / 2; // Getting the spaces to add by (max line length - length) / 2

        // Adding the spaces
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < spaces; i++) {
            builder.append(' ');
        }
        String copy = builder.toString();
        builder.append(text).append(copy);

        return builder.toString();
    }

    public void sendToPlayers() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            for(String line : lines){
                player.spigot().sendMessage(MineDown.parse(line));
            }
        });
    }
}
