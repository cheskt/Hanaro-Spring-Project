package hanaro.item.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import hanaro.item.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Integer> {
	@EntityGraph(attributePaths = "itemImages")
	Optional<Item> findByItemId(int itemId);
}
