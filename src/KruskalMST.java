//import com.sun.javafx.geom.Edge;


//import java.util.Queue;
import  java.util.Arrays;

public class KruskalMST {
    private static final double FLOATING_POINT_EPSILON = 1E-12;

    private double weight;
    private Queue<Edge> mst = new Queue<Edge>();

    public KruskalMST(EdgeWeightedGraph G) {

        Edge[] edges = new Edge[G.E()];
        int t = 0;
        for(Edge e: G.edges()) {
            edges[t++] = e;
        }
        Arrays.sort(edges);

        UF uf = new UF(G.V());
        for(int i = 0; i < G.E() && mst.size() < G.V() - 1; i++){
            Edge e = edges[i];
            int v = e.either();
            int w = e.other(v);

            if(uf.find(v) != uf.find(w)){
                uf.union(v, w);
                mst.enqueue(e);
                weight += e.weight();
            }
        }

        assert check(G);
    }

    public double weight() {
        return weight;
    }

    private boolean check(EdgeWeightedGraph G) {

        double total = 0.0;
        for(Edge e: edges()){
            total += e.weight();
        }
        if(Math.abs(total - weight()) > FLOATING_POINT_EPSILON) {
            System.err.printf("Weight of edges does not equal weight() %f vs. %f \n", total, weight());
            return false;
        }

        UF uf = new UF(G.V());
        for(Edge e: edges()){
            int v = e.either(), w = e.other(v);
            if(uf.find(v) == uf.find(w)){
                System.err.println("Not a forest");
                return false;
            }
            uf.union(v, w);
        }

        for (Edge e: G.edges()) {
            int v = e.either(), w = e.other(v);
            if(uf.find(v) != uf.find(w)) {
                System.err.println("Not a spanning forest");
                return false;
            }
        }

        for(Edge e : edges()) {

            uf = new UF(G.V());
            for(Edge f : mst) {
                int x = f.either(), y = f.other(x);
                if( f != e) uf.union(x, y);
            }

            for(Edge f: G.edges()) {
                int x = f.either(), y = f.other(x);
                if(uf.find(x) != uf.find(y)) {
                    if(f.weight() < e.weight()) {
                        System.err.println("Edge "+ f +"violates cut optimality conditions");

                        return false;
                    }
                }
            }
        }

        return true;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        EdgeWeightGraph G = new EdgeWeightedGraph(in);
        KruskalMST mst = new KruskalMST(G);
        for(Edge e: mst.edges()) {
            System.out.println(e);
        }
        System.out.println(mst.weight());
    }
}
