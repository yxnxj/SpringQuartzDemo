package com.example.quartz;

import static org.quartz.JobBuilder.newJob;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.quartz.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class JobConfig {


    private final Scheduler scheduler; // 쿼츠 스케줄 객체

    @PostConstruct
    public void run(){
        JobDetail detail = runJobDetail(TestJobA.class, new HashMap<>());
        JobDetail job = JobBuilder
                .newJob(TestJobB.class)
                .withIdentity("identity1")
                .build();
        try {
            // 크론형식 지정 후 스케줄 시작
//            scheduler.scheduleJob(detail, runJobTrigger("0/10 * * * * ?"));
            scheduler.scheduleJob(job, runJobTrigger());
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

    }

    public Trigger runJobTrigger(String scheduleExp){
        // 크론 스케줄 사용
        return TriggerBuilder.newTrigger()
//                .startAt()
                .withSchedule(CronScheduleBuilder.cronSchedule(scheduleExp)).build();
    }

    public Trigger runJobTrigger(){
        LocalDateTime localDateTime = LocalDateTime.now().plusMinutes(1);
//        localDateTime.plusMinutes(1);
        Date date = java.sql.Timestamp.valueOf(localDateTime);
        return TriggerBuilder.newTrigger()
                .startAt(date).build();
    }

    public JobDetail runJobDetail(Class job, Map params){
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.putAll(params);
        // 스케줄 생성
        return newJob(job).usingJobData(jobDataMap).build();
    }

}