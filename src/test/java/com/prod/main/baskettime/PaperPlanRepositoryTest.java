package com.prod.main.baskettime;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.prod.main.baskettime.dto.PaperPlanWithNickName;
import com.prod.main.baskettime.repository.PaperPlanRepository;

@SpringBootTest
public class PaperPlanRepositoryTest {

    @Autowired
    private PaperPlanRepository paperPlanRepository;

    @Test
    public void testFindReceivedMessagesWithNickName() {
        List<PaperPlanWithNickName> messages = paperPlanRepository.findReceivedMessagesWithNickName(1);
        messages.forEach(msg -> {
            System.out.println("ID: " + msg.getId());
            System.out.println("Content: " + msg.getContent());
            System.out.println("NickName: " + msg.getNickName());
        });
    }
}
