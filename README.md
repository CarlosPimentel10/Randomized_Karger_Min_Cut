# Randomized Karger Min Cut

##### By: Traci Lim

---

This repository contains an implementation **Karger’s Randomised Minimum Cut** algorithm in JAVA. 

Input: java RMinCut v1 v2 v3 v4 ... 

output: Size of the minimum cut found and the set of vertices on each side of the cut.

Each argument in the input is an integer and the arguments are to be processed in pairs to indicate the edges of the input graph: that is, the edges of the graph are v1v2, v3v4, and so on. If there is an odd number of arguments, the last one is to be ignored. 

There is no a priori bound on the number of vertices in the graph and the labels of the vertices are not necessarily a list of consecutive integers. 

This algorithm repeats Karger’s basic algorithm sufficiently many times to achieve an error probability at most 0.01.