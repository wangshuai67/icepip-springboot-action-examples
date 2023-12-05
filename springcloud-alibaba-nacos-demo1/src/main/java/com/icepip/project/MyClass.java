package com.icepip.project;

public class MyClass {
    public synchronized void synchronizedMethod() {
        System.out.println("sync");
    }
    
    public static synchronized void synchronizedStaticMethod() {
        System.out.println("sync");
    }
    
    public void synchronizedBlock() {
        synchronized (this) {
            System.out.println("sync");
        }
    }
    
    private int myPrivateMethod() {
        return 42;
    }
}