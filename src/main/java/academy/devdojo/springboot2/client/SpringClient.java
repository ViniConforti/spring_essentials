package academy.devdojo.springboot2.client;

import academy.devdojo.springboot2.domain.Anime;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Log4j2
public class SpringClient {
    public static void main(String[] args) {

        //REST TEMPLATE GET
        /*Anime anime = new RestTemplate().getForObject("http://localhost:8080/anime/{id}", Anime.class,2);
        log.info(anime);

        Anime[] animes = new RestTemplate().getForObject("http://localhost:8080/anime/all",Anime[].class);
        log.info(Arrays.toString(animes));*/

        /*ResponseEntity<List<Anime>> enchange = new RestTemplate().exchange("http://localhost:8080/anime/all",
                HttpMethod.GET,
                null, new ParameterizedTypeReference<>(){});

        log.info(enchange.getBody());*/

        //REST TEMPLATE POST
        /*Anime bleach = Anime.builder().name("bleach").build();
        log.info(bleach);
        Anime saved = new RestTemplate().postForObject("http://localhost:8080/anime",bleach,Anime.class);
        log.info("Anime saved{}",saved);*/

        //REST TEMPLATE POST COM EXHANGE
        Anime bleach = Anime.builder().name("bleach").build();
        ResponseEntity<Anime> saved = new RestTemplate().exchange("http://localhost:8080/anime",HttpMethod.POST,
                new HttpEntity<>(bleach,createJsonHeaders()),
                Anime.class);
        log.info("Anime saved{}",saved);

        //REST TEMPLATE PUT
        Anime putAnime = saved.getBody();
        putAnime.setName("El Gallos");

        ResponseEntity<Void> putResponse = new RestTemplate().exchange("http://localhost:8080/anime", HttpMethod.PUT,
                                    new HttpEntity<>(putAnime, createJsonHeaders()), Void.class);
        log.info(putResponse);

        //REST TEMPLATE DELETE COM POST

        ResponseEntity<Anime> deleteResponse = new RestTemplate().exchange("http://localhost:8080/anime/delete",
                HttpMethod.POST,
                new HttpEntity<>(putAnime, createJsonHeaders()), Anime.class);
        log.info(deleteResponse);

    }

    private static HttpHeaders createJsonHeaders(){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }
}
