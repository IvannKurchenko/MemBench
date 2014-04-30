package memory.benchmark.internal.runner.remote;

import com.sun.jndi.rmi.registry.RegistryContextFactory;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXServiceURL;
import javax.management.remote.rmi.RMIConnectorServer;
import java.util.HashMap;

import static javax.management.remote.JMXConnectorServerFactory.newJMXConnectorServer;

public class Server {

    protected MBeanServer server = null;
    String serviceURL = "service:jmx:rmi://localhost/jndi/rmi://localhost:1099/test";

    public void run() {
        try {

            // create MBean server
            server = MBeanServerFactory.createMBeanServer("test");

            // create JMXConnectorServer MBean
            HashMap<String, String> map = new HashMap<>();
            map.put("java.naming.factory.initial", RegistryContextFactory.class.getName());
            map.put(RMIConnectorServer.JNDI_REBIND_ATTRIBUTE, "true");
            JMXConnectorServer connector = newJMXConnectorServer(new JMXServiceURL(serviceURL), map, server);

            // register the connector server as an MBean
            server.registerMBean(connector, new ObjectName("system:name=rmiconnector"));

            // start the connector server
            connector.start();

            // create html adapter mbean

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
