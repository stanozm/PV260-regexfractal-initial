/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pv260.refactoring.regexfractals;

import java.io.IOException;
import static java.lang.String.format;
import java.nio.file.*;
import static java.nio.file.FileVisitResult.CONTINUE;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;

public class PreservedBehaviorTest {

    private static final Path OUTPUT_FOLDER = Paths.get("output");
    private static final Path TEST_RESOURCES_FOLDER = Paths.get("src/test/resources/difftest");

    private static final String INVALID_INPUTS = "invalid.txt";
    private static final String ASCII_FILE = "fractal.txt";
    private static final String PNG_FILE = "fractal.png";
    private static final String TESTCASE_FILE = "testcase.txt";

    @Before
    public void setup() throws IOException {
        if(!Files.exists(OUTPUT_FOLDER)){
            return;
        }
        Files.walkFileTree(OUTPUT_FOLDER, new SimpleFileVisitor<Path>(){

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return CONTINUE;
            }
        });
    }

    @Test
    public void testValidInputs() throws Exception {
        Files.deleteIfExists(OUTPUT_FOLDER);
        Files.list(TEST_RESOURCES_FOLDER).filter(Files::isDirectory)
                .forEach((Path testcaseDirectory) -> {
                    try {
                        this.validInputsTest(testcaseDirectory);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    private void validInputsTest(Path testcaseDirectory) throws Exception {
        List<String> testInputs = Files.readAllLines(testcaseDirectory.resolve(TESTCASE_FILE));
        RegexFractals.doMain(testInputs.toArray(new String[testInputs.size()]));
        assertArrayEquals("ascii",
                     Files.readAllBytes(testcaseDirectory.resolve(ASCII_FILE)),
                     Files.readAllBytes(OUTPUT_FOLDER.resolve(ASCII_FILE)));
        assertArrayEquals("png",
                     Files.readAllBytes(testcaseDirectory.resolve(PNG_FILE)),
                     Files.readAllBytes(OUTPUT_FOLDER.resolve(PNG_FILE)));
    }

    @Test
    public void testInvalidInputs() throws Exception {
        Files.readAllLines(TEST_RESOURCES_FOLDER.resolve(INVALID_INPUTS))
                .forEach(this::invalidInputsTest);
    }

    private void invalidInputsTest(String testcase) {
        try {
            String strippedComment = testcase.split("//")[0];
            String[] input = strippedComment.split(";");
            RegexFractals.doMain(input);
            fail(format("Testcase <%s> should result in error", testcase));
        } catch (Exception e) {
            //expected
        }
    }
}
