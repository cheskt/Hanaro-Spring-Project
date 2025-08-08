package hanaro.util;

import java.time.LocalDateTime;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE #{tableName} SET deleted_at = NOW() WHERE id = ?")
@Where(clause = "deleted_at is null")
public class BaseTime {
	@CreatedDate
	@Column(updatable = false, columnDefinition = "DATETIME(0)")
	private LocalDateTime createdAt;

	@LastModifiedDate
	@Column(columnDefinition = "DATETIME(0)")
	private LocalDateTime updatedAt;

	private LocalDateTime deletedAt;
}
