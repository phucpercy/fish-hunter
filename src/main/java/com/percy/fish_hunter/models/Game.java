package com.percy.fish_hunter.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners({AuditingEntityListener.class})
public class Game {

    @Id
    @GeneratedValue
    private int id;
    private GameStatus status;
    @CreatedDate
    private Date createdDate;
    private Integer roomId;

    @OneToMany(mappedBy = "primaryKey.game", fetch = FetchType.EAGER)
    private List<PlayerGame> playerGames = new ArrayList<>();
}
