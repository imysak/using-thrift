package com.imysak.thrift.server.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.thrift.TException;
import org.springframework.stereotype.Controller;

import com.imysak.thrift.INameMissed;
import com.imysak.thrift.IService;
import com.imysak.thrift.entities.IRequest;
import com.imysak.thrift.entities.IResponse;
import com.imysak.thrift.entities.IStat;


@Controller
public class ThriftIServiceImpl implements IService.Iface {

    private final Map<String, AtomicInteger> results = new ConcurrentHashMap<String, AtomicInteger>();

    public void ping() throws TException {
        // "Pong"; do nothing;
    }

    public IResponse query(final IRequest request) throws TException {
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
        final AtomicInteger record = results.get(name);
        if (record == null) {
            throw new INameMissed(String.format("name %s not found", name));
        }
        return new IStat(name, record.get());
    }

    public List<IStat> getStats() throws TException {
        final Set<String> keys = results.keySet();
        final List<IStat> response = new ArrayList<IStat>(keys.size());
        for (final String key : keys) {
            response.add(new IStat(key, results.get(key).get()));
        }
        return response;
    }


}
