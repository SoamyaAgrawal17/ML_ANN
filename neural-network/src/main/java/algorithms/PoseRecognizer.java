package algorithms;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;

@Slf4j
public class PoseRecognizer {

    public static void main(String[] args) throws Exception {

        final String right = "RIGHT";
        final String left = "LEFT";
        final String up = "UP";
        final String straight = "STRAIGHT";

        File f = new File("C:\\Users\\lenovo\\Desktop\\all_train.list.txt");
        File test1 = new File("C:\\Users\\lenovo\\Desktop\\all_test1.list.txt");
        File test2 = new File("C:\\Users\\lenovo\\Desktop\\all_test2.list.txt");
        String filePath = f.getAbsolutePath();
        FileInputStream fis = new FileInputStream(filePath);
        fis.close();
        String line;
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        int count1 = 0;

        //TRAINIG DATA:
        double[][] expOutput = new double[277][4];
        double[][] data = new double[277][960];

        //going through every file mentioned in the list.
        while ((line = br.readLine()) != null) {
            line = line.substring(line.lastIndexOf("ces/") + 4);
            String fp = "C:\\Users\\lenovo\\Desktop\\2-1\\ML\\faces\\" + line;
            FileInputStream fileInputStream = new FileInputStream("C:\\Users\\lenovo\\Desktop\\2-1\\ML\\faces\\" + line);
            DataInputStream dis = new DataInputStream(fileInputStream);

            //storing expected output from file name
            if (fp.contains(left)) {
                expOutput[count1][0] = 1;
                expOutput[count1][1] = 0;
                expOutput[count1][2] = 0;
                expOutput[count1][3] = 0;
            } else if (fp.contains(right)) {
                expOutput[count1][0] = 0;
                expOutput[count1][1] = 1;
                expOutput[count1][2] = 0;
                expOutput[count1][3] = 0;
            } else if (fp.contains(up)) {
                expOutput[count1][0] = 0;
                expOutput[count1][1] = 0;
                expOutput[count1][2] = 1;
                expOutput[count1][3] = 0;
            } else if (fp.contains(straight)) {
                expOutput[count1][0] = 0;
                expOutput[count1][1] = 0;
                expOutput[count1][2] = 0;
                expOutput[count1][3] = 1;
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
            // read the image data

            int row = 0;

            while (dis.available() > 0) {
                data[count1][row] = ((double) (dis.readUnsignedByte())) / 256;
                row++;
            }

            count1++;
        }


        //TESTING DATA1-----------------------------------------------------------------------------------------------------
        String testPath = test1.getAbsolutePath();
        FileInputStream fis2 = new FileInputStream(testPath);
        fis2.close();
        br = new BufferedReader(new FileReader(testPath));
        count1 = 0;


        double[][] testInput = new double[139][960];
        double[][] expectedTest = new double[139][4];


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
                testInput[count1][row] = ((double) (dis.readUnsignedByte())) / 256;
                row++;
            }

            //Expected out of test data from file name
            if (fp.contains(left)) {
                expectedTest[count1][0] = 1;
                expectedTest[count1][1] = 0;
                expectedTest[count1][2] = 0;
                expectedTest[count1][3] = 0;
            } else if (fp.contains(right)) {
                expectedTest[count1][0] = 0;
                expectedTest[count1][1] = 1;
                expectedTest[count1][2] = 0;
                expectedTest[count1][3] = 0;
            } else if (fp.contains(up)) {
                expectedTest[count1][0] = 0;
                expectedTest[count1][1] = 0;
                expectedTest[count1][2] = 1;
                expectedTest[count1][3] = 0;
            } else if (fp.contains(straight)) {
                expectedTest[count1][0] = 0;
                expectedTest[count1][1] = 0;
                expectedTest[count1][2] = 0;
                expectedTest[count1][3] = 1;
            }

            count1++;
        }


        int[] numberOfNodes = new int[3];
        double learnRate = 0.3;
        double moment = 0.3;
        long maxIter = 1000;
        double minError = 0.01;
        numberOfNodes[0] = 960;
        numberOfNodes[1] = 6;
        numberOfNodes[2] = 4;


        BackPropagation bp1 = new BackPropagation(numberOfNodes, data, expOutput, learnRate, moment, minError, maxIter);
        bp1.run();
        double[][] testOutput = new double[139][4];
        int index;
        double error = 0;
        double accuracy;

        //getting Actual output of test from the Algorithm
        for (int i = 0; i < 139; i++) {
            index = 0;
            testOutput[i] = bp1.test(testInput[i]);
            double max = 0;
            int maxi = 0;
            for (int j = 0; j < 4; j++) {


                if (testOutput[i][j] > max) {
                    maxi = j;
                    max = testOutput[i][j];
                }
                if (expectedTest[i][j] == 1) {
                    index = j;
                }

            }
            //counting data miss-classified
            if (maxi != index) {
                error++;
            }
        }

        //TESTING DATA2-----------------------------------------------------------------------------------------------------
        testPath = test2.getAbsolutePath();
        fis2 = new FileInputStream(testPath);
        fis2.close();
        br = new BufferedReader(new FileReader(testPath));
        count1 = 0;


        double[][] testInput2 = new double[208][960];
        double[][] expectedTest2 = new double[208][4];


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

                testInput2[count1][row] = ((double) (dis.readUnsignedByte())) / 256;
                row++;
            }

            //Expected out of test data from file name
            if (fp.contains(left)) {
                expectedTest2[count1][0] = 1;
                expectedTest2[count1][1] = 0;
                expectedTest2[count1][2] = 0;
                expectedTest2[count1][3] = 0;
            } else if (fp.contains(right)) {
                expectedTest2[count1][0] = 0;
                expectedTest2[count1][1] = 1;
                expectedTest2[count1][2] = 0;
                expectedTest2[count1][3] = 0;
            } else if (fp.contains(up)) {
                expectedTest2[count1][0] = 0;
                expectedTest2[count1][1] = 0;
                expectedTest2[count1][2] = 1;
                expectedTest2[count1][3] = 0;
            } else if (fp.contains(straight)) {
                expectedTest2[count1][0] = 0;
                expectedTest2[count1][1] = 0;
                expectedTest2[count1][2] = 0;
                expectedTest2[count1][3] = 1;
            }

            count1++;
        }


        bp1 = new BackPropagation(numberOfNodes, data, expOutput, learnRate, moment, minError, maxIter);
        bp1.run();
        double[][] testOutput2 = new double[208][4];
        double error2 = 0;
        double accuracy2;

        //getting Actual output of test from the Algorithm
        for (int i = 0; i < 208; i++) {
            index = 0;
            testOutput2[i] = bp1.test(testInput2[i]);
            double max = 0;
            int maxi = 0;
            for (int j = 0; j < 4; j++) {


                if (testOutput2[i][j] > max) {
                    maxi = j;
                    max = testOutput2[i][j];
                }
                if (expectedTest2[i][j] == 1) {
                    index = j;
                }

            }
            //counting data miss-classified
            if (maxi != index) {
                error2++;
            }
        }

        double trainingError = bp1.getError();
        log.info("Error: " + trainingError);
        double trainingAccuracy = 100 - trainingError;
        log.info("Accuracy: " + trainingAccuracy);

        error = error / 139;
        log.info("TestingError1: " + error);
        accuracy = 1 - error;
        log.info("TestingAccuracy1: " + accuracy);


        error2 = error2 / 208;
        log.info("TestingError2: " + error2);
        accuracy2 = 1 - error2;
        log.info("TestingAccuracy2: " + accuracy2);


    }

}
