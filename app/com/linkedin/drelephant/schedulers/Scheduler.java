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

package com.linkedin.drelephant.schedulers;

import java.util.Properties;

/**
 * Scheduler interface defining the
 */
public abstract class Scheduler {

  public static final String ORG_NAME = "job.billing.org";
  public static final String SUB_ORG_NAME = "job.org";
  public static final String NAMESPACE_NAME = "job.namespace";

  private final String organization;
  private final String subOrganization;

  protected Scheduler(Properties properties){
    organization = properties.getProperty(ORG_NAME);
    subOrganization = properties.getProperty(SUB_ORG_NAME) + "." + properties.getProperty(NAMESPACE_NAME);
  }

  /**
   * Return the name of the org
   *
   * @return the org name
   */
  public String getOrganization(){
    return organization;
  }

  /**
   * Return the name of the sub-org
   *
   * @return the sub-org name
   */
  public String getSubOrganization(){
    return subOrganization;
  }

  /**
   * Return the Scheduler Name
   *
   * @return the scheduler name
   */
  abstract public String getSchedulerName();

  /**
   * True if the the scheduler object was not able to parse the given properties
   *
   * @return true the scheduler is empty
   */
  abstract public boolean isEmpty();

  /**
   * Return the Job Definition Id of the job in the workflow
   *
   * @return the job definition id
   */
  abstract public String getJobDefId();

  /**
   * Return the Job Execution Id of the job in the workflow
   *
   * @return the job execution id
   */
  abstract public String getJobExecId();

  /**
   * Return the Flow Definition Id of the workflow
   *
   * @return the flow definition id
   */
  abstract public String getFlowDefId();

  /**
   * Return the Flow Execution Id of the workflow
   *
   * @return the flow execution id
   */
  abstract public String getFlowExecId();

  /**
   * Return a link to the job's definition
   *
   * @return the job definition url
   */
  abstract public String getJobDefUrl();

  /**
   * Return a link to the job's execution
   *
   * @return the job execution url
   */
  abstract public String getJobExecUrl();

  /**
   * Return a link to the flow's definition
   *
   * @return the flow definition url
   */
  abstract public String getFlowDefUrl();

  /**
   * Return a link to the flow's execution
   *
   * @return the flow execution url
   */
  abstract public String getFlowExecUrl();

  /**
   * Return the name of the Job/Action in the Flow
   *
   * @return the job/action name
   */
  abstract public String getJobName();

  /**
   * Return the workflow depth
   *
   * @return the workflow depth
   */
  abstract public int getWorkflowDepth();
}
