package algorithms;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;

@Slf4j
public class SunGlassRecogniser {
    public static void main(String[] args) throws Exception {

        final String noGlasses = "noGlasses";
        final String sunglasses = "sunglasses";
        final String glasses = "Glasses";

        File f = new File("C:/Users/lenovo/Desktop/straightrnd_train.list.txt");
        File test1 = new File("C:/Users/lenovo/Desktop/straightrnd_test1.list.txt");
        String filePath = f.getAbsolutePath();
        FileInputStream fis = new FileInputStream(filePath);
        fis.close();
        String line;
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        int count1 = 0;


        //TRAINING DATA:
        double[][] expOutput = new double[70][1];
        double[][] data = new double[70][960];

        //going through every file mentioned in the training data list .
        while ((line = br.readLine()) != null) {

            line = line.substring(line.lastIndexOf("ces/") + 4);
            String fp = "C:\\Users\\lenovo\\Desktop\\2-1\\ML\\faces\\" + line;
            FileInputStream fileInputStream = new FileInputStream("C://Users//lenovo//Desktop//2-1//ML//faces//" + line);
            DataInputStream dis = new DataInputStream(fileInputStream);
            if (fp.contains(sunglasses)) {
                expOutput[count1][0] = 1;           //storing the expecting value by reading name of file
            } else {
                expOutput[count1][0] = 0;
            }
            // look for 3 lines (i.e.: the header) and discard them
            int numnewlines = 3;
            while (numnewlines > 0) {
                char c;
                do {
                    c = (char) (dis.readUnsignedByte());
                } while (c != '\n');
                numnewlines--;
            }
            // reading the image data


            int row = 0;


            while (dis.available() > 0) {
                data[count1][row] = ((double) (dis.readUnsignedByte())) / 256;
                row++;
            }

            count1++;
        }

        //TESTING DATA1------------------------------------------------------------------------------------------------------:
        String testPath = test1.getAbsolutePath();
        FileInputStream fis2 = new FileInputStream(testPath);
        fis2.close();
        br = new BufferedReader(new FileReader(testPath));
        count1 = 0;

        double[][] testInput = new double[34][960];
        double[][] expectedTest = new double[34][1];

        //going through every file mentioned in the list for .
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

            // reading the image data
            int row = 0;

            while (dis.available() > 0) {
                testInput[count1][row] = ((double) (dis.readUnsignedByte())) / 256;
                row++;
            }


            //expected output of the test data from file name
            if (fp.contains(sunglasses)) {
                expectedTest[count1][0] = 1;
            } else {
                expectedTest[count1][0] = 0;
            }
            count1++;
        }

        int[] numberOfNodes = new int[3];
        double learnRate = 0.3;
        double moment = 0.3;
        long maxIter = 1000;
        double minError = 0.01;
        numberOfNodes[0] = 960;
        numberOfNodes[1] = 1;
        numberOfNodes[2] = 1;


        BackPropagation bp1 = new BackPropagation(numberOfNodes, data, expOutput, learnRate, moment, minError, maxIter);
        bp1.run();

        double error = 0;
        double accuracy;
        double[][] testOutput = new double[34][1];
        String answer = "";

        //getting Actual output of test from the Algorithm
        for (int i = 0; i < 34; i++) {
            testOutput[i] = bp1.test(testInput[i]);
            for (int j = 0; j < 1; j++) {
                if (testOutput[i][j] < 0.2) {
                    answer = noGlasses;
                } else {
                    answer = glasses;
                }
            }
            //counting data miss-classified
            if (answer.equals(noGlasses) && expectedTest[i][0] == 1 || answer.equals(glasses) && expectedTest[i][0] == 0) {
                error++;
            }

        }


        //TESTING DATA2------------------------------------------------------------------------------------------------------:
        testPath = test1.getAbsolutePath();
        fis2 = new FileInputStream(testPath);
        fis2.close();
        br = new BufferedReader(new FileReader(testPath));
        count1 = 0;

        double[][] testInput2 = new double[52][960];
        double[][] expectedTest2 = new double[52][1];

        //going through every file mentioned in the list for .
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

            // reading the image data

            int row = 0;

            while (dis.available() > 0) {

                testInput2[count1][row] = ((double) (dis.readUnsignedByte())) / 256;
                row++;
            }

            //expected output of the test data from file name
            if (fp.contains("sunglasses")) {
                expectedTest2[count1][0] = 1;
            } else {
                expectedTest2[count1][0] = 0;
            }
            count1++;
        }

        bp1 = new BackPropagation(numberOfNodes, data, expOutput, learnRate, moment, minError, maxIter);
        bp1.run();


        double error2 = 0;
        double accuracy2;
        double[][] testOutput2 = new double[52][1];
        answer = "";

        //getting Actual output of test from the Algorithm
        for (int i = 0; i < 34; i++) {
            testOutput2[i] = bp1.test(testInput2[i]);
            for (int j = 0; j < 1; j++) {
                if (testOutput2[i][j] < 0.2) {
                    answer = noGlasses;
                } else {
                    answer = glasses;
                }
            }
            //counting data miss-classified
            if (answer.equals(noGlasses) && expectedTest2[i][0] == 1 || answer.equals(glasses) && expectedTest2[i][0] == 0) {
                error2++;
            }

        }

        log.info("TrainigError: " + bp1.getError());
        log.info("TrainingAccuracy: " + (100 - bp1.getError()));

        error = error / 34;
        log.info("TestingError1: " + " " + error);
        accuracy = 1 - error;
        log.info("TestingAccuracy1: " + " " + accuracy);

        error2 = error2 / 52;
        log.info("TestingError2: " + " " + error2);
        accuracy2 = 1 - error2;
        log.info("TestingAccuracy2: " + " " + accuracy2);

    }

}
