package com.company;

import java.util.Random;

public class ArrClass {
    private final int dim;
    private final int threadNum;
    private final int[] arr;
    private int threadCount = 0;

    public ArrClass(int dim, int threadNum) {
        this.dim = dim;
        this.threadNum = threadNum;
        arr = RandomInsert();
        globalMin = arr[0];
    }

    public int minPart(int startIndex, int finishIndex){
        int min = arr[startIndex];

        for(int i = startIndex; i < finishIndex; i++){
            if(arr[i] < min){
                min = arr[i];
            }
        }
        return min;
    }

    public int getThreadCount(){
        return threadCount;
    }

    synchronized private int getMin(){
        while(getThreadCount() < threadNum){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return globalMin;
    }

    synchronized public void incThreadCount(){
        threadCount++;
        notify();
    }

    private int globalMin;
    synchronized public void collectMin(int min){
        if(min < globalMin){
            globalMin = min;
        }
    }

    private int[] RandomInsert(){
        Random r = new Random();
        int[] arr = new int[dim];
        for(int i = 0; i < dim; i++){
            arr[i] = r.nextInt(-20,20);
        }
        return arr;
    }

    public int ThreadMin(){
        ThreadMin[] threadMin = new ThreadMin[threadNum];
        int step = dim / threadNum;

        for(int i = 0; i < threadNum - 1; i++){
            threadMin[i] = new ThreadMin(i * step, (i + 1) * step, this);
            threadMin[i].run();
        }
        threadMin[threadNum - 1] = new ThreadMin((threadNum - 1) * step, dim - 1, this);
        return getMin();
    }

    public void print(){
        for(var item : arr){
            System.out.print(item + " ");
        }
        System.out.println();
    }
}
