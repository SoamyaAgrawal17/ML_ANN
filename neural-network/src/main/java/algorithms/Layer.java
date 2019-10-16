package algorithms;// The following java code is based on a multi-layer
// Back Propagation Neural Network Class (algorithms.BackPropagation.class)
//
// Created by Anthony J. Papagelis & Dong Soo Kim
//
//  DateCreated:	15 September, 2001
//  Last Update:	14 October, 2001

public class Layer {
    private double net;

    public double[] input;
    // Vector of inputs signals from previous
    // layer to the current layer

    public algorithms.Node[] node;
    // Vector of nodes in current layer

    // The feedForward function is called so that
    // the outputs for all the nodes in the current
    // layer are calculated
    public void feedForward() {
        for (Node aNode : node) {
            net = aNode.threshold;

            for (int j = 0; j < aNode.weight.length; j++)
                net = net + input[j] * aNode.weight[j];

            aNode.output = sigmoid(net);
        }
    }

    // The sigmoid function calculates the
    // activation/output from the current node
    private double sigmoid(double net) {
        return 1 / (1 + Math.exp(-net));
    }


    // Return the output from all node in the layer
    // in a vector form
    public double[] outputVector() {

        double[] vector;

        vector = new double[node.length];

        for (int i = 0; i < node.length; i++)
            vector[i] = node[i].output;

        return (vector);
    }

    Layer(int numberOfNodes, int numberOfInputs) {
        node = new Node[numberOfNodes];

        for (int i = 0; i < numberOfNodes; i++)
            node[i] = new Node(numberOfInputs);

        input = new double[numberOfInputs];
    }

    // added by DSK
    public algorithms.Node[] getNodes() {
        return node;
    }
}