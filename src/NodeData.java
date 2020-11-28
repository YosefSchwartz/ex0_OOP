package ex0.src;

import java.util.Collection;
import java.util.HashMap;

/**
 * NodeData class implements node_data interface, that displays a one node.
 * this class use a dynamic data structure, which allow to contain a lot of neighbors nodes.
 * In addition contains index that raise at 1 when create new node, that allow each node have a unique key.
 * and private string data (info) and int data (tag).
 */
public class NodeData implements node_data {
    static int index = 0; // static variable, to be sure that is unique key
    int key;
    HashMap<Integer, node_data> neighbors = new HashMap<>(10);
    int tag;
    String info;

    /**
     * constructor function, build a node
     */
    public NodeData(){
        this.key = index;
        index++;
    }

    /**
     * copy cunstructor function, build another node with same data and key.
     * @param other - the other node.
     */
    public NodeData (node_data other)
    {
        this.key = other.getKey();
        this.tag = other.getTag();
        this.info = other.getInfo();
    }

    /**
     * Return the key (id) associated with this node.
     * @return - unique key of this node.
     */
    @Override
    public int getKey() {
        return key;
    }

    /**
     * This method returns a collection with all the Neighbor nodes of this node_data
     * @return - collection of nodes that connected to this node (neighbors).
     */
    @Override
    public Collection<node_data> getNi() {
        return neighbors.values();
    }

    /**
     * return true iff this<==>key are adjacent, as an edge between them.
     * @param key - key of node that we will check if exist edge between there.
     * @return - True if exist, False if doesn't.
     */
    @Override
    public boolean hasNi(int key) {
        return neighbors.containsKey(key);
    }
    /** This method adds the node_data (t) to this node_data neighbors.
     */
    @Override
    public void addNi(node_data t) {
        if(!neighbors.containsValue(t)) // check maybe this already in the neighbors
            neighbors.put(t.getKey(),t);
    }

    /**
     * Removes the edge this-node,
     * @param node - we want to delete from the neighbors.
     */
    @Override
    public void removeNode(node_data node) {
        if(neighbors.containsValue(node)) //check if is really neighbors
            neighbors.remove(node.getKey());
    }

    /**
     * return the remark (meta data) associated with this node.
     * @return - Info, string data.
     */
    @Override
    public String getInfo() {
        return info;
    }

    /**
     * Allows changing the remark (meta data) associated with this node.
     * @param s - info
     */
    @Override
    public void setInfo(String s) {
        info = s;
    }

    /**
     * inner int data to use forward
     * @return
     */
    @Override
    public int getTag() {
        return tag;
    }

    /**
     * Allow setting the "tag" value for temporal marking an node - common
     * practice for marking by algorithms.
     * @param t - the new value of the tag
     */
    @Override
    public void setTag(int t) {
        tag = t;
    }

    /**
     *return print of node key and his list neighbors, also print the edges are connected to this node.
     * @return
     */
    public String toString ()
    {
        String s = "The node key #" + key + "\n\t\tThe neighbors are: [";

        int tmp = 1;
        for (int it: neighbors.keySet()) {
            s += "" + it;
            if (tmp < neighbors.size())
                s += ",";
            tmp++;
        }
        s+="]";

        s+="\n\t\t The edges are: ";
        tmp=1;
        for (int it: neighbors.keySet()) {
            s += "(" + key + "," + neighbors.get(it).getKey() + ")";
            if (tmp < neighbors.size())
                s += ",";
            tmp++;
        }
        return s+"\n";
    }
}
