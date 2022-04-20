package ascii_art;
import image.Image;
import java.util.logging.Logger;

/**
 * Main class. This class gets the given image and runs the Shell class if it opened. If it doesn't open or
 * there is no file in the argument, it prints an error message.
 */
public class Driver {
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("USAGE: java asciiArt ");
            return;
        }
        Image img = Image.fromFile(args[0]);
        if (img == null) {
            Logger.getGlobal().severe("Failed to open image file " + args[0]);
            return;
        }
        new Shell(img).run();
    }
}