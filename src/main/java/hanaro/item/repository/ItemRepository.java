package hanaro.item.repository;

import java.util.Optional;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import hanaro.item.entity.Item;
import jakarta.persistence.LockModeType;

public interface ItemRepository extends JpaRepository<Item, Integer> {
	@EntityGraph(attributePaths = "itemImages")
	Optional<Item> findByItemId(int itemId);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("select i from Item i where i.itemId = :itemId")
	Optional<Item> findByIdForUpdate(@Param("itemId") int itemId);
}
