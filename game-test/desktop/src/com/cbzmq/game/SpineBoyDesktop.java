package com.cbzmq.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3NativesLoader;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.SharedLibraryLoader;
import org.cbzmq.game.GameServer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * @ClassName SpineBoyDesktop
 * @Description TODO
 * @Author chenbiao
 * @Date 2023/7/25 12:35 上午
 * @Version 1.0
 **/
public class SpineBoyDesktop {
    public static void main(String[] args) throws Exception {
        System.out.println("java版本号：" + System.getProperty("java.version")); // java版本号

        System.out.println("Java提供商名称：" + System.getProperty("java.vendor")); // Java提供商名称

        System.out.println("Java提供商网站：" + System.getProperty("java.vendor.url")); // Java提供商网站

        System.out.println("jre目录：" + System.getProperty("java.home")); // Java，哦，应该是jre目录

        System.out.println("Java虚拟机规范版本号：" + System.getProperty("java.vm.specification.version")); // Java虚拟机规范版本号

        System.out.println("Java虚拟机规范提供商：" + System.getProperty("java.vm.specification.vendor")); // Java虚拟机规范提供商

        System.out.println("Java虚拟机规范名称：" + System.getProperty("java.vm.specification.name")); // Java虚拟机规范名称

        System.out.println("Java虚拟机版本号：" + System.getProperty("java.vm.version")); // Java虚拟机版本号

        System.out.println("Java虚拟机提供商：" + System.getProperty("java.vm.vendor")); // Java虚拟机提供商

        System.out.println("Java虚拟机名称：" + System.getProperty("java.vm.name")); // Java虚拟机名称

        System.out.println("Java规范版本号：" + System.getProperty("java.specification.version")); // Java规范版本号

        System.out.println("Java规范提供商：" + System.getProperty("java.specification.vendor")); // Java规范提供商

        System.out.println("Java规范名称：" + System.getProperty("java.specification.name")); // Java规范名称

        System.out.println("Java类版本号：" + System.getProperty("java.class.version")); // Java类版本号

        System.out.println("Java类路径：" + System.getProperty("java.class.path")); // Java类路径

        System.out.println("Java lib路径：" + System.getProperty("java.library.path")); // Java lib路径

        System.out.println("Java输入输出临时路径：" + System.getProperty("java.io.tmpdir")); // Java输入输出临时路径

        System.out.println("Java编译器：" + System.getProperty("java.compiler")); // Java编译器

        System.out.println("Java执行路径：" + System.getProperty("java.ext.dirs")); // Java执行路径

        System.out.println("操作系统名称：" + System.getProperty("os.name")); // 操作系统名称

        System.out.println("操作系统的架构：" + System.getProperty("os.arch")); // 操作系统的架构

        System.out.println("操作系统版本号：" + System.getProperty("os.version")); // 操作系统版本号

        System.out.println("文件分隔符：" + System.getProperty("file.separator")); // 文件分隔符

        System.out.println("路径分隔符：" + System.getProperty("path.separator")); // 路径分隔符

        System.out.println("直线分隔符：" + System.getProperty("line.separator")); // 直线分隔符

        System.out.println("操作系统用户名：" + System.getProperty("user.name")); // 用户名

        System.out.println("操作系统用户的主目录：" + System.getProperty("user.home")); // 用户的主目录

        System.out.println("当前程序所在目录：" + System.getProperty("user.dir")); // 当前程序所在目录
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Super Spineboy");
        float scale = 0.2f;
        config.setWindowedMode((int) (800 * scale), (int) (450 * scale));
        try {
            new Lwjgl3Application(new GameServer(), config);

        } catch (GdxRuntimeException e) {
            e.printStackTrace();
        }
    }


}
