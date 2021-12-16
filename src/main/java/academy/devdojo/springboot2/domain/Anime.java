package academy.devdojo.springboot2.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

// Anotacao do Lombok que gera get and setters, hash e por ai vai
@Data
//Cria o construtor com os argumentos  utilizando o lombok
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder

public class Anime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

}
