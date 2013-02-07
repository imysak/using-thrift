package com.imysak.thrift.client;


import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.imysak.thrift.IService;


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
        final Logger logger = LoggerFactory.getLogger(Main.class);
        final IService.Iface iService = (IService.Iface) context.getBean("iService");
        final String utf8Sample = iService.getUTF8Text();
        logger.info(utf8Sample);
    }

}
