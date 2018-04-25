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

package com.linkedin.drelephant.exceptions;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.security.authentication.client.AuthenticatedURL;
import org.apache.hadoop.security.authentication.client.AuthenticationException;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;


/**
 *  Client to interact with job history server and get the mapreduce logs
 **/
public class MRClient {
  private static final Logger logger = Logger.getLogger(MRClient.class);
  private static final String HADOOP_CONF = "HadoopConf.xml";

  private String jhistoryAddr;
  private AuthenticatedURL.Token _token;
  private AuthenticatedURL _authenticatedURL;
  private final ForkJoinPool _executorPool;

  public MRClient() {
    _executorPool = new ForkJoinPool(3);
    _token = new AuthenticatedURL.Token();
    _authenticatedURL = new AuthenticatedURL();
    Configuration configuration = new Configuration();
    configuration.addResource(this.getClass().getClassLoader().getResourceAsStream(HADOOP_CONF));
    jhistoryAddr = configuration.get("mapreduce.jobhistory.webapp.address");
  }

  /**
   * For a given rest url, fetchs and return the jsonnode
   *
   * @param url rest job history server url
   * @return Json node to which the url points
   */
  private JsonNode fetchJson(final URL url)
      throws IOException {
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      HttpURLConnection conn = _authenticatedURL.openConnection(url, _token);
      return objectMapper.readTree(conn.getInputStream());
    } catch (AuthenticationException e) {
      logger.error(String.format("Cannot authenticate in Mr Client %s", e.getMessage()));
    } catch (IOException e) {
      logger.error(String.format("Error reading stream in Mr Client %s", e.getMessage()));
    }
    return null;
  }

  /**
   * Returns the diagnostics for a given MR Job Id
   * @param mrJobId  MR Job Id
   * @return Diagnostics in a string format
   */

  public String getMRJobLog(String mrJobId) {
    String mrJobHistoryURL = "http://" + jhistoryAddr + "/ws/v1/history/mapreduce/jobs/" + mrJobId;
    try {
      JsonNode response = _executorPool.submit(() -> fetchJson(new URL(mrJobHistoryURL))).get();
      if (response.get("job").get("state").toString() != "SUCCEEDED") {
        return response.get("job").get("diagnostics").getTextValue();
      }
    } catch (NullPointerException e) {
      logger.error(String.format("Invalid response %s", e.getMessage()));
    } catch (InterruptedException e) {
      logger.error(String.format("InterruptedException in Mr Client %s", e.getMessage()));
    } catch (ExecutionException e) {
      logger.error(String.format("ExecutionException in Mr Client %s", e.getMessage()));
    }
    return null;
  }

  /**
   * Returns the last task attempt diagnostic for a given failed taskId
   *
   * @param mrJobId   MR Job Id
   * @param mrTaskId  MRTask Id
   * @return Diagnostic in a string format
   */
  public String getMRTaskLog(String mrJobId, String mrTaskId) {
    String mrTaskHistoryURL =
        "http://" + jhistoryAddr + "/ws/v1/history/mapreduce/jobs/" + mrJobId + "/tasks/" + mrTaskId + "/attempts";
    ;
    try {
      JsonNode response = fetchJson(new URL(mrTaskHistoryURL));
      int attempts = response.get("taskAttempts").get("taskAttempt").size();
      int maxattempt = 0;
      int maxattemptid = 0;
      for (int i = 0; i < attempts; i++) {
        int attempt = Integer
            .parseInt(response.get("taskAttempts").get("taskAttempt").get(i).get("id").getTextValue().split("_")[5]);
        if (attempt > maxattempt) {
          maxattemptid = i;
          maxattempt = attempt;
        }
      }
      return response.get("taskAttempts").get("taskAttempt").get(maxattemptid).get("diagnostics").getTextValue();
    } catch (MalformedURLException e) {
      logger.error(e.toString());
    } catch (IOException e) {
      logger.error(e.toString());
    }
    return null;
  }
}
