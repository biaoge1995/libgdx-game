package awt.utils;

import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * @ClassName YamlUtil
 * @Description TODO
 * @Author chenbiao
 * @Date 2023/7/3 3:04 下午
 * @Version 1.0
 **/
public class YamlUtil {


    static {
        //获取classpath下面的User.yaml文件
        InputStream resource = YamlUtil.class.getClassLoader().getResourceAsStream("config.yml");
        Yaml yaml = new Yaml();
        Object load = yaml.load(resource);

        System.out.println(load);
    }

    public static void main(String[] args) {

    }
}
