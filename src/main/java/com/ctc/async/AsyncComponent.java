package com.ctc.async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import com.ctc.download.util.FileDownload;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

@Component
public class AsyncComponent {
	private static final Logger log = LoggerFactory.getLogger(AsyncComponent.class);


	/**
	 * CompletableFuture를 이용한 비동기 구현
	 * 
	 * @param url
	 * @param localFilename
	 * @return
	 */
	public CompletableFuture<String> asyncDownloadUrl(String url, String localFilename) {
		return CompletableFuture.supplyAsync(() -> {
			try {
				FileDownload.downloadWithJavaNIO(url, localFilename);
			} catch (IOException e) {
				throw new CompletionException(e);
			}
			return localFilename;
		}).exceptionally((ex) -> {
			log.error(ex.getMessage());
			return null;
		});
	}

	@Async
	public void asyncMethodWithVoidReturnType() {
		System.out.println("Execute method asynchronously. " + Thread.currentThread().getName());
	}

	@Async
	public Future<String> asyncMethodWithReturnType() {
		System.out.println("Execute method asynchronously " + Thread.currentThread().getName());
		try {
			Thread.sleep(5000);
			return new AsyncResult<>("hello world !!!!");
		} catch (final InterruptedException e) {

		}

		return null;
	}

	@Async("threadPoolTaskExecutor")
	public void asyncMethodWithConfiguredExecutor() {
		System.out.println("Execute method asynchronously with configured executor" + Thread.currentThread().getName());
	}

	@Async
	public void asyncMethodWithExceptions() throws Exception {
		throw new Exception("Throw message from asynchronous method. ");
	}

}
