package academy.devdojo.springboot2.controller;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.exception.BadRequestException;
import academy.devdojo.springboot2.requests.anime.AnimePostRequestBody;
import academy.devdojo.springboot2.requests.anime.AnimePutRequestBody;
import academy.devdojo.springboot2.service.AnimeService;
import academy.devdojo.springboot2.util.anime.AnimeCreator;
import academy.devdojo.springboot2.util.anime.AnimeDeleteRequestBodyCreator;
import academy.devdojo.springboot2.util.anime.AnimePostRequestBodyCreator;
import academy.devdojo.springboot2.util.anime.AnimePutRequestBodyCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;

@ExtendWith(SpringExtension.class)
@DisplayName("Tests for Anime controller")

class AnimesControllerTest {

    //Utilizado quando quero testar a classe em si
    @InjectMocks
    private AnimesController animesController;

    //Utilizado para fazer um mock dos objetos utilizados dentro da classe que estou testando
    @Mock
    private AnimeService animeServiceMock;

    @BeforeEach
    void setup(){
        PageImpl<Anime> animePage =  new PageImpl<>(List.of(AnimeCreator.createValidAnime()));

        BDDMockito.when(animeServiceMock.listAll(ArgumentMatchers.any()))
                .thenReturn(animePage);

        BDDMockito.when(animeServiceMock.listAllNonPageable())
                .thenReturn(List.of(AnimeCreator.createValidAnime()));

        BDDMockito.when(animeServiceMock.findByIdOrThrowBadRequestException(ArgumentMatchers.anyLong()))
                .thenReturn(AnimeCreator.createValidAnime());

        BDDMockito.when(animeServiceMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(List.of(AnimeCreator.createValidAnime()));

        BDDMockito.when(animeServiceMock.save(ArgumentMatchers.any(AnimePostRequestBody.class)))
                .thenReturn(AnimeCreator.createValidAnime());

        // Mockito para métodos void
        BDDMockito
                .doNothing()
                .when(animeServiceMock).replace(ArgumentMatchers.any(AnimePutRequestBody.class));
        BDDMockito
                .doNothing()
                .when(animeServiceMock).delete(ArgumentMatchers.anyLong());
    }

    @Test
    @DisplayName("Returns a list of animes inside a page object when successful")
        //Padrao nomenclatura:Método_O que ele deve fazer_quando
    void list_returnsListOfAnimesInsidePageObject_whenSuccessful(){
        Anime createdAnime = AnimeCreator.createValidAnime();

        Page<Anime> animePage = this.animesController.list(null).getBody();

        Assertions.assertThat(animePage.toList())
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animePage.toList().get(0)).isEqualTo(createdAnime);
    }

    @Test
    @DisplayName("Returns a list of all animes inside a page object when successful")
        //Padrao nomenclatura:Método_O que ele deve fazer_quando
    void listAll_returnsListOfAnimesInsidePageObject_whenSuccessful(){

        List<Anime> animeList = this.animesController.listAll().getBody();

        Assertions.assertThat(animeList)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animeList.get(0)).isEqualTo(AnimeCreator.createValidAnime());
    }

    @Test
    @DisplayName("Returns an anime found by id when successful")
        //Padrao nomenclatura:Método_O que ele deve fazer_quando
    void findById_returnsAnAnime_whenSuccessful(){

       Anime validAnime = AnimeCreator.createValidAnime();

       Anime foundAnime = this.animesController.findbyId(validAnime.getId())
               .getBody();

       Assertions.assertThat(foundAnime).isNotNull()
               .isEqualTo(validAnime);

    }

    @Test
    @DisplayName("Throws a bad request exception when anime is not found")
        //Padrao nomenclatura:Método_O que ele deve fazer_quando
    void findById_throwsBadRequestException_whenAnimeNotFound(){

        BadRequestException badRequestException = new BadRequestException("Anime not found");

        BDDMockito.when(animeServiceMock
                .findByIdOrThrowBadRequestException(ArgumentMatchers.anyLong()))
                .thenThrow(badRequestException);

        Assertions.assertThatThrownBy(()-> this.animesController.findbyId(1L))
                .isInstanceOf(badRequestException.getClass());

    }

    @Test
    @DisplayName("Returns a list of animes found by name when successful")
        //Padrao nomenclatura:Método_O que ele deve fazer_quando
    void findByName_returnsAListOfAnimes_whenSuccessful(){

        Anime validAnime = AnimeCreator.createValidAnime();

        List<Anime> foundAnimes = this.animesController.findbyName(validAnime.getName()).getBody();

        Assertions.assertThat(foundAnimes)
                .isNotEmpty()
                .hasSize(1)
                .contains(validAnime);

    }

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


    }

}