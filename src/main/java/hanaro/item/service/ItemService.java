package hanaro.item.service;

import hanaro.exception.GeneralException;
import hanaro.item.dto.ItemDTO;
import hanaro.item.dto.ItemDetailDTO;
import hanaro.item.dto.ItemImageResponseDTO;
import hanaro.item.entity.Item;
import hanaro.item.entity.ItemImage;
import hanaro.item.repository.ItemRepository;
import hanaro.item.repository.ItemImageRepository;
import hanaro.response.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile; 

import java.io.IOException;
import java.nio.file.Files; 
import java.nio.file.Path; 
import java.nio.file.Paths; 
import java.time.LocalDate; 
import java.time.format.DateTimeFormatter; 
import java.util.List;
import java.util.UUID; 
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {

    private static final Logger logger = LoggerFactory.getLogger(ItemService.class);

    private final ItemRepository itemRepository;
    private final ItemImageRepository itemImageRepository;

    private static final String BASE_UPLOAD_PATH = "src/main/resources/static/";
    private static final String ORIGIN_DIR = "origin";
    private static final String UPLOAD_DIR = "upload";
    
    private static final long MAX_FILE_SIZE_BYTES = 512 * 1024;
    private static final long MAX_TOTAL_FILE_SIZE_BYTES = 3 * 1024 * 1024;

    @Transactional(readOnly = true)
    public List<ItemDTO> getAllItems() {
        logger.info("Fetching all items");
        return itemRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ItemDetailDTO getItemDetail(int itemId) {
        Item item = itemRepository.findByItemId(itemId)
                                  .orElseThrow(() -> new GeneralException(ErrorStatus.ITEM_NOT_FOUND));
        return toDetailDTO(item);
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

    @Transactional
    public ItemDTO updateItem(int itemId, ItemDTO requestDTO) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ITEM_NOT_FOUND));
        item.setItemName(requestDTO.getItemName());
        item.setPrice(requestDTO.getPrice());
        item.setStock(requestDTO.getStock());

        return toDTO(itemRepository.save(item));
    }

    @Transactional
    public void deleteItem(int itemId) {
        if (!itemRepository.existsById(itemId)) {
            throw new GeneralException(ErrorStatus.ITEM_NOT_FOUND);
        }
        itemRepository.deleteById(itemId);
    }

    @Transactional
    public ItemImageResponseDTO addImageToItem(int itemId, MultipartFile file) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ITEM_NOT_FOUND));

        if (file.getSize() > MAX_FILE_SIZE_BYTES) {
            throw new GeneralException(ErrorStatus.FILE_SIZE_EXCEEDED);
        }
        if (file.getSize() > MAX_TOTAL_FILE_SIZE_BYTES) {
            throw new GeneralException(ErrorStatus.FILE_SIZE_EXCEEDED);
        }

        LocalDate today = LocalDate.now();
        String datePath = today.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        Path uploadPath = Paths.get(BASE_UPLOAD_PATH, UPLOAD_DIR, datePath);
        Path originPath = Paths.get(BASE_UPLOAD_PATH, ORIGIN_DIR, datePath);

        try {
            Files.createDirectories(uploadPath);
            Files.createDirectories(originPath);
        } catch (IOException e) {
            throw new GeneralException(ErrorStatus.FILE_UPLOAD_FAILED);
        }

        String originalFilename = file.getOriginalFilename();
        String fileExtension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String uniqueFilename = UUID.randomUUID().toString() + fileExtension;

        Path targetUploadFile = uploadPath.resolve(uniqueFilename);
        Path targetOriginFile = originPath.resolve(uniqueFilename);

        try {
            file.transferTo(targetUploadFile);
            Files.copy(targetUploadFile, targetOriginFile);

            String relativeImageUrl = UPLOAD_DIR + "/" + datePath + "/" + uniqueFilename;

            ItemImage itemImage = ItemImage.builder()
                    .imageUrl(relativeImageUrl)
                    .build();
            item.addImage(itemImage);

            return toImageDTO(itemImageRepository.save(itemImage));

        } catch (IOException e) {
            throw new GeneralException(ErrorStatus.FILE_UPLOAD_FAILED);
        }
    }

    @Transactional
    public void deleteImageFromItem(int imageId) {
        ItemImage itemImage = itemImageRepository.findById(imageId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ITEM_NOT_FOUND));

        Path filePath = Paths.get(BASE_UPLOAD_PATH, itemImage.getImageUrl());
        Path originFilePath = Paths.get(BASE_UPLOAD_PATH, ORIGIN_DIR,
            itemImage.getImageUrl().substring(itemImage.getImageUrl()
                                                       .indexOf(UPLOAD_DIR) + UPLOAD_DIR.length() + 1));

        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        itemImage.getItem().removeImage(itemImage);
        itemImageRepository.delete(itemImage);
    }

    private ItemDTO toDTO(Item item) {
        return ItemDTO.builder()
                .itemName(item.getItemName())
                .price(item.getPrice())
                .stock(item.getStock())
                .build();
    }

    private ItemImageResponseDTO toImageDTO(ItemImage itemImage) {
        return ItemImageResponseDTO.builder()
                .imageId(itemImage.getImageId())
                .imageUrl(itemImage.getImageUrl())
                .itemId(itemImage.getItem().getItemId())
                .build();
    }

    private ItemDetailDTO toDetailDTO(Item item) {
        List<ItemImageResponseDTO> images = item.getItemImages().stream()
                                                .map(img -> ItemImageResponseDTO.builder()
                                                                                .imageId(img.getImageId())
                                                                                .imageUrl(img.getImageUrl())
                                                                                .itemId(item.getItemId())
                                                                                .build())
                                                .collect(Collectors.toList());

        return ItemDetailDTO.builder()
                            .itemName(item.getItemName())
                            .price(item.getPrice())
                            .stock(item.getStock())
                            .images(images)
                            .build();
    }

}
