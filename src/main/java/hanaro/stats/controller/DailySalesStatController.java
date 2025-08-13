package hanaro.stats.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hanaro.response.ApiResponse;
import hanaro.stats.dto.DailyItemStatDTO;
import hanaro.stats.dto.DailyTotalStatDTO;
import hanaro.stats.entity.DailyItemStat;
import hanaro.stats.entity.DailyTotalStat;
import hanaro.stats.repository.DailyItemStatRepository;
import hanaro.stats.repository.DailyTotalStatRepository;
import hanaro.stats.service.DailySaleBatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/stats")
@RequiredArgsConstructor
@Tag(name = "매출 통계")
@SecurityRequirement(name = "bearerAuth")
public class DailySalesStatController {

	private final DailyItemStatRepository dailyItemStatRepository;
	private final DailyTotalStatRepository dailyTotalStatRepository;
	private final DailySaleBatchService dailySaleBatchService;

	@Operation(summary = "[관리자] 일일 상품별 매출 통계 조회", description = "일일 상품별 매출 통계를 조회")
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/items")
	public ResponseEntity<ApiResponse<List<DailyItemStatDTO>>> getDailyItemStats(
		@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

		List<DailyItemStatDTO> result = dailyItemStatRepository.findAllByStatDate(date)
															   .stream().map(this::toDTO).toList();

		return ResponseEntity.ok(ApiResponse.onSuccess(result, "일일 상품별 통계 조회 성공"));
	}

	@Operation(summary = "[관리자] 일일 총 매출 통계 조회", description = "일일 총 매출 통계를 조회")
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/total")
	public ResponseEntity<ApiResponse<DailyTotalStatDTO>> getDailyTotalStat(
		@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

		dailySaleBatchService.runForDate(date);

		DailyTotalStat stat = dailyTotalStatRepository.findByStatDate(date)
													  .orElse(DailyTotalStat.builder()
																			.statId(0).statDate(date).orderCount(0).quantity(0).amt(0).build());

		return ResponseEntity.ok(ApiResponse.onSuccess(toDTO(stat), "일일 총 매출 통계 조회 성공"));
	}

	private DailyItemStatDTO toDTO(DailyItemStat s) {
		return DailyItemStatDTO.builder()
							   .statId(s.getStatId())
							   .statDate(s.getStatDate())
							   .itemId(s.getItemId())
							   .orderCount(s.getOrderCount())
							   .quantity(s.getQuantity())
							   .amt(s.getAmt())
							   .build();
	}

	private DailyTotalStatDTO toDTO(DailyTotalStat s) {
		return DailyTotalStatDTO.builder()
								.statId(s.getStatId())
								.statDate(s.getStatDate())
								.orderCount(s.getOrderCount())
								.quantity(s.getQuantity())
								.amt(s.getAmt())
								.build();
	}

}
