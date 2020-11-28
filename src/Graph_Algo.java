package ex0.src;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Graph_Algo class implements graph_algorithms interface, that displays some algorithms in the graph.
 * this class use just a graph to perform the algorithms about it

 * This class represents the "regular" Graph Theory algorithms including:
 * 0. clone(); (copy)
 * 1. init(graph);
 * 2. isConnected();
 * 3. int shortestPathDist(int src, int dest);
 * 4. List<node_data> shortestPath(int src, int dest);
 */
public class Graph_Algo implements graph_algorithms {
    graph ga;

    // no document because its a private function!
    /*
    reset the tag field in all graph's nodes
     */
    private void resetTag(graph ga) {
        for(node_data tmpNode : ga.getV())
            tmpNode.setTag(0);
    }
    // no document because its a private function!
    /*
    reset the info field in all graph's nodes
     */
    private void resetInfo(graph ga) {
        for(node_data tmpNode : ga.getV())
            tmpNode.setInfo(null);
    }

    /**
     * Init the graph on which this set of algorithms operates on.
     * @param g - graph.
     */
    @Override
    public void init(graph g) {
        this.ga = g;
    }

    /**
     * Compute a deep copy of this graph.
     * @return - new graph, copy of this graph
     */
    @Override
    public graph copy() {
        Graph_DS gaCopy = new Graph_DS();

        for(node_data graphNode : ga.getV()) {          // copy all nodes
            node_data tmpNode = new NodeData(graphNode);
            gaCopy.addNode(tmpNode);
        }
        for(node_data nodeSource : ga.getV())                   //for each node-
            for (node_data nodeDest:nodeSource.getNi())         // scan all the neighbors
                gaCopy.connect(nodeSource.getKey(),nodeDest.getKey());// and connect them.

        gaCopy.updateModeCounter(ga); // update the correct mode counter
        /*
        note: its must to update the mode counter here because maybe the original graph
        was exist, and we get a graph with some change like remove and add, and now the
        mode counter diffrent.
        if we are want a complete copy of the original graph we need also update his mode counter.
        */
        return gaCopy;
    }

    /**
     * Returns true if and only if (iff) there is a valid path from EVREY node to each
     * other node. NOTE: assume ubdirectional graph.
     * @return - True iff is a valid path.
     */
    @Override
    public boolean isConnected() {
        if(ga==null) return false;          //2 stop conditions
        if(ga.nodeSize()==0) return true;

        // we use this field, need to reset it before use to avoid error.
        resetTag(ga);

        //use BFS algorithm, some node to start.
        node_data tmp = ga.getV().stream().findFirst().orElse(new NodeData());

        //BFS algorithms
        //https://en.wikipedia.org/wiki/Breadth-first_search
        List<node_data> nodes = new ArrayList<>();
        final int mark = 1;
        tmp.setTag(mark);
        nodes.add(tmp);
        int pointer = 0;
        while (pointer<nodes.size()) {
            for(node_data tmpNi:ga.getV(nodes.get(pointer).getKey())) {
                if(tmpNi.getTag()!=mark) {
                    nodes.add(tmpNi);
                    tmpNi.setTag(mark);
                }
            }
            pointer++;
        }
        // check if collection of the nodes are connected to start node
        // are have sam size to graph nodes size.
        return  (nodes.size()==ga.nodeSize());
    }

    // no document because its a private function!
    /*
    check if path between to nodes is exist
    use same algorithms like isConnected()
     */
    private boolean isConnected(node_data src, node_data dest)
    {
        if(ga==null) return false;
        if(ga.nodeSize()==0) return true;

        if(src==dest) return true;

        resetTag(ga);
        List<node_data> nodes = new ArrayList<>();
        final int mark = 1;
        src.setTag(mark);
        nodes.add(src);
        int pointer = 0;
        while (pointer<nodes.size()) {
            for(node_data tmpNi:ga.getV(nodes.get(pointer).getKey())) {
                if(tmpNi.getTag()!=mark) {
                    nodes.add(tmpNi);
                    tmpNi.setTag(mark);
                }
            }
            pointer++;
        }
        //we start from source node, and this line check if destination node are in
        //the collection that we can get from source node.
        return (nodes.contains(dest));
    }

    /**
     * returns the length of the shortest path between src to dest
     * Note: if no such path --> returns -1
     * @param src - start node
     * @param dest - end (target) node
     * @return
     */
    @Override
    public int shortestPathDist(int src, int dest) {
        if (ga.getNode(src) != null && ga.getNode(dest) != null) {//check the src and dest are exist in the graph
            if(src==dest) return 0;
            if (!isConnected(ga.getNode(src), ga.getNode(dest))) // no valid path
                return -1;
            return (shortestPath(src, dest).size()) - 1; // return the path size (size of shortestPath -1)
        }
        return -1;
    }

    /**
     * returns the the shortest path between src to dest - as an ordered List of nodes:
     * src--> n1-->n2-->...dest
     * see: https://en.wikipedia.org/wiki/Shortest_path_problem
     * Note if no such path --> returns null;
     * @param src - start node
     * @param dest - end (target) node
     * @return - list of the shortest path between src to dest.
     */
    @Override
    public List<node_data> shortestPath(int src, int dest) {
        // reset tag and info fields before start
        resetTag(ga);
        resetInfo(ga);

        List<node_data> path = new ArrayList<>();
        if(src == dest)
            return path; // csae that the path is from node to himself (no edge)

        node_data srcNode = ga.getNode(src);
        node_data destNode = ga.getNode(dest);

        if(!isConnected(srcNode,destNode)) return null;


        //use variation of BFS algorithms
        //https://en.wikipedia.org/wiki/Breadth-first_search
        final String beHere = "we be here!";
        LinkedList<node_data> queue = new LinkedList<>();
        queue.add(srcNode);
        srcNode.setInfo(beHere);
        srcNode.setTag(-1);

        boolean flag = false;

        while (!queue.isEmpty() && !flag) {
            node_data tmp = queue.remove();
            for(node_data tmpNi: tmp.getNi()) {
                if(tmpNi.getInfo() != beHere){
                    tmpNi.setInfo(beHere);
                    tmpNi.setTag(tmp.getKey());
                    queue.add(tmpNi);
                }
                if(tmpNi==destNode) {
                    flag = true;
                    break;
                }
            }
        }
        /*
        each node's tag contain the key of node that have path to it
        and the src mark at (-1)
        so we need to go from dest to source following tag field
         */
        while(destNode.getTag()!=-1) {
            path.add(destNode);
            destNode=ga.getNode(destNode.getTag());
        }
        // it stop when the tag is -1 (source node), so we need to add it.
        path.add(destNode);

        /*
        we get list:
        dest -> dest-1 ->...-> src+1 -> src
        and we need opposite!
         */
        //reverse the path
        List<node_data> pathCorrectDirection = new ArrayList<>();
        for(int i=path.size()-1;i>=0;i--)
            pathCorrectDirection.add(path.get(i));

        return pathCorrectDirection; // return rhe correct side path
    }

    /**
     *return print of the graph if it not null.
     * @return
     */
    public String toString()
    {
        if(ga != null)
            return ga.toString();
        else
            return "No graph yet!\n\n";
    }
}
