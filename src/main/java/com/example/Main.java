package com.example;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import java.io.File;

public class Main {
    public static void main(String[] args) throws LifecycleException {

        String webappDirLocation = "src/main/webapp/";
        Tomcat tomcat = new Tomcat();

        tomcat.setPort(8080);
        tomcat.getConnector();

        Context context = tomcat.addWebapp("",
                new File(webappDirLocation).getAbsolutePath());

        File additionWebInfClasses = new File("target/classes");
        File additionWebInfLib = new File("target/lib");

        WebResourceRoot resources = new StandardRoot(context);
        resources.addPreResources(new DirResourceSet(resources,
                "/WEB-INF/classes",
                additionWebInfClasses.getAbsolutePath(), "/"));
        context.setResources(resources);

        tomcat.start();
        tomcat.getServer().await();
    }
}