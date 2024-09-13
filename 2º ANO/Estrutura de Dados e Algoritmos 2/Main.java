import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

class Main {
    public static void main(String[] args) throws IOException, NumberFormatException {
        int R, C, T;

        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

        String[] linha = input.readLine().split(" ");
        R = Integer.parseInt(linha[0]);
        C = Integer.parseInt(linha[1]);
        T = Integer.parseInt(linha[2]);
        char[][] mapa = new char[R][C];

        for (int i = 0; i < R; i++) {
            String rows = input.readLine();
            for (int j = 0; j < C; j++) {
                mapa[i][j] = rows.charAt(j);
            }
        }

        Dream D = new Dream(R, C);

        D.CreateGraph(mapa);

        while (T != 0) {
            String[] read_teste = input.readLine().split(" ");

            D.MinimumNumberOfMoves((Integer.parseInt(read_teste[0]) - 1) * C + Integer.parseInt(read_teste[1]) - 1);

            T--;
        }

    }
}

class Dream {

    enum Colour {
        WHITE, GREY, BLACK
    };

    private Graph G;
    private int rows, cols, hole = 0, nodes;

    public Dream(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.G = new Graph(rows * cols);
    }

    public void AddNode(int u, int v) {
        this.G.addEdge(u, v);
    }

    public void CreateGraph(char[][] mapa) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int node = i * cols + j;
                if (mapa[i][j] != 'O') {
                    if (i > 0 && mapa[i - 1][j] != 'O') // up
                        AddNode(node, ((i - 1) * cols + j));
                    if (i < rows - 1 && mapa[i + 1][j] != 'O') // down
                        AddNode(node, (i + 1) * cols + j);
                    if (j > 0 && mapa[i][j - 1] != 'O') // left
                        AddNode(node, i * cols + (j - 1));
                    if (j < cols - 1 && mapa[i][j + 1] != 'O') // right
                        AddNode(node, i * cols + (j + 1));
                    if (mapa[i][j] == 'H') {
                        hole = node;
                    }
                }
            }
        }
    }

    public void MinimumNumberOfMoves(int s) {
        final int INFINITY = Integer.MAX_VALUE;
        final int NONE = -1;
        nodes = rows * cols;
        Colour[] colour = new Colour[nodes];
        int[] d = new int[nodes];
        int[] p = new int[nodes];
        for (int u = 0; u < nodes; ++u) {
            colour[u] = Colour.WHITE;
            d[u] = INFINITY;
            p[u] = NONE;
        }
        colour[s] = Colour.GREY;
        d[s] = 0;
        Queue<Integer> Q = new LinkedList<>();
        Q.add(s);
        while (!Q.isEmpty()) {
            int u = Q.remove();
            for (Integer v : G.adjacents[u]) {
                if (colour[v] == Colour.WHITE) {
                    colour[v] = Colour.GREY;
                    d[v] = d[u] + 1;
                    p[v] = u;
                    Q.add(v);
                    if(v==hole){
                        System.out.println("ENCONTREI O H!");
                        System.out.println("Distancia da partida ao H: "+d[v]);
                    }
                }
            }
            colour[u] = Colour.BLACK;
        }
    }

}

class Graph {
    int nodes;
    List<Integer>[] adjacents;

    @SuppressWarnings("unchecked")

    public Graph(int nodes) {
        this.nodes = nodes;
        adjacents = new List[nodes];
        for (int i = 0; i < nodes; ++i)
            adjacents[i] = new LinkedList<>();
    }

    public void addEdge(int u, int v) {
        adjacents[u].add(v);
    }
}