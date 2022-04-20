package ascii_art;

import ascii_art.img_to_char.BrightnessImgCharMatcher;
import ascii_output.AsciiOutput;
import ascii_output.ConsoleAsciiOutput;
import ascii_output.HtmlAsciiOutput;
import image.Image;

import java.util.*;
import java.util.stream.Stream;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * The class that extends the ASCII art optionality. It provides functionality for: adding, removing, rendering,
 * showing all characters in the run, choosing to render to the console, increasing and decreasing the
 * resolution.
 */
public class Shell {
    private static final String CMD_EXIT = "exit";
    private static final String ADD = "add";
    private static final String CHARS = "chars";
    private static final String REMOVE = "remove";
    private static final String RES = "res";
    private static final String CONSOLE = "console";
    private static final String RENDER = "render";
    private static final String UP = "up";
    private static final String DOWN = "down";
    private static final String INVALID_INPUT = "Invalid input";
    private static final String TAB = ">>> ";
    private static final String SPACE = "space";
    private static final String ALL = "all";
    private static final String WIDTH_INDICATOR = "Width set to %d\n";
    private static final String MAX_RES_MSG = "You're using the maximal resolution";
    private static final String MIN_RES_MSG = "You're using the minimal resolution";
    private static final int INITIAL_CHARS_IN_ROW = 64;
    private static final int MIN_PIXELS_PER_CHAR = 2;
    private static final String FONT_NAME = "Courier New";
    private static final String OUTPUT_FILENAME = "out.html";
    private static final String INITIAL_CHARS_RANGE = "0-9";
    private static final int FIRST_ASCII_CHAR = 32;
    private static final int LAST_ASCII_CHAR = 126;

    private Image img;
    private Set<Character> charSet = new HashSet<>();
    private final int minCharsInRow;
    private final int maxCharsInRow;
    private int charsInRow;
    private BrightnessImgCharMatcher charMatcher;
    private AsciiOutput output;

    /**
     * Constructor. Initializes the minimum and maximum resolution, the algorithm that calculates the
     * brightness level, the html output class and adds the characters: 0-9 to the
     * @param img
     */
    public Shell(Image img) {
        this.img = img;
        minCharsInRow = max(1, img.getWidth()/img.getHeight());
        maxCharsInRow = img.getWidth() / MIN_PIXELS_PER_CHAR;
        charsInRow = max(min(INITIAL_CHARS_IN_ROW, maxCharsInRow), minCharsInRow);
        charMatcher = new BrightnessImgCharMatcher(img, FONT_NAME);
        output = new HtmlAsciiOutput(OUTPUT_FILENAME, FONT_NAME);
        addChars(INITIAL_CHARS_RANGE);
    }

    /**
     * Runs the extension. prints '>>> ' and expects for one of the inputs: exit, add, remove, chars, res up, res down,
     * console, render. It runs until the user typed exit. It informs the user for invalid input.
     */
    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.print(TAB);
        String cmd = scanner.nextLine().trim();
        while(!cmd.equals(CMD_EXIT)) {
            boolean switchFlag = true;
            String[] splitInput = cmd.split(" ");
            if (splitInput.length == 0) {
                System.out.println(INVALID_INPUT);
                System.out.print(TAB);
                cmd = scanner.nextLine();
                switchFlag = false;
            }
            if (switchFlag) {
                switch (splitInput[0]) {
                    case CHARS:
                        if (splitInput.length != 1) {
                            System.out.println(INVALID_INPUT);
                            break;
                        }
                        showChars();
                        break;
                    case ADD:
                        if (splitInput.length != 2) {
                            System.out.println(INVALID_INPUT);
                            break;
                        }
                        addChars(splitInput[1]);
                        break;
                    case REMOVE:
                        if (splitInput.length != 2) {
                            System.out.println(INVALID_INPUT);
                            break;
                        }
                        removeChars(splitInput[1]);
                        break;
                    case RES:
                        if (splitInput.length != 2) {
                            System.out.println(INVALID_INPUT);
                            break;
                        }
                        resChange(splitInput[1]);
                        break;
                    case CONSOLE:
                        if (splitInput.length != 1) {
                            System.out.println(INVALID_INPUT);
                            break;
                        }
                        output = new ConsoleAsciiOutput();
                        break;
                    case RENDER:
                        if (splitInput.length != 1) {
                            System.out.println(INVALID_INPUT);
                            break;
                        }
                        render();
                        output = new HtmlAsciiOutput(OUTPUT_FILENAME, FONT_NAME);
                        break;
                    default:
                        System.out.println(INVALID_INPUT);
                        break;
                }
            }
            else
                System.out.println(INVALID_INPUT);
            System.out.print(TAB);
            cmd = scanner.nextLine();
        }
    }

    /**
     * Shown all the ASCII symbols that we can use to render.
     */
    private void showChars() {
        charSet.stream().sorted().forEach(c-> System.out.print(c + " "));
        System.out.println();
    }

    /**
     * After remove or add command the user inputs what ascii char to add. This function places in an array of
     * 2 characters the range of chars to add to the list of all possible ascii characters that can be used
     * for rendering the image.
     * @param param string that comes after add or remove to parse.
     * @return array of 2 characters for the range indication.
     */
    private static char[] parseCharRange(String param) {
        char[] charRange;
        char[] stringToChar = param.toCharArray();
        if ((param.length() == 1) && (FIRST_ASCII_CHAR <= (int) stringToChar[0]) &&
                ((int) stringToChar[0] <= LAST_ASCII_CHAR))
            charRange = new char[]{stringToChar[0], stringToChar[0]};
        else if (param.equals(SPACE))
            charRange = new char[]{FIRST_ASCII_CHAR, FIRST_ASCII_CHAR};
        else if (param.equals(ALL))
            charRange = new char[]{FIRST_ASCII_CHAR, LAST_ASCII_CHAR};
        else if (param.contains("-") && (FIRST_ASCII_CHAR <= (int) stringToChar[0]) &&
                ((int) stringToChar[0] <= LAST_ASCII_CHAR)
                && (FIRST_ASCII_CHAR <= (int) stringToChar[2]) && ((int) stringToChar[2] <= LAST_ASCII_CHAR))
            charRange = new char[]{(char) min(stringToChar[0], stringToChar[2]),
                    (char) max(stringToChar[0], stringToChar[2])};
        else {
            System.out.println(INVALID_INPUT);
            return null;
        }

        return charRange;
    }

    /**
     * Adds characters to the list of all possible ASCII chars that can be rendered. It can be: 1 char,
     * range of chars, all chars or space char.
     * @param s string that was entered by the user after add.
     */
    private void addChars(String s) {
        char[] range = parseCharRange(s);
        if(range != null){
            Stream.iterate(range[0], c -> c <= range[1], c -> (char)((int)c+1)).forEach(charSet::add);
        }
    }

    /**
     * Removes characters from the list of all possible ASCII chars that can be rendered. It can be: 1 char,
     * range of chars, all chars or space char.
     * @param s string that was entered by the user after remove.
     */
    private void removeChars(String s) {
        char[] range = parseCharRange(s);
        if(range != null){
            Stream.iterate(range[0], c -> c <= range[1], c -> (char)((int)c+1)).forEach(charSet::remove);
        }
    }

    /**
     * Increases or decrease the image's resolution by 2.
     * @param s string that represents if to increase or decrease the resolution.
     */
    private void resChange(String s) {
        if (s.equals(UP)) {
            if (charsInRow * MIN_PIXELS_PER_CHAR <= maxCharsInRow) {
                charsInRow *= MIN_PIXELS_PER_CHAR;
                System.out.printf(WIDTH_INDICATOR, charsInRow);
            }

            else
                System.out.println(MAX_RES_MSG);
        }
        else if (s.equals(DOWN)) {
            if (minCharsInRow <= charsInRow / MIN_PIXELS_PER_CHAR) {
                charsInRow /= MIN_PIXELS_PER_CHAR;
                System.out.printf(WIDTH_INDICATOR, charsInRow);
            }
            else
                System.out.println(MIN_RES_MSG);
        }
        else {
            System.out.println(INVALID_INPUT);
        }
    }

    /**
     * Renders the input by the provided render classe: by html or to the console.
     * The default is to th html, but if the user typed "console" it can also render to the console.
     */
    private void render() {
        if (charSet.size() == 0)
            return;
        Character[] charList = new Character[charSet.size()];
        int i = 0;
        for (Character character: charSet) {
            charList[i] = character;
            i++;
        }
        output.output(charMatcher.chooseChars(charsInRow, charList));
    }
}
