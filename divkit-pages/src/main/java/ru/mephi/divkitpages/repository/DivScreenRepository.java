package ru.mephi.divkitpages.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import ru.mephi.divkitpages.service.DivKitPageEntity;

@Repository
public interface DivScreenRepository extends ReactiveMongoRepository<DivKitPageEntity, String> {
}