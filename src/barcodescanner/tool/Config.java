package barcodescanner.tool;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Author: MaoMorn
 * Date: 2017/9/28
 * Time: 16:22
 * Description: 配置类
 * 读取配置文件，方便生产者和消费者的使用
 */
public class Config {
    private static class ConfigHolder{
        private static final Config INSTANCE=new Config();
    }
    private String saveService;
    private String scanListener;

    private Config(){
        InputStream inputStream=Config.class.getResourceAsStream("/application.properties");
        Properties properties=new Properties();
        try {
            properties.load(inputStream);
            saveService=properties.getProperty("saveService");
            scanListener=properties.getProperty("keyboardListener");
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getSaveService() {
        return saveService;
    }

    public String getScanListener() {
        return scanListener;
    }

    public static final Config getInstance(){
        return ConfigHolder.INSTANCE;
    }
}
