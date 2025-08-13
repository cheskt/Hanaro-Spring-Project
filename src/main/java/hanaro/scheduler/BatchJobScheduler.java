package hanaro.scheduler;

import hanaro.response.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BatchJobScheduler {

    private final JobLauncher jobLauncher;
    private final Job dailySaleJob;

    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    public void runDailySaleJob() {
        try {
            JobParameters params = new JobParametersBuilder()
                .addLong("ts", System.currentTimeMillis())
                .toJobParameters();

            jobLauncher.run(dailySaleJob, params);
            log.info("[BATCH] DailySaleJob started. params={}", params);

        } catch (JobExecutionAlreadyRunningException e) {
            log.warn("[BATCH] {} {} : {}",
                ErrorStatus.BATCH_ALREADY_RUNNING.getCode(),
                ErrorStatus.BATCH_ALREADY_RUNNING.getMessage(), e.toString());

        } catch (JobInstanceAlreadyCompleteException e) {
            log.info("[BATCH] {} {} : {}",
                ErrorStatus.BATCH_INSTANCE_COMPLETE.getCode(),
                ErrorStatus.BATCH_INSTANCE_COMPLETE.getMessage(), e.toString());

        } catch (JobParametersInvalidException e) {
            log.error("[BATCH] {} {} : {}",
                ErrorStatus.BATCH_INVALID_PARAM.getCode(),
                ErrorStatus.BATCH_INVALID_PARAM.getMessage(), e.getMessage(), e);

        } catch (JobRestartException e) {
            log.error("[BATCH] {} {} : {}",
                ErrorStatus.BATCH_RESTART_FAILED.getCode(),
                ErrorStatus.BATCH_RESTART_FAILED.getMessage(), e.getMessage(), e);

        } catch (Exception e) {
            // 예기치 못한 모든 오류
            log.error("[BATCH] {} {} : {}",
                ErrorStatus.BATCH_LAUNCH_FAILED.getCode(),
                ErrorStatus.BATCH_LAUNCH_FAILED.getMessage(), e.getMessage(), e);
        }
    }
}

