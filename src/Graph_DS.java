package ex0.src;

import java.util.Collection;
import java.util.HashMap;

/**
 * Graph_DS class implements graph interface, that displays a undirectional unweighted graph.
 * this class use a dynamic data structure, which allow to contain a large graph.
 * In addition contains the total actions performed, and the number of edges.
 */
public class Graph_DS implements graph {
    int edges;
    HashMap<Integer, node_data> myGraph = new HashMap<>(10);
    int modeCounter;

    /**
     * construction function, build a graph
     */
    public Graph_DS(){
        this.edges=0;
        this.modeCounter=0;
    }

    /**
     * use to update the ModeCounter from another graph
     * (it used by another function at Graph_Algo class)
     *
     * @param g - other graph
     */
    public void updateModeCounter (graph g){
        this.modeCounter=g.getMC();
    }

    /**
     * return the node_data by the node_id.
     *
     * @param key - the node_id
     * @return the node_data by the node_id, null if none.
     */
    @Override
    public node_data getNode(int key) {
        if(myGraph.containsKey(key))
            return myGraph.get(key);
        return null;
    }

    /**
     * return true iff (if and only if) there is an edge between node1 and node2.
     *
     * @param node1 - key of node 1.
     * @param node2 - key of node 2.
     * @return true iff there is an edge between node1 and node2.
     */
    @Override
    public boolean hasEdge(int node1, int node2) {
        if(node1==node2) return false;
        return myGraph.get(node1).hasNi(node2);
    }

    /**
     * add a new node to the graph with the given node_data.
     *
     * @param n - the node that needs to be added
     */
    @Override
    public void addNode (node_data n) {
        if(n!= null) {
            if (!myGraph.containsKey(n.getKey())) {
                myGraph.put(n.getKey(), n);
                modeCounter++;
            }
        }
    }

    /**
     * Connect an edge between node1 and node2.
     * Note: if the edge node1-node2 already exists - the method simply does nothing.
     */
    @Override
    public void connect(int node1, int node2) {
        if(node1 != node2){ //cannot connect one node to himself
            if(myGraph.containsKey(node1) && myGraph.containsKey(node2)) // check if both of nodes are in the graph
             if(!myGraph.get(node1).hasNi(node2)) {
                 myGraph.get(node1).addNi(myGraph.get(node2));
                 myGraph.get(node2).addNi(myGraph.get(node1));
                 edges++;
                 modeCounter++;
             }
        }
    }

    /**
     * This method return a pointer (shallow copy) for the
     * collection representing all the nodes in the graph.
     *
     * @return Collection<node_data>
     */
    @Override
    public Collection<node_data> getV() {
        return myGraph.values();
    }

    /**
     * This method returns a collection containing all the
     * nodes connected to node_id
     *
     * @return Collection<node_data> - collection containing all the nodes connected to node_id
     */
    @Override
    public Collection<node_data> getV(int node_id) {
        if(myGraph.containsKey(node_id))
            return myGraph.get(node_id).getNi();
        else
            return null;
    }
    /**
     * Delete the node (with the given ID) from the graph -
     * and removes all edges which starts or ends at this node.
     * This method should run in O(n), |V|=n, as all the edges should be removed.
     * @param key - the key of node will be remove.
     */
    @Override
    public node_data removeNode(int key) {
        if(!myGraph.containsKey(key))
            return null;
        else {
            for (node_data it : myGraph.get(key).getNi()) { // scan all neighbors of the node
                it.removeNode(getNode(key));                // each one disconnect this edge ->(it,key)
                modeCounter++;
                edges--;
            }
            return myGraph.remove(key);                     // delete this node from the graph
        }
    }

    /**
     * Delete the edge from the graph,
     * @param node1 - first node.
     * @param node2 - second node.
     */
    @Override
    public void removeEdge(int node1, int node2) {
        if(myGraph.get(node1).hasNi(node2)) { // check that edge is exist
            myGraph.get(node1).removeNode(myGraph.get(node2));
            myGraph.get(node2).removeNode(myGraph.get(node1));
            edges--;
            modeCounter++;
        }
    }

    /** return the number of vertices (nodes) in the graph.
     * @return - sum of edges in the graph.
     */
    @Override
    public int nodeSize() {
        return myGraph.size();
    }

    /**
     * return the number of edges (undirectional graph).
     * @return - sum of edges in the graph.
     */
    @Override
    public int edgeSize() {
        return edges;
    }

    /**
     * return the Mode Count - for testing changes in the graph.
     * Any change in the inner state of the graph should cause an increment in the ModeCount
     * @return - sum of act that occurred in the graph.
     */
    @Override
    public int getMC() {
        return modeCounter;
    }

    /**
     *return print of all nodes in the graph.
     * @return
     */
    public String toString()
    {
        String s = "";
        for(node_data node: myGraph.values())
            s+= node.toString() + "\n";
        return s;

    }
}
