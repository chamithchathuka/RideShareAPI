package lk.mobility.rideshare.repository.search;

import lk.mobility.rideshare.domain.Ride;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Ride entity.
 */
public interface RideSearchRepository extends ElasticsearchRepository<Ride, Long> {
}
