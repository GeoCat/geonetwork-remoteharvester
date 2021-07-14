package net.geocat.database.linkchecker.service;

import net.geocat.database.linkchecker.entities.Link;
import net.geocat.database.linkchecker.repos.LinkRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope("prototype")
public class LinkService {
    @Autowired
    LinkRepo linkRepo;

    public List<Link> findLinks(String linkCheckJobId) {
        return linkRepo.findByLinkCheckJobId(linkCheckJobId);
    }
}
