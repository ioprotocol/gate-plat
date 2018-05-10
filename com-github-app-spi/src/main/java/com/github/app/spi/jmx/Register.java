package com.github.app.spi.jmx;

import javax.management.*;
import java.lang.management.ManagementFactory;

public class Register {

    public static void registe() {
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        String domainName = "XsyMBean";

        ObjectName helloName = null;
        try {
            helloName = new ObjectName(domainName+":name=HelloWorld");
        } catch (MalformedObjectNameException e) {
            e.printStackTrace();
        }

        try {
            server.registerMBean(new Hellow(),helloName);
        } catch (InstanceAlreadyExistsException e) {
            e.printStackTrace();
        } catch (MBeanRegistrationException e) {
            e.printStackTrace();
        } catch (NotCompliantMBeanException e) {
            e.printStackTrace();
        }
    }
}
