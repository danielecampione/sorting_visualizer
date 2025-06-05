# Sorting Visualizer

## Overview

**Sorting Visualizer** is an interactive JavaFX application that demonstrates nine classical sorting algorithms in parallel. It allows the user to visualise the step-by-step execution of each algorithm, comparing their behaviours and performance on the same random dataset. The application features:

- **Nine Sorting Algorithms**: Bubble Sort, Selection Sort, Insertion Sort, Quick Sort, Merge Sort, Heap Sort, Counting Sort, Radix Sort and Shell Sort.
- **Real‐time Visualisation**: Each algorithm runs concurrently in its own pane, updating bar heights and colours at each key operation.
- **Adjustable Parameters**: The user may select the number of elements (e.g. 12, 20, 50, 100) and enable or disable continuous bar blinking.
- **Performance Indicators**: Each pane displays the elapsed time (in milliseconds) and a “Completed” label once the sort finishes.

### Screenshot

![Png](https://i.ibb.co/3YSN69yv/Immagine-2025-06-04-232353.png)

### Installation

1. Clone this repository:
   ```
   git clone https://github.com/danielecampione/sorting_visualizer.git
   ```
2. Open the project in your favourite IDE (e.g. IntelliJ IDEA or Eclipse) with JavaFX support.
3. Ensure that JavaFX is on the module path.
4. Build and run `SortingVisualizerGUI.java`.

### Usage

- **Generate Data**: Click “🎲 New Random List” to create a fresh dataset of random integers in the selected range.
- **Select Size**: Use the drop‐down menu labelled “Number of elements” to choose the array size.
- **Start Sorting**: Click “🚀 Start Sorting” to begin all nine algorithms simultaneously.
- **Enable Blink**: Tick “Enable continuous blinking” to activate a subtle fill‐transition on active bars.

### Algorithmic Explanations

Below are concise academic‐style summaries of each sorting algorithm, including mathematical formulations of time and space complexities.

#### 1. Bubble Sort

Bubble Sort repeatedly steps through the array, compares adjacent elements and swaps them if they are in the wrong order. After each pass, the largest unsorted element “bubbles” to its correct position.

- **Pseudocode**:
  ```
  for i = 0 to n−2:
      for j = 0 to n−i−2:
          if A[j] > A[j+1]:
              swap A[j], A[j+1]
  ```
- **Time Complexity**:
  - Best‐case: $\Theta(n)$ (already sorted, only comparisons)
  - Average‐case: $\Theta(n^2)$
  - Worst‐case: $\Theta(n^2)$
- **Space Complexity**: $O(1)$ (in‐place)
- **Mathematical Note**:  
  Let $T(n)$ denote the total number of comparisons. Then
  $T(n) = \sum_{i=0}^{n-2} \sum_{j=0}^{n-i-2} 1 = \sum_{k=1}^{n-1} k = \frac{n(n-1)}{2} = \Theta(n^2).$

#### 2. Selection Sort

Selection Sort partitions the array into a sorted and an unsorted region. On each iteration, it selects the minimum from the unsorted region and swaps it with the first unsorted element.

- **Pseudocode**:
  ```
  for i = 0 to n−2:
      minIdx = i
      for j = i+1 to n−1:
          if A[j] < A[minIdx]:
              minIdx = j
      swap A[i], A[minIdx]
  ```
- **Time Complexity**:
  - Best‐, Average‐, and Worst‐case: $\Theta(n^2)$
- **Space Complexity**: $O(1)$
- **Mathematical Note**:  
  The number of comparisons is $\sum_{i=0}^{n-2} (n - i - 1) = \sum_{k=1}^{n-1} k = \frac{n(n-1)}{2} = \Theta(n^2).$

#### 3. Insertion Sort

Insertion Sort builds the sorted array incrementally by removing one element from the input data, finding its place within the sorted portion, and shifting the larger elements one position to the right.

- **Pseudocode**:
  ```
  for i = 1 to n−1:
      key = A[i]
      j = i − 1
      while j ≥ 0 and A[j] > key:
          A[j+1] = A[j]
          j = j − 1
      A[j+1] = key
  ```
- **Time Complexity**:
  - Best‐case: $\Theta(n)$ (already sorted)
  - Average‐ and Worst‐case: $\Theta(n^2)$
- **Space Complexity**: $O(1)$
- **Mathematical Note**:  
  When the array is reverse‐sorted, the inner loop executes $\sum_{i=1}^{n-1} i = \frac{n(n-1)}{2}$ shifts, hence $\Theta(n^2)$. In the best case, only $n−1$ comparisons occur: $\Theta(n)$.

#### 4. Quick Sort

Quick Sort employs a divide‐and‐conquer strategy. A pivot element is chosen (commonly the last element). The array is partitioned so that all items less than or equal to the pivot precede it, and all items greater follow it. Recursively, the subarrays are sorted.

- **Pseudocode** (Lomuto partition):
  ```
  function quickSort(A, low, high):
      if low < high:
          p = partition(A, low, high)
          quickSort(A, low, p-1)
          quickSort(A, p+1, high)

  function partition(A, low, high):
      pivot = A[high]
      i = low - 1
      for j = low to high-1:
          if A[j] < pivot:
              i = i + 1
              swap A[i], A[j]
      swap A[i+1], A[high]
      return i + 1
  ```
- **Time Complexity**:
  - Best‐ and Average‐case: $\Theta(n \log n)$
  - Worst‐case: $\Theta(n^2)$ (e.g. already sorted with poor pivot choice)
- **Space Complexity**:  
  - Average auxiliary (stack) space: $O(\log n)$
  - Worst‐case stack depth: $O(n)$
- **Mathematical Note**:  
  Let $T(n)$ be the expected time for Quick Sort. If the pivot splits the array roughly in half, then
  $T(n) = 2\,T\bigl(\tfrac{n}{2}\bigr) + \Theta(n) \quad\Longrightarrow\quad T(n) = \Theta(n \log n).$
  In the pathological case of always picking the largest (or smallest) element, one obtains
  $T(n) = T(n-1) + \Theta(n) = \Theta(n^2).$

#### 5. Merge Sort

Merge Sort is also divide‐and‐conquer: the array is recursively halved until subarrays of size one remain, then merged in sorted order.

- **Pseudocode**:
  ```
  function mergeSort(A, left, right):
      if left < right:
          mid = (left + right) // 2
          mergeSort(A, left, mid)
          mergeSort(A, mid+1, right)
          merge(A, left, mid, right)

  function merge(A, left, mid, right):
      n1 = mid - left + 1
      n2 = right - mid
      create arrays L[0..n1-1], R[0..n2-1]
      copy A[left..mid] into L, A[mid+1..right] into R
      i = j = 0, k = left
      while i < n1 and j < n2:
          if L[i] ≤ R[j]:
              A[k] = L[i]; i = i + 1
          else:
              A[k] = R[j]; j = j + 1
          k = k + 1
      copy remaining L[i..n1-1], then R[j..n2-1] into A
  ```
- **Time Complexity**:  
  - All cases (Best, Average, Worst): $\Theta(n \log n)$
- **Space Complexity**:  
  - Additional arrays: $O(n)$
- **Mathematical Note**:  
  The recurrence is
  $T(n) = 2\,T\bigl(\tfrac{n}{2}\bigr) + \Theta(n) \quad\Longrightarrow\quad T(n) = \Theta(n \log n)$
  by the Master Theorem (Case 2: $a=2, b=2, f(n)=\Theta(n)$ ).

#### 6. Heap Sort

Heap Sort transforms the array into a binary max‐heap, then repeatedly extracts the maximum element (root) and places it at the end, reducing the heap size by 1.

- **Pseudocode**:
  ```
  procedure heapSort(A):
      n = length(A)
      // Build max‐heap in O(n) time
      for i = (n // 2) - 1 downto 0:
          heapify(A, n, i)
      // Extract elements from heap
      for i = n - 1 downto 1:
          swap A[0], A[i]
          heapify(A, i, 0)

  procedure heapify(A, heapSize, root):
      largest = root
      left = 2*root + 1
      right = 2*root + 2
      if left < heapSize and A[left] > A[largest]:
          largest = left
      if right < heapSize and A[right] > A[largest]:
          largest = right
      if largest != root:
          swap A[root], A[largest]
          heapify(A, heapSize, largest)
  ```
- **Time Complexity**:
  - Best, Average, Worst: $\Theta(n \log n)$
- **Space Complexity**: $O(1)$ (in‐place, apart from recursive stack, which is $O(\log n)$ )
- **Mathematical Note**:  
  Building the heap takes $O(n)$ time, and each of the $n$ extraction steps invokes `heapify` in $O(\log n)$, for a total of
  $\Theta(n) + n \cdot \Theta(\log n) = \Theta(n \log n).$

#### 7. Counting Sort

Counting Sort is a non‐comparison integer sorting algorithm that counts the occurrences of each unique key value. It then computes prefix sums (cumulative counts) to place each element in its correct position in the output array.

- **Pseudocode**:
  ```
  procedure countingSort(A):
      maxVal = max(A)
      C = new array of zeros of size maxVal + 1
      for each x in A:
          C[x] = C[x] + 1
      for i = 1 to maxVal:
          C[i] = C[i] + C[i - 1]   # prefix sums
      B = new array of same length as A
      for i = length(A) - 1 downto 0: 
          x = A[i]
          B[C[x] - 1] = x
          C[x] = C[x] - 1
      return B
  ```
- **Time Complexity**:
  - Best, Average, Worst: $\Theta(n + k)$, where $k = \max(A)$.
- **Space Complexity**: $O(n + k)$
- **Mathematical Note**:  
  Let $n = |A|$ and $k = \max(A)$. Counting frequencies is $O(n)$. Computing prefix sums is $O(k)$. Building the output is $O(n)$. Hence total
  $T(n, k) = O(n + k).$

#### 8. Radix Sort

Radix Sort processes integer keys digit by digit, starting from the least significant digit up to the most significant. Each pass uses a stable sort (commonly Counting Sort) on the digit.

- **Pseudocode**:
  ```
  procedure radixSort(A):
      maxVal = max(A)
      exp = 1
      while maxVal / exp > 0:
          countingSortByDigit(A, exp)
          exp = exp * 10

  procedure countingSortByDigit(A, exp):
      n = length(A)
      B = new array of length n
      C = new array of zeros of size 10
      for i = 0 to n - 1:
          digit = (A[i] / exp) % 10
          C[digit] = C[digit] + 1
      for i = 1 to 9:
          C[i] = C[i] + C[i - 1]
      for i = n - 1 downto 0:
          digit = (A[i] / exp) % 10
          B[C[digit] - 1] = A[i]
          C[digit] = C[digit] - 1
      copy B back into A
  ```
- **Time Complexity**:
  - $O(d * (n + b))$, where $d$ = number of digits, $b$ = base (often $b = 10$), so typically $O(d * n)$.
- **Space Complexity**: $O(n + b)$
- **Mathematical Note**:  
  If numbers have at most $d$ digits, and each Counting Sort pass costs $O(n + b)$, the total is
  $T(n, d, b) = d * O(n + b).$
  For fixed $b$, $T(n) = O(d * n)$. If $d = O(\log_b M)$, where $M$ is the maximum key, then $T(n) = O(n \log_b M)$.

#### 9. Shell Sort

Shell Sort generalises Insertion Sort by allowing exchanges of far‐apart elements. An initial “gap” sequence $g_1 > g_2 > \cdots > g_t = 1$ is used; the array is h‐sorted for each gap $g_i$, finishing with $g_t = 1$.

- **Pseudocode**:
  ```
  procedure shellSort(A):
      n = length(A)
      for gap = floor(n/2) downto 1 by floor(gap/2):
          for i = gap to n - 1:
              temp = A[i]
              j = i
              while j >= gap and A[j - gap] > temp:
                  A[j] = A[j - gap]
                  j = j - gap
              A[j] = temp
  ```
- **Time Complexity**:
  - Depends on the gap sequence. For Shell’s original sequence floor(n/2), floor(n/4), …, 1: worst‐case around $O(n^2)$.  
  - Other sequences (e.g. Hibbard’s, Sedgewick’s) yield better complexities: $O(n^{1.5})$ or $O(n \log^2 n)$.
- **Space Complexity**: $O(1)$
- **Mathematical Note**:  
  If using Shell’s sequence $g_k = floor(g_{k-1} / 2)$, precise analysis is intricate. Empirically, for random input, performance is often $O(n^{1.25})$ – $O(n^{1.5})$ depending on sequence.

---

## Panoramica in italiano

**Sorting Visualizer** è un’applicazione interattiva basata su JavaFX che mostra nove algoritmi di ordinamento classici in esecuzione contemporanea. L’utente può osservare, su pannelli affiancati, l’evoluzione passo dopo passo di ciascun algoritmo sullo stesso dataset casuale. L’applicativo presenta:

- **Nove algoritmi di ordinamento**: Bubble Sort, Selection Sort, Insertion Sort, Quick Sort, Merge Sort, Heap Sort, Counting Sort, Radix Sort e Shell Sort.
- **Visualizzazione in tempo reale**: Ogni algoritmo viene eseguito in parallelo nel proprio pannello, aggiornando l’altezza e il colore delle barre ad ogni operazione significativa.
- **Parametri configurabili**: L’utente può scegliere il numero di elementi (per esempio 12, 20, 50, 100) e abilitare o disabilitare il lampeggiamento continuo delle barre.
- **Indicatori di prestazione**: Ciascun pannello visualizza il tempo trascorso (in millisecondi) e mostra un’etichetta “Completato” al termine dell’ordinamento.

### Installazione

1. Clonare questo repository:
   ```bash
   git clone https://github.com/danielecampione/sorting_visualizer.git
   ```
2. Aprire il progetto in un IDE con supporto JavaFX (ad esempio IntelliJ IDEA o Eclipse).
3. Verificare che JavaFX sia sul module‐path.
4. Compilare ed eseguire la classe `SortingVisualizerGUI.java`.

### Utilizzo

- **Generare dati**: Cliccare “🎲 Nuovo Elenco Casuale” per creare un dataset di interi casuali nella gamma selezionata.
- **Selezionare dimensione**: Usare il menu a tendina “Numero elementi” per impostare la lunghezza dell’array.
- **Avviare l’ordinamento**: Cliccare “🚀 Avvia Ordinamento” per far partire i nove algoritmi simultaneamente.
- **Abilitare il lampeggiamento**: Selezionare “Abilita lampeggiamento continuo” per un effetto visivo più dinamico sugli elementi attivi.

---

**License**: GNU GENERAL PUBLIC LICENSE Version 3.0
**Author**: Daniele Campione
