package com.shdq.OPCUA_YML_gen_platform.util;

import com.esotericsoftware.yamlbeans.YamlConfig;
import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.esotericsoftware.yamlbeans.YamlWriter;
import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author fujun
 */
@Slf4j
public class YamlConverter {

    private YamlConverter() {
    }

    private static class SingleHolder{
        private static YamlConverter instance = new YamlConverter();
    }

    public static YamlConverter getInstance(){
        return SingleHolder.instance;
    }

    public <T> T readFromYAMLFile(String filePath,Class<T> clazz){
        T t = null;
        try {
            YamlReader reader = new YamlReader(new FileReader(filePath));
            t = reader.read(clazz);
            reader.close();
        }catch (FileNotFoundException | YamlException e){
            log.error(e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        return t;
    }

    public void writeToYAMLFile(String filePath,Object object){
        try {
            YamlConfig config = new YamlConfig();
            config.writeConfig.setWriteRootTags(false);
            YamlWriter writer = new YamlWriter(new FileWriter(filePath),config);
            writer.write(object);
            writer.close();
        } catch (IOException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }
}
