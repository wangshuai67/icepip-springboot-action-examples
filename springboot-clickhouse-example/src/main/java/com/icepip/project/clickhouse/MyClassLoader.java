package com.icepip.project.clickhouse;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

public class MyClassLoader extends ClassLoader{

    // 热加载文件的存放路径
    private String classpath;

    MyClassLoader(String classpath){
        super(ClassLoader.getSystemClassLoader());
        this.classpath = classpath;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] classData = loadClassData(name);
        if (classData == null) {
            return null;
        }
        // defineClass：接收以字节数组表示的类字节码，并把它转换成Class实例
        return defineClass(null, classData, 0, classData.length);
    }

    private byte[] loadClassData(String name) {
        try {
            name = name.replaceAll("\\.", "//");
            String path = classpath + name + ".class";
            File file = new File(path);
            if(!file.exists()) {
                return null;
            }
            // 将class文件转化成字节数组
            FileInputStream in = new FileInputStream(file);
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            int b = 0;
            while ((b = in.read()) != -1) {
                bao.write(b);
            }
            in.close();
            return bao.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}