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

        System.out.println("кирилиця з буквами і Ї ґ");
        System.out
                .println("Μια λίστα με τους καλύτερους κιθαρίστες της ροκ με βάση τις προσωπικές μου μουσικές προτιμήσεις.Η εφαρμογή φτιάχτηκε για προσωπική διασκέδαση και για να δοκιμαστούν κάποιες τεχνολογίες (jQueryMobile, phoneGap).Αν κάποιος ακούει ροκ και θέλει, ας τη δει και ας σχολιάσει αν του άρεσε το user interface (και για το αν συμφωνεί με τη λίστα ή έχω ξεχάσει κάποιον).Σημείωση: Η εφαρμογή δεν περιέχει μουσικά κομμάτια, μόνο συνδέσμους σε βίντεο στο youtube.");
        logger.info(iService.getUTF8Text());
    }

}
