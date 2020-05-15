package com.susiha.asyncstarter;

import com.susiha.library.AsyncStarter;

/**
 * 模拟器
 */
public class Simulation {

    /**
     * 情景1
     * task1 ------> task7
     * task2 ------> task4 ------>task6
     *      -          -         >
     *       -         -        -
     *        -        -       -
     *         -       -      -
     *          ->     >     -
     * task3 ------> task5
     *
     */
    public static  void scene(){

        AsyncStarter.getInstance()
                .addTask(new Task1("task1"))
                .addTask(new Task2("task2"))
                .addTask(new Task3("task3"))
                .addTask(new Task4("task4"))
                .addTask(new Task5("task5"))
                .addTask(new Task6("task6"))
                .addTask(new Task7("task7"))
                .start();
    }
}
