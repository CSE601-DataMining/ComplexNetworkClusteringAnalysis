import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Clustering {

	public static void main(String[] args) {
		List<String> textData = null;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter the file name in data folder");
		String file = "";
		try {
			file = br.readLine();
			textData = Files.readAllLines(Paths.get("data/" + file), StandardCharsets.UTF_8);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Data file not found");
			e.printStackTrace();
		}
		
		int m = textData.size();
		int[][] data = new int[m][2];
		int max = 0;
		HashMap<String,Integer> map = new HashMap<String, Integer>();
		int count = 0;
		for (int i = 0; i < m; i++)
		{
			String[] tuple = textData.get(i).split("\\s+");
			if (!map.containsKey(tuple[0]))
			{
				map.put(tuple[0], count++);
			}
			if (!map.containsKey(tuple[1]))
			{
				map.put(tuple[1], count++);
			}
			data[i][0] = map.get(tuple[0]);
			data[i][1] = map.get(tuple[1]);
			if (data[i][0] > max)
				max = data[i][0];
			if (data[i][1] > max)
				max = data[i][1];
		}
		
		System.out.println("Enter Expansion and inflation");
		int exp = 2;
		double inf = 2;
		try {
			String input = br.readLine();
			exp = Integer.parseInt(input.split("\\s")[0]);
			inf = Double.parseDouble(input.split("\\s")[1]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		double [][]adjMat = new double[max+1][max+1];
		
		for (int i = 0; i < m; i++)
		{
			adjMat[data[i][0]][data[i][1]] = 1;
			adjMat[data[i][1]][data[i][0]] = 1;
		}
		
		for (int i = 0; i < adjMat.length; i++) {
			adjMat[i][i] = 1;
		}
		
		adjMat = normalize(adjMat);
		
		while(true)
		{
			double[][] oldMat = adjMat;
			adjMat = expand(adjMat,exp);
			adjMat = inflation(adjMat, inf);
			if(compare(oldMat,adjMat))
				break;
		}
		
/*		for (int i = 0; i < adjMat.length; i++) {
			for (int j = 0; j < adjMat.length; j++) {
				System.out.print(adjMat[i][j] + " ");
			}
			System.out.println();
		}*/
		
		boolean[] covered = new boolean[adjMat.length];
		int[] cluster = new int[adjMat.length];
		
		for (int i = 0; i < cluster.length; i++) {
			cluster[i] = i;
		}
		
		HashMap<Integer,ArrayList<Integer>> attractors = new HashMap<Integer,ArrayList<Integer>>();
		for (int i = 0; i < cluster.length; i++) {
			if (adjMat[i][i] != 0)
				attractors.put(i, new ArrayList<Integer>());
		}
		
		
		for (int i = 0; i < cluster.length; i++) {
			for (int j = 0; j < cluster.length; j++) {
				if (adjMat[j][i] != 0 && adjMat[i][i] != 0)
				{
					attractors.get(cluster[i]).add(j);
					cluster[j] = cluster[i];
					covered[j] = true;
				}
			}
		}
		
		for (Integer attractor : attractors.keySet()) {
			for (int i = 0; i < cluster.length; i++) {
				if (!covered[i] && adjMat[attractor][i] != 0){
					cluster[i] = cluster[attractor];
					covered[i] = true;
				}
			}
		}
		
			/*for (int j = 0; j < covered.length; j++) {
				int parent = -1;
				for (int j2 = 0; j2 < covered.length; j2++) {
					if(!covered[j2] && adjMat[j][j2] != 0 && parent == -1)
					{
						parent = j2;
						covered[j2] = true;
					}
					else if(adjMat[j][j2] != 0 && !covered[j2])
					{
						cluster[j2] = parent;
						covered[j2] = true;
					}
				}
				
			}
		}*/
		
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss");
			PrintWriter wr = new PrintWriter(Paths.get("result/" + formatter.format(new Date()) + ".clu").toString());
			wr.println("*Vertices " + cluster.length);
			
			List<String> netData = null;
			file = file.replace(".txt", ".net");
			netData = Files.readAllLines(Paths.get("data/ForNewPajekFormat/" + file), StandardCharsets.UTF_8);
			
			for (String line: netData) {
				if(!line.contains("\"")) continue;
				int no = map.get(line.substring(line.indexOf('"')+1, line.lastIndexOf('"')));
				wr.println(cluster[no]);
			}
			wr.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}
	
	static boolean compare(double[][] a, double[][] b)
	{
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a.length; j++) {
				if (a[i][j] != b[i][j])
					return false;
			}
		}
		return true;
	}
	
	static double[][] normalize(double[][] a)
	{
		for (int i = 0; i < a.length; i++) {
			double sum = 0;
			for (int j = 0; j < a.length; j++) {
				sum +=a[j][i];
			}
			
			for (int j = 0; j < a.length; j++) {
				a[j][i] = a[j][i]/sum;
			}
		}
		return a;
	}
	
	static double[][] multiply(double[][] a, double[][] b)
	{
		double[][] c = new double[a.length][b.length];
		for (int i = 0; i < b.length; i++) {
			for (int j = 0; j < b.length; j++) {
				for (int j2 = 0; j2 < c.length; j2++) {
					c[i][j]+=a[i][j2]*b[j2][j];
				}
			}
		}
		
		return c;
	}
	
	static double[][] expand(double a[][], int n)
	{
		double[][] c = a;
		for (int i = 0; i < n; i++) {
			c = multiply(c, a);
		}
		
		return c;
	}
	
	static double[][] inflation (double a[][], double factor)
	{
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a.length; j++) {
				a[i][j] = Math.pow(a[i][j], factor);
			}
		}
		
		return normalize(a);
	}

}
