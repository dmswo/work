package spring.work.user.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import spring.work.user.document.PostDocument;

public interface ElasticSearchRepository extends ElasticsearchRepository<PostDocument, Long> {
}
