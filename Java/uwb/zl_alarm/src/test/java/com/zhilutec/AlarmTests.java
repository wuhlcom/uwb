package com.zhilutec;


import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AlarmTests {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	// @Autowired
	// private AsyncTask asyncTask;
    //
	// @Test
	// public void AsyncTaskTest() throws InterruptedException, ExecutionException {
	// 	for (int i = 0; i < 100; i++) {
	// 		asyncTask.doTask1(i);
	// 	}
	// 	logger.info("All tasks finished.");
	// }

}
