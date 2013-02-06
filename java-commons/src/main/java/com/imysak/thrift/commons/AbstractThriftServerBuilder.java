package com.imysak.thrift.commons;

import java.lang.reflect.Constructor;

import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Builder class for Thrift servers.
 *
 * @author iMysak
 *
 */
public abstract class AbstractThriftServerBuilder implements ApplicationContextAware {

    private final Logger logger = LoggerFactory.getLogger(AbstractThriftServerBuilder.class);

    enum InnerClasses {
        Iface,
        Processor;

        public boolean matches(final Class<?> clazz) {
            return name().equals(clazz.getSimpleName());
        }
    }

    private TProcessor processor;
    private TProtocolFactory protocolFactory;
    private final Class<?> serviceClass;
    private final Integer servicePort;
    private Object serviceImplementation;
    private ApplicationContext context;
    private TServer server;

    @Override
    public void setApplicationContext(final ApplicationContext context) throws BeansException {
        this.context = context;
    }

    /**
     * Create a builder for the indicated service class.
     *
     * @param serviceClass The Thrift-generated service class
     */
    public AbstractThriftServerBuilder(final Class<?> serviceClass, final int servicePort) {
        this.serviceClass = serviceClass;
        this.servicePort = servicePort;
    }

    protected abstract TServer createServer(final int servicePort, TProtocolFactory protocolFactory,
            TProcessor processor) throws TTransportException;

    /**
     * Builds the service. A lot of defaults apply if the withXxx methods haven't been
     * called.

     * @return
     * @throws Exception
     */
    public TServer build() throws Exception {

        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        server = createServer(servicePort, buildProtocolFactory(), buildProcessor());
                        server.serve();

                    } catch (final Exception e) {
                        logger.error("Thrift server error", e);
                    } finally {
                        server.stop();
                    }
                }
            }
        });
        thread.start();
        return server;
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

    private TProtocolFactory buildProtocolFactory() {
        if (protocolFactory == null) {
            protocolFactory = new TBinaryProtocol.Factory();// TCompactProtocol.Factory();// true, true);
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

    private Object buildServiceImplementation(final Class<?> ifaceClass) {
        if (serviceImplementation == null) {
            serviceImplementation = context.getBean(ifaceClass);
        }
        return serviceImplementation;
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
