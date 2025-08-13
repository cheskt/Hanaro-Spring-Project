package hanaro.stats.batch;

import hanaro.item.entity.Item;
import hanaro.item.repository.ItemRepository;
import hanaro.member.entity.Member;
import hanaro.member.repository.MemberRepository;
import hanaro.stats.repository.DailyItemStatRepository;
import hanaro.stats.repository.DailyTotalStatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@SpringBatchTest
class DailySaleBatchJobTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private DailyTotalStatRepository dailyTotalStatRepository;

    @Autowired
    private DailyItemStatRepository dailyItemStatRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        // Clear previous test data in the correct order to avoid constraint violations
        jdbcTemplate.execute("DELETE FROM DailyItemStat");
        jdbcTemplate.execute("DELETE FROM DailyTotalStat");
        jdbcTemplate.execute("DELETE FROM OrderItem");
        jdbcTemplate.execute("DELETE FROM Orders");
        jdbcTemplate.execute("DELETE FROM Item");
        jdbcTemplate.execute("DELETE FROM Member");
    }

    @Test
    @DisplayName("일일 판매 통계 배치 작업 실행 테스트")
    void dailySaleJob_runsSuccessfully() throws Exception {
        // given: Create test data
        Member testMember = memberRepository.save(Member.builder().email("test@test.com").password("password").build());
        Item testItem = itemRepository.save(Item.builder().itemName("Test Item").price(1000).stock(100).build());

        LocalDate yesterday = LocalDate.now().minusDays(1);
        jdbcTemplate.update(
            "INSERT INTO Orders (memberId, status, totalAmount, createdAt, updatedAt) VALUES (?, ?, ?, ?, ?)",
            testMember.getUserId(), "PAID", 1000, yesterday, yesterday
        );
        Integer orderId = jdbcTemplate.queryForObject("SELECT orderId FROM Orders WHERE memberId = ?", Integer.class, testMember.getUserId());

        jdbcTemplate.update(
            "INSERT INTO OrderItem (orderId, itemId, quantity, orderPrice) VALUES (?, ?, ?, ?)",
            orderId, testItem.getItemId(), 1, testItem.getPrice()
        );

        // when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        // then
        assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);

        // verify that stats were created for yesterday
        assertThat(dailyTotalStatRepository.count()).isEqualTo(1);
        assertThat(dailyItemStatRepository.count()).isEqualTo(1);

        assertThat(dailyTotalStatRepository.findAll().get(0).getStatDate()).isEqualTo(yesterday);
        assertThat(dailyItemStatRepository.findAll().get(0).getStatDate()).isEqualTo(yesterday);
    }
}