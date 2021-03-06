package com.benefitj.scaffold.quartz.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.benefitj.spring.quartz.JobType;
import com.benefitj.spring.quartz.QuartzJobTask;
import com.benefitj.spring.quartz.TriggerType;
import com.benefitj.spring.quartz.WorkerType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "quartz_job_task")
public class QuartzJobTaskEntity implements QuartzJobTask {

  /**
   * 任务id
   */
  @Id
  @Column(name = "id", columnDefinition = "varchar(32) comment '调度任务的ID'", length = 32)
  private String id;
  /**
   * group名称
   */
  @Column(name = "job_group", columnDefinition = "varchar(50) comment '任务分组'", length = 50)
  private String jobGroup;
  /**
   * Job名称
   */
  @Column(name = "job_name", columnDefinition = "varchar(50) comment '任务名称'", length = 50)
  private String jobName;
  /**
   * 任务别名
   */
  @Column(name = "job_alias", columnDefinition = "varchar(50) comment '任务别名'", length = 50)
  private String jobAlias;
  /**
   * 描述
   */
  @Column(name = "description", columnDefinition = "varchar(1024) comment '任务描述'", length = 1024)
  private String description;
  /**
   * 是否异步
   */
  @Column(name = "async", columnDefinition = "tinyint(1) comment '是否异步执行程序' DEFAULT 0", length = 1)
  private Boolean async;
  /**
   * 是否不恢复
   */
  @Column(name = "recovery", columnDefinition = "tinyint(1) comment '是否恢复' DEFAULT 0", length = 1)
  private Boolean recovery;
  /**
   * 执行后是否持久化数据，默认不持久化
   */
  @Column(name = "persistent", columnDefinition = "tinyint(1) comment '执行后是否持久化数据，默认不持久化' DEFAULT 0", length = 1)
  private Boolean persistent;
  /**
   * 是否不允许并发执行，默认并发执行
   */
  @Column(name = "disallow_concurrent", columnDefinition = "tinyint(1) comment '是否不允许并发执行，默认并发执行' DEFAULT 1", length = 1)
  private Boolean disallowConcurrent;
  /**
   * 创建时间
   */
  @JSONField(serialize = false)
  @JsonIgnore
  @Column(name = "create_time", columnDefinition = "datetime comment '创建时间'")
  private Date createTime;
  /**
   * 更新时间
   */
  @JSONField(serialize = false)
  @JsonIgnore
  @Column(name = "update_time", columnDefinition = "datetime comment '更新时间'")
  private Date updateTime;
  /**
   * Job的执行类型，参考: {@link JobType }
   */
  @Column(name = "job_type", columnDefinition = "varchar(50) comment 'Job的执行类型'", length = 50)
  private String jobType;
  /**
   * JobWorker的实现
   */
  @Column(name = "worker", columnDefinition = "varchar(50) comment 'JobWorker的实现类'", length = 50)
  private String worker;
  /**
   * jobWorker的类型，参考：{@link WorkerType}
   */
  @Column(name = "worker_type", columnDefinition = "varchar(50) comment 'jobWorker的类型'", length = 50)
  private String workerType;
  /**
   * Job携带的数据
   */
  @Column(name = "job_data", columnDefinition = "varchar(1024) comment 'Job携带的数据'", length = 1024)
  private String jobData;
  /**
   * 触发器组
   */
  @Column(name = "trigger_group", columnDefinition = "varchar(50) comment '触发器组'", length = 50)
  private String triggerGroup;
  /**
   * 触发器名称
   */
  @Column(name = "trigger_name", columnDefinition = "varchar(50) comment '触发器名称'", length = 50)
  private String triggerName;
  /**
   * 触发器的优先级
   */
  @Column(name = "priority", columnDefinition = "integer comment '触发器的优先级' DEFAULT 50")
  private Integer priority;
  /**
   * 开始执行的时间
   */
  @Column(name = "start_at", columnDefinition = "bigint comment '开始执行的时间'")
  private Long startAt;
  /**
   * 结束执行的时间
   */
  @Column(name = "end_at", columnDefinition = "bigint comment '结束执行的时间'")
  private Long endAt;
  /**
   * Calendar
   */
  @Column(name = "calendar_name", columnDefinition = "varchar(50) comment 'Calendar Name'", length = 50)
  private String calendarName;
  /**
   * 失效后的策略
   */
  @Column(name = "misfire_policy", columnDefinition = "integer comment '失效后的策略'")
  private Integer misfirePolicy;
  /**
   * 触发器类型: {@link TriggerType#SIMPLE}, {@link TriggerType#CRON}
   */
  @Column(name = "trigger_type", columnDefinition = "varchar(30) comment 'Cron表达式'", length = 30)
  private String triggerType;
  /**
   * 每次执行的间隔
   */
  @Column(name = "simple_interval", columnDefinition = "bigint comment '每次执行的间隔'")
  private Long simpleInterval;
  /**
   * 重复次数
   */
  @Column(name = "simple_repeat_count", columnDefinition = "integer comment '重复次数'")
  private Integer simpleRepeatCount;
  /**
   * Cron表达式
   */
  @Column(name = "cron_expression", columnDefinition = "varchar(50) comment 'Cron表达式'", length = 50)
  private String cronExpression;
  /**
   * 可用状态
   */
  @Column(name = "active", columnDefinition = "tinyint(1) NOT NULL DEFAULT 1 comment '可用状态'", length = 1)
  private Boolean active;
  /**
   * 机构ID
   */
  @Column(name = "org_id", columnDefinition = "varchar(32) comment '拥有者'", length = 32)
  private String orgId;
  /**
   * 拥有者
   */
  @Column(name = "owner", columnDefinition = "varchar(32) comment '拥有者'", length = 32)
  private String owner;
  /**
   * 拥有者类型
   */
  @Column(name = "owner_type", columnDefinition = "varchar(30) comment '拥有者类型'", length = 30)
  private String ownerType;

  @Override
  public String getId() {
    return id;
  }

  @Override
  public void setId(String id) {
    this.id = id;
  }

  @Override
  public String getJobGroup() {
    return jobGroup;
  }

  @Override
  public void setJobGroup(String jobGroup) {
    this.jobGroup = jobGroup;
  }

  @Override
  public String getJobName() {
    return jobName;
  }

  @Override
  public void setJobName(String jobName) {
    this.jobName = jobName;
  }

  @Override
  public String getJobAlias() {
    return jobAlias;
  }

  @Override
  public void setJobAlias(String jobAlias) {
    this.jobAlias = jobAlias;
  }

  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public Boolean getAsync() {
    return async;
  }

  @Override
  public void setAsync(Boolean async) {
    this.async = async;
  }

  @Override
  public Boolean getRecovery() {
    return recovery;
  }

  @Override
  public void setRecovery(Boolean recovery) {
    this.recovery = recovery;
  }

  @Override
  public Boolean getPersistent() {
    return persistent;
  }

  @Override
  public void setPersistent(Boolean persistent) {
    this.persistent = persistent;
  }

  @Override
  public Boolean getDisallowConcurrent() {
    return disallowConcurrent;
  }

  @Override
  public void setDisallowConcurrent(Boolean disallowConcurrent) {
    this.disallowConcurrent = disallowConcurrent;
  }

  @Override
  public Date getCreateTime() {
    return createTime;
  }

  @Override
  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  @Override
  public Date getUpdateTime() {
    return updateTime;
  }

  @Override
  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }

  @Override
  public String getJobType() {
    return jobType;
  }

  @Override
  public void setJobType(String jobType) {
    this.jobType = jobType;
  }

  @Override
  public String getWorker() {
    return worker;
  }

  @Override
  public void setWorker(String worker) {
    this.worker = worker;
  }

  @Override
  public String getWorkerType() {
    return workerType;
  }

  @Override
  public void setWorkerType(String workerType) {
    this.workerType = workerType;
  }

  @Override
  public String getJobData() {
    return jobData;
  }

  @Override
  public void setJobData(String jobData) {
    this.jobData = jobData;
  }

  @Override
  public String getTriggerGroup() {
    return triggerGroup;
  }

  @Override
  public void setTriggerGroup(String triggerGroup) {
    this.triggerGroup = triggerGroup;
  }

  @Override
  public String getTriggerName() {
    return triggerName;
  }

  @Override
  public void setTriggerName(String triggerName) {
    this.triggerName = triggerName;
  }

  @Override
  public Integer getPriority() {
    return priority;
  }

  @Override
  public void setPriority(Integer priority) {
    this.priority = priority;
  }

  @Override
  public Long getStartAt() {
    return startAt;
  }

  @Override
  public void setStartAt(Long startAt) {
    this.startAt = startAt;
  }

  @Override
  public Long getEndAt() {
    return endAt;
  }

  @Override
  public void setEndAt(Long endAt) {
    this.endAt = endAt;
  }

  @Override
  public String getCalendarName() {
    return calendarName;
  }

  @Override
  public void setCalendarName(String calendarName) {
    this.calendarName = calendarName;
  }

  @Override
  public Integer getMisfirePolicy() {
    return misfirePolicy;
  }

  @Override
  public void setMisfirePolicy(Integer misfirePolicy) {
    this.misfirePolicy = misfirePolicy;
  }

  @Override
  public String getTriggerType() {
    return triggerType;
  }

  @Override
  public void setTriggerType(String triggerType) {
    this.triggerType = triggerType;
  }

  @Override
  public Long getSimpleInterval() {
    return simpleInterval;
  }

  @Override
  public void setSimpleInterval(Long simpleInterval) {
    this.simpleInterval = simpleInterval;
  }

  @Override
  public Integer getSimpleRepeatCount() {
    return simpleRepeatCount;
  }

  @Override
  public void setSimpleRepeatCount(Integer simpleRepeatCount) {
    this.simpleRepeatCount = simpleRepeatCount;
  }

  @Override
  public String getCronExpression() {
    return cronExpression;
  }

  @Override
  public void setCronExpression(String cronExpression) {
    this.cronExpression = cronExpression;
  }

  @Override
  public Boolean getActive() {
    return active;
  }

  @Override
  public void setActive(Boolean active) {
    this.active = active;
  }

  public String getOrgId() {
    return orgId;
  }

  public void setOrgId(String orgId) {
    this.orgId = orgId;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public String getOwnerType() {
    return ownerType;
  }

  public void setOwnerType(String ownerType) {
    this.ownerType = ownerType;
  }
}
