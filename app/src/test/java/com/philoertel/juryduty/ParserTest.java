package com.philoertel.juryduty;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static com.google.common.truth.Truth.assertThat;

@RunWith(JUnit4.class)
public class ParserTest {

    @Test
    public void file20180828() throws Exception {
        Instructions instructions = Parser.parseInstructions(parseFromFile("20180828.html"));
        assertThat(instructions.getDateString()).isEqualTo("20180828");
        assertThat(instructions.getReportingGroups()).containsExactly(
                "605", "619", "601", "613", "617", "616", "620", "625", "614", "627",
                "629");
    }

    @Test
    public void file20181016() throws Exception {
        Instructions instructions = Parser.parseInstructions(parseFromFile("20181016.html"));
        assertThat(instructions.getDateString()).isEqualTo("20181016");
        assertThat(instructions.getReportingGroups()).containsExactly(
                "115", "118", "105", "114", "601", "611", "604", "613", "618", "607",
                "617", "623");
    }

    private String parseFromFile(String fileName) throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(fileName);
        File file = new File(resource.getPath());
        return FileUtils.readFileToString(file, StandardCharsets.UTF_8);
    }
}
