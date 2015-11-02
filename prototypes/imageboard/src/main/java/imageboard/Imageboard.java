package imageboard;

import org.springframework.data.repository.CrudRepository;

/**
 * Created by spiollinux on 01.11.15.
 */
public interface Imageboard extends CrudRepository<ImagePost, Long>{
}
