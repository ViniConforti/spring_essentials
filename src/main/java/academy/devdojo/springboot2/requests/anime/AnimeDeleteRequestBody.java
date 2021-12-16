package academy.devdojo.springboot2.requests.anime;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class AnimeDeleteRequestBody {
    @NotNull
    private Long id;
}
