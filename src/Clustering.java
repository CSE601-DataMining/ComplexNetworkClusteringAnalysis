import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Clustering {

	public static void main(String[] args) {
		List<String> textData = null;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter the file name in data folder");
		try {
			String file = br.readLine();
			textData = Files.readAllLines(Paths.get("data/" + file), StandardCharsets.UTF_8);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Data file not found");
			e.printStackTrace();
		}
		
		int m = textData.size();
		int[][] data = new int[m][2];
		int max = 0;
		for (int i = 0; i < m; i++)
		{
			String[] tuple = textData.get(i).split("\\s");

			data[i][0] = Integer.parseInt(tuple[0]);
			data[i][1] = Integer.parseInt(tuple[1]);
			if (data[i][0] > max)
				max = data[i][0];
			if (data[i][1] > max)
				max = data[i][1];
		}
		
		System.out.println("Enter Expansion and inflation");
		int exp = 2, inf = 2;
		try {
			String input = br.readLine();
			exp = Integer.parseInt(input.split("\\s")[0]);
			inf = Integer.parseInt(input.split("\\s")[1]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		double [][]adjMat = new double[max][max];
		
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
			int sum = 0;
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
	
	static double[][] inflation (double a[][], int factor)
	{
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a.length; j++) {
				a[i][j] = Math.pow(a[i][j], factor);
			}
		}
		
		return normalize(a);
	}

}
