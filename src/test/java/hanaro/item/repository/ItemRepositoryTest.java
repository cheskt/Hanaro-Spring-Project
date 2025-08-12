package hanaro.item.repository;

import hanaro.item.entity.Item;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Test
    @DisplayName("상품 저장 및 조회 테스트")
    void saveAndFindItem() {
        // given
        Item item = Item.builder()
                .itemName("Test Item")
                .price(10000)
                .stock(100)
                .build();

        // when
        Item savedItem = itemRepository.save(item);
        Item foundItem = itemRepository.findById(savedItem.getItemId()).orElse(null);

        // then
        assertThat(foundItem).isNotNull();
        assertThat(foundItem.getItemName()).isEqualTo("Test Item");
    }
}
