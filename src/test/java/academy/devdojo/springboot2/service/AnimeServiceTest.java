package academy.devdojo.springboot2.service;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.exception.BadRequestException;
import academy.devdojo.springboot2.repository.AnimeRepository;
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
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


@ExtendWith(SpringExtension.class)
@DisplayName("Tests for Anise Service")
class AnimeServiceTest {

    //Utilizado quando quero testar a classe em si
    @InjectMocks
    private AnimeService animeService;

    //Utilizado para fazer um mock dos objetos utilizados dentro da classe que estou testando
    @Mock
    private AnimeRepository animeRepositoryMock;

    @BeforeEach
    void setup(){
        PageImpl<Anime> animePage =  new PageImpl<>(List.of(AnimeCreator.createValidAnime()));

        BDDMockito.when(animeRepositoryMock.findAll(ArgumentMatchers.any(Pageable.class)))
                .thenReturn(animePage);

        /* É possivel mockar métodos com o mesmo nome, desde que nao sejam utilizados os mesmos
           tipos de argument matchers*/
        BDDMockito.when(animeRepositoryMock.findAll())
                .thenReturn(List.of(AnimeCreator.createValidAnime()));

        BDDMockito.when(animeRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(AnimeCreator.createValidAnime()));

        BDDMockito.when(animeRepositoryMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(List.of(AnimeCreator.createValidAnime()));

        BDDMockito.when(animeRepositoryMock.save(ArgumentMatchers.any(Anime.class)))
                .thenReturn(AnimeCreator.createValidAnime());

        BDDMockito
                .doNothing()
                .when(animeRepositoryMock).delete(ArgumentMatchers.any(Anime.class));
    }

    @Test
    @DisplayName("Returns a page of animes when successful")
        //Padrao nomenclatura:Método_O que ele deve fazer_quando
    void listAll_returnsPageOfAnimes_whenSuccessful(){
        Anime createdAnime = AnimeCreator.createValidAnime();

        Page<Anime> animePage = this.animeService.listAll(Pageable.ofSize(1));

        Assertions.assertThat(animePage.toList())
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animePage.toList().get(0)).isEqualTo(createdAnime);
    }

    @Test
    @DisplayName("Returns a list of all animes when successful")
        //Padrao nomenclatura:Método_O que ele deve fazer_quando
    void listAllNonPageable_returnsListOfAnimesInsidePageObject_whenSuccessful(){

        List<Anime> animeList = this.animeService.listAllNonPageable();

        Assertions.assertThat(animeList)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animeList.get(0)).isEqualTo(AnimeCreator.createValidAnime());
    }

    @Test
    @DisplayName("Returns an anime found by id when successful")
        //Padrao nomenclatura:Método_O que ele deve fazer_quando
    void findByIdOrThrowBadRequestException_returnsAnAnime_whenSuccessful(){

        Anime validAnime = AnimeCreator.createValidAnime();

        Anime foundAnime = this.animeService
                .findByIdOrThrowBadRequestException(validAnime.getId());

        Assertions.assertThat(foundAnime).isNotNull()
                .isEqualTo(validAnime);
    }

    @Test
    @DisplayName("Throws a bad request exception when anime is not found")
        //Padrao nomenclatura:Método_O que ele deve fazer_quando
    void findByIdOrThrowBadRequestException_whenAnimeNotFound(){

        BadRequestException badRequestException = new BadRequestException("Anime not found");

        BDDMockito.when(this.animeRepositoryMock.
                findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(()-> this.animeService.findByIdOrThrowBadRequestException(1L))
                .isInstanceOf(badRequestException.getClass());

    }

    @Test
    @DisplayName("Returns a list of animes found by name when successful")
        //Padrao nomenclatura:Método_O que ele deve fazer_quando
    void findByName_returnsAListOfAnimes_whenSuccessful(){

        Anime validAnime = AnimeCreator.createValidAnime();

        List<Anime> foundAnimes = this.animeService.findByName(validAnime.getName());

        Assertions.assertThat(foundAnimes)
                .isNotEmpty()
                .hasSize(1)
                .contains(validAnime);

    }

    @Test
    @DisplayName("Returns an empty list when anime is not found")
        //Padrao nomenclatura:Método_O que ele deve fazer_quando
    void findByName_returnsAnEmptyList_whenAnimeNotFound(){
        BDDMockito.when(this.animeRepositoryMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(Collections.emptyList());

        List<Anime> foundAnimes = this.animeService.findByName("123");
        Assertions.assertThat(foundAnimes)
                .isEmpty();
    }

    @Test
    @DisplayName("Save persists an anime when successfull")
        //Padrao nomenclatura:Método_O que ele deve fazer_quando
    void save_ReturnsAnAnime_whenSuccessful(){

        Anime savedAnime = this.animeService.
                save(AnimePostRequestBodyCreator
                        .animePostRequestBody());
        Assertions.assertThat(savedAnime)
                .isNotNull()
                .isEqualTo(AnimeCreator.createValidAnime());


    }

    @Test
    @DisplayName("Replace updates anime when successful")
        //Padrao nomenclatura:Método_O que ele deve fazer_quando
    void replace_UpdatesAnime_whenSuccessful(){

        Assertions.assertThatCode
                        (()-> this.animeService.replace(AnimePutRequestBodyCreator.animePutRequestBody()))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Deletes an anime when successful")
        //Padrao nomenclatura:Método_O que ele deve fazer_quando
    void deletes_AnAnime_whenSuccessful(){

        Assertions.assertThatCode
                        (()-> this.animeService
                                .delete(AnimeDeleteRequestBodyCreator.animeDeleteRequestBody().getId()))
                .doesNotThrowAnyException();

    }


}