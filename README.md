Dla implementacji splotu, przetestowane zostały 2 obrazy o różnej rozdzielczości oraz kilka ustawień ilości iteracji, w celu sprawdzenia, po jakim czasie uruchomi sie kompilacja natywna JIT. 

## Obrazek 1 - 217x280
### 1 iteracja
* **Java:** 9.233458 ms
* **Native:** 2.08675 ms


  **Średnie:**
* Average java time: **9,23 ms**
* Average native time: **2,09 ms**

### 10 iteracji
| Nr iteracji | Java [ms] | Native [ms] |
|:---:|:---:|:---:|
| 1 | 12.176416 | 1.653541 |
| 2 | 5.588791 | 1.496167 |
| 3 | 5.938500 | 1.441916 |
| 4 | 5.601708 | 1.504833 |
| 5 | 6.231125 | 1.446250 |
| 6 | 5.856542 | 1.440667 |
| 7 | 6.150583 | 1.437458 |
| 8 | 5.774167 | 1.406875 |
| 9 | 4.141375 | 1.536084 |
| 10 | 5.512459 | 1.443750 |

**Średnie po 10 uruchomieniach:**
* Average java time: **6,30 ms**
* Average native time: **1,48 ms**

### Dłuższe serie testowe
* **100 iteracji:**
    * Average java time: **2,58 ms**
    * Average native time: **1,42 ms**
* **1000 iteracji:**
    * Average java time: **2,20 ms**
    * Average native time: **1,39 ms**

--- 

## Obraz 2 – rozdzielczość 3840x2160

### 1 iteracja
* **Java:** 313.534416 ms
* **Native:** 201.603042 ms

**Średnie:**
* Average java time: **313,53 ms**
* Average native time: **201,60 ms**

### 10 iteracji
| Nr iteracji | Java [ms] | Native [ms] |
|:---:|:---:|:---:|
| 1 | 337.645958 | 197.506917 |
| 2 | 302.257000 | 193.099208 |
| 3 | 316.736375 | 194.572375 |
| 4 | 296.752875 | 195.289083 |
| 5 | 290.833791 | 193.608583 |
| 6 | 291.065250 | 192.713625 |
| 7 | 289.129166 | 192.933291 |
| 8 | 293.265584 | 190.953375 |
| 9 | 289.943334 | 192.331084 |
| 10 | 290.846125 | 193.844666 |

**Średnie po 10 uruchomieniach:**
* Average java time: **299,85 ms**
* Average native time: **193,69 ms**

### 100 iteracji
* Average java time: **302,10 ms**
* Average native time: **195,09 ms**

W testowanych konfiguracjach, implementacja splotu w metodzie natywnej - napisanej w c++ z włączoną optymalizacją -O3 okazała się szybsza w każdym testowanym przypadku. Największe różnica widoczna jest dla mniejszych instancji oraz dla pierwszych iteracji, wtedy gdy metoda Javy nadal jest interpretowana. W momencie, gdy zostanie ona wywołana kilkukrotnie, elementy składajace się na tę metodę pojawiły się już wielokrotnie, przekraczając threshold jaki JIT ma ustawiony na granicę optymalizacji kodu do kodu natywnego. W momencie, gdy kod zostanie zoptymalizowany przez JIT, różnica znacząco maleje, jednak nadal metoda natywna jest szybsza. Różnica między zoptymalizowaną metodą, a tą napisaną natywnie wynosi około 50% na korzyść metody natywnej. 

Momenty optymalizacji konkretnych metod można podejrzeć używając parametru: -XX:+PrintCompilation

Przykład sytuacji, gdy metoda została zoptymalizowana:
```text
Czas wykonania: 5.344834 ms
Czas wykonania: 5.360458 ms
409  346 %     3       image.ConvolutionEngine::convolveJava @ 67 (294 bytes)   made not entrant: OSR invalidation of lower level
409  361       4       image.ConvolutionEngine::convolveJava (294 bytes)
410  348 %     4       image.ConvolutionEngine::convolveJava @ 67 (294 bytes)   made not entrant: uncommon trap
Czas wykonania: 4.129625 ms
Czas wykonania: 5.133 ms
Czas wykonania: 5.136167 ms
Czas wykonania: 4.904042 ms
Czas wykonania: 4.995292 ms
431  347       3       image.ConvolutionEngine::convolveJava (294 bytes)   made not entrant: JVMCI register method
431  362 %     4       image.ConvolutionEngine::convolveJava @ 67 (294 bytes)
Czas wykonania: 5.065292 ms
Czas wykonania: 2.238625 ms
437  363       3       java.lang.String::checkBoundsOffCount (10 bytes)
Czas wykonania: 2.2755 ms
Czas wykonania: 2.118042 ms
Czas wykonania: 2.114375 ms
```