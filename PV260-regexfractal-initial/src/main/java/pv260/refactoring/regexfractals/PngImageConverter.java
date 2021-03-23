package pv260.refactoring.regexfractals;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class PngImageConverter extends AsciiImageConverter {

	private File outputFile;

	public PngImageConverter(File outputFile) {
		this.outputFile = outputFile;
	}

	@Override
	public String convert(Color[][] colorPattern, int size, int pixelSize) {
		validatePixSiz(pixelSize);
		int width = colorPattern[0].length * pixelSize;
		int height = colorPattern.length * pixelSize;
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = image.createGraphics();
		for (int x = 0; x < colorPattern[0].length; x++) {
			for (int y = 0; y < colorPattern.length; y++) {
				g2d.setColor(colorPattern[y][x]);
				g2d.fillRect(x * pixelSize, y * pixelSize, pixelSize, pixelSize);
			}
		}
		output(image);
		return null;
	}

	private void output(BufferedImage image) {
		try {
			ImageIO.write(image, "png", outputFile);
		} catch (IOException ex) {
			throw new IllegalStateException("Cant write image to " + outputFile, ex);
		}

	}

}
