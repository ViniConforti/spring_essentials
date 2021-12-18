package academy.devdojo.springboot2.repository;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.util.anime.AnimeCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

/*ESSE TESTE DE REPOSITÓRIO É SÓ PARA FINS EDUCATIVOS, NAO SE TESTA MÉTODOS JA IMPLEMENTADOS PELO FRAMEWORK
TESTE DE REPOSITÓRIO É MELHOR FAZER EM TESTES DE INTEGRAÇAO E NAO UNITARIOS*/

@DataJpaTest
@DisplayName("Tests for Anime Repository")
class AnimeRepositoryTest {
    @Autowired
    private AnimeRepository animeRepository;

    @Test
    @DisplayName("Save persists anime when successful")
    //Padrao nomenclatura:Método_O que ele deve fazer_quando
    void save_persistAnime_whenSuccessful(){

        Anime animeGenerated = AnimeCreator.createAnimeToBeSaved();

        Anime savedAnime = this.animeRepository.save(animeGenerated);

        Assertions.assertThat(savedAnime).isNotNull();

        Assertions.assertThat(savedAnime.getId()).isNotNull();

        Assertions.assertThat(savedAnime.getName()).isEqualTo(animeGenerated.getName());
    }

    @Test
    @DisplayName("Save updates anime when successful")
        //Padrao nomenclatura:Método_O que ele deve fazer_quando
    void save_updatesAnime_whenSuccessful(){
        Anime animeGenerated = AnimeCreator.createAnimeToBeSaved();

        Anime savedAnime = this.animeRepository.save(animeGenerated);

        savedAnime.setName("Updated_name");

        Anime updatedAnime = this.animeRepository.save(savedAnime);

        Assertions.assertThat(updatedAnime.getId()).isEqualTo(savedAnime.getId());

        Assertions.assertThat(updatedAnime.getName()).isEqualTo(savedAnime.getName());

    }

    @Test
    @DisplayName("Delete removes anime when successful")
        //Padrao nomenclatura:Método_O que ele deve fazer_quando
    void delete_removesAnime_whenSuccessful(){
        Anime animeGenerated = AnimeCreator.createAnimeToBeSaved();

        Anime savedAnime = this.animeRepository.save(animeGenerated);

        this.animeRepository.delete(savedAnime);

        Optional<Anime> animeOptional = this.animeRepository.findById(savedAnime.getId());

        Assertions.assertThat(animeOptional).isEmpty();

    }

    @Test
    @DisplayName("Find by name and returns a list of animes when successful")
        //Padrao nomenclatura:Método_O que ele deve fazer_quando
    void findByName_returnsListOfAnimes_whenSuccessful(){
        Anime animeGenerated = AnimeCreator.createAnimeToBeSaved();

        Anime savedAnime = this.animeRepository.save(animeGenerated);

        List<Anime> foundAnimeList = this.animeRepository.findByName(savedAnime.getName());

        Assertions.assertThat(foundAnimeList)
                .isNotEmpty()
                .contains(savedAnime);

    }

    @Test
    @DisplayName("Find by name returns empty list of animes when anime anime is found")
        //Padrao nomenclatura:Método_O que ele deve fazer_quando
    void findByName_returnsEmptyListOfAnimes_whenNoAnimeIsFound(){

        this.animeRepository.save(AnimeCreator.createAnimeToBeSaved());

        List<Anime> foundAnimeList = this.animeRepository.findByName("Nao existo");

        Assertions.assertThat(foundAnimeList).isEmpty();

    }


    @Test
    @DisplayName("Save throws constraint violation exception when name is null")
        //Padrao nomenclatura:Método_O que ele deve fazer_quando
    void save_throws_ConstraintViolationException_WhenNameIsNull(){

        Anime animeGenerated = new Anime();

        Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(()-> this.animeRepository.save(animeGenerated));
    }


}