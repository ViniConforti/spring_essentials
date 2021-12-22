package academy.devdojo.springboot2.integration;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.exception.BadRequestException;
import academy.devdojo.springboot2.repository.AnimeRepository;
import academy.devdojo.springboot2.util.anime.AnimeCreator;
import academy.devdojo.springboot2.util.anime.AnimeDeleteRequestBodyCreator;
import academy.devdojo.springboot2.util.anime.AnimePostRequestBodyCreator;
import academy.devdojo.springboot2.util.anime.AnimePutRequestBodyCreator;
import academy.devdojo.springboot2.wrapper.PageableResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

/*Antes de fazer o teste de integracao, é necessario inicializar o springboot
  com a anotacao abaixo. É importante setar uma porta aleatoria para o webenviroment tbm,
  pois possivelmente a porta padrao 8080 ja estara sendo utilizada por sua aplicacao
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class AnimeControllerIt {

    @Autowired
    private AnimeRepository animeRepository;

    @Autowired
    private TestRestTemplate testRestTemplate;

    /*Nao é necessario, é só uma maneira de pegar qual porta o Spring esta rodando, nesse caso ali,
      pega o valor da SpringBootTest.WebEnviroment.RANDOM_PORT
    */

    @LocalServerPort
    private int port;


    @BeforeEach
     void setup(){
        this.animeRepository.deleteAll();
     }

    @Test
    @DisplayName("Returns a list of animes inside a page object when successful")
        //Padrao nomenclatura:Método_O que ele deve fazer_quando
    void list_returnsListOfAnimesInsidePageObject_whenSuccessful(){
        Anime savedAnime = AnimeCreator.createAnimeToBeSaved();
        this.animeRepository.save(savedAnime);

        PageableResponse<Anime> animePage = testRestTemplate.exchange("/anime",
               HttpMethod.GET, null,
        new ParameterizedTypeReference<PageableResponse<Anime>>() {}).getBody();

        Assertions.assertThat(animePage.toList())
                        .isNotEmpty()
                        .hasSize(1);

        Assertions.assertThat(animePage.toList().get(0)).isEqualTo(savedAnime);
    }

    @Test
    @DisplayName("Returns a list of all animes object when successful")
        //Padrao nomenclatura:Método_O que ele deve fazer_quando
    void listAll_returnsListOfAnimes_whenSuccessful(){

        Anime savedAnime = AnimeCreator.createAnimeToBeSaved();
        this.animeRepository.save(savedAnime);

        List<Anime> animeList = testRestTemplate.exchange("/anime/all",
                HttpMethod.GET,null,
                new ParameterizedTypeReference<List<Anime>>() {}).getBody();

        Assertions.assertThat(animeList)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animeList.get(0)).isEqualTo(savedAnime);
    }

    @Test
    @DisplayName("Returns an anime found by id when successful")
        //Padrao nomenclatura:Método_O que ele deve fazer_quando
    void findById_returnsAnAnime_whenSuccessful(){
        Anime savedAnime = AnimeCreator.createAnimeToBeSaved();
        this.animeRepository.save(savedAnime);

        Anime foundAnime = this.testRestTemplate.getForObject("/anime/{id}",
                Anime.class,savedAnime.getId());

        Assertions.assertThat(foundAnime).isNotNull()
                .isEqualTo(savedAnime);

    }

    /*
    @Test
    @DisplayName("Throws a bad request exception when anime is not found")
        //Padrao nomenclatura:Método_O que ele deve fazer_quando
    void findById_throwsBadRequestException_whenAnimeNotFound(){
        Assertions.assertThatThrownBy(()-> this.testRestTemplate.getForObject("/anime/{id}",
                Anime.class,1000L))
                .isInstanceOf(BadRequestException.class);
    }*/


    @Test
    @DisplayName("Returns a list of animes found by name when successful")
        //Padrao nomenclatura:Método_O que ele deve fazer_quando
    void findByName_returnsAListOfAnimes_whenSuccessful(){

        Anime savedAnime = AnimeCreator.createAnimeToBeSaved();
        this.animeRepository.save(savedAnime);
        String url = String.format("/anime/find?name=%s",savedAnime.getName());

        List<Anime> animeList = testRestTemplate.exchange(url,
                HttpMethod.GET,null,
                new ParameterizedTypeReference<List<Anime>>() {}).getBody();

        Assertions.assertThat(animeList)
                .isNotEmpty()
                .hasSize(1)
                .contains(savedAnime);

    }

    /*
    @Test
    @DisplayName("Returns an empty list when anime is not found")
        //Padrao nomenclatura:Método_O que ele deve fazer_quando
    void findByName_returnsAnEmptyList_whenAnimeNotFound(){
        BDDMockito.when(animeServiceMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(Collections.emptyList());

        List<Anime> foundAnimes = this.animesController.findbyName("123").getBody();
        Assertions.assertThat(foundAnimes)
                .isEmpty();
    }

    @Test
    @DisplayName("Save persists an anime when successfull")
        //Padrao nomenclatura:Método_O que ele deve fazer_quando
    void save_ReturnsAnAnime_whenSuccessful(){

        Anime savedAnime = this.animesController.
                save(AnimePostRequestBodyCreator
                        .animePostRequestBody()).getBody();
        Assertions.assertThat(savedAnime)
                .isNotNull()
                .isEqualTo(AnimeCreator.createValidAnime());


    }

    @Test
    @DisplayName("Replace updates anime when successful")
        //Padrao nomenclatura:Método_O que ele deve fazer_quando
    void replace_UpdatesAnime_whenSuccessful(){

        Assertions.assertThatCode
                        (()-> this.animesController.replace(AnimePutRequestBodyCreator.animePutRequestBody()))
                .doesNotThrowAnyException();

        ResponseEntity<Void> entity = this.animesController
                .replace(AnimePutRequestBodyCreator.animePutRequestBody());

        Assertions.assertThat(entity).isNotNull();
        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);


    }

    @Test
    @DisplayName("Deletes an anime when successful")
        //Padrao nomenclatura:Método_O que ele deve fazer_quando
    void deletes_AnAnime_whenSuccessful(){

        Assertions.assertThatCode
                        (()-> this.animesController
                                .delete(AnimeDeleteRequestBodyCreator.animeDeleteRequestBody()))
                .doesNotThrowAnyException();

        ResponseEntity<Void> entity = this.animesController
                .delete(AnimeDeleteRequestBodyCreator.animeDeleteRequestBody());

        Assertions.assertThat(entity).isNotNull();
        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);


    }*/

}
