class RMinCut {
	
	static int numvertices_old; // Stores number of vertices of uncut graph
	static int numvertices_current; // Stores number of vertices of graph that has been cut
	static Integer[] V; // Stores vertex set 
	static Integer[][] cut_vertexset; // Stores vertex index of 2 components of graph after cut
	static Integer[][] cut_vertexset_final; // Stores vertex index of 2 components of graph after best cut
	static int[][] adjmatrix_uncut, adjmatrix_current; // Adjacency matrix for uncut and cut graph
	static int iterations; // Number of iterations for an error prob <=0.01
	static int mincut_final; // Stores size of final mincut after all iterations
	
	public static void main(String[] args) {

		// Force args length to be even
		V = new Integer[(args.length/2)*2];
		numvertices_old = 0;
		
		// initialize V[] and count the number of vertices of uncut graph
		for (int i=0; i<V.length; i++) {
			for (int j=0; j<V.length; j++) {
				if (V[j]==null) {
					V[j] = Integer.parseInt(args[i]);
					numvertices_old++;
					break;
				}
				else if (Integer.parseInt(args[i])==V[j]){
					break;
				}
			}
		}
		
		adjmatrix_uncut = new int[numvertices_old][numvertices_old];
		adjmatrix_current = new int[numvertices_old][numvertices_old];
		
		// Fill in the adjacency matrix based on args
		for (int i=0; i<(args.length/2)*2; i++) {
			int v_index = getVertexIndex(Integer.parseInt(args[i]));
			int u_index = getVertexIndex(Integer.parseInt(args[++i]));
			adjmatrix_uncut[v_index][u_index] = 1;
			adjmatrix_uncut[u_index][v_index] = 1;
		}
		// Number of iterations for an error prob <=0.01, (add 1 to round up)
		iterations = (int)((Math.log(1/0.01)*numvertices_old*numvertices_old)/2 + 1);
		
		// Give a placeholder value for mincut_final
		mincut_final = numvertices_old;
		
		// Select the least cut after all iterations
		for (int i=0; i<iterations; i++) {
			int mincut_current = findMinCut();
			if (mincut_current<mincut_final) {
				mincut_final = mincut_current;
				cut_vertexset_final = cut_vertexset;
			}
		}	
		// Print results
		print();
	}
	
	public static int findMinCut() {
		
		// Create a vertexset to store components after cut
		cut_vertexset = new Integer[numvertices_old][numvertices_old];
		
		// fill in cut_vertex  with original vertices
		for (int i=0; i<numvertices_old; i++)
			cut_vertexset[i][0]= V[i];
		
		// Create another instance of adjacency matrix for contraction
		for (int i=0; i<numvertices_old; i++)
			adjmatrix_current[i] = adjmatrix_uncut[i].clone();
		
		// Create new variable to store current number of vertices in each iteration
		numvertices_current = numvertices_old;
		
		int v_index = 0;
		
		// Keep contracting vertices until only 2 vertices are left
		while (numvertices_current>2) {
			v_index = pickRandomIndex();
			int u_index = pickRandomEdge(v_index);
			contract(v_index, u_index);
		}
		
		// Find the min cut by counting the number of edges
		int edges_count = 0;
		for (int i=0; i<numvertices_old; i++) {
			edges_count += adjmatrix_current[v_index][i];
		}
		
		return edges_count;
	}

	// 	Create a supernode
	public static void contract(int v_index, int u_index) {
		// find the next available index
		int i = 0; 
		while (cut_vertexset[v_index][i] != null)
			i++;
		
		// move all labels from u to v and mark u as null; 
		int k=0;
		while (cut_vertexset[u_index][k] != null) {
			cut_vertexset[v_index][i] = cut_vertexset[u_index][k];
			cut_vertexset[u_index][k] = null;
			i++;
			k++;
		}
		// Move u's edges to v's, and remove u's edges
		for (int l=0; l<numvertices_old; l++) {
			adjmatrix_current[l][v_index] += adjmatrix_current[u_index][l];
			adjmatrix_current[v_index][l] += adjmatrix_current[u_index][l];
 
			adjmatrix_current[l][u_index] = 0;
			adjmatrix_current[u_index][l] = 0; 
		}
		
		// Remove any self loops
		for (int m=0; cut_vertexset[v_index][m]!= null; m++) {	
			int index = getVertexIndex(cut_vertexset[v_index][m]);
			adjmatrix_current[v_index][index] = 0;
			adjmatrix_current[index][v_index] = 0;
		}
		
		numvertices_current--;
	}
	
	public static int pickRandomIndex() {
		int random = (int)(Math.random() * numvertices_current);
		int count = -1;
		int r_index;
		// Iterate until the random vertex by going through vertices that are not null 
		for (r_index=0; count<random; r_index++) {
			if (cut_vertexset[r_index][0] != null) 
				count++;
		}
		return r_index-1;
	}
	
	public static int pickRandomEdge(int v_index) {
		
		// Count edges of vertex
		int edges_count = 0;
		for (int i=0; i<numvertices_old; i++) {
			edges_count += adjmatrix_current[v_index][i];
		}
			
		int random1 = (int)(Math.random() * edges_count);
		int count = -1;
		int re_index;
		//Iterate until the random edge 
		for (re_index=0; count<random1; re_index++) {
			count+= adjmatrix_current[v_index][re_index];
		}

		return re_index-1;
	}
	
	// Gets vertex index of any vertex x
	public static int getVertexIndex(int x) {
		int i=0;
		while (V[i]!=x)
			i++;
		
		return i;	
	}
	
	// Prints array
	public static void print(Integer[] array) {
		for (int i=0; i<array.length; i++) {
			if (array[i]!=null)
				System.out.print(array[i] + ",");
		}
		System.out.println();
	}
	
	// Prints results
	public static void print() {
		System.out.println("Size of min-cut: " + mincut_final);
		System.out.print("Component A: ");
		int i=0;
		while (cut_vertexset_final[i][0]==null)
			i++;
		print(cut_vertexset_final[i++]);

		
		System.out.print("Component B: ");
		while (cut_vertexset_final[i][0]==null)
			i++;
		print(cut_vertexset_final[i]);
	}
	

}