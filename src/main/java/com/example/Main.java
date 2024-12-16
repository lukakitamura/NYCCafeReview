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
        WebResourceRoot resources = new StandardRoot(context);

        // Add classes directory
        resources.addPreResources(new DirResourceSet(resources,
                "/WEB-INF/classes",
                additionWebInfClasses.getAbsolutePath(), "/"));

        // Add lib directory
        File libDirectory = new File("target/lib");
        if (libDirectory.exists()) {
            resources.addPreResources(new DirResourceSet(resources,
                    "/WEB-INF/lib",
                    libDirectory.getAbsolutePath(), "/"));
        }

        context.setResources(resources);

        tomcat.start();
        System.out.println("Server started at http://localhost:8080");
        tomcat.getServer().await();
    }
}