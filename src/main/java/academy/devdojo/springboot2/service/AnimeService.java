package academy.devdojo.springboot2.service;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.exception.BadRequestException;
import academy.devdojo.springboot2.mapper.anime.AnimeMapper;
import academy.devdojo.springboot2.repository.AnimeRepository;
import academy.devdojo.springboot2.requests.anime.AnimePostRequestBody;
import academy.devdojo.springboot2.requests.anime.AnimePutRequestBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class AnimeService  {
    private final AnimeRepository animeRepository;
    private final AnimeMapper animeMapper;

    // Paginacao feita pelo spring
    public Page<Anime> listAll(Pageable pageable) {
        return this.animeRepository.findAll(pageable);
    }

    public List<Anime> listAllNonPageable() {
        return animeRepository.findAll();
    }

    public List<Anime> findByName(String name) {
        return this.animeRepository.findByName(name);
    }

    public Anime findByIdOrThrowBadRequestException(long id) {
        return this.animeRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Anime not found"));
    }

    @Transactional(rollbackOn = Exception.class)
    public void delete(long id) {
      this.animeRepository.delete(this.findByIdOrThrowBadRequestException(id));
    }

    @Transactional(rollbackOn = Exception.class)
    public void replace(AnimePutRequestBody animePutRequestBody) {
        Anime savedAnime = this.findByIdOrThrowBadRequestException(animePutRequestBody.getId());
        Anime newAnime = animeMapper.INSTANCE.toAnime(animePutRequestBody);
        newAnime.setId(savedAnime.getId());
        this.animeRepository.save(newAnime);
    }

    /* Nao commita transacao enquanto metodo nao for finalizado,
    excelente pra dar rollback em caso de excecao. No caso, é um rollback automatico
     caso alguma excecao ocorra.*/

    /*OBS muito importante usar a propriedade rollbackOn e declarar Exception.class
    Caso contrario, excecoes do tipo Checked nao deixarao o metodo dar o rollback
    Exemplo de excecao do tipo checked new Exception(msg)
    Professor disse que nao é uma boa usar o parametro rollback exception, verificar nas proximas aulas
    o que usar
     */
    @Transactional(rollbackOn = Exception.class)
    public Anime save(AnimePostRequestBody animePostRequestBody){
        //Ele mapeia corretamente de volta, o certo é enviar o mapper como retorno e nao a entidade.
        //Anime anime = animeRepository.save(animeMapper.INSTANCE.toAnime(animePostRequestBody));
        //System.out.println(animeMapper.toAnimePostRequestBody(anime));
        //return anime;
        //log.info();
        //log.info(animeMapper.INSTANCE.toAnime(animePostRequestBody));
        return this.animeRepository.save(animeMapper.INSTANCE.toAnime(animePostRequestBody));
    }

}