package hanaro.config;

import hanaro.stats.service.DailySaleBatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class BatchConfig {

    private final DailySaleBatchService dailySaleBatchService;

    @Bean
    public Job dailySaleJob(JobRepository jobRepository, Step dailySaleStep) {
        return new JobBuilder("dailySaleJob", jobRepository)
                .start(dailySaleStep)
                .build();
    }

    @Bean
    public Step dailySaleStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("dailySaleStep", jobRepository)
                .tasklet(dailySaleTasklet(), transactionManager)
                .build();
    }

    @Bean
    public Tasklet dailySaleTasklet() {
        return (contribution, chunkContext) -> {
            dailySaleBatchService.runDaily();
            return RepeatStatus.FINISHED;
        };
    }
}
