package com.imysak.thrift.commons;

import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.server.TNonblockingServer;
import org.apache.thrift.server.TNonblockingServer.Args;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TTransportException;


/**
 * Builder class for Thrift servers.
 * 
 * @author iMysak
 * 
 */
public class ThriftNonblockingServerBuilder extends AbstractThriftServerBuilder {

    /**
     * Create a builder for the indicated service class.
     *
     * @param serviceClass The Thrift-generated service class
     */
    public ThriftNonblockingServerBuilder(final Class<?> serviceClass, final int servicePort) {
        super(serviceClass, servicePort);
    }

    @Override
    protected TServer createServer(final int servicePort, final TProtocolFactory protocolFactory, final TProcessor processor) throws TTransportException {
        final Args args = new Args(new TNonblockingServerSocket(servicePort)).processor(processor).
                protocolFactory(protocolFactory);
        return new TNonblockingServer(args);
    }


}
