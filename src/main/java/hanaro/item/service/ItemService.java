package hanaro.item.service;

import hanaro.exception.GeneralException;
import hanaro.item.dto.ItemDTO;
import hanaro.item.entity.Item;
import hanaro.item.repository.ItemRepository;
import hanaro.response.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    private ItemDTO toDTO(Item item) {
        return ItemDTO.builder()
                .itemId(item.getItemId())
                .itemName(item.getItemName())
                .price(item.getPrice())
                .stock(item.getStock())
                .build();
    }

    @Transactional
    public ItemDTO addItem(ItemDTO requestDTO) {
        Item item = Item.builder()
                .itemName(requestDTO.getItemName())
                .price(requestDTO.getPrice())
                .stock(requestDTO.getStock())
                .build();
        return toDTO(itemRepository.save(item));
    }

    @Transactional(readOnly = true)
    public List<ItemDTO> getAllItems() {
        return itemRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ItemDTO getItemById(int itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ITEM_NOT_FOUND)); // ITEM_NOT_FOUND는 ErrorStatus에 추가해야 합니다.
        return toDTO(item);
    }

    @Transactional
    public ItemDTO updateItem(int itemId, ItemDTO requestDTO) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ITEM_NOT_FOUND)); // ITEM_NOT_FOUND는 ErrorStatus에 추가해야 합니다.

        Item updatedItem = Item.builder()
                .itemId(item.getItemId()) // Keep the original ID
                .itemName(requestDTO.getItemName())
                .price(requestDTO.getPrice())
                .stock(requestDTO.getStock())
                .build();

        return toDTO(itemRepository.save(updatedItem));
    }

    @Transactional
    public void deleteItem(int itemId) {
        if (!itemRepository.existsById(itemId)) {
            throw new GeneralException(ErrorStatus.ITEM_NOT_FOUND); // ITEM_NOT_FOUND는 ErrorStatus에 추가해야 합니다.
        }
        itemRepository.deleteById(itemId);
    }
}