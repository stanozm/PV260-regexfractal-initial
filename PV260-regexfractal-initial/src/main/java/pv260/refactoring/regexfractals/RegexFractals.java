package pv260.refactoring.regexfractals;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class RegexFractals {

	private static final String OUT_A = "output/fractal.txt";
	private static final String OUT_P = "output/fractal.png";

    public static void doMain(String[] args) throws Exception{
        int siz;
		int px;
		String pt;
		Color c;
		FractalGrid grid;
		if (pot(args[0])) {
			siz = Integer.valueOf(args[0]);
		} else {
			throw new IllegalArgumentException("Bad input");
		}
        try{
            px = Integer.parseInt(args[1]);
            if(px <= 0){
                throw new IllegalArgumentException("Bad input");
            }
        } catch(NumberFormatException e){
            throw new IllegalArgumentException("Bad input");
        }
		if (reg(args[2])) {
			pt = args[2];
		} else {
			throw new IllegalArgumentException("Bad input");
		}
		if (args.length != 3) {
			throw new IllegalArgumentException("Bad input");
		}
		AsciiImageConverter conv1 = new AsciiImageConverter();
		grid = new FractalGrid(siz, 1);
		Color[][] clrd = new Color[siz][siz];
		for (int i = 0; i < siz; i++) {
			for (int j = 0; j < siz; j++) {
				c = conv1.cFor(Pattern.compile(pt), grid.sigOf(i, j));
				clrd[j][i] = c;
			}
		}

        Files.createDirectories(Paths.get(OUT_A).getParent());
		try (BufferedWriter w = Files.newBufferedWriter(Paths.get(OUT_A), Charset.forName("UTF-8"))) {
			w.write(conv1.convert(clrd, clrd.length, px));
		}

        PngImageConverter conv2 = new PngImageConverter(new File(OUT_P));
        conv2.convert(clrd, clrd.length, px);
    }

	public static void main(String[] args) {
		try{
            doMain(args);
        } catch(Exception e){
            System.err.println(e);
            System.exit(1);
        }
	}

	//regex must conform to this pattern to be valid
	//that is it must contain only characters 123456 and no braces
	private static final Pattern VALID_REEGX = Pattern.compile("[\\[\\]().1234^\\\\*+|]+");

	/**
	 * return true if number is pot (that means Power Of Two, )
	 * @param number the number to check
	 * @return true if pot, else false if not pot
	 * @see http://en.wikipedia.org/wiki/Power_of_two
	 */
	public static boolean pot(String number) {
		int parsedNumber;
		int one = 1;
		int two = 2;
		try {
			parsedNumber = Integer.valueOf(number);
		} catch (NumberFormatException e) {
			System.out.println("Input " + number + " is not a number.");
			return false;
		}
		if (parsedNumber <= one) {
			System.out.println("Number " + number + " is not power of two greater than 1");
			return false;
		}
		while (parsedNumber > one) {
			if ((parsedNumber & 1) == 0) {
				parsedNumber = parsedNumber / two;
			} else {
				System.out.println("Number " + number + " is not power of two");
				return false;
			}
		}
		return true;
	}

	/**
	 * checks the rgex
	 * @param regex the regex
	 * @return true if ok regex else false if not ok regex
	 */
	public static boolean reg(String regex) {
		Matcher validRegex = VALID_REEGX.matcher(regex);
		if (!validRegex.matches()) {
			System.out.println("Regex " + regex + " contains invalid characters.");
			return false;
		}
		try {
			Pattern.compile(regex);
		} catch (PatternSyntaxException e) {
			System.out.println("Input " + regex + " is not a valid regular expression.");
			return false;
		}
		return true;
	}
}
