package hanaro.item.controller;

import hanaro.item.dto.ItemDTO;
import hanaro.item.service.ItemService;
import hanaro.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/item")
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ItemService itemService;

    @Operation(summary = "상품 등록", description = "상품을 추가합니다")
    @PostMapping
    public ResponseEntity<ApiResponse<ItemDTO>> addItem(@RequestBody ItemDTO requestDTO) {
        ItemDTO registeredItem = itemService.addItem(requestDTO);
        return ResponseEntity.ok(ApiResponse.onSuccess(registeredItem, "상품 추가 완료"));
    }

    @Operation(summary = "상품 리스트 조회", description = "상품 리스트를 조회합니다")
    @GetMapping
    public ResponseEntity<ApiResponse<List<ItemDTO>>> getAllItems() {
        List<ItemDTO> items = itemService.getAllItems();
        return ResponseEntity.ok(ApiResponse.onSuccess(items, "상품 리스트"));
    }

    @Operation(summary = "상품 상세 조회", description = "상품 상세 조회합니다")
    @GetMapping("/{itemId}")
    public ResponseEntity<ApiResponse<ItemDTO>> getItemById(@PathVariable int itemId) {
        ItemDTO item = itemService.getItemById(itemId);
        return ResponseEntity.ok(ApiResponse.onSuccess(item, "상품 조회 완료"));
    }

    @Operation(summary = "상품 수정", description = "상품을 수정합니다")
    @PatchMapping("/{itemId}")
    public ResponseEntity<ApiResponse<ItemDTO>> updateItem(@PathVariable int itemId, @RequestBody ItemDTO requestDTO) {
        ItemDTO updatedItem = itemService.updateItem(itemId, requestDTO);
        return ResponseEntity.ok(ApiResponse.onSuccess(updatedItem, "상품 수정 완료"));
    }

    @Operation(summary = "상품 삭제", description = "상품을 삭제합니다")
    @DeleteMapping("/{itemId}")
    public ResponseEntity<ApiResponse<Void>> deleteItem(@PathVariable int itemId) {
        itemService.deleteItem(itemId);
        return ResponseEntity.ok(ApiResponse.onSuccess(null, "상품 삭제 완료"));
    }
}
