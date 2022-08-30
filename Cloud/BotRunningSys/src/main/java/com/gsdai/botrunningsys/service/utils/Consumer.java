package com.gsdai.botrunningsys.service.utils;

import org.joor.Reflect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.UUID;
import java.util.function.Supplier;

@Component
public class Consumer extends Thread {

    private Bot bot;
    private static RestTemplate restTemplate;
    private final static String receiveBotMoveUrl = "http://127.0.0.1:8081/battle/botmove";

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        Consumer.restTemplate = restTemplate;
    }

    public void startTimeout(long timeout, Bot bot) {
        this.bot = bot;
        this.start();
        try {
            this.join(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            this.interrupt();
        }
    }

    private String addUID(String code, String uid) {
        int index = code.indexOf(" implements java.util.function.Supplier<Integer>");
        return code.substring(0, index) + uid + code.substring(index);
    }

    @Override
    public void run() {
        UUID uuid = UUID.randomUUID();
        String uid = uuid.toString().substring(0, 8);

        Supplier<Integer> botInterface = Reflect.compile(
                "com.Bot" + uid,
                addUID(bot.getBotCode(), uid)
        ).create().get();

        File file = new File("input.txt");
        try (PrintWriter printWriter = new PrintWriter(file)) {
            printWriter.println(bot.getInput());
            printWriter.flush();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        Integer direction = botInterface.get();
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("userId", bot.getUserId().toString());
        data.add("direction", direction.toString());
        restTemplate.postForObject(receiveBotMoveUrl, data, String.class);
    }
}
