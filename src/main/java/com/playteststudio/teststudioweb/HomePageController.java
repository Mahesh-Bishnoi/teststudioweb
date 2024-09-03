package com.playteststudio.teststudioweb;

import java.util.Collections;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.impl.driver.Driver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

import static java.util.Arrays.asList;

import java.io.IOException;

@Controller
public class HomePageController {
    @GetMapping
    public String home(){
        return "homePage.html";
    }
    
    @PostMapping
    @ResponseBody
    public String startRecorder(@RequestBody Map<String,String> body) {
        String fileName = StringUtils.capitalize(body.get("fileName"));
        Driver driver = Driver.ensureDriverInstalled(Collections.emptyMap(), false);
        ProcessBuilder pb = driver.createProcessBuilder();
        String filePath = "src\\test\\java\\com\\playteststudio\\teststudioweb\\"+fileName+".java";
        String[] args = {"codegen","--target=java-junit","--output",filePath};
        pb.command().addAll(asList(args));
        String version = Playwright.class.getPackage().getImplementationVersion();
        if (version != null) {
            pb.environment().put("PW_CLI_DISPLAY_VERSION", version);
        }
        pb.inheritIO();
        try{
            Process process = pb.start();
            process.waitFor();
        }
        catch(IOException | InterruptedException e){
            System.out.println(e.getMessage());
        }
        try {
            // Read the file content
            StringBuilder content = new StringBuilder();
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = reader.readLine()) != null) {
                if(line.contains("TestExample")){
                    line = line.replace("TestExample", fileName);
                }
                content.append(line).append(System.lineSeparator());
            }
            reader.close();

            // Insert the new line at the beginning
            content.insert(0, "package com.playteststudio.teststudioweb;" + System.lineSeparator());

            // Write the modified content back to the file
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            writer.write(content.toString());
            writer.close();

            System.out.println("New line inserted at the beginning of the file.");

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
        return "OK";
    }
    
}
