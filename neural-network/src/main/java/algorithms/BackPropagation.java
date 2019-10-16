package algorithms;// The following java code is based on a multi-layer
// Back Propagation Neural Network Class (algorithms.BackPropagation.class)
//
// Created by Anthony J. Papagelis & Dong Soo Kim
//
//  DateCreated:	15 September, 2001
//  Last Update:	24 October, 2001

public class BackPropagation extends Thread {
    // Private Variables

    // Error Function variable that is calculated using the calculateOverallError() function
    private double overallError;

    // The minimum Error Function defined by the user
    private double minimumError;

    // The user-defined expected output pattern for a set of samples
    private double[][] expectedOutput;

    // The user-defined input pattern for a set of samples
    private double[][] input;

    // User defined learning rate - used for updating the network weights
    private double learningRate;

    // Users defined momentum - used for updating the network weights
    private double momentum;

    // Number of layers in the network - includes the input, output and hidden layers
    private int numberOfLayers;

    // Number of training sets
    private int numberOfSamples;

    // Current training set/sample that is used to train network
    private int sampleNumber;

    // Maximum number of Epochs before the traing stops training - user defined
    private long maximumNumberOfIterations;

    // Public Variables
    private Layer[] layer;
    private double[][] actualOutput;

    private boolean die = false;

    // Calculate the node activations
    private void feedForward() {

        int i;

        // Since no weights contribute to the output
        // vector from the input layer,
        // assign the input vector from the input layer
        // to all the node in the first hidden layer
        for (i = 0; i < layer[0].node.length; i++) {
            layer[0].node[i].output = layer[0].input[i];
        }

        layer[1].input = layer[0].input;
        for (i = 1; i < numberOfLayers; i++) {
            layer[i].feedForward();

            // Unless we have reached the last layer, assign the layer i's output vector
            // to the (i+1) layer's input vector
            if (i != numberOfLayers - 1)
                layer[i + 1].input = layer[i].outputVector();
        }

    } // feedForward()

    // Back propagated the network outputy error through
    // the network to update the weight values
    private void updateWeights() {

        calculateSignalErrors();
        backPropagateError();

    }


    private void calculateSignalErrors() {

        int i;
        int j;
        int k;
        int outputLayer;
        double sum;

        outputLayer = numberOfLayers - 1;

        // Calculate all output signal error
        for (i = 0; i < layer[outputLayer].node.length; i++)
            layer[outputLayer].node[i].signalError
                    = (expectedOutput[sampleNumber][i] -
                    layer[outputLayer].node[i].output) *
                    layer[outputLayer].node[i].output *
                    (1 - layer[outputLayer].node[i].output);

        // Calculate signal error for all nodes in the hidden layer
        // (back propagate the errors)
        for (i = numberOfLayers - 2; i > 0; i--) {
            for (j = 0; j < layer[i].node.length; j++) {
                sum = 0;

                for (k = 0; k < layer[i + 1].node.length; k++)
                    sum = sum + layer[i + 1].node[k].weight[j] *
                            layer[i + 1].node[k].signalError;

                layer[i].node[j].signalError
                        = layer[i].node[j].output * (1 -
                        layer[i].node[j].output) * sum;
            }
        }

    }


    private void backPropagateError() {

        int i;
        int j;
        int k;

        // Update Weights
        for (i = numberOfLayers - 1; i > 0; i--) {
            for (j = 0; j < layer[i].node.length; j++) {
                // Calculate Bias weight difference to node j
                layer[i].node[j].thresholdDiff
                        = learningRate *
                        layer[i].node[j].signalError +
                        momentum * layer[i].node[j].thresholdDiff;

                // Update Bias weight to node j
                layer[i].node[j].threshold =
                        layer[i].node[j].threshold +
                                layer[i].node[j].thresholdDiff;

                // Update Weights
                for (k = 0; k < layer[i].input.length; k++) {
                    // Calculate weight difference between node j and k
                    layer[i].node[j].weightDiff[k] =
                            learningRate *
                                    layer[i].node[j].signalError * layer[i - 1].node[k].output +
                                    momentum * layer[i].node[j].weightDiff[k];

                    // Update weight between node j and k
                    layer[i].node[j].weight[k] =
                            layer[i].node[j].weight[k] +
                                    layer[i].node[j].weightDiff[k];
                }
            }
        }
    }


    private void calculateOverallError() {

        int i;
        int j;

        overallError = 0;

        for (i = 0; i < numberOfSamples; i++)
            for (j = 0; j < layer[numberOfLayers - 1].node.length; j++) {

                overallError = overallError + 0.5 * (Math.pow(expectedOutput[i][j] - actualOutput[i][j], 2));
            }
    }


    BackPropagation(int[] numberOfNodes,
                    double[][] inputSamples,
                    double[][] outputSamples,
                    double learnRate,
                    double moment,
                    double minError,
                    long maxIter) {

        int i;
        int j;

        // Initiate variables
        numberOfSamples = inputSamples.length;
        minimumError = minError;
        learningRate = learnRate;
        momentum = moment;
        numberOfLayers = numberOfNodes.length;
        maximumNumberOfIterations = maxIter;

        // Create network layers
        layer = new Layer[numberOfLayers];

        // Assign the number of node to the input layer
        layer[0] = new Layer(numberOfNodes[0], numberOfNodes[0]);

        // Assign number of nodes to each layer
        for (i = 1; i < numberOfLayers; i++)
            layer[i] = new Layer(numberOfNodes[i], numberOfNodes[i - 1]);

        input = new double[numberOfSamples][layer[0].node.length];
        expectedOutput = new double[numberOfSamples][layer[numberOfLayers - 1].node.length];
        actualOutput = new double[numberOfSamples][layer[numberOfLayers - 1].node.length];

        // Assign input set
        for (i = 0; i < numberOfSamples; i++)
            for (j = 0; j < layer[0].node.length; j++)
                input[i][j] = inputSamples[i][j];

        // Assign output set
        for (i = 0; i < numberOfSamples; i++)
            for (j = 0; j < layer[numberOfLayers - 1].node.length; j++)
                expectedOutput[i][j] = outputSamples[i][j];
    }

    private void trainNetwork() {

        int i;
        long k = 0;

        do {
            // For each pattern
            for (sampleNumber = 0; sampleNumber < numberOfSamples; sampleNumber++) {
                for (i = 0; i < layer[0].node.length; i++) {
                    layer[0].input[i] = input[sampleNumber][i];
                }


                feedForward();
                // Assign calculated output vector from network to actualOutput
                for (i = 0; i < layer[numberOfLayers - 1].node.length; i++)
                    actualOutput[sampleNumber][i] =
                            layer[numberOfLayers - 1].node[i].output;
                updateWeights();

                // if we've been told to stop training, then
                // notify the parent that we're exiting
                // then stop thread execution
                if (die) {
                    return;
                }

            }

            k++;
            // Calculate Error Function
            calculateOverallError();
        } while ((overallError > minimumError) && (k < maximumNumberOfIterations));

    }

    // called when testing the network.
    // does not interfere with the error plotting
    // of the applet.
    public double[] test(double[] input) {
        int winner = 0;
        Node[] outputNodes;

        System.arraycopy(input, 0, layer[0].input, 0, layer[0].node.length);

        feedForward();

        // get the last layer of nodes (the outputs)
        outputNodes = (layer[layer.length - 1]).getNodes();
        double[] output = new double[outputNodes.length];

        for (int k = 0; k < outputNodes.length; k++) {
            if (outputNodes[winner].output <
                    outputNodes[k].output) {
                winner = k;
            } // if
        } // for

        for (int i = 0; i < outputNodes.length; i++) {
            output[i] = outputNodes[i].output;
        }
        return output;

    } // test()

    // report the batch error.
    public double getError() {
        calculateOverallError();

        return overallError;
    } // getError()

    // needed to implement threading.
    @Override
    public void run() {
        trainNetwork();
    } // run()

    // to notify the network to stop training.
    public void kill() {
        die = true;
    }


}
