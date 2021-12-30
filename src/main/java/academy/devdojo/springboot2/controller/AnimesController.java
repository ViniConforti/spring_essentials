package academy.devdojo.springboot2.controller;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.requests.anime.AnimeDeleteRequestBody;
import academy.devdojo.springboot2.requests.anime.AnimePostRequestBody;
import academy.devdojo.springboot2.requests.anime.AnimePutRequestBody;
import academy.devdojo.springboot2.service.AnimeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("anime")
@Log4j2
//Cria um construtor com todos os atributos finais. Faz automaticamente a injecao de dependencia
@RequiredArgsConstructor
public class AnimesController {
    private final AnimeService animeService;

    @GetMapping
    public ResponseEntity<Page<Anime>> list(Pageable pageable){
        return new ResponseEntity<>(this.animeService.listAll(pageable),
                HttpStatus.OK);
    }

    @GetMapping(path = "/all")
    public ResponseEntity<List<Anime>> listAll(){
        return new ResponseEntity<>(this.animeService.listAllNonPageable(),
                HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Anime> findbyId(@PathVariable long id){
        return new ResponseEntity<>(this.animeService.findByIdOrThrowBadRequestException(id), HttpStatus.OK);
    }

    //find?name=
    @GetMapping(path = "/find")
    public ResponseEntity<List<Anime>> findbyName(@RequestParam(defaultValue = "") String name){
        return new ResponseEntity<>(this.animeService.findByName(name), HttpStatus.OK);
    }

    @PostMapping(path = "/admin/save")
    public ResponseEntity<Anime>save(@RequestBody @Valid AnimePostRequestBody animePostRequestBody){
        return new ResponseEntity<>(animeService.save(animePostRequestBody), HttpStatus.CREATED);

    }

    @PostMapping(path="/admin/delete")
    public ResponseEntity<Void>delete(@RequestBody @Valid AnimeDeleteRequestBody animeDeleteRequestBody){
        animeService.delete(animeDeleteRequestBody.getId());
        return new ResponseEntity(HttpStatus.OK);
    }

    @PutMapping(path = "/admin/replace")
    public ResponseEntity<Void>replace(@RequestBody @Valid AnimePutRequestBody animePutRequestBody){
        animeService.replace(animePutRequestBody);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


}
