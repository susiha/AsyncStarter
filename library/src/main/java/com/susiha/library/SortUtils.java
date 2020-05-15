package com.susiha.library;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
/**
 *
 */
public class SortUtils {



    /**
     * 拓扑排序
     * @param source
     * @return
     */
    public static List<Task>  dagTopology(List<Task> source,
                                          HashMap<Class<? extends Task>,Task> taskMap){
        //管理入度
        HashMap<Task,List< Task>> inDegreeMap = new HashMap<>();
        //管理入度为0的队列
        LinkedList<Task> queue = new LinkedList<>();

        Map<Task,Integer> inDegreeNumMap= new HashMap<>();

        List<Task> result = new ArrayList<>();

        initTaskMap(source,taskMap);


        for (Map.Entry<Class<? extends Task>, Task> taskEntry : taskMap.entrySet()) {
            Task task = taskEntry.getValue();
            inDegreeMap.put(task,initInDegreeMap(task,taskMap));
            inDegreeNumMap.put(task,task.denpendsList()==null?0:task.denpendsList().size());
            //从队尾如队列
            if(!isTaskHasDepends(task)){
                queue.addLast(task);
            }
        }

        //通过入度 转化为出度 列表
        for (Map.Entry<Task, List<Task>> entry : inDegreeMap.entrySet()) {
            if(entry.getValue()!=null){
                for (Task task : entry.getValue()) {
                    if(task.getSucceedTasks() ==null){
                        List<Task> outDegreeList = new ArrayList<>();
                        outDegreeList.add(entry.getKey());
                        task.setSucceedTasks(outDegreeList);
                    }else{
                        task.getSucceedTasks().add(entry.getKey());
                    }
                }
            }
        }

        while(!queue.isEmpty()){
            //从队列中取出 放入到结果中
           Task task = queue.removeFirst();
           result.add(task);

           if(task.getSucceedTasks()!=null){
               for (Task outDegreeTask : task.getSucceedTasks()) {
                   inDegreeNumMap.put(outDegreeTask ,inDegreeNumMap.get(outDegreeTask)-1);
                   if(inDegreeNumMap.get(outDegreeTask) ==0){
                       queue.addLast(outDegreeTask);
                   }
               }

           }
        }

        return result;
    }

    /**
     * 对于Class 与 instance
     * @param source
     * @param taskMap
     */
    private static void initTaskMap(List<Task> source,
                                    HashMap<Class<? extends Task>,Task> taskMap){

        for (Task task : source) {

            if(!taskMap.containsKey(task.getClass())){
                taskMap.put(task.getClass(),task);
            }
        }
    }


    private static List<Task> initInDegreeMap(Task task,
                                        HashMap<Class<? extends Task>,Task> taskMap
                                        ){


        if(task.denpendsList()==null){
            return null;
        }

        List<Task> result =new ArrayList<>();
        for (Class<? extends Task> aClass : task.denpendsList()) {
            result.add(taskMap.get(aClass));
        }
        return result;


    }


    /**
     * Task 是否有依赖
     * @param task
     * @return
     */
    public static boolean isTaskHasDepends(Task task){
        return task.denpendsList() != null && task.denpendsList().size()!=0;
    }





}
