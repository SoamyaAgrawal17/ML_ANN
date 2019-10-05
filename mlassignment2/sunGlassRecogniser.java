package A;


import java.io.*;
import java.util.*;

public class sunGlassRecogniser 
{
	

	public static void main(String[] args) throws Exception 
	{
		
		File f = new File("C:/Users/lenovo/Desktop/straightrnd_train.list.txt");
		File test1 = new File("C:/Users/lenovo/Desktop/straightrnd_test1.list.txt");
		File test2 = new File("C:/Users/lenovo/Desktop/straightrnd_test2.list.txt");
		String filePath = f.getAbsolutePath();
		FileInputStream fis = new FileInputStream(filePath);
		fis.close();
		String line;
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		int count1=0;
		
		
		//TRAINING DATA:
		double[][] expOutput = new double[70][1];
		double[][] data = new double[70][960];
		
		//going through every file mentioned in the training data list .
		while((line = br.readLine()) != null)
		{
			int count = 0;
			
			line = line.substring(line.lastIndexOf("ces/")+4);
			//System.out.println(line);
			String fp = "C:\\Users\\lenovo\\Desktop\\2-1\\ML\\faces\\" + line;
			FileInputStream fileInputStream = new FileInputStream("C://Users//lenovo//Desktop//2-1//ML//faces//"+line);
			 DataInputStream dis = new DataInputStream(fileInputStream);
			 if(fp.contains("sunglasses"))
			 {
				 expOutput[count1][0] = 1;           //storing the expecting value by reading name of file
			 }
			 else
			 {
				 expOutput[count1][0] = 0;
			 }
			 // look for 3 lines (i.e.: the header) and discard them
			 int numnewlines = 3;
			 while (numnewlines > 0) {
			     char c;
			     do {
			         c = (char)(dis.readUnsignedByte());
			     } while (c != '\n');
			     numnewlines--;
			 }
		    // reading the image data 
			 
			 count = 0;
			 int row = 0;
			 
			
			 while (dis.available()>0) 
			 {			        
			    	 count++;
			         data[count1][row] = ((double)(dis.readUnsignedByte()))/256;
			         //System.out.print(data[count1][row] + " ");
			         row++;	
			 }
			 
			count1++;
		}
		
		//TESTING DATA1------------------------------------------------------------------------------------------------------:
		String testPath = test1.getAbsolutePath();
		FileInputStream fis2 = new FileInputStream(testPath);
		fis2.close();
		br = new BufferedReader(new FileReader(testPath));
		count1=0;
		
		double[][] testInput = new double[34][960];
		double expectedTest[][]=new double[34][1];
		
		//going through every file mentioned in the list for .
		while((line = br.readLine()) != null)
		{
			int count = 0;
			
			line = line.substring(line.lastIndexOf("ces/")+4);
			
			String fp = "C:\\Users\\lenovo\\Desktop\\2-1\\ML\\faces\\" + line;
			FileInputStream fileInputStream = new FileInputStream("C:\\Users\\lenovo\\Desktop\\2-1\\ML\\faces\\"+line);
			 DataInputStream dis = new DataInputStream(fileInputStream);
			 
			 // look for 3 lines (i.e.: the header) and discard them
			 int numnewlines = 3;
			 while (numnewlines > 0) {
			     char c;
			     do {
			         c = (char)(dis.readUnsignedByte());
			     } while (c != '\n');
			     numnewlines--;
			 }
			 
		    // reading the image data
			 count = 0;
			 int row = 0;
			
			 while (dis.available()>0) 
			 {			        
			    	 count++;
			         testInput[count1][row] = ((double)(dis.readUnsignedByte()))/256;
			         //System.out.print(testInput[count1][row] + " ");
			         row++;	
			 }
			 //System.out.println("\nCOUNT: " + count);
			 
			 
			//expected output of the test data from file name
			 if(fp.contains("sunglasses"))
			 {
				 expectedTest[count1][0] = 1;     
			 }
			 else
			 {
				 expectedTest[count1][0] = 0;
			 }
			count1++;
		}
		
		int[] NumberOfNodes = new int[3];  
		double LearnRate = 0.3;
		double Moment = 0.3;
		long MaxIter = 1000;
		double MinError = 0.01;
		NumberOfNodes[0] = 960;
		NumberOfNodes[1] =1;
		NumberOfNodes[2] =1;
		
		
		
		BackPropagation bp1 = new BackPropagation(NumberOfNodes,data,expOutput,LearnRate,Moment,MinError,MaxIter);
		bp1.run();


		double Error=0,Accuracy;
		double[][] testOutput = new double[34][1];
		String answer = new String();
		
		//getting Actual output of test from the Algorithm
		for(int i=0;i<34;i++)
		{
			testOutput[i] = bp1.test(testInput[i]); 
			for(int j=0;j<1;j++)
			{
				//System.out.println("\n" + i + ")" + "ANS: "+ testOutput[i][j]);
				if(testOutput[i][j]<0.2)
				{
					answer = "noGlasses";
				}
				else
				{
					answer = "Glasses";
				}
			}
			//counting data miss-classified
			if(answer.equals("noGlasses")&&expectedTest[i][0]==1||answer.equals("Glasses")&&expectedTest[i][0]==0) 
			{
				Error++; 
			}
			
		}
		
		
		
		
		
		//TESTING DATA2------------------------------------------------------------------------------------------------------:
			     testPath = test1.getAbsolutePath();
				fis2 = new FileInputStream(testPath);
				fis2.close();
				br = new BufferedReader(new FileReader(testPath));
				count1=0;
				
				double[][] testInput2 = new double[52][960];
				double expectedTest2[][]=new double[52][1];
				
				//going through every file mentioned in the list for .
				while((line = br.readLine()) != null)
				{
					
					
					line = line.substring(line.lastIndexOf("ces/")+4);
					
					String fp = "C:\\Users\\lenovo\\Desktop\\2-1\\ML\\faces\\" + line;
					FileInputStream fileInputStream = new FileInputStream("C:\\Users\\lenovo\\Desktop\\2-1\\ML\\faces\\"+line);
					 DataInputStream dis = new DataInputStream(fileInputStream);
					 
					 // look for 3 lines (i.e.: the header) and discard them
					 int numnewlines = 3;
					 while (numnewlines > 0) {
					     char c;
					     do {
					         c = (char)(dis.readUnsignedByte());
					     } while (c != '\n');
					     numnewlines--;
					 }
					 
				    // reading the image data
					
					 int row = 0;
					
					 while (dis.available()>0) 
					 {			        
					
					         testInput2[count1][row] = ((double)(dis.readUnsignedByte()))/256;
					         //System.out.print(testInput[count1][row] + " ");
					         row++;	
					 }
					 //System.out.println("\nCOUNT: " + count);
					 
					 
					//expected output of the test data from file name
					 if(fp.contains("sunglasses"))
					 {
						 expectedTest2[count1][0] = 1;     
					 }
					 else
					 {
						 expectedTest2[count1][0] = 0;
					 }
					count1++;
				}
				
				bp1 = new BackPropagation(NumberOfNodes,data,expOutput,LearnRate,Moment,MinError,MaxIter);
				bp1.run();


				double Error2=0,Accuracy2;
				double[][] testOutput2 = new double[52][1];
				answer = new String();
				
				//getting Actual output of test from the Algorithm
				for(int i=0;i<34;i++)
				{
					testOutput2[i] = bp1.test(testInput2[i]); 
					for(int j=0;j<1;j++)
					{
						//System.out.println("\n" + i + ")" + "ANS: "+ testOutput[i][j]);
						if(testOutput2[i][j]<0.2)
						{
							answer = "noGlasses";
						}
						else
						{
							answer = "Glasses";
						}
					}
					//counting data miss-classified
					if(answer.equals("noGlasses")&&expectedTest2[i][0]==1||answer.equals("Glasses")&&expectedTest2[i][0]==0) 
					{
						Error2++; 
					}
					
				}
				

		
		
		
		
		
		double error = bp1.get_error();
		System.out.println("TrainigError: " + error);
		double accuracy = 100-error;
		System.out.println("TrainingAccuracy: " + accuracy);
		
		Error=Error/34;
		System.out.println("TestingError1: "+" "+Error);
		Accuracy=1-Error;
		System.out.println("TestingAccuracy1: "+" "+Accuracy);

		Error2=Error2/52;
		System.out.println("TestingError2: "+" "+Error2);
		Accuracy2=1-Error2;
		System.out.println("TestingAccuracy2: "+" "+Accuracy2);

	}

}
