import com.shdq.OPCUA_YML_gen_platform.model.OpcUaProperties;
import com.shdq.OPCUA_YML_gen_platform.util.CommonUtil;
import com.shdq.OPCUA_YML_gen_platform.util.YamlConverter;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Test {

    public static void main(String[] args) {
        String filePath = CommonUtil.default_file_path+ File.separator+CommonUtil.default_file_name+CommonUtil.default_file_suffix;
        File file = new File(filePath);
        System.out.println(file.exists());
        YamlConverter converter = YamlConverter.getInstance();
        OpcUaProperties properties = converter.readFromYAMLFile(filePath,OpcUaProperties.class);
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("YYYY-MM-dd-HH-mm-ss"));
        String fileName = CommonUtil.default_file_name+"-"+dateStr+CommonUtil.default_file_suffix;
        converter.writeToYAMLFile(CommonUtil.history_record_file_path+ File.separator+fileName,properties);
    }
}
