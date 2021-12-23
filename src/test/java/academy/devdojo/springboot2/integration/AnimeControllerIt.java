package academy.devdojo.springboot2.integration;
import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.repository.AnimeRepository;
import academy.devdojo.springboot2.requests.anime.AnimeDeleteRequestBody;
import academy.devdojo.springboot2.requests.anime.AnimePostRequestBody;
import academy.devdojo.springboot2.requests.anime.AnimePutRequestBody;
import academy.devdojo.springboot2.util.anime.AnimeCreator;
import academy.devdojo.springboot2.util.anime.AnimeDeleteRequestBodyCreator;
import academy.devdojo.springboot2.util.anime.AnimePostRequestBodyCreator;
import academy.devdojo.springboot2.util.anime.AnimePutRequestBodyCreator;
import academy.devdojo.springboot2.wrapper.PageableResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

/*Antes de fazer o teste de integracao, é necessario inicializar o springboot
  com a anotacao abaixo. É importante setar uma porta aleatoria para o webenviroment tbm,
  pois possivelmente a porta padrao 8080 ja estara sendo utilizada por sua aplicacao
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase

//Limpa o banco a cada testes executado
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
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


    @Test
    @DisplayName("Returns an empty list when anime is not found")
        //Padrao nomenclatura:Método_O que ele deve fazer_quando
    void findByName_returnsAnEmptyList_whenAnimeNotFound(){

        String url = String.format("/anime/find?name=%s","Nao_Existo");

        List<Anime> animeList = testRestTemplate.exchange(url,
                HttpMethod.GET,null,
                new ParameterizedTypeReference<List<Anime>>() {}).getBody();

        Assertions.assertThat(animeList)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("Save persists an anime when successfull")
        //Padrao nomenclatura:Método_O que ele deve fazer_quando
    void save_ReturnsAnAnime_whenSuccessful(){

        AnimePostRequestBody animePostRequestBody = AnimePostRequestBodyCreator.animePostRequestBody();

        ResponseEntity<Anime> responseEntity = testRestTemplate.postForEntity("/anime",
                animePostRequestBody,Anime.class);

        Anime savedAnime = responseEntity.getBody();

        Assertions.assertThat(savedAnime.getId()).isNotNull();

        Assertions.assertThat(savedAnime.getName()).isEqualTo(animePostRequestBody.getName());

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

    }


    @Test
    @DisplayName("Replace updates anime when successful")
        //Padrao nomenclatura:Método_O que ele deve fazer_quando
    void replace_UpdatesAnime_whenSuccessful(){

        Anime savedAnime = this.animeRepository.save(AnimeCreator.createValidAnime());

        AnimePutRequestBody animePutRequestBody =
                AnimePutRequestBodyCreator.animePutRequestBody();

        animePutRequestBody.setId(savedAnime.getId());

        this.testRestTemplate.put("/anime", animePutRequestBody);

        Assertions.assertThat(this.animeRepository.findAll().get(0).getId())
                .isEqualTo(animePutRequestBody.getId());

        Assertions.assertThat(this.animeRepository.findAll().get(0).getName())
                .isEqualTo(animePutRequestBody.getName());

    }

    @Test
    @DisplayName("Deletes an anime when successful")
        //Padrao nomenclatura:Método_O que ele deve fazer_quando
    void deletes_AnAnime_whenSuccessful(){
        Anime savedAnime = this.animeRepository.save(AnimeCreator.createValidAnime());

        AnimeDeleteRequestBody animeDeleteRequestBody =
                AnimeDeleteRequestBodyCreator.animeDeleteRequestBody();

        animeDeleteRequestBody.setId(savedAnime.getId());

        ResponseEntity<Void> responseEntity =
                testRestTemplate.postForEntity("/anime/delete",animeDeleteRequestBody,
                Void.class);

        Assertions.assertThat(this.animeRepository.findById(animeDeleteRequestBody.getId()))
                .isEmpty();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

}
