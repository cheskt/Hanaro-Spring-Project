package hanaro.stats.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hanaro.stats.repository.DailyItemStatRepository;
import hanaro.stats.repository.DailyTotalStatRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DailySaleBatchService {

	private final DailyItemStatRepository dailyItemStatRepository;
	private final DailyTotalStatRepository dailyTotalStatRepository;

	@Transactional
	public void runDaily() {
		runFor(LocalDate.now(ZoneId.of("Asia/Seoul")).minusDays(1));
	}

	@Transactional
	public void runFor(LocalDate targetDate) {
		LocalDateTime start = targetDate.atStartOfDay();
		LocalDateTime end   = start.plusDays(1);
		LocalDateTime now   = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

		dailyItemStatRepository.upsertDailyItemStat(targetDate, start, end, now);
		dailyTotalStatRepository.upsertDailyTotalStat(targetDate, start, end, now);
	}
}

