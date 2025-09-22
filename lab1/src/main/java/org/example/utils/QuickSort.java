package org.example.utils;

public class QuickSort {

    int partition(double[] a, int low, int high) {
        double pivot = a[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (a[j] <= pivot) {
                i++;

                double temp = a[i];
                a[i] = a[j];
                a[j] = temp;
            }
        }

        double temp = a[i + 1];
        a[i + 1] = a[high];
        a[high] = temp;

        return i + 1;
    }

    void sort(double[] a, int l, int h){
        if(l < h){
            int pi = partition(a, l, h);
            sort(a, l, pi-1);
            sort(a, pi+1, h);
        }
    }

    public void sort(double[] a){
        int n =  a.length;
        sort(a, 0, n-1);
    }
}
