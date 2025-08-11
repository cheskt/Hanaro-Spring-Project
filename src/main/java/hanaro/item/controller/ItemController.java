package hanaro.item.controller;

import hanaro.item.dto.ItemDTO;
import hanaro.item.dto.ItemDetailDTO;
import hanaro.item.dto.ItemImageResponseDTO;
import hanaro.item.service.ItemService;
import hanaro.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/item")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "상품")
public class ItemController {

    private final ItemService itemService;

    @Operation(summary = "상품 리스트", description = "상품 목록을 조회합니다")
    @PreAuthorize("hasAnyRole('ADMIN','MEMBER')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<ItemDTO>>> getAllItems() {
        List<ItemDTO> items = itemService.getAllItems();
        return ResponseEntity.ok(ApiResponse.onSuccess(items, "상품 리스트"));
    }

    @Operation(summary = "상품 조회", description = "상품을 조회합니다")
    @PreAuthorize("hasAnyRole('ADMIN','MEMBER')")
    @GetMapping("/{itemId}")
    public ResponseEntity<ApiResponse<ItemDetailDTO>> getItemById(@PathVariable int itemId) {
        ItemDetailDTO item = itemService.getItemDetail(itemId);
        return ResponseEntity.ok(ApiResponse.onSuccess(item, "상품 조회 완료"));
    }

    @Operation(summary = "상품 추가", description = "상품을 추가합니다")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<ApiResponse<ItemDTO>> addItem(@RequestBody ItemDTO requestDTO) {
        ItemDTO registeredItem = itemService.addItem(requestDTO);
        return ResponseEntity.ok(ApiResponse.onSuccess(registeredItem, "상품 추가 완료"));
    }

    @Operation(summary = "상품 수정", description = "상품을 수정합니다")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{itemId}/update")
    public ResponseEntity<ApiResponse<ItemDTO>> updateItem(@PathVariable int itemId, @RequestBody ItemDTO requestDTO) {
        ItemDTO updatedItem = itemService.updateItem(itemId, requestDTO);
        return ResponseEntity.ok(ApiResponse.onSuccess(updatedItem, "상품 수정 완료"));
    }

    @Operation(summary = "상품 삭제", description = "상품을 삭제합니다")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{itemId}/delete")
    public ResponseEntity<ApiResponse<Void>> deleteItem(@PathVariable int itemId) {
        itemService.deleteItem(itemId);
        return ResponseEntity.ok(ApiResponse.onSuccess(null, "상품 삭제 완료"));
    }

    @Operation(summary = "상품 이미지 추가", description = "상품 이미지를 추가합니다")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/{itemId}/addimage", consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<ItemImageResponseDTO>> addImageToItem(
        @PathVariable int itemId,
        @RequestPart("file") MultipartFile file) {
        ItemImageResponseDTO itemImage = itemService.addImageToItem(itemId, file);
        return ResponseEntity.ok(ApiResponse.onSuccess(itemImage, "상품 이미지 등록 완료"));
    }

    @Operation(summary = "상품 이미지 삭제", description = "상품 이미지를 삭제합니다")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{imageId}/deleteimage")
    public ResponseEntity<ApiResponse<Void>> deleteImageFromItem(@PathVariable int imageId) {
        itemService.deleteImageFromItem(imageId);
        return ResponseEntity.ok(ApiResponse.onSuccess(null, "상품 이미지 삭제 완료"));
    }
}
