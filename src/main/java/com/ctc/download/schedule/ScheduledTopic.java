package com.ctc.download.schedule;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.ctc.async.AsyncComponent;
@Service
public class ScheduledTopic {
	private static final Logger log = LoggerFactory.getLogger(ScheduledTopic.class);
	@Autowired
    private AsyncComponent asyncComponent;
	
	@Scheduled(fixedRate = 60000)
	public void downloadFile() {
		log.info("downloadFile schedule start");
		CompletableFuture<String> completableFuture = null;
		String url = "https://public.bybit.com/trading/BTCUSD/BTCUSD2019-10-01.csv.gz";
		completableFuture = asyncComponent.asyncDownloadUrl(url, "BTCUSD2019-10-01.csv.gz");
		
		completableFuture.thenAccept((filename) -> {
			
			if(filename == null) {
				log.info("file download failed : " + filename);
			} else {
				log.info("file download success : " + filename);
			}
		});
	}

}
