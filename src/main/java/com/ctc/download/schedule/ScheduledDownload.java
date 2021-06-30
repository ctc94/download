package com.ctc.download.schedule;

import java.io.File;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.ctc.async.AsyncComponent;
import com.ctc.download.util.DateUtil;
import com.ctc.download.util.GzipReader;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ScheduledDownload {
	private static final Logger log = LoggerFactory.getLogger(ScheduledDownload.class);
	@Autowired
	private AsyncComponent asyncComponent;

	@Scheduled(fixedRate = 36000000)
	public void downloadFile() throws InterruptedException {
		log.info("downloadFile schedule start");
		String endDate = DateUtil.AddDate(DateUtil.now("yyyy-MM-dd"), 0, 0, -1, "yyyy-MM-dd");
		String startDate = "2019-09-30";

		log.info(endDate);

		while (!endDate.equals(startDate)) {

			startDate = DateUtil.AddDate(startDate, 0, 0, 1, "yyyy-MM-dd");

			String url = "https://public.bybit.com/spot_index/BTCUSD/BTCUSD" + startDate + "_index_price.csv.gz";
			String localFilename = "./backup/spot_index/BTCUSD/BTCUSD" + startDate + "_index_price.csv.gz";
			if (new File(localFilename).exists()) {
				continue;
			}

			asyncComponent.asyncDownloadUrl(url, localFilename).thenAccept((filename) -> {

				if (filename == null) {
					log.info("file download failed : " + filename);
				} else {
					log.info("file download success : " + filename);
					ObjectMapper mapper = new ObjectMapper();
					int[] line = { 0 };
					GzipReader.readGzip_BufferedReader(filename).forEach(m -> {
						log.info(++line[0] + ":" + m.toString());
					});
				}

			}).exceptionally((ex) -> {// thenAccept 에러 처리
				log.error(ex.getMessage());
				return null;
			});
		}

		log.info("downloadFile schedule end");

	}

}
