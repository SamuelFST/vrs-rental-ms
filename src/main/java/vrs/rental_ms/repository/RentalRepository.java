package vrs.rental_ms.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import vrs.rental_ms.document.Rental;

@Repository
public interface RentalRepository extends MongoRepository<Rental, String> {
}
