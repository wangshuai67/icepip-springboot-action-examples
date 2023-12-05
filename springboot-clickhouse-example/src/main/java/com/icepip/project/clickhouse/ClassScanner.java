package com.icepip.project.clickhouse;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

public class ClassScanner {
    private final String classPath;
    private final ApplicationContext applicationContext;

    public ClassScanner(String classPath, ApplicationContext applicationContext) {
        this.classPath = classPath;
        this.applicationContext = applicationContext;
    }

    public void scanAndLoad() throws Exception {
        // 获取并遍历目录下的所有文件
        File dir = new File(classPath);
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                // 只处理 .class 文件
                if (file.isFile() && file.getName().endsWith(".class")) {
                    // 加载类
                    String className = file.getName().substring(0, file.getName().length() - 6);
                    URL[] urls = new URL[]{dir.toURI().toURL()};
                    try (URLClassLoader loader = new URLClassLoader(urls)) {
                        Class<?> clazz = loader.loadClass(className);
                        // 将类的实例添加到 Spring 容器
                        AutowireCapableBeanFactory beanFactory = applicationContext.getAutowireCapableBeanFactory();
                        beanFactory.autowireBean(clazz.newInstance());
                    }
                }
            }
        }
    }
}
