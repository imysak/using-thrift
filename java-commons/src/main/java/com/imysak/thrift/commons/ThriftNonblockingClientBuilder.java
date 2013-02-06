package com.imysak.thrift.commons;

import java.lang.reflect.InvocationTargetException;

import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

/**
 * A spring thrift service client factory that can handle creating a dynamic proxy onto a thrift service that
 * uses a Non-blocked IO.
 */
public class ThriftNonblockingClientBuilder<T> extends AbstractThriftClientBuilder<T> {

    /**
     * Creates a new ServiceClientFactory that talks to the services on the specified host using the specified port
     *
     * @param clientClass The client class of the service that we wish to proxy
     * @param host The host to talk to
     * @param port The port ot talk to
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws SecurityException
     * @throws IllegalArgumentException
     * @throws TTransportException
     */
    public ThriftNonblockingClientBuilder(final Class<? extends T> clientClass, final String host, final int port)
            throws IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException,
            InvocationTargetException, NoSuchMethodException {
        super(clientClass, host, port);
    }

    @Override
    protected TTransport createTrasnport(final String host, final int port) {
        return new TFramedTransport(new TSocket(host, port));
    }


}
