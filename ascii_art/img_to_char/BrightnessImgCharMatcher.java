package ascii_art.img_to_char;

import image.Image;

import java.awt.*;
import java.util.*;

/**
 * Responsible for converting images to an ASCII art - receives address to the picture and returns the same
 * picture that is made from ASCII letters according to each pixel's brightness level.
 */
public class BrightnessImgCharMatcher {
    private static final int NUM_OF_PIXELS = 16;
    private static final int RANGE = 255;
    private static final float RED = 0.2126f;
    private static final float GREEN = 0.7152f;
    private static final float BLUE = 0.0722f;

    private final Image image;
    private final String font;
    private final HashMap<Image, Double> cache = new HashMap<>();

    /**
     * Constructor.
     * @param image - image to convert to an ASCII art image.
     * @param font - font type of the ASCII letters to be used in the ASCII art.
     */
    public BrightnessImgCharMatcher(Image image, String font) {
        this.image = image;
        this.font = font;
    }

    /**
     * Calculates the brightness level of each given character.
     * @param charSet - array of characters to calculate their character level.
     * @return - new array of the corresponding brightness level of each character in the given array.
     */
    private float[] brightnessLevel(Character[] charSet) {
        float[] result = new float[charSet.length];
        int charIdx = 0;
        for (Character character: charSet ) {
            boolean[][] curCharBrightness = CharRenderer.getImg(character, NUM_OF_PIXELS, font);
            float whiteCounter = 0;
            for (boolean[] charBrightness : curCharBrightness) {
                for (boolean brightness : charBrightness) {
                    if (brightness)
                        whiteCounter++;
                }
            }
            result[charIdx] = whiteCounter / (NUM_OF_PIXELS * NUM_OF_PIXELS);
            charIdx++;
        }
        return result;
    }

    /**
     * Preforms a linear stretch on the given brightness level of characters.
     * @param brightnessLevels - array of brightness level of characters to linearly stretch.
     * @return - array of the corresponding linear stretch on each character's brightness level given.
     */
    private float[] linearStretch(float[] brightnessLevels) {
        float[] result = new float[brightnessLevels.length];
        float minBrightness = 1;
        float maxBrightness = 0;
        for (float brightnessLevel: brightnessLevels) {
            if (brightnessLevel < minBrightness)
                minBrightness = brightnessLevel;
            if (brightnessLevel > maxBrightness)
                maxBrightness = brightnessLevel;
        }
        for (int i = 0; i < brightnessLevels.length; i++) {
            if ((maxBrightness - minBrightness) == 0)
                result[i] = brightnessLevels[i];
            else
                result[i] = (brightnessLevels[i] - minBrightness) / (maxBrightness - minBrightness);
        }
        return result;
    }

    /**
     * Converts the given image to one ASCII character according to its linear stretch brightness level.
     * @param image - image to convert.
     * @return - image's average brightness level.
     */
    private double averageBrightnessPerImage(Image image) {
        if (image == null) return 0f;
        if (cache.containsKey(image)) {
            return cache.get(image);
        }

        float pixelsBrightness = 0;
        int numOfPixels = 0;
        for (Color pixel: image.pixels()) {
            numOfPixels++;
            float noColorPixel = pixel.getBlue();
            if (!(pixel.equals(Color.WHITE) || pixel.equals(Color.BLACK))) {
                noColorPixel = pixel.getRed() * RED + pixel.getGreen() * GREEN + pixel.getBlue() * BLUE;
            }
            pixelsBrightness += (noColorPixel / RANGE);
        }
        cache.put(image, (double) pixelsBrightness / numOfPixels);
        return (double) pixelsBrightness / numOfPixels;
    }

    /**
     * Converts the given image to an ASCII art. It divides the image to small images and each image sized
     * pixels X pixels , gets the ASCII character that replaces it according to the closest char's brightness level
     * to it's brightness level.
     * @param charsBrightnessLevel - map that maps each char to it's linearly stretched float.
     * @param numCharsInRow - image resolution - number of pictures in a row.
     * @return - 2D array that represents the ASCII char to replace each pixel in the given image.
     */
    private char[][] convertImageToAscii(Map<Character, Float> charsBrightnessLevel, int numCharsInRow) {
        int pixels = image.getWidth() / numCharsInRow;
        char[][] asciiArt = new char[image.getHeight()/pixels][image.getWidth()/pixels];
        int row = 0;
        int col = 0;
        for(Image subImage : image.squareSubImagesOfSize(pixels)) {
            float averageBrightnessPerImage = (float) averageBrightnessPerImage(subImage);
            float closestLevel = 1;
            char closestLevelChar = 'a';
            for (Map.Entry<Character, Float> pair: charsBrightnessLevel.entrySet()) {
                if (Math.abs(averageBrightnessPerImage - pair.getValue()) <= closestLevel) {
                    closestLevelChar = pair.getKey();
                    closestLevel = Math.abs(averageBrightnessPerImage - pair.getValue());
                }
            }
            asciiArt[row][col] = closestLevelChar;
            col = ((col + 1) % (image.getWidth() / pixels));
            if (col == 0)
                row++;
        }
        return asciiArt;
    }

    /**
     * For given image resolution and ASCII characters array, Converts the given image to an ASCII art.
     * It divides the image to small images and each image sized pixels X pixels, gets the ASCII character
     * that replaces it according to the closest char's brightness level to it's brightness level.
     * @param numCharsInRow - resolution of the picture - number of ASCII chars in each row and column.
     * @param charSet - ASCII chars array to use in the art.
     * @return - 2D array that represents the ASCII char to replace each pixel in the given image.
     */
    public char[][] chooseChars(int numCharsInRow, Character[] charSet) {
        char[][] asciiArt = null;
        if (image != null) {
            float[] brightnessLevel = brightnessLevel(charSet);
            float[] linearStretch = linearStretch(brightnessLevel);
            Map<Character, Float> map = new HashMap<>();
            for (int i = 0; i < linearStretch.length; i++)
                map.put(charSet[i], linearStretch[i]);
            asciiArt = convertImageToAscii(map, numCharsInRow);
        }
        return asciiArt;
    }
}
