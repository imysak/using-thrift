package com.imysak.thrift.commons;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.NoSuchElementException;

import org.apache.commons.pool.ObjectPool;
import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.imysak.thrift.commons.PoolableClientFactory.ClientWrapper;

/**
 * An InvocationHandler that will use a GenericObjectPool to get a Thrift client
 * connection for each call.
 *
 * @see PoolableClientFactory
 *
 * @author cdegroot
 *
 * @param <ClientType> The type of the generated Thrift client class
 */
public class PoolingClientInvocationHandler<ClientType> implements InvocationHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(PoolingClientInvocationHandler.class);

    private final ObjectPool pool;

    public PoolingClientInvocationHandler(final ObjectPool pool) {
        this.pool = pool;
    }

    /**
     * This implementation will get a service client from its pool. If that throws a
     * transport-related exception, then it will invalidate the client and try once more.
     * If the retry throws a transport-related exception, the exception will be rethrown
     * (and the second client will be invalidated as well).
     *
     * Note that this needs some real-life checking w.r.t. exceptions that occur on an
     * actual network instead of in theory :)
     */
    @SuppressWarnings("unchecked")
    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args)
            throws Throwable {

        ClientWrapper<ClientType> clientWrapper;
        try {
            clientWrapper = (ClientWrapper<ClientType>) pool.borrowObject();
        } catch (final NoSuchElementException e) {
            // NoSuchElementException, as thrown by the pool, is too opaque. Translate
            // into a Thrift exception
            throw new TTransportException(e);
        }
        try {
            return doInvoke(clientWrapper, method, args);
        } catch (final TTransportException t) {
            doInvalidateObject(clientWrapper);
            clientWrapper = (ClientWrapper) pool.borrowObject();
            try {
                final Object returnValue = doInvoke(clientWrapper, method, args);
                //We succeeded the second time, so we failed over.
                return returnValue;
            } catch (final TTransportException tt) {
                doInvalidateObject(clientWrapper);
                clientWrapper = null;
                throw tt;
            }
        } finally {
            if (clientWrapper != null) {
                pool.returnObject(clientWrapper);
            }
        }
    }

    /**
     * Invalidate the object to the pool, called in case of transport exceptions
     * which we interpret that the connection the client represents has gone invalid.
     *
     * @param clientWrapper the ClientWrapper containing the connection.
     */
    private void doInvalidateObject(final ClientWrapper<ClientType> clientWrapper) {
        try {
            pool.invalidateObject(clientWrapper);
        } catch (final Exception e) {
            // ignore
        }
    }

    /**
     * The actual invocation of the method call. Moved here to have a cleaner
     * exception wrapping/handling.
     *
     * @param clientWrapper the ClientWrapper containing the connection.
     * @param method the Method to invoke
     * @param argumentsForInvokation the arguments to the Method
     * @return the return value of the method call
     * @throws TTransportException to be interpreted as a signal that the client connection is invalid
     * @throws Throwable on all other conditions (to be thrown up as an application-level exception)
     */
    private Object doInvoke(final ClientWrapper<ClientType> clientWrapper, final Method method, final Object[] argumentsForInvokation) throws Throwable {
        final long startTime = System.currentTimeMillis();
        try {
            final Object returnValue = method.invoke(clientWrapper.client, argumentsForInvokation);
            clientWrapper.registerSuccessfulCall();
            return returnValue;
        } catch (final InvocationTargetException e) {
            //Increment failure count when exception thrown if Thrift exception, a runtime exception or an error
            if(e.getTargetException() instanceof TException ||
                    e.getTargetException() instanceof RuntimeException ||
                    e.getTargetException() instanceof Error) {

            }
            throw e.getTargetException();
        } finally {
            if(LOGGER.isTraceEnabled()) {
                LOGGER.trace("ServiceInvocation {class=" + method.getDeclaringClass().getName() + ", method=" + method.getName() + ", time=" + (System.currentTimeMillis() - startTime));
            }
        }

    }
}
