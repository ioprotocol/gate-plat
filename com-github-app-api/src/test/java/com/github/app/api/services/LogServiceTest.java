package com.github.app.api.services;

import com.github.app.api.dao.domain.Log;
import com.github.app.api.dao.domain.LogTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class LogServiceTest extends BaseServiceTest {

	@Autowired
	private LogService logService;

	 @Test
	public void testTruncate() {
		logService.truncate();
	}

//	@Test
	public void testInsert60wan() {
		long id = System.currentTimeMillis();
		id = id - 60 * 10000 * 1000;

		List<Log> list = new ArrayList<>();

		for (int i = 0; i < 60 * 10000; i++) {
			id = id + i * 1000;
			Log log = LogTest.create();
			log.setLogId(id);
			list.add(log);

			if (list.size() > 20) {
				logService.addLog(list);
				list.clear();
			}
		}
	}

//	@Test
	public void testSelect() {
		long startTime = System.currentTimeMillis() - 31 * 10000 * 1000;
		long endTime = System.currentTimeMillis() - 30 * 10000 * 1000;
		long now = System.currentTimeMillis();
		List<Log> list = logService.find(null, null, startTime, endTime, 0, 20);
		long count = logService.count(null, null, startTime, endTime);

		long finishTime = System.currentTimeMillis();
		System.out.println(finishTime - now);
	}
}