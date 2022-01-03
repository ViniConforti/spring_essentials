package academy.devdojo.springboot2.requests.anime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotNull;

@Data
@Builder

/*Usar essa anotacao quando usar um builder nesses dtos. O jackson se bate quando tem um construtor
/ com parametros*/
@Jacksonized
public class AnimeDeleteRequestBody {
    @NotNull
    @Schema(description = "This is the anime id")
    private Long id;
}
