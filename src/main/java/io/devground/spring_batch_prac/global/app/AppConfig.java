package io.devground.spring_batch_prac.global.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.EntityManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

	private static String activeProfile;

	@Getter
	private static EntityManager entityManager;

	@Getter
	private static ObjectMapper objectMapper;

	@Getter
	private static String siteName;

	@Getter
	private static String tempDirPath;
	@Getter
	private static String genFileDirPath;

	@Getter
	private static String tossPaymentsWidgetSecretKey;
	@Getter
	private static String tossPaymentsTargetUrl;

	@Getter
	private static double rebateRate;

	@Getter
	private static int orderCancelableSeconds;

	@Value("${spring.profiles.active}")
	public void setActiveProfile(String value) {
		AppConfig.activeProfile = value;
	}

	@Value("${custom.site.name}")
	public void setSiteName(String siteName) {
		AppConfig.siteName = siteName;
	}

	@Value("${custom.temp.dirPath}")
	public void setTempDirPath(String tempDirPath) {
		AppConfig.tempDirPath = tempDirPath;
	}

	@Value("${custom.genFile.dirPath}")
	public void setGenFileDirPath(String genFileDirPath) {
		AppConfig.genFileDirPath = genFileDirPath;
	}

	@Value("${custom.toss-payments.widget.secret-key}")
	public void setTossPaymentsWidgetSecretKey(String tossPaymentsWidgetSecretKey) {
		AppConfig.tossPaymentsWidgetSecretKey = tossPaymentsWidgetSecretKey;
	}

	@Value("${custom.toss-payments.targetUrl}")
	public void setTossPaymentsTargetUrl(String tossPaymentsTargetUrl) {
		AppConfig.tossPaymentsTargetUrl = tossPaymentsTargetUrl;
	}

	@Value("${custom.rebate.rate}")
	public void setRebateRate(double rebateRate) {
		AppConfig.rebateRate = rebateRate;
	}

	@Value("${custom.order.cancelable-seconds}")
	public void setOrderCancelableSeconds(int orderCancelableSeconds) {
		AppConfig.orderCancelableSeconds = orderCancelableSeconds;
	}

	@Autowired
	public void setEntityManager(EntityManager entityManager) {
		AppConfig.entityManager = entityManager;
	}

	@Autowired
	public void setObjectMapper(ObjectMapper objectMapper) {
		AppConfig.objectMapper = objectMapper;
	}

	public static boolean isNotProd() {
		return !isProd();
	}

	public static boolean isProd() {
		return activeProfile.equals("prod");
	}
}
