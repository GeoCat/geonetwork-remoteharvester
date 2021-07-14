package net.geocat.database.linkchecker.repos;

import net.geocat.database.linkchecker.entities.Link;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LinkRepo extends CrudRepository<Link, Long> {

    List<Link>   findByLinkCheckJobId (String linkCheckJobId);

}