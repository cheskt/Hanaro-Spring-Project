package hanaro.stats.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hanaro.stats.repository.DailyItemStatRepository;
import hanaro.stats.repository.DailyTotalStatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
public class DailySaleBatchService {

	private final DailyItemStatRepository dailyItemStatRepository;
	private final DailyTotalStatRepository dailyTotalStatRepository;

	private static final Logger log =
		LoggerFactory.getLogger(DailySaleBatchService.class);

	private static final ZoneId KST = ZoneId.of("Asia/Seoul");

	@Transactional
	public int runForDate(LocalDate statDate) {
		LocalDateTime start = statDate.atStartOfDay();
		LocalDateTime end   = statDate.plusDays(1).atStartOfDay();
		LocalDateTime now   = LocalDateTime.now(KST);
		int updated = dailyTotalStatRepository.upsertDailyTotalStat(statDate, start, end, now);
		log.info("[STATS][DAILY] statDate={}, updated={}", statDate, updated);
		return updated;
	}

	@Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
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

