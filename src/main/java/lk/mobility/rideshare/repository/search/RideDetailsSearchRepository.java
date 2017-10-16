package lk.mobility.rideshare.repository.search;

import lk.mobility.rideshare.domain.RideDetails;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the RideDetails entity.
 */
public interface RideDetailsSearchRepository extends ElasticsearchRepository<RideDetails, Long> {
}
