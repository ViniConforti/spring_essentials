package academy.devdojo.springboot2.repository;
import academy.devdojo.springboot2.domain.Anime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Tecla alt enter para criar teste unitario
public interface AnimeRepository extends JpaRepository<Anime,Long> {
    /*FindBy nome do atributo. No caso, atributo name dentro da minha entidade
      O Spring faz o mapeamento automatico assim
     */
    List<Anime> findByName(String name);
}
