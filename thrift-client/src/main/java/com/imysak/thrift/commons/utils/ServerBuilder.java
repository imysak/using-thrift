package com.imysak.thrift.commons.utils;

import java.lang.reflect.Constructor;

import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.server.TThreadPoolServer.Args;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Builder class for Thrift servers.
 *
 * @author cdegroot
 *
 */
public class ServerBuilder {

    private static final String DEFAULT_CONTEXT_FILE = "applicationContext.xml";

    enum InnerClasses {
        Iface,
        Processor;

        public boolean matches(final Class<?> clazz) {
            return name().equals(clazz.getSimpleName());
        }
    }

    private TProcessor processor;
    private TServerTransport serverTransport;
    private TProtocolFactory protocolFactory;
    private final Class<?> serviceClass;
    private Integer servicePort;
    private Object serviceImplementation;
    private ApplicationContext configuredContext;

    /**
     * Create a builder for the indicated service class.
     *
     * @param serviceClass The Thrift-generated service class
     */
    public ServerBuilder(final Class<?> serviceClass) {
        this.serviceClass = serviceClass;
    }

    /**
     * Builds the service. A lot of defaults apply if the withXxx methods haven't been
     * called.

     * @return
     * @throws Exception
     */
    public TServer build() throws Exception {
        final Args args = new Args(buildServerTransport()).processor(buildProcessor()).
                protocolFactory(buildProtocolFactory()).minWorkerThreads(3).maxWorkerThreads(20);
        return new TThreadPoolServer(args);
    }

    /**
     * Set a custom service implementation (the default comes from Spring)
     *
     * @param serviceImplementation
     * @return
     */
    public ServerBuilder withImplementation(final Object serviceImplementation) {
        this.serviceImplementation = serviceImplementation;
        return this;
    }

    /**
     * @param context The applicationContext to get the service implementation from, will load classpath:/applicationContext otherwise.
     * @return
     */
    public ServerBuilder withApplicationContext(final ApplicationContext context) {
        this.configuredContext = context;
        return this;
    }

    /**
     * Set a custom service port (the default comes from the port registry)
     *
     * @param port
     * @return
     */
    public ServerBuilder withPort(final int port) {
        this.servicePort = port;
        return this;
    }

    /**
     * get a custom service port (the default comes from the port registry)
     *
     * @param port
     * @return
     */
    public Integer getServicePort() {
        return servicePort;
    }

    /**
     * Set a custom service port (the default comes from the port registry)
     *
     * @param port
     * @return
     */
    public void setServicePort(final int servicePort) {
        this.servicePort = servicePort;
    }

    private TProtocolFactory buildProtocolFactory() {
        if (protocolFactory == null) {
            protocolFactory = new TCompactProtocol.Factory();// true, true);
        }
        return protocolFactory;
    }

    private TProcessor buildProcessor() throws Exception {
        if (processor == null) {
            final Class<?> ifaceClass = extractInnerClass(serviceClass, InnerClasses.Iface);
            @SuppressWarnings("unchecked")
            final
            Class<TProcessor> processorClass = (Class<TProcessor>) extractInnerClass(serviceClass, InnerClasses.Processor);

            final Constructor<?> processorConstructor = processorClass.getConstructor(ifaceClass);
            processor = (TProcessor) processorConstructor.newInstance(buildServiceImplementation(ifaceClass));
        }
        return processor;
    }

    private TServerTransport buildServerTransport() throws TTransportException {
        if (serverTransport == null) {
            serverTransport = new TServerSocket(buildServicePort());
        }
        return serverTransport;
    }

    private Integer buildServicePort() {
        return servicePort;
    }

    private Object buildServiceImplementation(final Class<?> ifaceClass) {
        if (serviceImplementation == null) {
            final ApplicationContext applicationContext = buildApplicationContext();
            serviceImplementation = applicationContext.getBean(ifaceClass);
        }
        return serviceImplementation;
    }

    private ApplicationContext buildApplicationContext() {
        return configuredContext == null ? new ClassPathXmlApplicationContext(DEFAULT_CONTEXT_FILE) : configuredContext;
    }

    private static Class<?> extractInnerClass(final Class<?> serviceClass, final InnerClasses kind) {
        final Class<?>[] classes = serviceClass.getClasses();

        for (final Class<?> aClass : classes) {
            if (kind.matches(aClass)) {
                return aClass;
            }
        }

        throw new IllegalArgumentException("Unable to find inner class: " + kind.name() + " inside service class: " + serviceClass.getName());
    }
}
