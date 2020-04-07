/*
 * Copyright 2016 LinkedIn Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.linkedin.drelephant;

import com.linkedin.drelephant.analysis.AnalyticJobGenerator;
import com.linkedin.drelephant.analysis.AnalyticJobGeneratorHadoop2;
import com.linkedin.drelephant.analysis.HDFSContext;
import com.linkedin.drelephant.analysis.HadoopSystemContext;
import com.linkedin.drelephant.executors.IExecutorService;
import com.linkedin.drelephant.security.HadoopSecurity;
import com.linkedin.drelephant.util.Utils;
import controllers.MetricsController;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.log4j.Logger;

import java.security.PrivilegedAction;


/**
 * The class that runs the Dr. Elephant daemon
 */
public class ElephantRunner implements Runnable {
  private static final Logger logger = Logger.getLogger(ElephantRunner.class);

  private static final long FETCH_INTERVAL = 60 * 1000;     // Interval between fetches
  private static final long RETRY_INTERVAL = 60 * 1000;     // Interval between retries

  private static final String FETCH_INTERVAL_KEY = "drelephant.analysis.fetch.interval";
  private static final String RETRY_INTERVAL_KEY = "drelephant.analysis.retry.interval";
  private static final String EXECUTOR_SERVICE = "drelephant.executor.service.class.name";
  private static final String HADOOP_CONF = "HadoopConf.xml";

  private long _fetchInterval;
  private long _retryInterval;
  private HadoopSecurity _hadoopSecurity;
  private AnalyticJobGenerator _analyticJobGenerator;
  private IExecutorService _executorService;

  private static ElephantRunner _elephantRunner;

  private ElephantRunner() {
  }

  public static ElephantRunner getInstance() {
    if (_elephantRunner == null) {
      _elephantRunner = new ElephantRunner();
    }
    return _elephantRunner;
  }

  public long getFetchInterval() {
    return _fetchInterval;
  }

  public long getRetryInterval() {
    return _retryInterval;
  }

  public HadoopSecurity getHadoopSecurity() {
    return _hadoopSecurity;
  }

  public AnalyticJobGenerator getAnalyticJobGenerator() {
    return _analyticJobGenerator;
  }

  public IExecutorService getExecutorService() {
    return _executorService;
  }

  private void loadGeneralConfiguration() {
    Configuration configuration = ElephantContext.instance().getGeneralConf();

    _fetchInterval = Utils.getNonNegativeLong(configuration, FETCH_INTERVAL_KEY, FETCH_INTERVAL);
    _retryInterval = Utils.getNonNegativeLong(configuration, RETRY_INTERVAL_KEY, RETRY_INTERVAL);
  }

  private void loadAnalyticJobGenerator() {
    if (HadoopSystemContext.instance().isHadoop2Env()) {
      _analyticJobGenerator = new AnalyticJobGeneratorHadoop2();
    } else {
      throw new RuntimeException("Unsupported Hadoop major version detected. It is not 2.x.");
    }

    try {
      Configuration configuration = new Configuration();
      configuration.addResource(this.getClass().getClassLoader().getResourceAsStream(HADOOP_CONF));
      _analyticJobGenerator.configure(configuration);
    } catch (Exception e) {
      logger.error("Error occurred when configuring the analysis provider.", e);
      throw new RuntimeException(e);
    }
  }

  private void loadExecutorService() {

    Configuration configuration = ElephantContext.instance().getGeneralConf();
    String service = configuration.get(EXECUTOR_SERVICE);
    try {
      _executorService = (IExecutorService) Class.forName(service).newInstance();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void run() {
    logger.info("Dr.elephant has started");
    try {
      _hadoopSecurity = HadoopSecurity.getInstance();
      _hadoopSecurity.doAs(new PrivilegedAction<Void>() {
        @Override
        public Void run() {
          HDFSContext.load();
          loadGeneralConfiguration();
          loadAnalyticJobGenerator();
          loadExecutorService();
          ElephantContext.init();

          // Initialize the metrics registries.
          MetricsController.init();

          _executorService.startService();

          logger.info("Main thread is terminated.");
          return null;
        }
      });
    } catch (Exception e) {
      logger.error(e.getMessage());
      logger.error(ExceptionUtils.getStackTrace(e));
    }
  }

  public void kill() {
    _executorService.stopService();
  }
}
