package com.linkedin.drelephant.executors;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.linkedin.drelephant.ElephantRunner;
import com.linkedin.drelephant.analysis.AnalyticJob;
import controllers.MetricsController;
import org.apache.log4j.Logger;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolExecutorService extends ThreadPoolExecutor implements IExecutorService {

    private static final Logger logger = Logger.getLogger(ThreadPoolExecutorService.class);

    public ThreadPoolExecutorService() {

        super(ElephantRunner.getInstance().getExecutorNum(), ElephantRunner.getInstance().getExecutorNum(), 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(), new ThreadFactoryBuilder().setNameFormat("dr-el-executor-thread-%d").build());
    }

    @Override
    public void execute(AnalyticJob analyticJob) {

        submit(new ExecutorJob(analyticJob));
    }

    @Override
    public void startService() {

        long checkPoint = 0L;
        ElephantRunner elephantRunner = ElephantRunner.getInstance();

        while (elephantRunner.getRunningStatus().get() && !Thread.currentThread().isInterrupted()) {

            elephantRunner.getAnalyticJobGenerator().fetchAndExecuteJobs(checkPoint);
            checkPoint = elephantRunner.getAnalyticJobGenerator().getCheckPoint();

            int queueSize = getQueue().size();
            MetricsController.setQueueSize(queueSize);
            logger.info("Job queue size is " + queueSize);

            elephantRunner.getAnalyticJobGenerator().getWaitInterval(elephantRunner.getFetchInterval());
        }
    }

    @Override
    public void stopService() {
        shutdownNow();
    }

    private class ExecutorJob implements Runnable {

        private AnalyticJob _analyticJob;

        public ExecutorJob(AnalyticJob analyticJob) {
            _analyticJob = analyticJob;
        }

        @Override
        public void run() {
            ElephantRunner.getInstance().getAnalyticJobGenerator().analyseJob(_analyticJob);
        }
    }
}