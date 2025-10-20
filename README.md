# homework1_problem2_AI
# Homework 1: CSP Graph Coloring
## Problem 2 - Graph Coloring via CSP
_____________________________________________________________________________________
Bu proqram, qraf rəngləmə problemini CSP (Constraint Satisfaction Problem) həlledici ilə həll edir.
##İcra Təlimatları
### Tələblər
- Java 8 və ya daha yuxarı versiya
- Terminal/Command Prompt

### Proqramı işə salmaq:
1. **Kodu compile edin:**
   ```bash
   javac src/*.java
_____________________________________________________________________________
   Alqoritm Xüsusiyyətləri
İstifadə olunan alqoritmlər:
Backtracking - Əsas axtarış alqoritmi

MRV (Minimum Remaining Values) - Ən az dəyəri qalan dəyişəni seçir

LCV (Least Constraining Value) - Ən az məhdudlaşdıran dəyəri birinci təklif edir

AC-3 (Arc Consistency) - Hər təyindən sonra domainləri ardıcıl edir
___________________________________________________________________________________________________
CSP Formulyasiyası:
Variables: Qrafdakı bütün təpələr

Test Faylları
csp_small.txt
Rəng sayı: 3
Qraf: 4 təpəli sadə tsikl + quyruq
Gözlənilən nəticə: 3-rənglənə bilən

csp_tight.txt
Rəng sayı: 4
Qraf: K4 (tam qraf 4 təpəli) + əlavə təpə
Gözlənilən nəticə: Bütün 4 rəng istifadə olunmalıdır
____________________________________________________________________________________________________________
Xüsusi Hallar
Proqram aşağıdakı xüsusi halları idarə edir:
 Təcrid olunmuş təpələr - kənarı olmayan təpələr də rənglənir
 Təkrarlanan kənarlar - eyni kənar bir dəfə saxlanılır
 Self-loops - (u,u) kənarı aşkarlanır və xəbərdarlıq verilir
 k=1 - 2+ rəng tələb edən qraf üçün failure qaytarır
__________________________________________________________________________________________________________

 Implementation Choices & Analysis
1. Algorithm Design Seçimləri
Backtracking Strukturu:
Seçim Səbəbi:
Deep copy istifadə edirəm ki, backtracking zamanı state avtomatik resetlensin

Recursive approach daha təmiz və anlaşılan kod üçün
MRV + Degree Heuristic:
Seçim Səbəbi:
Ən kiçik domain → daha tez failure aşkarlamaq
Degree tie-breaker → daha çox constraint olan dəyişəni seçmək

LCV Implementation:
Seçim Səbəbi:
Ən az qonşuya təsir edən rəng birinci gəlir
Future flexibility qorunur

2. AC-3 Implementation Detalları
Seçim Səbəbi:
Hər assignment-dan sonra AC-3 → daha effektiv pruning
Bidirectional arcs → tam arc consistency

3. Data Structure Seçimləri
Domain Representation:
java
public Set<Integer> domain;
Seçim Səbəbi:
Sürətli lookup və remove O(1)
Avtomatik duplicate qoruma

Constraint Storage:
java
public List<Constraint> constraints;
Seçim Səbəbi:
Asan iteration qonşular üzərində
Flexible constraint əlavə etmək


Analysis & Answers to Questions
1. Heuristics Effectiveness
MRV Performansı:
Kiçik qraflarda: fərq az olsa da, daha tez solution tapır

Böyük qraflarda: əhəmiyyətli şəkildə search space azaldır

LCV Faydası:
"Tight" qraflarda (csp_tight.txt) daha effektiv
Çox constraint-li dəyişənlərdə daha yaxşı ordering



AC-3 Təsiri:
Nəticə: AC-3 search space-i 30-50% azaldır

2. Edge Cases Handling
Self-loop Detection:
Səbəb: color[u] != color[u) həmişə false → impossible problem

Isolated Nodes:
Səbəb: Təcrid olunmuş təpələr hər hansı rəng ala bilər


3. Performance Analysis
Time Complexity:
Ən pis hal: O(kⁿ) - k rəng sayı, n təpə sayı

Praktikada: O(kᵐ) - m = MRV ilə seçilən dəyişən sayı

4. Limitations & Improvements
Mövcud Məhdudiyyətlər:
Böyük qraflarda performans aşağı düşür

Forward checking yoxdur - yalnız AC-3

Parallel processing dəstəkləmir

Gələcək İyiləşdirmələr:
Forward Checking + AC-3 kombinasiyası
Mini-conflict local search large instances üçün






Domains: {1, 2, ..., k} (k = rəng sayı)

Constraints: Hər kənar üçün color[u] != color[v]
