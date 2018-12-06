package com.zhilutec.kafka;

import com.zhilutec.services.IKafkaService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;


/**
 * 1 spring.kafka.consumer.concurrency配置为1单线从kafka server poll数据
 * 2 获取数据后马上保存到队列并提交kafka偏移量
 * 3 启用多个线程多队列中获取保存数据库，线程数量在配置文件中配置
 */
public class KafkaConsumerBatchListener {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private IKafkaService kafkaService;

    private BlockingQueue blockingQueue;

    @Value("${uwb.queue.thread.number}")
    private Integer queueThreadNumber;

    private ExecutorService executorService;

    /**
     * 批量处理
     */
    @KafkaListener(topics = "${spring.kafka.consumer.topic}", containerFactory = "kafkaListenerContainerFactory")
    public void listener(List<ConsumerRecord<String, String>> records, Acknowledgment ack) {
        logger.info("---------批量Poll到的数组大小:" + records.size() + "-------");
        for (ConsumerRecord<?, ?> record : records) {
            logger.info("==value:{}", record.value());
        }
        try {
            this.blockingQueue.put(records);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ack.acknowledge();//手动提交偏移量
        }
    }

    public KafkaConsumerBatchListener() {
    }

    //初始化队列和线程池
    @PostConstruct
    public void init() {
        this.blockingQueue = new LinkedBlockingDeque();
        this.createExecutors();
    }

    //创建线程从队列中获取数据
    class SaveRunnable implements Runnable {

        private final BlockingQueue<List<ConsumerRecord<String, String>>> queue;

        public SaveRunnable(BlockingQueue q) {
            this.queue = q;
        }
        public void run() {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    kafkaService.batchSave(queue.take());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //创建线程池
    public void createExecutors() {
        if (queueThreadNumber == null || queueThreadNumber <= 0) {
            queueThreadNumber = 1;
        }
        //1、创建线程池对象，控制要创建几个线程对象
        executorService = Executors.newFixedThreadPool(queueThreadNumber);
        for (int j = 1; j <= queueThreadNumber; j++) {
            SaveRunnable saveRunnable = new SaveRunnable(blockingQueue);
            Thread saveThr = new Thread(saveRunnable);
            saveThr.setName("status-thr-"+j);
            //可以执行Runnable对象或者Callable对象代表的线程
            executorService.submit(saveThr);
        }

    }

}