package lk.mobility.rideshare.repository.search;

import lk.mobility.rideshare.domain.MyLocation;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the MyLocation entity.
 */
public interface MyLocationSearchRepository extends ElasticsearchRepository<MyLocation, Long> {
}
