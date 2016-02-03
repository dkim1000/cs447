import java.util.Set;
import java.util.Map;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Stack;
import org.objectweb.asm.commons.*;
import org.objectweb.asm.tree.*;

public class CFG {
    Set<Node> nodes = new HashSet<Node>();
    Map<Node, Set<Node>> edges = new HashMap<Node, Set<Node>>();

    static class Node {
        int position;
        MethodNode method;
        ClassNode clazz;

        Node(int p, MethodNode m, ClassNode c) {
            position = p; method = m; clazz = c;
        }

        public boolean equals(Object o) {
            if (!(o instanceof Node)) return false;
            Node n = (Node)o;
            return (position == n.position) &&
            method.equals(n.method) && clazz.equals(n.clazz);
        }

        public int hashCode() {
            return position + method.hashCode() + clazz.hashCode();
        }

        public String toString() {
            return clazz.name + "." +
            method.name + method.signature + ": " + position;
        }
    }

    public void addNode(int p, MethodNode m, ClassNode c) {
        Node newNode = new Node(p, m, c);

        //if p already exists, do nothing
        if (nodes.contains(newNode))
            return;

        //otherwise add the node and add an empty edge set

        //adding to nodes
        nodes.add(newNode);

        //adding to edges

        //check if already exists in edges
        if (edges.containsKey(newNode))
            return;

        //if doesn't exists, add it to edges
        Set<Node> n = new HashSet<Node>();
        edges.put(newNode, n);

    }

    public void addEdge(int p1, MethodNode m1, ClassNode c1,
			int p2, MethodNode m2, ClassNode c2) {

        //try adding the nodes. It will do all existence check
        addNode(p1,m1,c1);
        addNode(p2,m2,c2);

        Node one = new Node(p1, m1, c1);
        Node two = new Node(p2, m2, c2);

        //always will exist since added at the beginning
        Set<Node> p1EdgeList = edges.get(one);

        //check if p1 has edge to p2
        if (!p1EdgeList.contains(two)) {
            edges.get(one).add(two);
        }

    }
	
	public void deleteNode(int p, MethodNode m, ClassNode c) {
        Node deleteThisNode = new Node(p, m, c);

        //deleting from nodes
        if (nodes.contains(deleteThisNode)) {
            //delete the node
            nodes.remove(deleteThisNode);
        }

        //deleting from edges map
        edges.remove(deleteThisNode);

        //deleting the edges that connect to this
        for (Set<Node> edgePairs : edges.values()) {
            if (edgePairs.contains(deleteThisNode))
                edgePairs.remove(deleteThisNode);
        }
    }
	
    public void deleteEdge(int p1, MethodNode m1, ClassNode c1,
						int p2, MethodNode m2, ClassNode c2) {
        Node one = new Node(p1, m1, c1);
        Node two = new Node(p2, m2, c2);

        if (!nodes.contains(one))
            return;
        if (!nodes.contains(two))
            return;

        if (edges.containsKey(one)) {
            edges.get(one).remove(two);
        }
    }
	

    public boolean isReachable(int p1, MethodNode m1, ClassNode c1,
			       int p2, MethodNode m2, ClassNode c2) {
        Node one = new Node(p1, m1, c1);
        Node two = new Node(p2, m2, c2);

        if (!nodes.contains(one))
            return false;
        if (!nodes.contains(two))
            return false;

        //do a breadth first search over the CFG
        Stack<Node> queue = new Stack<Node>();
        queue.add(one);

        while (!queue.isEmpty()) {
            Node curNode = queue.pop();

            if (two.equals(curNode))
                return true;

            if (!edges.get(curNode).isEmpty()) {
                for (Node n : edges.get(curNode)) {
                    queue.add(n);
                }
            }
        }

    	return false;
    }
}
