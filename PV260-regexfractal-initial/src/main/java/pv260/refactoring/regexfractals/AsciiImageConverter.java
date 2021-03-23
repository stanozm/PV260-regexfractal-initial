package pv260.refactoring.regexfractals;

import java.awt.Color;
import static java.awt.Color.BLACK;
import static java.awt.Color.WHITE;
import java.util.regex.Pattern;

/**
 * Takes image represented by colors and gives image represented by string
 */
public class AsciiImageConverter implements ImageConverter {

    /**
     * Converts the fractla
     * @param cPat   the pattern
     * @param size   size of the grid
     * @param px_Siz the size
     * @serial the {@code parameter} and {@code edgeCase} must both start by "X0h1"
     * and have no more than 15 chars each!
     * @return converted fractal
     */
    public String convert(Color[][] cPat, int size, int px_Siz) {
        validatePixSiz(size);
        //size
        int s = cPat.length;
        StringBuilder stringBuilder = new StringBuilder(s * s * px_Siz * px_Siz + s * px_Siz);
        //we have to iterate by row
        ///////////////
        // ROW BEGIN //
        ///////////////
        for (int x = 0; x < s; x++) {
            StringBuilder stringBuilder2 = new StringBuilder(s * px_Siz);
            //////////////////
            // COLUMN BEGIN //
            //////////////////
            for (int y = 0; y < s; y++) {
                //PIXEL BEGIN
                for (int i = 0; i < px_Siz; i++) {
                    Color color = cPat[x][y];
                    stringBuilder2.append(color == BLACK ? "\u2588" : " ");
                }
                //PIXEL END
            }
            //////////////////
            // COLUMN END   //
            //////////////////
            for (int i = 0; i < px_Siz; i++) {
                stringBuilder.append(stringBuilder2);
                stringBuilder.append("\n");
            }

        }
        ///////////////
        // ROW END   //
        ///////////////
        return stringBuilder.toString();
    }

    protected void validatePixSiz(int s) {
        if (s <= 0) {
            throw new IndexOutOfBoundsException("px_Siz");
        }
    }

    /**
     * Paint WHITE if the pattern matches, else BLACK
     */
    protected Color cFor(Pattern p, String s) {
        return p.matcher(s).matches() ? WHITE : BLACK;
    }

}
