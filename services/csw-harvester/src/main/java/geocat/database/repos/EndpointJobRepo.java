package geocat.database.repos;

import geocat.database.entities.EndpointJob;
import geocat.database.entities.EndpointJobState;
import org.springframework.context.annotation.Scope;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope("prototype")
public interface EndpointJobRepo extends CrudRepository<EndpointJob, Long> {

    EndpointJob findFirstByHarvestJobIdAndUrl(String harvestId, String url);

    List<EndpointJob> findByHarvestJobIdAndUrlIn(String harvestId, List<String> urls);

    List<EndpointJob> findByHarvestJobIdAndStateNot(String harvestId, EndpointJobState not_state);

    List<EndpointJob> findByHarvestJobIdAndState(String harvestId, EndpointJobState state);

    List<EndpointJob> findByHarvestJobId(String harvestId);


}
