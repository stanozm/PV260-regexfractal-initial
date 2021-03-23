package pv260.refactoring.regexfractals;

import java.awt.Color;

public interface ImageConverter {

	/**
	 * Convert the image
	 * @param colorPattern to convert
	 * @param size         side of the image
	 * @param pixelSize    size of every pixel, must be at least 1
	 * @return converted representation of the pattern
	 */
	String convert(Color[][] colorPattern, int size, int pixelSize);

}
