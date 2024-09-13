package srv;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class servidor implements Runnable {

    private ArrayList<ConnectionHandler> connections;
    private ServerSocket server;
    private boolean done;
    private ExecutorService pool;
    private static long serverStartTime;
    private static List<Utilizador> listaUtilizadores = Collections.synchronizedList(new ArrayList<>());
    private static List<Pergunta> listaPerguntas = Collections.synchronizedList(new ArrayList<>());
    private static int perguntaCount = 0;
    private static int idUtilizador = 0;
    private static List<String> listaArquivos = Collections.synchronizedList(new ArrayList<>());
    
    public servidor() {
        connections = new ArrayList<>();
        done = false;
        carregarDados();
    }
    /*
     * Método onde carrega os dados se o server for desligado e ligado ou algum erro
     */
    public void carregarDados() {
        File file = new File("dados.txt");
        if (file.exists()) {
            try {
                FileReader fileReader = new FileReader("dados.txt");
                BufferedReader bufferedReader = new BufferedReader(fileReader);

                String linha;
                String secao = "";

                while ((linha = bufferedReader.readLine()) != null) {
                    if (linha.startsWith("#")) {
                        secao = linha;
                    } else {
                        if (secao.equals("# Questions")) {
                            if (linha.startsWith("ANSWERS")) {
                                String[] partes = linha.split(":");
                                int numero = Integer.parseInt(partes[1]);
                                String resposta = partes[2];
                                addResposta(numero, resposta);
                            } else {
                                String[] partes = linha.split(":");
                                int numero = Integer.parseInt(partes[0]);
                                String texto = partes[1];
                                addPergunta(texto);
                                perguntaCount = Math.max(perguntaCount, numero);
                            }
                        } else if (secao.equals("# ATTENDANCE")) {
                            String[] partes = linha.split(":");
                            int id = Integer.parseInt(partes[0]);
                            String nome = partes[1];
                            String presenca = partes[2];
                            Utilizador utilizador = new Utilizador(nome, presenca, id);
                            listaUtilizadores.add(utilizador);
                            idUtilizador = Math.max(idUtilizador, id);
                        }
                    }
                }

                bufferedReader.close();
            } catch (IOException e) {
                System.err.println(e.getMessage());;
            }
        }
    }
    /*
     * Adiciona Pergunta à lista de Perguntas
     */
    public void addPergunta(String texto) {
        int numeroPergunta = ++perguntaCount;
        Pergunta pergunta = new Pergunta(numeroPergunta, texto);
        listaPerguntas.add(pergunta);
    }
    /*
     * Adiciona a resposta à lista da pergunta respondida
     */
    public void addResposta(int numeroPergunta, String resposta) {
        for (Pergunta pergunta : listaPerguntas) {
            if (pergunta.getnumeroPergunta() == numeroPergunta) {
                pergunta.addResposta(resposta);
                break;
            }
        }
    }
    /*
     * Guarda os dados num ficheiro txt
     */
    public void salvarDados() {
        try {
            FileWriter fileWriter = new FileWriter("dados.txt");
            PrintWriter printWriter = new PrintWriter(fileWriter);

            // Guarda as perguntas
            printWriter.println("# Questions");
            for (Pergunta pergunta : listaPerguntas) {
                printWriter.println(pergunta.getnumeroPergunta() + ":" + pergunta.getTexto());
                if (pergunta.isRespondida()) {
                    for (String resposta : pergunta.getRespostas()) {
                        printWriter.println("ANSWERS:" + pergunta.getnumeroPergunta() + ":" + resposta);
                    }
                }
            }

            // Guarda os utilizadores
            printWriter.println("# ATTENDANCE");
            for (Utilizador utilizador : listaUtilizadores) {
                printWriter
                        .println(utilizador.get_Id() + ":" + utilizador.get_nome() + ":"
                                + utilizador.get_presenca());
            }

            printWriter.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            System.out.println("Server started.");
            server = new ServerSocket(5555);
            pool = Executors.newCachedThreadPool();
            while (!done) {
                Socket client = server.accept(); // Quando aceitamos a conecção é criada uma client socket
                ConnectionHandler handler = new ConnectionHandler(client); // sempre que criamos outro cliente
                connections.add(handler); // ele é adicionado à lista

                pool.execute(handler);
            }
        } catch (Exception e) {
            shutdownSv();
        }
    }

    public void shutdownSv() {
        done = true;
        pool.shutdown();
        try {
            if (!server.isClosed()) {
                server.close();
            }
            for (ConnectionHandler ch : connections) {
                
                PrintWriter msgFIM = new PrintWriter(ch.client.getOutputStream(), true);
                msgFIM.println("Finished class.");
                ch.shutdownCl();
                msgFIM.close();
            }
            System.out.println("Finished class.");
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
    /*
     * Esta Classe é responsável por encarregar-se da conexão entre servidor e um cliente em especifico 
     */
    class ConnectionHandler implements Runnable {

        private Socket client;
        private BufferedReader inFromClient;
        private PrintWriter outToClient;

        String fileName;
        int bytes;
        String[] array;

        public ConnectionHandler(Socket client) {
            this.client = client;
        }

        @Override
        /*
         * Funcionamento principal do servidor
         * Nesta função acontece a principal interção cliente - servidor
         * Pois aqui o servidor recebe e manda mensagens ao cliente                                  
         */
        public void run() {

            try {
                outToClient = new PrintWriter(client.getOutputStream(), true); // serve para enviar mensagems do
                                                                               // servidor ao cliente
                inFromClient = new BufferedReader(new InputStreamReader(client.getInputStream())); // serve para receber
                                                                                                   // mensagens do
                                                                                                   // cliente para o
                                                                                                   // servidor
                String clientSentence, serverResponse, nome, presenca, pergunta, resposta;
                int id = getIdUtilizador();
                while (true) {
                    clientSentence = inFromClient.readLine();

                    if (clientSentence.startsWith("IAM")) {
                        nome = clientSentence.substring(4);

                        serverResponse = "HELLO " + nome;

                        long serverUptime = getServerUptime();
                        int delay = (int) serverUptime / (1000 * 60);
                        if (delay < 20) {
                            presenca = "Presença registada.";
                        } else if (delay >= 20 && delay < 45) {
                            presenca = "Meia registada";
                        } else {
                            presenca = "Presença não registada";
                        }
                        Utilizador utlz = new Utilizador(nome, presenca, id);
                        addUtilizador(utlz);
                        outToClient.println(validarMsg(serverResponse));
                        idUtilizador++;

                    } else if (clientSentence.startsWith("ASK")) {
                        pergunta = clientSentence.substring(4);

                        addPergunta(pergunta);

                        serverResponse = "QUESTION " + getCountPergunta() + ": "
                                + pergunta.substring(0, (pergunta.length() / 2))
                                + "...";
                        outToClient.println(validarMsg(serverResponse));

                    } else if (clientSentence.startsWith("ANSWER")) {
                        String[] partes = clientSentence.split(" ");
                        int numero = Integer.parseInt(partes[1]);

                        resposta = "(" + findUtilizador(id) + ") "
                                + clientSentence.substring(clientSentence.indexOf(partes[1]) + 1);

                        addResposta(numero, resposta);

                        serverResponse = "REGISTERED " + numero;
                        outToClient.println(validarMsg(serverResponse));

                    } else if (clientSentence.startsWith("LISTQUESTIONS")) {

                        resposta = obterPerguntas();
                        outToClient.println(validarMsg(resposta) + "ENDQUESTIONS");

                    } else if (clientSentence.startsWith("PUTFILE")) {
                        String[] parts = clientSentence.split(" ");
                        String filename = parts[1];

                        long fileSize = Long.parseLong(parts[2]);

                        FileOutputStream fileOutputStream = new FileOutputStream(filename);

                        byte[] buffer = new byte[8192];
                        int bytesRead;
                        long totalBytesRead = 0;

                        while (totalBytesRead < fileSize
                                && (bytesRead = client.getInputStream().read(buffer)) != -1) {
                            if (totalBytesRead + bytesRead > fileSize) {
                                int excessBytes = (int) (totalBytesRead + bytesRead - fileSize);
                                fileOutputStream.write(buffer, 0, bytesRead - excessBytes);
                            } else {
                                fileOutputStream.write(buffer, 0, bytesRead);
                            }
                            totalBytesRead += bytesRead;

                            if (totalBytesRead < fileSize) {
                                if (buffer.length > fileSize - totalBytesRead) {
                                    buffer = new byte[(int) (fileSize - totalBytesRead)];
                                }
                            }
                        }
                        fileOutputStream.close();

                        serverResponse = "UPLOADED " + filename;
                        addFile(filename);
                        outToClient.println(validarMsg(serverResponse));
                    } else if (clientSentence.equalsIgnoreCase("LISTFILES")) {
                        resposta = obterFiles();
                        outToClient.println(validarMsg(resposta) + "ENDFILES");

                    } else if (clientSentence.startsWith("GETFILE")) {

                        String[] parts = clientSentence.split(" ");
                        int fileNumber = Integer.parseInt(parts[1]);

                        if (fileNumber >= 1 && fileNumber <= sizeofFileList()) {
                            String filename = obterFilename(fileNumber - 1);
                            File file = new File(filename);

                            if (file.exists()) {
                                FileInputStream fileInputStream = new FileInputStream(file);
                                byte[] buffer = new byte[8192];
                                int bytesRead;

                                serverResponse = "FILE " + fileNumber + " " + filename + " " + file.length();
                                outToClient.println(validarMsg(serverResponse));

                                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                                    client.getOutputStream().write(buffer, 0, bytesRead);
                                }
                                fileInputStream.close();

                            } else {
                                serverResponse = "FILE " + fileNumber + " NOTFOUND";
                                outToClient.println(validarMsg(serverResponse));
                            }
                        } else {
                            serverResponse = "FILE " + fileNumber + " INVALID";
                            outToClient.println(validarMsg(serverResponse));
                        }
                    }

                }
            } catch (IOException e) {
                shutdownCl();

            }
        }
        /*
         * Valida se a mensagem têm ou não mais de 1024 bytes
         * Caso tenha mais, o servidor só manda a mensagem até 1024 bytes
         */
        public String validarMsg(String message) {
            byte[] bytes = message.getBytes();
            if (bytes.length <= 1024) {
                return message;
            } else {
                byte[] msgdescartada = Arrays.copyOf(bytes, 1024);
                return new String(msgdescartada);
            }
        }

        /*
        * retorna o tempo decorrido desde o ligar do servidor
        * até ao momento atual
        * System.currentTimeMillis() - retorna o tempo em milisegundos
        */
        public long getServerUptime() {
            return System.currentTimeMillis() - serverStartTime;
        }

        /*
         * Adiciona um Utilizador a uma lista de Utilizadores
         */
        public void addUtilizador(Utilizador utilizador) {
            listaUtilizadores.add(utilizador);
        }

        /*
         * Retorna o número do ID do utilizador
         */
        public int getIdUtilizador() {
            return listaUtilizadores.size() + 1;
        }

        /*
         * Encontra o Utilizador através do ID
         */
        public String findUtilizador(int id) {
            String nome = "";
            for (Utilizador u : listaUtilizadores) {
                if (u.get_Id() == id) {
                    nome = u.get_nome();
                }
            }
            return nome;
        }

        /*
         * Imprime a lista de presenças
         */
        public void imprimirPresencas() {
            for (Utilizador utlz : listaUtilizadores) {
                System.out.println(utlz.toString());
            }
        }

        /*
         * Retorna o número da Pergunta
         */
        public int getCountPergunta() {
            return perguntaCount;
        }

        /*
         * Retorna a Pergunta
         */
        public String obterPerguntas() {
            StringBuilder builder = new StringBuilder();
            for (Pergunta pergunta : listaPerguntas) {
                builder.append(pergunta.toString()).append("\n");
            }
            return builder.toString();
        }

        /*
         * Adiciona o ficheiro À Lista de ficheiros
         */
        public void addFile(String texto) {
            listaArquivos.add(texto);
        }

        /*
         * Retorna os Ficheiros
         */
        public String obterFiles() {
            StringBuilder builderfiles = new StringBuilder();
            int count = 1;
            for (String arqv : listaArquivos) {
                builderfiles.append("(").append(count).append(") ").append(arqv).append("\n");
                count++;
            }
            return builderfiles.toString();
        }

        /*
         * Retorna o Ficheiros através do número
         */
        public String obterFilename(int numero) {
            return listaArquivos.get(numero);
        }
        
        public int sizeofFileList() {
            return listaArquivos.size();
        }

        public void shutdownCl() { // desliga o cliente
            try {
                inFromClient.close();
                outToClient.close();
                if (!client.isClosed()) {
                    client.close();
                }
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }

    }

    /*
     * Guarda os dados a cada 5 minutos
     */
    public void salvarTimer() {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(this::salvarDados, 0, 5, TimeUnit.MINUTES);
    }

    /*
     * Desliga servidor passado 2 horas desde de o servidor seja ligado 
     */
    public void terminaraula() {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.schedule(this::desligarServidor, 2, TimeUnit.HOURS);
    }

    /*
     * Desligar Servidor
     */
    public void desligarServidor() {
        shutdownSv();
        System.exit(0);
    }

    public static void main(String[] args) {
        servidor server = new servidor();
        serverStartTime = System.currentTimeMillis();
        server.salvarTimer(); // Executa de 5 em 5 minutos
        server.terminaraula(); // Passado 2horas
        server.run();
    }
}

/*
 * Classe que cria o objeto Utilizador 
 */
class Utilizador {
    private String nome;
    private String presenca;
    private int id;

    public Utilizador(String nome, String presenca, int id) {
        this.nome = nome;
        this.presenca = presenca;
        this.id = id;
    }

    int get_Id() {
        return id;
    }

    String get_nome() {
        return nome;
    }

    String get_presenca() {
        return presenca;
    }

    @Override
    public String toString() {
        return "Utilizador [nome=" + this.nome + ", presenca=" + this.presenca + "]";
    }
}

/*
 * Classe que cria o objeto Pergunta
 */
class Pergunta {
    private int nPergunta;
    private String texto;
    private List<String> respostas;
    private boolean respondida;

    public Pergunta(int nPergunta, String texto) {
        this.nPergunta = nPergunta;
        this.texto = texto;
        this.respostas = new ArrayList<>();
        this.respondida = false;
    }

    public int getnumeroPergunta() {
        return nPergunta;
    }

    public String getTexto() {
        return texto;
    }

    public List<String> getRespostas() {
        return respostas;
    }

    public boolean isRespondida() {
        return respondida;
    }

    public void addResposta(String resposta) {
        respostas.add(resposta);
        respondida = true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("(").append(nPergunta).append(") ").append(texto).append("\n");

        if (respondida) {
            for (String resposta : respostas) {
                builder.append("    ").append(resposta).append("\n");
            }
        } else {
            builder.append("    ").append("NOTANSWERED").append("\n");
        }

        return builder.toString();
    }
}