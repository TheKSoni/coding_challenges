package hackerRank.tree;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

@SuppressWarnings("CommentedOutCode")
class Result {
    /*
     * https://www.hackerrank.com/challenges/swap-nodes-algo/problem?isFullScreen=true
     * Complete the 'swapNodes' function below.
     *
     * The function is expected to return a 2D_INTEGER_ARRAY.
     * The function accepts following parameters:
     *  1. 2D_INTEGER_ARRAY indexes
     *  2. INTEGER_ARRAY queries
     */


    public static List<List<Integer>> swapNodes(List<List<Integer>> indexes, List<Integer> queries) {

        List<List<Integer>> result = new ArrayList<>();
        List<Node> tree = createTree(indexes);
        System.out.println("print init tree");
        printTree(tree.get(0), new ArrayList<>());
        System.out.println();
        Map<Integer, List<Node>> nodesAtLevel = createLevelMap(tree);

        for (Integer qInt : queries) {
            System.out.println("result for query " + qInt);
            List<Integer> r = new ArrayList<>();
            processQuery(nodesAtLevel, qInt);
            result.add(printTree(tree.get(0), r));

        }

        return result;
    }

    private static List<Node> createTree(List<List<Integer>> indexes) {
        List<Node> nodes = new ArrayList<>();

        // Set Root
        Node root = new Node(1, null);
        nodes.add(root);

        int currentParentIndex = 0;
        // System.out.println("parent index is " + currentParentIndexNode);

        for (List<Integer> index : indexes) {
            Node currentParentIndexNode = nodes.get(currentParentIndex);
            if (index.get(0) != -1) {
                Node left = new Node(index.get(0), currentParentIndexNode);
                nodes.add(left);
                currentParentIndexNode.setLeft(left);
                // System.out.println("Left index is " + left);

            }
            if (index.get(1) != -1) {
                Node right = new Node(index.get(1), currentParentIndexNode);
                nodes.add(right);
                currentParentIndexNode.setRight(right);
                // System.out.println("Right index is " + right);

            }
            currentParentIndex++;
        }
        System.out.println("nodes size = " + nodes.size());
        return nodes;
    }

    private static List<Integer> printTree(Node node, List<Integer> indexes) {
        if (node.getLeft() != null) {
            printTree(node.getLeft(), indexes);
        }

        System.out.print(node.getIndex() + " ");
        indexes.add(node.getIndex());

        if (node.getRight() != null) {
            printTree(node.getRight(), indexes);
            System.out.println(" ");
        }
        return indexes;
    }

    private static void processQuery(Map<Integer, List<Node>> nodesAtLevel, int qInt) {
        // System.out.println("nodes Levels = " + nodesAtLevel.size());
        // System.out.println("qInt = " + qInt);
        // System.out.println("nodesAtLevel.get(" + qInt +") = " + nodesAtLevel.get(qInt));
        for (int i = 1; i < nodesAtLevel.size(); i++) {

            if (qInt * i < nodesAtLevel.size() && nodesAtLevel.get(qInt * i) != null) {
                // System.out.println("nodesAtLevel.get(" + qInt * i+ ") = " + nodesAtLevel.get(qInt * i).get(0));

                for (Node sNode : nodesAtLevel.get(qInt * i)) {
                    Node temp = sNode.getLeft();
                    sNode.setLeft(sNode.getRight());
                    sNode.setRight(temp);
                }
            }
        }

    }

    private static Map<Integer, List<Node>> createLevelMap(List<Node> tree) {
        Map<Integer, List<Node>> map = new HashMap<>();

        for (Node node : tree) {
            // System.out.println("node = " + node);
            // System.out.println("node level= " + node.getLevel());
            List<Node> nodeList = map.get(node.getLevel());
            if (nodeList == null) {
                nodeList = new ArrayList<>();
            }
            nodeList.add(node);
            map.put(node.getLevel(), nodeList);
        }

        return map;

    }

}

class Node {
    private final Integer index;
    private Node parent;
    private int level = 1;
    private Node right;
    private Node left;

    Node(int index, Node parent) {
        this.index = index;
        if (parent != null) {
            this.parent = parent;
            this.level = parent.getLevel() + 1;
        }
    }

    public int getIndex() {
        return index;
    }

    public Node getLeft() {
        return left;
    }

    public Node getRight() {
        return right;
    }

    public int getLevel() {
        return level;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    @Override
    public String toString() {
        return "index = " + index + " parent index = " + ((parent == null) ? "" : parent.getIndex()) + " level = " + level + " left = " + left + " right = " + right;
    }
}

public class SwapNodes {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        int n = Integer.parseInt(bufferedReader.readLine().trim());

        List<List<Integer>> indexes = new ArrayList<>();

        IntStream.range(0, n).forEach(i -> {
            try {
                indexes.add(Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" ")).map(Integer::parseInt).collect(toList()));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        int queriesCount = Integer.parseInt(bufferedReader.readLine().trim());

        List<Integer> queries = IntStream.range(0, queriesCount).mapToObj(i -> {
            try {
                return bufferedReader.readLine().replaceAll("\\s+$", "");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }).map(String::trim).map(Integer::parseInt).collect(toList());

        List<List<Integer>> result = Result.swapNodes(indexes, queries);

        result.stream().map(r -> r.stream().map(Object::toString).collect(joining(" "))).map(r -> r + "\n").collect(toList()).forEach(e -> {
            try {
                bufferedWriter.write(e);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        bufferedReader.close();
        bufferedWriter.close();
    }

}
