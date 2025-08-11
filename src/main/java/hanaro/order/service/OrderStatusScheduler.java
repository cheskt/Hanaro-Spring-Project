package hanaro.order.service;

import java.time.LocalDateTime;

import hanaro.order.entity.enums.Status;
import hanaro.order.repository.OrdersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderStatusScheduler {

	private final OrdersRepository ordersRepository;

	@Scheduled(fixedRate = 60_000)
	@Transactional
	public void advanceStatuses() {
		LocalDateTime now = LocalDateTime.now(java.time.ZoneId.of("Asia/Seoul"));

		ordersRepository.advanceStatus(Status.TRANSIT, Status.DELIVERED, now.minusMinutes(60), now);

		ordersRepository.advanceStatus(Status.READY,   Status.TRANSIT,  now.minusMinutes(15), now);

		ordersRepository.advanceStatus(Status.PAID,    Status.READY,    now.minusMinutes(5), now);

		// ordersRepository.advanceStatus(Status.TRANSIT, Status.DELIVERED, now.minusSeconds(30), now);
		//
		// ordersRepository.advanceStatus(Status.READY,   Status.TRANSIT,  now.minusSeconds(20), now);
		//
		// ordersRepository.advanceStatus(Status.PAID,    Status.READY,    now.minusSeconds(10), now);

	}
}
