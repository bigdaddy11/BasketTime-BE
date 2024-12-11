package com.prod.main.baskettime.batch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prod.main.baskettime.entity.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/kbl/players/batch")
public class KBLPlayerController {
    @GetMapping
    public List<Player> getPlayers() {
        List<Player> players = new ArrayList<>();
        try {
            // KBL 웹사이트 URL
            String url = "https://www.kbl.or.kr/stats/team_players";
            Document doc = Jsoup.connect(url).get(); 

            // 선수 정보가 포함된 태그 선택
            Elements playerElements = doc.select(".player-info");

            for (Element playerElement : playerElements) {
                String name = playerElement.select(".name").text();
                String team = playerElement.select(".team-name").text();

                players.add(new Player(name, team));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return players;
    }
}
