package com.imysak.thrift.commons.utils;

import org.apache.thrift.server.TServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public final class ServerBootstrap {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerBootstrap.class);

    private static final String DEFAULT_CONFIG_LOCATION = "applicationContext.xml";

    private final ApplicationContext context;

    public ServerBootstrap() {
        this.context = createApplicationContext(DEFAULT_CONFIG_LOCATION);
    }

    /**
     * Constructor bootstrapping a server created via an application context created using the
     * provided configuration file locations.
     * @param configLocations The configuration file context.
     */
    public ServerBootstrap(final String... configLocations) {
        this.context = createApplicationContext(configLocations);
    }

    /**
     * Constructor bootstrapping a server created via the provided application context.
     * @param context The application context.
     */
    public ServerBootstrap(final ApplicationContext context) {
        this.context = context;
    }

    /**
     * Creates and starts a server on a configured port.
     * @param serviceClass The class of service to be started.
     * @return The server.
     * @throws Exception When a failure occurs.
     */
    public TServer createServerAndStartInThread(final Class<?> serviceClass) throws Exception {
        try {
            return startServerInThread(createServerBuilder(serviceClass).build());
        } catch (final Throwable throwable) {
            LOGGER.error(String.format("Failed to start service %s", serviceClass), throwable);
            throw new RuntimeException(throwable);
        }
    }

    /**
     * Creates and starts a server on a specified port.
     * @param serviceClass The class of service to be started.
     * @param port The service port.
     * @return The server.
     * @throws Exception When a failure occurs.
     */
    public TServer createServerAndStartInThread(final Class<?> serviceClass, final int port) throws Exception {
        try {
            return startServerInThread(createServerBuilder(serviceClass).withPort(port).build());
        } catch (final Throwable throwable) {
            LOGGER.error(String.format("Failed to start service %s on port %d.", serviceClass, port), throwable);
            throw new RuntimeException(throwable);
        }
    }

    private ApplicationContext createApplicationContext(final String... configLocations) {
        try {
            return new ClassPathXmlApplicationContext(configLocations);
        } catch (final Throwable throwable) {
            LOGGER.error("Failed to initialise application context", throwable);
            throw new RuntimeException(throwable);
        }
    }

    private ServerBuilder createServerBuilder(final Class<?> serviceClass) {
        return new ServerBuilder(serviceClass).withApplicationContext(context);
    }

    private TServer startServerInThread(final TServer server) {
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                server.serve();
            }
        });
        thread.start();
        return server;
    }

}
