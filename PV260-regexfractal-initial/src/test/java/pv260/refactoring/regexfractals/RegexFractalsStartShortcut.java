package pv260.refactoring.regexfractals;

import org.junit.Test;

/**
 * This is not a real test, it merely eases running
 * the app with parameters from Netbeans
 */
public class RegexFractalsStartShortcut {

    //some nice inputs
    //.*1.*
    //.*(12|23|34|41).*
    //.*(13|31|24|42).*
    @Test
    public void runApp() throws Exception {
        RegexFractals.doMain(new String[]{"2", "10", "1"});
    }
}
