package com.justinb.ramwal.util;

import java.util.ArrayList;

public class Node<T> {
    ArrayList<T> data;
    ArrayList<Node<T>> groups;

    public Node(ArrayList<T> d, ArrayList<Node<T>> g) {
        data = d;
        groups = g;
    }

    public ArrayList<Node<T>> invalidate() {
        ArrayList<Node<T>> remove = new ArrayList<>();

        for (Node<T> n : groups) {
            remove.add(n);
            groups.remove(n);
        }

        return remove;
    }

    public boolean isInvalid() {
        return groups.size() == 0;
    }

    public void runTest(NodeRunnable<T> runnable) {
        runnable.run(data);
    }

    public int getDepth() {
        return getDepth(0);
    }

    private int getDepth(int count) {
        if (isInvalid()) return count;

        return groups.iterator().next().getDepth(count+1);
    }

    public int nodesAtDepth(int depth) {
        return (int) Math.pow(groups.size(), depth);
    }

    public Node<T> getNode(int depth, int index) throws Exception {
        int size = groups.size();
        int range = nodesAtDepth(depth) - 1;

        if (index > range) throw new Exception("Index " + index + " is out of range of " + range);
        if (depth > getDepth()) throw new Exception("Depth must be at or below " + getDepth());

        ArrayList<Integer> path = new ArrayList<>();
        int sub = index;

        for (int i = 0; i < depth; i++) {
            sub = (int) Math.ceil(((double) sub) / size);

            path.add(0, sub);
        }

        Node<T> s = this;

        for (int h = 0; h < depth; h++)
            s = s.groups.get(path.get(h));

        return s;
    }

    public static <G> Node<G> createTree(ArrayList<G> data, int groupNum) {
        if (data.size() % groupNum != 0) return new Node<>(data, new ArrayList<>());

        int each = data.size() / groupNum;

        ArrayList<Node<G>> nodes = new ArrayList<>();

        for (int g = 0; g < groupNum; g++) {
            ArrayList<G> smallData = new ArrayList<>();

            for (int i = g * each; i < (g + 1) * each; i++)
                smallData.add(data.get(i));

            nodes.add(createTree(smallData, groupNum));
        }

        return new Node<>(data, nodes);
    }

    interface NodeRunnable<T> {
        void run(ArrayList<T> data);
    }
}
