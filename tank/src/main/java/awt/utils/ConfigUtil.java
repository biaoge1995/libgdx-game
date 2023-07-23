package awt.utils;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

/**
 * @author chenbiao
 * @date 2020-11-14 10:03
 */
public class ConfigUtil {
    private static Config config;
    private static String env;
    private static String configPath = "conf";


    public static InputStream getConfigInputStream(String resourceFileName) throws FileNotFoundException {
        String configPath = getConfigPath(resourceFileName);
        FileInputStream fileInputStream = new FileInputStream(configPath);
        return fileInputStream;
    }

    private static String getConfigPath(String resourceFileName) {
        String userDir = System.getProperty("user.dir");
        String filePath = userDir + "/" + configPath + "/" + resourceFileName;
        return filePath;
    }

    private static synchronized Config getConfig() {
        if (config == null) {
            String configPath = getConfigPath("app.conf");
            config = ConfigFactory.parseFile(new File(configPath));
            System.out.println("加载配置文件" + configPath);

            if (System.getProperties().getProperty("os.name").toLowerCase().startsWith("win")) {
                env = "dev";
                System.out.println("测试环境");
            } else {
                env = "prod";
                System.out.println("生产环境");
            }
        }
        return config;
    }

    /**
     * 获取配置
     *
     * @param item
     * @return
     */
    public static String getItem(String item) {
        return getConfig().getString(env + "." + item);
    }

    /**
     *
     * @param item
     * @return
     */
    public static List<String> getStringList(String item) {
        List<String> stringList = getConfig().getStringList(env + "." + item);
        return stringList;
    }

    public static void main(String[] args) {
        System.out.println(getItem("mysql.url"));
    }
}
