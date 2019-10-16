package algorithms;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.Arrays;

@Slf4j
public class FaceRecognizer {


    public static void main(String[] args) throws Exception {

        File f = new File("C:\\Users\\lenovo\\Desktop\\straighteven_train.list.txt");
        File test1 = new File("C:\\Users\\lenovo\\Desktop\\straighteven_test1.list.txt");
        File test2 = new File("C:\\Users\\lenovo\\Desktop\\straighteven_test2.list.txt");
        String filePath = f.getAbsolutePath();
        FileInputStream fis = new FileInputStream(filePath);
        fis.close();
        String line;
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        int count1 = 0;


        //TRAINING DATA
        double[][] expOutput = new double[80][20];
        double[][] data = new double[80][960];

        for (int i = 0; i < 80; i++) {
            Arrays.fill(expOutput[i], 0);
        }
        BufferedReader br2 = new BufferedReader(new FileReader("C:\\Users\\lenovo\\Desktop\\.anonr"));
        String[] names = new String[20];
        int c1 = 0;
        while ((line = br2.readLine()) != null) {
            names[c1] = line;
            c1++;
        }

        //going through every file mentioned in the list.
        while ((line = br.readLine()) != null) {


            line = line.substring(line.lastIndexOf("ces/") + 4);
            String fp = "C:\\Users\\lenovo\\Desktop\\2-1\\ML\\faces\\" + line;
            FileInputStream fileInputStream = new FileInputStream("C:\\Users\\lenovo\\Desktop\\2-1\\ML\\faces\\" + line);
            DataInputStream dis = new DataInputStream(fileInputStream);
            for (int k = 0; k < 20; k++) {
                if (fp.contains(names[k])) {
                    expOutput[count1][k] = 1;
                    break;
                }
            }
            // look for 3 lines (i.e.: the header) and discard them
            int numNewLines = 3;
            while (numNewLines > 0) {
                char c;
                do {
                    c = (char) (dis.readUnsignedByte());
                } while (c != '\n');
                numNewLines--;
            }
            // read the image data

            int row = 0;
            while (dis.available() > 0) {

                data[count1][row] = ((double) (dis.readUnsignedByte())) / 256;
                row++;
            }

            count1++;
        }


        //TESTING DATA 1------------------------------------------------------------------------------------------------
        String testPath = test1.getAbsolutePath();
        FileInputStream fis2 = new FileInputStream(testPath);
        fis2.close();
        br = new BufferedReader(new FileReader(testPath));
        count1 = 0;


        double[][] testInput = new double[36][960];
        double[][] expectedTest = new double[36][20];


        //going through every file mentioned in the list.
        while ((line = br.readLine()) != null) {

            line = line.substring(line.lastIndexOf("ces/") + 4);

            String fp = "C:\\Users\\lenovo\\Desktop\\2-1\\ML\\faces\\" + line;
            FileInputStream fileInputStream = new FileInputStream("C:\\Users\\lenovo\\Desktop\\2-1\\ML\\faces\\" + line);
            DataInputStream dis = new DataInputStream(fileInputStream);

            // look for 3 lines (i.e.: the header) and discard them
            int numNewLines = 3;
            while (numNewLines > 0) {
                char c;
                do {
                    c = (char) (dis.readUnsignedByte());
                } while (c != '\n');
                numNewLines--;
            }
            // read the image data


            int row = 0;

            while (dis.available() > 0) {

                testInput[count1][row] = ((double) (dis.readUnsignedByte())) / 256;
                row++;
            }

            for (int k = 0; k < 20; k++) {
                if (fp.contains(names[k])) {
                    expectedTest[count1][k] = 1;
                    break;
                }
            }
            count1++;
        }

        int[] numberOfNodes = new int[3];
        double learnRate = 0.3;
        double moment = 0.3;
        long maxIter = 50000;
        double minError = 0.01;
        numberOfNodes[0] = 960;
        numberOfNodes[1] = 20;
        numberOfNodes[2] = 20;


        BackPropagation bp1 = new BackPropagation(numberOfNodes, data, expOutput, learnRate, moment, minError, maxIter);
        bp1.run();
        double[][] testOutput = new double[36][20];
        double error = 0;
        //getting Actual output of test from the Algorithm
        for (int i = 0; i < 36; i++) {
            int index = 0;
            testOutput[i] = bp1.test(testInput[i]);
            double max = 0;
            int maxi = 0;
            for (int j = 0; j < 20; j++) {
                if (testOutput[i][j] > max) {
                    maxi = j;
                    max = testOutput[i][j];
                }
                //counting  miss-classified data
                if (expectedTest[i][j] == 1) {
                    index = j;
                }

            }
            if (maxi != index) {
                log.info("test1" + " " + (i + 1) + " " + names[index]);
                error++;
            }

        }


        //TESTING DATA 2------------------------------------------------------------------------------------------------------
        testPath = test2.getAbsolutePath();
        fis2 = new FileInputStream(testPath);
        fis2.close();
        br = new BufferedReader(new FileReader(testPath));
        count1 = 0;


        double[][] test2Input = new double[40][960];
        double[][] expectedTest2 = new double[40][20];


        //going through every file mentioned in the list.
        while ((line = br.readLine()) != null) {
            line = line.substring(line.lastIndexOf("ces/") + 4);
            String fp = "C:\\Users\\lenovo\\Desktop\\2-1\\ML\\faces\\" + line;
            FileInputStream fileInputStream = new FileInputStream("C:\\Users\\lenovo\\Desktop\\2-1\\ML\\faces\\" + line);
            DataInputStream dis = new DataInputStream(fileInputStream);

            // look for 3 lines (i.e.: the header) and discard them
            int numnewlines = 3;
            while (numnewlines > 0) {
                char c;
                do {
                    c = (char) (dis.readUnsignedByte());
                } while (c != '\n');
                numnewlines--;
            }
            // read the image data


            int row = 0;

            while (dis.available() > 0) {

                test2Input[count1][row] = ((double) (dis.readUnsignedByte())) / 256;
                row++;
            }
            
            for (int k = 0; k < 20; k++) {
                if (fp.contains(names[k])) {
                    expectedTest2[count1][k] = 1;
                    break;
                }
            }
            count1++;
        }

        bp1 = new BackPropagation(numberOfNodes, data, expOutput, learnRate, moment, minError, maxIter);
        bp1.run();
        double[][] testOutput2 = new double[40][20];
        double error2 = 0;
        double accuracy2;
        //getting Actual output of test from the Algorithm
        for (int i = 0; i < 40; i++) {
            int index = 0;
            testOutput2[i] = bp1.test(test2Input[i]);
            double max = 0;
            int maxi = 0;
            for (int j = 0; j < 20; j++) {
                if (testOutput2[i][j] > max) {
                    maxi = j;
                    max = testOutput2[i][j];
                }
                //counting  miss-classified data
                if (expectedTest2[i][j] == 1) {
                    index = j;
                }

            }
            if (maxi != index) {
                log.info("test2" + " " + (i + 1) + " " + names[index]);
                error2++;
            }

        }
        
        double trainingError = bp1.getError();
        log.info("TrainError: " + trainingError);
        double accuracy = 100 - trainingError;
        log.info("TrainAccuracy: " + accuracy);

        error = error / 36;
        log.info("TestingError1: " + " " + error);
        accuracy = 1 - error;
        log.info("TestAccuracy1: " + accuracy);

        error2 = error2 / 40;
        log.info("TestingError2: " + " " + error2);
        accuracy2 = 1 - error2;
        log.info("TestAccuracy2: " + accuracy2);


    }

}
