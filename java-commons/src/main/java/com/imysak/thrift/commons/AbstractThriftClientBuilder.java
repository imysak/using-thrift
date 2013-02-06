package com.imysak.thrift.commons;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;

import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedAttribute;


/**
 * A spring thrift service client factory that can handle creating a dynamic proxy onto a thrift service that
 * uses a Non-blocked IO.
 */
public abstract class AbstractThriftClientBuilder<T> implements FactoryBean<T>, InitializingBean, BeanNameAware {

    private final Logger logger = LoggerFactory.getLogger(AbstractThriftClientBuilder.class);

    private final Class<? extends T> clientClass;
    private String beanName;
    private final String host;
    private final int port;
    private Class<T> serviceInterface;

    private final T proxy;
    private final T wrapper;

    // private InvocationHandler proxyHandler;
    private final GenericObjectPool clientPool;
    private PoolingClientInvocationHandler<T> proxyHandler;


    protected abstract TTransport createTrasnport(final String host, final int port);

    /**
     * Creates a new ServiceClientFactory that talks to the services on the specified host using the specified port
     *
     * @param clientClass The client class of the service that we wish to proxy
     * @param host The host to talk to
     * @param port The port to talk to
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws SecurityException
     * @throws IllegalArgumentException
     * @throws TTransportException
     */
    public AbstractThriftClientBuilder(final Class<? extends T> clientClass, final String host, final int port)
            throws IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException,
            InvocationTargetException, NoSuchMethodException {
        this.clientClass = clientClass;
        this.host = host;
        this.port = port;

        final PoolableClientFactory<T> poolableClientFactory = new PoolableClientFactory<T>(clientClass, this.host,
                this.port);
        clientPool = new GenericObjectPool(poolableClientFactory);
        // We rely on validateObject in our factory being called!
        clientPool.setTestOnBorrow(true);

        final TTransport transport = createTrasnport(this.host, this.port);
        final TProtocol tProtocol = new TBinaryProtocol(transport);// TCompactProtocol(transport);

        wrapper = clientClass.getConstructor(TProtocol.class).newInstance(tProtocol);
        proxy = createProxy(clientClass);

        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (!transport.isOpen()) {
                        try {
                            transport.open();
                            logger.info("Success connection to the service {}", clientClass.getName());
                        } catch (final TTransportException e) {
                            logger.warn("Can't connect for server {}", clientClass.getName());
                        }
                    }
                    try {
                        Thread.sleep(5000);
                    } catch (final InterruptedException e) {
                        logger.debug("InterruptedException durring sleep");
                    }
                }
            }
        });
        thread.start();

    }

    /**
     * Creates a new ServiceClientFactory
     *
     * @param clientClass The client class of the service that we wish to proxy
     * @return a proxy to the service
     */
    @SuppressWarnings({ "unchecked" })
    private T createProxy(final Class<? extends T> clientClass) {

        // Here we loop through the interfaces implemented by the client class and look for one in the com.airupt
        // package, which we then assume to be the service interface. The client class also implements a thrift
        // interface
        // so we need to do a little search. This is potentially fragile, and maybe we can think of something better
        // but it should work.
        final Class<?>[] interfaces = clientClass.getInterfaces();
        for (final Class<?> anInterface : interfaces) {
            // if (anInterface.getPackage().getName().startsWith("com.imysak")) {
                serviceInterface = (Class<T>) anInterface;
                // proxyHandler = new InvocationHandler() {
                // @Override
                // public Object invoke(final Object proxy, final Method method, final Object[] args) throws
                // Throwable {
                // final Object returnValue = method.invoke(wrapper, args);
                // return returnValue;
                // }
                // };
                proxyHandler = new PoolingClientInvocationHandler<T>(clientPool);

                return (T) Proxy.newProxyInstance(
                        anInterface.getClassLoader(),
                        new Class[] { serviceInterface },
                        proxyHandler);
            // }
        }
        throw new IllegalArgumentException("Could not find service interface, cannot continue");
    }

    /**
     * Obtains a new proxy onto the service
     *
     * @return The proxy object
     * @throws Exception if there was a problem in creating the proxy
     */
    @Override
    public T getObject() throws Exception {
        return proxy;
    }

    /**
     * Tells spring that this factory bean will return an object that implements the service interface
     *
     * @return The service interface class.
     */
    @ManagedAttribute
    @Override
    public Class<?> getObjectType() {
        return serviceInterface;
    }

    /**
     * This factory always returns a singleton
     *
     * @return true
     */
    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void setBeanName(final String name) {
        this.beanName = name;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // do nothing
    }

    /**
     * Allows you to autowire a generic config for all your thrift clients if you want
     * You can still set the config so something specific instead, or leave the defaults.
     * @param config
     */
    @Autowired(required = false)
    public void setConfig(final GenericObjectPool.Config config) {
        this.clientPool.setConfig(config);
    }

}
