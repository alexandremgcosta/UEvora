package cli;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;

public class cliente implements Runnable {

    private Socket client;
    private BufferedReader inFromServer;
    private PrintWriter outToServer;
    private boolean done;

    @Override
    public void run() {
        try {
            client = new Socket("localhost", 5555);
            outToServer = new PrintWriter(client.getOutputStream(), true);
            inFromServer = new BufferedReader(new InputStreamReader(client.getInputStream()));

            InputHandler inHandler = new InputHandler();
            Thread t = new Thread(inHandler);
            t.start(); // começa a thread

        } catch (IOException e) {
            shutdown();
        }
    }
    /*
     * public void shutdown - Cliente desconecta se com o servidor 
     */
    public void shutdown() {
        done = true;
        try {
            inFromServer.close();
            outToServer.close();
            if (!client.isClosed()) {
                client.close();
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
    /*
     *  Funcionamento do Menu no output do cliente
     */
    class InputHandler implements Runnable {

        @Override
        public void run() {
            try {
                BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
                String modifiedSentence;

                boolean authenticated = false;
                while (!done) {
                    System.out.println(
                            "Available commands IAM, ASK, ANSWER, LISTQUESTIONS,"+ '\n' + "                      PUTFILE, LISTFILES, GETFILE, EXIT");
                    System.out.print("");
                    System.out.print("Enter a command: ");

                    String message = inFromUser.readLine();
                    message = validarMsg(message);
                    System.out.println("");

                    if (authenticated == false && !message.startsWith("IAM")) {
                        System.out.println("You need to login first.");
                        System.out.println("");
                        System.out.println("");
                        continue; // Volta para o início do loop
                    }

                    if (message.equalsIgnoreCase("exit")) {
                        outToServer.println(message);
                        inFromUser.close();
                        shutdown();
                    } else {
                        if (message.startsWith("IAM")) {
                            if (authenticated == true) {
                                System.out.println("Login has already been done.");

                            } else {
                                outToServer.println(message);
                                authenticated = true;
                                modifiedSentence = inFromServer.readLine();
                                System.out.println(modifiedSentence);
                            }
                        } else if (message.startsWith("ASK")) {
                            outToServer.println(message);
                            modifiedSentence = inFromServer.readLine();
                            System.out.println(modifiedSentence);
                        } else if (message.startsWith("ANSWER")) {
                            outToServer.println(message);
                            modifiedSentence = inFromServer.readLine();
                            System.out.println(modifiedSentence);
                        } else if (message.equals("LISTQUESTIONS")) {
                            String resposta;
                            outToServer.println(message);
                            while ((resposta = inFromServer.readLine()) != null) {
                                if (resposta.equals("ENDQUESTIONS")) {
                                    break;
                                }
                                System.out.println(resposta);
                            }
                            System.out.println("ENDQUESTIONS");

                        } else if (message.startsWith("PUTFILE")) {
                            outToServer.println(message);
                            String[] parts = message.split(" ");
                            String filename = parts[1];

                            FileInputStream fileInputStream = new FileInputStream(filename);
                            byte[] buffer = new byte[8192];
                            int bytesRead;
                            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                                client.getOutputStream().write(buffer, 0, bytesRead);
                            }
                            fileInputStream.close();

                            modifiedSentence = inFromServer.readLine();

                            System.out.println(modifiedSentence);

                        } else if (message.equals("LISTFILES")) {
                            outToServer.println(message);
                            String resposta;
                            while ((resposta = inFromServer.readLine()) != null) {
                                if (resposta.equals("ENDFILES")) {
                                    break; // Término da lista
                                }
                                System.out.println(resposta);
                            }
                            System.out.println("");
                            System.out.println("ENDFILES");

                        } else if (message.startsWith("GETFILE")) {
                            outToServer.println(message);
                            String[] parts = message.split(" ");
                            int fileNumber = Integer.parseInt(parts[1]);
                            outToServer.println(message);

                            modifiedSentence = inFromServer.readLine();

                            if (modifiedSentence.startsWith("FILE")) {
                                String[] responseParts = modifiedSentence.split(" ");

                                if (responseParts[2].equals("NOTFOUND")) {
                                    System.out.println("File not found.");
                                } else if (responseParts[2].equals("INVALID")) {
                                    System.out.println("Invalid file number.");
                                } else {
                                    String filename = responseParts[2];
                                    long fileSize = Long.parseLong(responseParts[3]);

                                    FileOutputStream fileOutputStream = new FileOutputStream(filename);
                                    byte[] buffer = new byte[8192];
                                    int bytesRead;
                                    long totalBytesRead = 0;

                                    while (totalBytesRead < fileSize
                                            && (bytesRead = client.getInputStream().read(buffer)) != -1) {
                                        fileOutputStream.write(buffer, 0, bytesRead);
                                        totalBytesRead += bytesRead;
                                    }
                                    fileOutputStream.close();

                                    FileInputStream fileInputStream = new FileInputStream(filename);
                                    BufferedReader fileReader = new BufferedReader(
                                            new InputStreamReader(fileInputStream));

                                    System.out.println("FILE " + fileNumber + " " + filename + " " + fileSize);
                                    String line;
                                    while ((line = fileReader.readLine()) != null) {
                                        System.out.println(line);
                                    }

                                    fileReader.close();
                                }
                            }

                        } else{
                            System.out.println("ERROR");
                        }

                        System.out.println("");
                        System.out.println("");
                    }
                }
            } catch (Exception e) {
                shutdown();
            }
        }
    }
    /*
     * Esta função valida se a mensagem tem menos de 1024 bytes, se tiver inferior envia
     * só até 1024 bytes.
     */
    public String validarMsg(String message){
        byte[] bytes = message.getBytes();
        if(bytes.length <= 1024){
            return message;
        } else{
            byte[] msgdescartada = Arrays.copyOf(bytes, 1024);
            return new String(msgdescartada);
        }
    }

    public static void main(String[] args) {
        cliente cliente = new cliente();
        cliente.run();
    }
}