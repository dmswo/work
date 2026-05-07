package spring.work.user.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import spring.work.post.document.PostDocument;

public interface ElasticSearchRepository extends ElasticsearchRepository<PostDocument, Long> {
}
