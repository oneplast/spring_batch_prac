package io.devground.spring_batch_prac.domain.base.genFile.entity;

import io.devground.spring_batch_prac.global.app.AppConfig;
import io.devground.spring_batch_prac.global.jpa.BaseTime;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
@Table(
	uniqueConstraints = @UniqueConstraint(
		columnNames = {
			"relId", "relTypeCode", "typeCode", "type2Code", "fileNo"
		}
	),
	indexes = {
		@Index(name = "GenFile_idx2", columnList = "relTypeCode, typeCode, type2Code")
	}
)
public class GenFile extends BaseTime {

	private String relTypeCode;
	private long relId;
	private String typeCode;
	private String type2Code;
	private String fileExtTypeCode;
	private String fileExtType2Code;
	private long fileSize;
	private long fileNo;
	private String fileExt;
	private String fileDir;
	private String originFileName;

	public String getFileName() {
		return getId() + "." + getFileExt();
	}

	public String getUrl() {
		return "/gen/" + getFileDir() + "/" + getFileName();
	}

	public String getDownloadUrl() {
		return "/domain/genFile/download/" + getId();
	}

	public String getFilePath() {
		return AppConfig.getGenFileDirPath() + "/" + getFileDir() + "/" + getFileName();
	}
}
