package com.sortingvisualizer;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

public class SortingBusinessLogic {
    
    private static final int MAX_VALUE = 40;
    private static final int BASE_DELAY = 2400; // kb = BASE_DELAY / arraySize
    
    private int arraySize;
    private List<Integer> originalData;
    private final Random random = new Random();
    
    public SortingBusinessLogic(int initialSize) {
        this.arraySize = initialSize;
        generateNewDataset();
    }
    
    public void setArraySize(int newSize) {
        this.arraySize = newSize;
    }
    
    public void generateNewDataset() {
        originalData = new ArrayList<>();
        for (int i = 0; i < arraySize; i++) {
            originalData.add(random.nextInt(MAX_VALUE) + 1);
        }
    }
    
    public List<Integer> getOriginalData() {
        return new ArrayList<>(originalData);
    }
    
    private int getDelay() {
        return Math.max(1, BASE_DELAY / arraySize);
    }
    
    public void startSorting(SortingVisualizerGUI.SortingCallback callback) {
        AtomicInteger completedCount = new AtomicInteger(0);
        int totalAlgorithms = 9;
        
        CompletableFuture.allOf(
            startBubbleSort(callback, completedCount, totalAlgorithms),
            startSelectionSort(callback, completedCount, totalAlgorithms),
            startInsertionSort(callback, completedCount, totalAlgorithms),
            startQuickSort(callback, completedCount, totalAlgorithms),
            startMergeSort(callback, completedCount, totalAlgorithms),
            startHeapSort(callback, completedCount, totalAlgorithms),
            startShellSort(callback, completedCount, totalAlgorithms),
            startBinaryInsertionSort(callback, completedCount, totalAlgorithms),
            startCountingSort(callback, completedCount, totalAlgorithms)
        );
    }
    
    private CompletableFuture<Void> startBubbleSort(SortingVisualizerGUI.SortingCallback callback,
                                                   AtomicInteger completedCount, int totalAlgorithms) {
        return CompletableFuture.runAsync(() -> {
            List<Integer> data = new ArrayList<>(originalData);
            final long[] startTimeRef = {0L};
            final boolean[] startedFlag = {false};
            int delay = getDelay();
            
            int n = data.size();
            for (int i = 0; i < n - 1; i++) {
                boolean swapped = false;
                for (int j = 0; j < n - i - 1; j++) {
                    if (!startedFlag[0]) {
                        startTimeRef[0] = System.currentTimeMillis();
                        startedFlag[0] = true;
                    }
                    long elapsedCompare = System.currentTimeMillis() - startTimeRef[0];
                    callback.onProgress("Bubble Sort", new ArrayList<>(data), Arrays.asList(j, j + 1), elapsedCompare, false);
                    sleep(delay);
                    if (data.get(j) > data.get(j + 1)) {
                        Collections.swap(data, j, j + 1);
                        swapped = true;
                        long elapsedSwap = System.currentTimeMillis() - startTimeRef[0];
                        callback.onProgress("Bubble Sort", new ArrayList<>(data), Arrays.asList(j, j + 1), elapsedSwap, false);
                        sleep(delay);
                    }
                }
                if (!swapped) break;
            }
            if (!startedFlag[0]) {
                startTimeRef[0] = System.currentTimeMillis();
                startedFlag[0] = true;
            }
            long elapsedFinal = System.currentTimeMillis() - startTimeRef[0];
            callback.onProgress("Bubble Sort", data, Collections.emptyList(), elapsedFinal, true);
            
            if (completedCount.incrementAndGet() == totalAlgorithms) {
                callback.onAllCompleted();
            }
        });
    }
    
    private CompletableFuture<Void> startSelectionSort(SortingVisualizerGUI.SortingCallback callback,
                                                      AtomicInteger completedCount, int totalAlgorithms) {
        return CompletableFuture.runAsync(() -> {
            List<Integer> data = new ArrayList<>(originalData);
            final long[] startTimeRef = {0L};
            final boolean[] startedFlag = {false};
            int delay = getDelay();
            
            int n = data.size();
            for (int i = 0; i < n - 1; i++) {
                int minIdx = i;
                for (int j = i + 1; j < n; j++) {
                    if (!startedFlag[0]) {
                        startTimeRef[0] = System.currentTimeMillis();
                        startedFlag[0] = true;
                    }
                    long elapsedCompare = System.currentTimeMillis() - startTimeRef[0];
                    callback.onProgress("Selection Sort", new ArrayList<>(data), Arrays.asList(j, minIdx), elapsedCompare, false);
                    sleep(delay);
                    if (data.get(j) < data.get(minIdx)) {
                        minIdx = j;
                        long elapsedMin = System.currentTimeMillis() - startTimeRef[0];
                        callback.onProgress("Selection Sort", new ArrayList<>(data), Arrays.asList(minIdx, i), elapsedMin, false);
                        sleep(delay);
                    }
                }
                if (minIdx != i) {
                    Collections.swap(data, i, minIdx);
                    long elapsedSwap = System.currentTimeMillis() - startTimeRef[0];
                    callback.onProgress("Selection Sort", new ArrayList<>(data), Arrays.asList(i, minIdx), elapsedSwap, false);
                    sleep(delay);
                }
            }
            if (!startedFlag[0]) {
                startTimeRef[0] = System.currentTimeMillis();
                startedFlag[0] = true;
            }
            long elapsedFinal = System.currentTimeMillis() - startTimeRef[0];
            callback.onProgress("Selection Sort", data, Collections.emptyList(), elapsedFinal, true);
            
            if (completedCount.incrementAndGet() == totalAlgorithms) {
                callback.onAllCompleted();
            }
        });
    }
    
    private CompletableFuture<Void> startInsertionSort(SortingVisualizerGUI.SortingCallback callback,
                                                      AtomicInteger completedCount, int totalAlgorithms) {
        return CompletableFuture.runAsync(() -> {
            List<Integer> data = new ArrayList<>(originalData);
            final long[] startTimeRef = {0L};
            final boolean[] startedFlag = {false};
            int delay = getDelay();
            
            int n = data.size();
            for (int i = 1; i < n; i++) {
                int key = data.get(i);
                int j = i - 1;
                while (j >= 0 && data.get(j) > key) {
                    if (!startedFlag[0]) {
                        startTimeRef[0] = System.currentTimeMillis();
                        startedFlag[0] = true;
                    }
                    long elapsedCompare = System.currentTimeMillis() - startTimeRef[0];
                    callback.onProgress("Insertion Sort", new ArrayList<>(data), Arrays.asList(j, i), elapsedCompare, false);
                    sleep(delay);
                    data.set(j + 1, data.get(j));
                    long elapsedShift = System.currentTimeMillis() - startTimeRef[0];
                    callback.onProgress("Insertion Sort", new ArrayList<>(data), Arrays.asList(j, j + 1), elapsedShift, false);
                    sleep(delay);
                    j--;
                }
                data.set(j + 1, key);
                if (!startedFlag[0]) {
                    startTimeRef[0] = System.currentTimeMillis();
                    startedFlag[0] = true;
                }
                long elapsedInsert = System.currentTimeMillis() - startTimeRef[0];
                callback.onProgress("Insertion Sort", new ArrayList<>(data), Arrays.asList(j + 1), elapsedInsert, false);
                sleep(delay);
            }
            if (!startedFlag[0]) {
                startTimeRef[0] = System.currentTimeMillis();
                startedFlag[0] = true;
            }
            long elapsedFinal = System.currentTimeMillis() - startTimeRef[0];
            callback.onProgress("Insertion Sort", data, Collections.emptyList(), elapsedFinal, true);
            
            if (completedCount.incrementAndGet() == totalAlgorithms) {
                callback.onAllCompleted();
            }
        });
    }
    
    private CompletableFuture<Void> startQuickSort(SortingVisualizerGUI.SortingCallback callback,
                                                  AtomicInteger completedCount, int totalAlgorithms) {
        return CompletableFuture.runAsync(() -> {
            List<Integer> data = new ArrayList<>(originalData);
            final long[] startTimeRef = {0L};
            final boolean[] startedFlag = {false};
            int delay = getDelay();
            
            quickSort(data, 0, data.size() - 1, callback, startTimeRef, startedFlag, delay);
            if (!startedFlag[0]) {
                startTimeRef[0] = System.currentTimeMillis();
                startedFlag[0] = true;
            }
            long elapsedFinal = System.currentTimeMillis() - startTimeRef[0];
            callback.onProgress("Quick Sort", data, Collections.emptyList(), elapsedFinal, true);
            
            if (completedCount.incrementAndGet() == totalAlgorithms) {
                callback.onAllCompleted();
            }
        });
    }
    
    private void quickSort(List<Integer> data, int low, int high,
                          SortingVisualizerGUI.SortingCallback callback,
                          long[] startTimeRef, boolean[] startedFlag, int delay) {
        if (low < high) {
            int pi = partition(data, low, high, callback, startTimeRef, startedFlag, delay);
            quickSort(data, low, pi - 1, callback, startTimeRef, startedFlag, delay);
            quickSort(data, pi + 1, high, callback, startTimeRef, startedFlag, delay);
        }
    }
    
    private int partition(List<Integer> data, int low, int high,
                         SortingVisualizerGUI.SortingCallback callback,
                         long[] startTimeRef, boolean[] startedFlag, int delay) {
        int pivot = data.get(high);
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (!startedFlag[0]) {
                startTimeRef[0] = System.currentTimeMillis();
                startedFlag[0] = true;
            }
            long elapsedCompare = System.currentTimeMillis() - startTimeRef[0];
            callback.onProgress("Quick Sort", new ArrayList<>(data), Arrays.asList(j, high), elapsedCompare, false);
            sleep(delay);
            if (data.get(j) <= pivot) {
                i++;
                Collections.swap(data, i, j);
                long elapsedSwap = System.currentTimeMillis() - startTimeRef[0];
                callback.onProgress("Quick Sort", new ArrayList<>(data), Arrays.asList(i, j), elapsedSwap, false);
                sleep(delay);
            }
        }
        Collections.swap(data, i + 1, high);
        long elapsedSwapPivot = System.currentTimeMillis() - startTimeRef[0];
        callback.onProgress("Quick Sort", new ArrayList<>(data), Arrays.asList(i + 1, high), elapsedSwapPivot, false);
        sleep(delay);
        return i + 1;
    }
    
    private CompletableFuture<Void> startMergeSort(SortingVisualizerGUI.SortingCallback callback,
                                                   AtomicInteger completedCount, int totalAlgorithms) {
        return CompletableFuture.runAsync(() -> {
            List<Integer> data = new ArrayList<>(originalData);
            final long[] startTimeRef = {0L};
            final boolean[] startedFlag = {false};
            int delay = getDelay();
            
            mergeSort(data, 0, data.size() - 1, callback, startTimeRef, startedFlag, delay);
            if (!startedFlag[0]) {
                startTimeRef[0] = System.currentTimeMillis();
                startedFlag[0] = true;
            }
            long elapsedFinal = System.currentTimeMillis() - startTimeRef[0];
            callback.onProgress("Merge Sort", data, Collections.emptyList(), elapsedFinal, true);
            
            if (completedCount.incrementAndGet() == totalAlgorithms) {
                callback.onAllCompleted();
            }
        });
    }
    
    private void mergeSort(List<Integer> data, int left, int right,
                           SortingVisualizerGUI.SortingCallback callback,
                           long[] startTimeRef, boolean[] startedFlag, int delay) {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSort(data, left, mid, callback, startTimeRef, startedFlag, delay);
            mergeSort(data, mid + 1, right, callback, startTimeRef, startedFlag, delay);
            merge(data, left, mid, right, callback, startTimeRef, startedFlag, delay);
        }
    }
    
    private void merge(List<Integer> data, int left, int mid, int right,
                       SortingVisualizerGUI.SortingCallback callback,
                       long[] startTimeRef, boolean[] startedFlag, int delay) {
        int n1 = mid - left + 1;
        int n2 = right - mid;
        List<Integer> L = new ArrayList<>(n1);
        List<Integer> R = new ArrayList<>(n2);
        
        for (int i = 0; i < n1; i++) {
            L.add(data.get(left + i));
        }
        for (int j = 0; j < n2; j++) {
            R.add(data.get(mid + 1 + j));
        }
        
        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) {
            if (!startedFlag[0]) {
                startTimeRef[0] = System.currentTimeMillis();
                startedFlag[0] = true;
            }
            int idxL = left + i;
            int idxR = mid + 1 + j;
            long elapsedCompare = System.currentTimeMillis() - startTimeRef[0];
            callback.onProgress("Merge Sort", new ArrayList<>(data), Arrays.asList(idxL, idxR), elapsedCompare, false);
            sleep(delay);
            if (L.get(i) <= R.get(j)) {
                data.set(k, L.get(i));
                i++;
            } else {
                data.set(k, R.get(j));
                j++;
            }
            long elapsedWrite = System.currentTimeMillis() - startTimeRef[0];
            callback.onProgress("Merge Sort", new ArrayList<>(data), Arrays.asList(k), elapsedWrite, false);
            sleep(delay);
            k++;
        }
        while (i < n1) {
            if (!startedFlag[0]) {
                startTimeRef[0] = System.currentTimeMillis();
                startedFlag[0] = true;
            }
            data.set(k, L.get(i));
            long elapsedLeft = System.currentTimeMillis() - startTimeRef[0];
            callback.onProgress("Merge Sort", new ArrayList<>(data), Arrays.asList(k), elapsedLeft, false);
            sleep(delay);
            i++;
            k++;
        }
        while (j < n2) {
            if (!startedFlag[0]) {
                startTimeRef[0] = System.currentTimeMillis();
                startedFlag[0] = true;
            }
            data.set(k, R.get(j));
            long elapsedRight = System.currentTimeMillis() - startTimeRef[0];
            callback.onProgress("Merge Sort", new ArrayList<>(data), Arrays.asList(k), elapsedRight, false);
            sleep(delay);
            j++;
            k++;
        }
    }
    
    private CompletableFuture<Void> startHeapSort(SortingVisualizerGUI.SortingCallback callback,
                                                  AtomicInteger completedCount, int totalAlgorithms) {
        return CompletableFuture.runAsync(() -> {
            List<Integer> data = new ArrayList<>(originalData);
            final long[] startTimeRef = {0L};
            final boolean[] startedFlag = {false};
            int delay = getDelay();
            
            heapSort(data, callback, startTimeRef, startedFlag, delay);
            if (!startedFlag[0]) {
                startTimeRef[0] = System.currentTimeMillis();
                startedFlag[0] = true;
            }
            long elapsedFinal = System.currentTimeMillis() - startTimeRef[0];
            callback.onProgress("Heap Sort", data, Collections.emptyList(), elapsedFinal, true);
            
            if (completedCount.incrementAndGet() == totalAlgorithms) {
                callback.onAllCompleted();
            }
        });
    }
    
    private void heapSort(List<Integer> data, SortingVisualizerGUI.SortingCallback callback,
                          long[] startTimeRef, boolean[] startedFlag, int delay) {
        int n = data.size();
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(data, n, i, callback, startTimeRef, startedFlag, delay);
        }
        for (int i = n - 1; i > 0; i--) {
            Collections.swap(data, 0, i);
            if (!startedFlag[0]) {
                startTimeRef[0] = System.currentTimeMillis();
                startedFlag[0] = true;
            }
            long elapsedSwap = System.currentTimeMillis() - startTimeRef[0];
            callback.onProgress("Heap Sort", new ArrayList<>(data), Arrays.asList(0, i), elapsedSwap, false);
            sleep(delay);
            heapify(data, i, 0, callback, startTimeRef, startedFlag, delay);
        }
    }
    
    private void heapify(List<Integer> data, int heapSize, int rootIndex,
                         SortingVisualizerGUI.SortingCallback callback,
                         long[] startTimeRef, boolean[] startedFlag, int delay) {
        int largest = rootIndex;
        int left = 2 * rootIndex + 1;
        int right = 2 * rootIndex + 2;
        
        if (left < heapSize) {
            if (!startedFlag[0]) {
                startTimeRef[0] = System.currentTimeMillis();
                startedFlag[0] = true;
            }
            long elapsedCompareLeft = System.currentTimeMillis() - startTimeRef[0];
            callback.onProgress("Heap Sort", new ArrayList<>(data), Arrays.asList(rootIndex, left), elapsedCompareLeft, false);
            sleep(delay);
            if (data.get(left) > data.get(largest)) {
                largest = left;
            }
        }
        if (right < heapSize) {
            if (!startedFlag[0]) {
                startTimeRef[0] = System.currentTimeMillis();
                startedFlag[0] = true;
            }
            long elapsedCompareRight = System.currentTimeMillis() - startTimeRef[0];
            callback.onProgress("Heap Sort", new ArrayList<>(data), Arrays.asList(largest, right), elapsedCompareRight, false);
            sleep(delay);
            if (data.get(right) > data.get(largest)) {
                largest = right;
            }
        }
        if (largest != rootIndex) {
            Collections.swap(data, rootIndex, largest);
            if (!startedFlag[0]) {
                startTimeRef[0] = System.currentTimeMillis();
                startedFlag[0] = true;
            }
            long elapsedSwap = System.currentTimeMillis() - startTimeRef[0];
            callback.onProgress("Heap Sort", new ArrayList<>(data), Arrays.asList(rootIndex, largest), elapsedSwap, false);
            sleep(delay);
            heapify(data, heapSize, largest, callback, startTimeRef, startedFlag, delay);
        }
    }
    
    private CompletableFuture<Void> startShellSort(SortingVisualizerGUI.SortingCallback callback,
                                                   AtomicInteger completedCount, int totalAlgorithms) {
        return CompletableFuture.runAsync(() -> {
            List<Integer> data = new ArrayList<>(originalData);
            final long[] startTimeRef = {0L};
            final boolean[] startedFlag = {false};
            int delay = getDelay();
            
            shellSort(data, callback, startTimeRef, startedFlag, delay);
            if (!startedFlag[0]) {
                startTimeRef[0] = System.currentTimeMillis();
                startedFlag[0] = true;
            }
            long elapsedFinal = System.currentTimeMillis() - startTimeRef[0];
            callback.onProgress("Shell Sort", data, Collections.emptyList(), elapsedFinal, true);
            
            if (completedCount.incrementAndGet() == totalAlgorithms) {
                callback.onAllCompleted();
            }
        });
    }
    
    private void shellSort(List<Integer> data, SortingVisualizerGUI.SortingCallback callback,
                           long[] startTimeRef, boolean[] startedFlag, int delay) {
        int n = data.size();
        for (int gap = n / 2; gap > 0; gap /= 2) {
            for (int i = gap; i < n; i++) {
                int temp = data.get(i);
                int j = i;
                while (j >= gap && data.get(j - gap) > temp) {
                    if (!startedFlag[0]) {
                        startTimeRef[0] = System.currentTimeMillis();
                        startedFlag[0] = true;
                    }
                    long elapsedCompare = System.currentTimeMillis() - startTimeRef[0];
                    callback.onProgress("Shell Sort", new ArrayList<>(data), Arrays.asList(j - gap, j), elapsedCompare, false);
                    sleep(delay);
                    data.set(j, data.get(j - gap));
                    long elapsedShift = System.currentTimeMillis() - startTimeRef[0];
                    callback.onProgress("Shell Sort", new ArrayList<>(data), Arrays.asList(j, j - gap), elapsedShift, false);
                    sleep(delay);
                    j -= gap;
                }
                data.set(j, temp);
                if (!startedFlag[0]) {
                    startTimeRef[0] = System.currentTimeMillis();
                    startedFlag[0] = true;
                }
                long elapsedInsert = System.currentTimeMillis() - startTimeRef[0];
                callback.onProgress("Shell Sort", new ArrayList<>(data), Arrays.asList(j), elapsedInsert, false);
                sleep(delay);
            }
        }
    }
    
    private CompletableFuture<Void> startBinaryInsertionSort(SortingVisualizerGUI.SortingCallback callback,
                                                             AtomicInteger completedCount, int totalAlgorithms) {
        return CompletableFuture.runAsync(() -> {
            List<Integer> data = new ArrayList<>(originalData);
            final long[] startTimeRef = {0L};
            final boolean[] startedFlag = {false};
            int delay = getDelay();
            
            binaryInsertionSort(data, callback, startTimeRef, startedFlag, delay);
            if (!startedFlag[0]) {
                startTimeRef[0] = System.currentTimeMillis();
                startedFlag[0] = true;
            }
            long elapsedFinal = System.currentTimeMillis() - startTimeRef[0];
            callback.onProgress("Binary Insertion Sort", data, Collections.emptyList(), elapsedFinal, true);
            
            if (completedCount.incrementAndGet() == totalAlgorithms) {
                callback.onAllCompleted();
            }
        });
    }
    
    private void binaryInsertionSort(List<Integer> data, SortingVisualizerGUI.SortingCallback callback,
                                     long[] startTimeRef, boolean[] startedFlag, int delay) {
        int n = data.size();
        for (int i = 1; i < n; i++) {
            int key = data.get(i);
            int insertIdx = binarySearchForInsertion(data, key, 0, i - 1, callback, startTimeRef, startedFlag, delay);
            for (int j = i - 1; j >= insertIdx; j--) {
                if (!startedFlag[0]) {
                    startTimeRef[0] = System.currentTimeMillis();
                    startedFlag[0] = true;
                }
                data.set(j + 1, data.get(j));
                long elapsedShift = System.currentTimeMillis() - startTimeRef[0];
                callback.onProgress("Binary Insertion Sort", new ArrayList<>(data), Arrays.asList(j, j + 1), elapsedShift, false);
                sleep(delay);
            }
            data.set(insertIdx, key);
            if (!startedFlag[0]) {
                startTimeRef[0] = System.currentTimeMillis();
                startedFlag[0] = true;
            }
            long elapsedInsert = System.currentTimeMillis() - startTimeRef[0];
            callback.onProgress("Binary Insertion Sort", new ArrayList<>(data), Arrays.asList(insertIdx), elapsedInsert, false);
            sleep(delay);
        }
    }
    
    private int binarySearchForInsertion(List<Integer> data, int key, int left, int right,
                                         SortingVisualizerGUI.SortingCallback callback,
                                         long[] startTimeRef, boolean[] startedFlag, int delay) {
        if (left > right) {
            return left;
        }
        int mid = (left + right) / 2;
        if (!startedFlag[0]) {
            startTimeRef[0] = System.currentTimeMillis();
            startedFlag[0] = true;
        }
        long elapsedCompare = System.currentTimeMillis() - startTimeRef[0];
        callback.onProgress("Binary Insertion Sort", new ArrayList<>(data), Arrays.asList(mid), elapsedCompare, false);
        sleep(delay);
        if (data.get(mid) == key) {
            return mid + 1;
        }
        if (data.get(mid) < key) {
            return binarySearchForInsertion(data, key, mid + 1, right, callback, startTimeRef, startedFlag, delay);
        } else {
            return binarySearchForInsertion(data, key, left, mid - 1, callback, startTimeRef, startedFlag, delay);
        }
    }
    
    private CompletableFuture<Void> startCountingSort(SortingVisualizerGUI.SortingCallback callback,
                                                      AtomicInteger completedCount, int totalAlgorithms) {
        return CompletableFuture.runAsync(() -> {
            List<Integer> data = new ArrayList<>(originalData);
            final long[] startTimeRef = {0L};
            final boolean[] startedFlag = {false};
            int delay = getDelay();
            
            countingSort(data, callback, startTimeRef, startedFlag, delay);
            if (!startedFlag[0]) {
                startTimeRef[0] = System.currentTimeMillis();
                startedFlag[0] = true;
            }
            long elapsedFinal = System.currentTimeMillis() - startTimeRef[0];
            callback.onProgress("Counting Sort", data, Collections.emptyList(), elapsedFinal, true);
            
            if (completedCount.incrementAndGet() == totalAlgorithms) {
                callback.onAllCompleted();
            }
        });
    }
    
    private void countingSort(List<Integer> data, SortingVisualizerGUI.SortingCallback callback,
                              long[] startTimeRef, boolean[] startedFlag, int delay) {
        if (data.isEmpty()) return;
        
        int maxVal = Collections.max(data);
        int minVal = Collections.min(data);
        int range = maxVal - minVal + 1;
        
        int[] count = new int[range];
        for (int idx = 0; idx < data.size(); idx++) {
            int num = data.get(idx);
            if (!startedFlag[0]) {
                startTimeRef[0] = System.currentTimeMillis();
                startedFlag[0] = true;
            }
            count[num - minVal]++;
            long elapsedCount = System.currentTimeMillis() - startTimeRef[0];
            callback.onProgress("Counting Sort", new ArrayList<>(data), Arrays.asList(idx), elapsedCount, false);
            sleep(delay);
        }
        
        int index = 0;
        for (int i = 0; i < range; i++) {
            while (count[i] > 0) {
                data.set(index, i + minVal);
                if (!startedFlag[0]) {
                    startTimeRef[0] = System.currentTimeMillis();
                    startedFlag[0] = true;
                }
                long elapsedWrite = System.currentTimeMillis() - startTimeRef[0];
                callback.onProgress("Counting Sort", new ArrayList<>(data), Arrays.asList(index), elapsedWrite, false);
                sleep(delay);
                index++;
                count[i]--;
            }
        }
    }
    
    private void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
