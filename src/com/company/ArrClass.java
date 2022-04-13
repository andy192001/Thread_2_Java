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
        int startIndex = 0;
        int finishIndex = dim / threadNum;
        int temp = dim % threadNum;

        threadMin[0] = new ThreadMin(startIndex, finishIndex + temp, this);
        startIndex = finishIndex;
        finishIndex += (dim / threadNum) + temp;
        threadMin[0].run();
        for(int i = 1; i < threadNum; i++){
            threadMin[i] = new ThreadMin(startIndex, finishIndex, this);
            startIndex = finishIndex;
            finishIndex += (dim / threadNum);
            threadMin[i].run();
        }
        return getMin();
    }

    public void print(){
        for(var item : arr){
            System.out.print(item + " ");
        }
        System.out.println();
    }
}
