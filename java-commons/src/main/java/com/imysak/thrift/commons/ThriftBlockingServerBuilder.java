package com.imysak.thrift.commons;

import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.server.TThreadPoolServer.Args;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TTransportException;


/**
 * Builder class for Thrift servers.
 *
 * @author iMysak
 *
 */
public class ThriftBlockingServerBuilder extends AbstractThriftServerBuilder {

    /**
     * Create a builder for the indicated service class.
     *
     * @param serviceClass The Thrift-generated service class
     */
    public ThriftBlockingServerBuilder(final Class<?> serviceClass, final int servicePort) {
        super(serviceClass, servicePort);
    }

    @Override
    protected TServer createServer(final int servicePort, final TProtocolFactory protocolFactory, final TProcessor processor) throws TTransportException {
        final Args args = new Args(new TServerSocket(servicePort)).processor(processor).
                protocolFactory(protocolFactory).minWorkerThreads(3).maxWorkerThreads(20);
        return new TThreadPoolServer(args);
    }


}
