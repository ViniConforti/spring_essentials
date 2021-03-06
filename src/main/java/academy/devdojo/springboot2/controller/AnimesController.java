package academy.devdojo.springboot2.controller;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.requests.anime.AnimeDeleteRequestBody;
import academy.devdojo.springboot2.requests.anime.AnimePostRequestBody;
import academy.devdojo.springboot2.requests.anime.AnimePutRequestBody;
import academy.devdojo.springboot2.service.AnimeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @Operation(summary = "List all animes paginated", description = "The default page size is 20",
    tags = {"anime"})
    public ResponseEntity<Page<Anime>> list(@ParameterObject Pageable pageable){
        return new ResponseEntity<>(this.animeService.listAll(pageable),
                HttpStatus.OK);
    }

    @GetMapping(path = "/all")
    @ApiResponse(responseCode = "200", description = "Successfull operation",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Anime.class))))
    public ResponseEntity<List<Anime>> listAll(){
        return new ResponseEntity<>(this.animeService.listAllNonPageable(),
                HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description = "Successfull operation",
            content= @Content(schema= @Schema(implementation = Anime.class))),
            @ApiResponse(responseCode = "400", description = "Anime doesnt exists")
    })
    public ResponseEntity<Anime> findbyId(@PathVariable long id){
        return new ResponseEntity<>(this.animeService.findByIdOrThrowBadRequestException(id), HttpStatus.OK);
    }

    //find?name=
    @GetMapping(path = "/find")

    @ApiResponse(responseCode = "200", description = "Successfull operation",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = Anime.class))))
    public ResponseEntity<List<Anime>> findbyName(@RequestParam(defaultValue = "") String name){
        return new ResponseEntity<>(this.animeService.findByName(name), HttpStatus.OK);
    }

    @PostMapping(path = "/admin/save")
    @ApiResponse(responseCode = "201", description = "Successfull operation",
            content = @Content(schema = @Schema(implementation = Anime.class)))
    public ResponseEntity<Anime>save(@RequestBody @Valid AnimePostRequestBody animePostRequestBody){
        return new ResponseEntity<>(animeService.save(animePostRequestBody), HttpStatus.CREATED);

    }

    @PostMapping(path="/admin/delete")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description = "Successfull operation"),
            @ApiResponse(responseCode = "400", description = "Anime doesnt exists"),

    })
    public ResponseEntity<Void>delete(@RequestBody @Valid AnimeDeleteRequestBody animeDeleteRequestBody){
        animeService.delete(animeDeleteRequestBody.getId());
        return new ResponseEntity(HttpStatus.OK);
    }

    @PutMapping(path = "/admin/replace")
    @ApiResponses(value={
            @ApiResponse(responseCode = "204", description= "Successfull operation"),
            @ApiResponse(responseCode = "400", description= "Anime doesnt exists")}
    )
    public ResponseEntity<Void>replace(@RequestBody @Valid AnimePutRequestBody animePutRequestBody){
        animeService.replace(animePutRequestBody);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


}
