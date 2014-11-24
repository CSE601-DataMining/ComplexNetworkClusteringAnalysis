#### General Descriptions ####
- All the data you need for HW 3 have been generated in this directory.
- You may open them in Excel to have a better view. 
- There are three files, xxx.txt, xxx.paj, and xxx.pdf, for each dataset.
- You need to work on xxx.txt as an input of your program for clustering and visualization.
- You may choose one of any visualization tools, e.g. Pajek and Cytoscape.
- xxx.txt, xxx.paj, and xxx.pdf are for dataset, Pajek visualization example, and clustering result, respectively.
- OutputManager.java is an example Java source file for generating xxx.paj file for the visualization on Pajek. (Please note that the code is not executable by itself.)

#### Process on generating Pajek File and Figure from xxx.txt ####
1. Load xxx.txt into your program.
2. Create a data (network) structure in your program based on data in xxx.txt.
3. Run your clustering algorithm.
4. Write an output file (xxx.paj) in terms of the Pajek input file format described in the following section.
5. Load xxx.paj with Pajek.
6. Select "Draw" from Main menu in Pajek.
7. Reorganize the layout of a network figure by selecting "Layout-Energy-KamadaKawai-Free" from Figure Menu.
8. Export a network figure into xxx.bmp or xxx.eps file by selecting "Export-2D-Bitmap or Export-2D-EPS/PS" from Figure Menu.

#### Pajek Input File Data Format ####
The file format accepted by Pajek provides information on the vertices, arcs (directed edges), and undirected edges. A short example showing the file format is given below:
-------------------------------------
*Vertices 3
1 "Doc1" 0.0 0.0 0.0 ic Green bc Brown
2 "Doc2" 0.0 0.0 0.0 ic Green bc Brown
3 "Doc3" 0.0 0.0 0.0 ic Green bc Brown 
*Arcs
1 2 3 c Green
2 3 5 c Black
*Edges
1 3 4 c Green
-------------------------------------
Herein there are 3 vertices Doc1, Doc2 and Doc3 denoted by numbers 1, 2 and 3. The (fill) color of these nodes is Green and the border color is Brown. The initial layout location of the nodes is (0,0,0). Note that the (x,y,z) values can be changed interactively after drawing.
More information can be found at "http://pajek.imfm.si/doku.php".

#### "attweb_net.txt" Dataset ####
- The file is space-delimited.
- It is an AT&T Web network data consisting of 180 nodes and 228 edges.
- A node represents for a computer and an edge represents for a connection.
- There are two columns and each column has one specific computer. (Names are serialized.) 
- A computer can be shown multiple times in the dataset.
- A row represents one specific computer interaction.

#### "physics_collaboration_net.txt" Dataset ####
- The file is space-delimited.
- It is a physics collaboration network data consisting of 142 nodes and 340 edges.
- A node represents for a physicist and an edge represents for a research collaboration.
- There are two columns and each column has one specific person.
- A person can be shown multiple times in the dataset.
- A row represents one specific collaboration interaction.

#### "yeast_undirected_metabolic.txt" Dataset ####
- The file is tab-delimited.
- It is a yeast metabolic network consisting of 359 nodes and 435 edges.
- A node represents for a biochemical compound and an edge represents for a biochemical interaction.
- There are two columns and each column has one specific compound. (Names are serialized.) 
- A compound can be shown multiple times in the dataset.
- A row represents one specific biochemical interaction.
