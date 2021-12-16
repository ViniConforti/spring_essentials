package academy.devdojo.springboot2.requests.anime;
import lombok.Data;
import javax.validation.constraints.NotNull;

@Data
public class AnimePutRequestBody {
    @NotNull
    private Long id;
    @NotNull
    private String name;
}
