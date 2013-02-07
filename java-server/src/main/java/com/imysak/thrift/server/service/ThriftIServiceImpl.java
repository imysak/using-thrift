package com.imysak.thrift.server.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import com.imysak.thrift.INameMissed;
import com.imysak.thrift.IService;
import com.imysak.thrift.entities.IRequest;
import com.imysak.thrift.entities.IResponse;
import com.imysak.thrift.entities.IStat;


@Controller
public class ThriftIServiceImpl implements IService.Iface {

    private final Logger logger = LoggerFactory.getLogger(ThriftIServiceImpl.class);

    private final Map<String, AtomicInteger> results = new ConcurrentHashMap<String, AtomicInteger>();

    public void ping() throws TException {
        // "Pong"; do nothing;
    }

    public IResponse query(final IRequest request) throws TException {
        logger.info("Method: query({})", request);
        final AtomicInteger record = results.get(request.getMyName());
        if (record == null) {
            results.put(request.getMyName(), new AtomicInteger(1));
        } else {
            record.incrementAndGet();
        }
        final String text = request.getText();
        final IResponse response = new IResponse(true);
        response.setText(text);
        return response;
    }

    public IStat getStat(final String name) throws INameMissed, TException {
        logger.info("Method getStat({})", name);
        final AtomicInteger record = results.get(name);
        if (record == null) {
            throw new INameMissed(String.format("name %s not found", name));
        }
        return new IStat(name, record.get());
    }

    public List<IStat> getStats() throws TException {
        logger.info("Method: getStats()");
        final Set<String> keys = results.keySet();
        final List<IStat> response = new ArrayList<IStat>(keys.size());
        for (final String key : keys) {
            response.add(new IStat(key, results.get(key).get()));
        }
        return response;
    }

    public String getUTF8Text() {
        logger.info("Method: getUTF8Text()");
        return "Μια λίστα με τους καλύτερους κιθαρίστες της ροκ με βάση τις προσωπικές μου μουσικές προτιμήσεις.Η εφαρμογή φτιάχτηκε για προσωπική διασκέδαση και για να δοκιμαστούν κάποιες τεχνολογίες (jQueryMobile, phoneGap).Αν κάποιος ακούει ροκ και θέλει, ας τη δει και ας σχολιάσει αν του άρεσε το user interface (και για το αν συμφωνεί με τη λίστα ή έχω ξεχάσει κάποιον).Σημείωση: Η εφαρμογή δεν περιέχει μουσικά κομμάτια, μόνο συνδέσμους σε βίντεο στο youtube.";
    }

}
