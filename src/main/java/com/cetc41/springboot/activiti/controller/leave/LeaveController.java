package com.cetc41.springboot.activiti.controller.leave;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.task.Task;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @auther liupeiqing
 * @date 2019/5/30 13:30
 */
@RestController
public class LeaveController {


    /**
     * 1、部署流程
     * 2、启动流程实例
     * 3、请假人发出请假申请
     * 4、班主任查看任务
     * 5、班主任审批
     * 6、最终的教务处Boss审批
     */


    /**
     * 1：部署一个Activiti流程
     * 运行成功后，查看之前的数据库表，就会发现多了很多内容
     */
    @GetMapping("/creatActivitiTask")
    public void creatActivitiTask() {
        //加载的那两个内容就是我们之前已经弄好的基础内容哦。
        //得到了流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        processEngine.getRepositoryService()
                .createDeployment()
                .addClasspathResource("processes/leave.bpmn")
                .addClasspathResource("processes/leave.png")
                .deploy();

    }

    /**
     * 2：启动流程实例
     */
    @GetMapping("/startProcess")
    public void testStartProcessInstance(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        processEngine.getRuntimeService()
                .startProcessInstanceById("leave:1:15004");  //这个是查看数据库中act_re_procdef表
    }


    /**
     * 完成请假申请
     */
    @GetMapping("/completeLeave")
    public void testLeave(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        processEngine.getTaskService()
                .complete("17504"); //查看act_ru_task表
    }

    /**
     * 小明学习的班主任小毛查询当前正在执行任务
     */
    @GetMapping("/getTaskOfHeadTeacher")
    public void testQueryTask(){
        //下面代码中的小毛，就是我们之前设计那个流程图中添加的班主任内容
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        List<Task> tasks = processEngine.getTaskService()
                .createTaskQuery()
                .taskAssignee("小毛")
                .list();
        for (Task task : tasks) {
            System.out.println(task.getName());
        }
    }

    /**
     * 班主任小毛完成任务
     */
    @GetMapping("/finishTaskOfHeadTeacher")
    public void testFinishTask_manager(){
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        engine.getTaskService()
                .complete("17504"); //查看act_ru_task数据表
    }

    /**
     * 教务处的大毛完成的任务
     */
    @GetMapping("finishTaskOfBoss")
    public void testFinishTask_Boss(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        processEngine.getTaskService()
                .complete("20002");  //查看act_ru_task数据表
    }


}
