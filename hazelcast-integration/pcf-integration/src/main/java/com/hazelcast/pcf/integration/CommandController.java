package com.hazelcast.pcf.integration;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IExecutorService;
import com.hazelcast.core.IMap;
import com.hazelcast.core.Member;
import googlielmo.executor.Area;
import googlielmo.shape.Rectangle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RestController
public class CommandController {

    @ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="Bad value format! For instance you can use: 100")  // 404
    public static class BadValueException extends RuntimeException {
    }

    @Autowired
    HazelcastInstance hazelcastClient;

    @RequestMapping("/put")
    public CommandResponse put(@RequestParam(value = "key") String key, @RequestParam(value = "value") String value) {
        IMap<String, Rectangle> map = hazelcastClient.getMap("native-map");
        Rectangle oldValue = null;
        try {
            Float fv = Float.valueOf(value);
            oldValue = map.put(key, new Rectangle(fv, fv * 2.0f));
        } catch (Exception e) {
            throw new BadValueException();
        }
        return new CommandResponse(oldValue == null ? "null" : oldValue.toString());
    }

    @RequestMapping("/get")
    public CommandResponse get(@RequestParam(value = "key") String key) {
        IMap<String, Rectangle> map = hazelcastClient.getMap("native-map");
        Rectangle dateTime = map.get(key);
        String value = dateTime == null ? "null" : dateTime.toString();
        return new CommandResponse(value);
    }

    @RequestMapping("/remove")
    public CommandResponse remove(@RequestParam(value = "key") String key) {
        IMap<String, Rectangle> map = hazelcastClient.getMap("native-map");
        Rectangle value = map.remove(key);
        return new CommandResponse(value == null ? "null" : value.toString());
    }

    @RequestMapping("/size")
    public CommandResponse size() {
        IMap<String, Rectangle> map = hazelcastClient.getMap("native-map");
        int size = map.size();
        return new CommandResponse(Integer.toString(size));
    }

    @RequestMapping("/populate")
    public CommandResponse populate() {
        IMap<String, Rectangle> map = hazelcastClient.getMap("native-map");
        for (int i = 0; i < 1000; i++) {
            Rectangle dt = new Rectangle(i + 0.0f, i * 2.0f);
            String s = Integer.toString(i);
            map.put(s, dt);
        }
        return new CommandResponse("1000 entry inserted to the map");
    }

    @RequestMapping("/clear")
    public CommandResponse clear() {
        IMap<String, Rectangle> map = hazelcastClient.getMap("native-map");
        map.clear();
        return new CommandResponse("Map cleared");
    }

    @RequestMapping("/execute")
    public CommandResponse execute() throws InterruptedException, ExecutionException {
        IExecutorService executor = hazelcastClient.getExecutorService("default");

        Map<Member, Future<Double>> result = executor.submitToAllMembers(new Area());
        double sum = 0;
        for (Future<Double> future : result.values()) {
            sum += future.get();
        }

        return new CommandResponse(String.format("%,.2f", sum));
    }
}
