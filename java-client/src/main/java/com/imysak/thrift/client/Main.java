package com.imysak.thrift.client;


import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.imysak.thrift.IService;
import org.apache.thrift.TException;


/**
 * Main class for setup application Context.
 *
 * @author Ihor Mysak (ihor.mysak@gmail.com)
 */
public final class Main {

    private Main() {
    }

    /**
     * @param args
     *            not used
     */
    public static void main(final String[] args) throws TException{
        @SuppressWarnings("unused") final ApplicationContext context = new ClassPathXmlApplicationContext("classpath:/context.xml");

        final IService.Iface iService = (IService.Iface) context.getBean("com.imysak.thrift.IService.Iface");
        
        System.out.println(iService.getUTF8Text());
    }

}
