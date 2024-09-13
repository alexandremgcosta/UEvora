package com.example;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Scanner;

public class SeekClienteInterface {

    private final Scanner scanner;

    private final UtilizadorHttpClient utilizadorHttpClient;

    public SeekClienteInterface() {
        this.scanner = new Scanner(System.in);
        this.utilizadorHttpClient = new UtilizadorHttpClient();
    }

    public void start() {
        while (true) {
            System.out.println("\nBem-vindo ao Cliente SeekArtist2.0!");
            System.out.println("1. Login");
            System.out.println("2. Registar");
            System.out.println("X. Sair");
            System.out.print("Escolha uma opção: ");
            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1":
                    realizarLogin();
                    break;
                case "2":
                    realizarRegisto();
                    break;
                case "X":
                case "x":
                    System.out.println("A desconectar...");
                    return;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    private void realizarLogin() {
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        String token = utilizadorHttpClient.login(username, password);
        if (token != null) {
            System.out.println("Login bem-sucedido.");
            menuLogado();
        } else {
            System.out.println("Falha no login.");
        }
    }

    private void realizarRegisto() {
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Role: ");
        String role = scanner.nextLine();

        boolean sucesso = utilizadorHttpClient.register(username, password, email, role);
        if (sucesso) {
            System.out.println("Registo bem-sucedido.");
        } else {
            System.out.println("Falha no registro.");
        }
    }

    public void menuLogado() {
        String opcao;
        do {
            System.out.println("\nMenu do ClientSeekArtist2.0");
            System.out.println("1. Pedido de registo de um novo Artista.");
            System.out.println("2. Adicionar data de apresentação a Artista.");
            System.out.println("3. Listar artista por localização e/ou arte.");
            System.out.println("4. Listar localizações ondem existem artistas a atuar.");
            System.out.println("5. Listar localizações e datas para determinado Artista.");
            System.out.println("6. Listar localização e data da proxima atuação de um Artista.");
            System.out.println("7. Enviar donativo a um Artista.");
            System.out.println("8. Listar donativos de um Artista.");
            System.out.println("9. Dar permissão de administrador a um Utilizador.");
            System.out.println("10. Listar Artistas por estado.");
            System.out.println("11. Aprovar Artista.");
            System.out.println("12. Consultar Artista.");
            System.out.println("13. Alterar informações de um Artista.");

            System.out.println("X. Logout");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextLine();

            switch (opcao) {
                case "1":
                    registarArtista();
                    break;
                case "2":
                    adicionarDataArtista();
                    break;
                case "3":
                    listarArtistasporLocArte();
                    break;
                case "4":
                    listarLocalizacoesAtuar();
                    break;
                case "5":
                    listarLocalizacoesDeArtista();
                    break;
                case "6":
                    listarAtuacaoFutura();
                    break;
                case "7":
                    realizarDonativo();
                    break;
                case "8":
                    listarDonativosArtista();
                    break;
                case "9":
                    darPermissaoAdmin();
                    break;
                case "10":
                    listarArtistasPorEstado();
                    break;
                case "11":
                    aprovarArtista();
                    break;
                case "12":
                    consultarArtista();
                    break;
                case "13":
                    alterarInformacoesArtista();
                    break;
                case "X":
                case "x":
                    System.out.println("A desconectar...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (!opcao.equalsIgnoreCase("X"));
    }

    private void registarArtista() {
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Arte: ");
        String arte = scanner.nextLine();
        System.out.print("Latitude: ");
        String latitude = scanner.nextLine();
        System.out.print("Longitude: ");
        String longitude = scanner.nextLine();
        System.out.print("Data (AAAA-MM-DD): ");
        String data = scanner.nextLine();
        System.out.print("Hora (HH:MM): ");
        String hora = scanner.nextLine();

        String resposta = utilizadorHttpClient.registarArtista(username, arte, latitude, longitude, LocalDateTime.parse(data + "T" + hora));
        System.out.println(resposta);
    }

    private void adicionarDataArtista() {
        System.out.print("Username(Artista): ");
        String username = scanner.nextLine();
        System.out.print("Latitude: ");
        String latitude = scanner.nextLine();
        System.out.print("Longitude: ");
        String longitude = scanner.nextLine();
        System.out.print("Data (AAAA-MM-DD): ");
        String data = scanner.nextLine();
        System.out.print("Hora (HH:MM): ");
        String hora = scanner.nextLine();

        String resposta = utilizadorHttpClient.adicionarDataArtista(username, latitude, longitude, LocalDateTime.parse(data + "T" + hora));
        System.out.println(resposta);
    }


    private void listarArtistasporLocArte() {
        System.out.println("Deixe em branco campos pelos quais não quer filtrar!");
        System.out.println("Para filtrar por localização é necessário inserir ambos os campos da Localização!");
        System.out.print("Arte:");
        String arte = scanner.nextLine();
        System.out.print("Latitude: ");
        String latitude = scanner.nextLine();
        System.out.print("Longitude: ");
        String longitude = scanner.nextLine();

        if (arte.isEmpty()) {
            arte = "";
        }
        if (latitude.isEmpty()) {
            latitude = "";
        }
        if (longitude.isEmpty()) {
            longitude = "";
        }

        String resposta = utilizadorHttpClient.listarArtistasporLocArte(arte, latitude, longitude);
        System.out.println(resposta);
    }

    private void listarLocalizacoesAtuar() {
        String resposta = utilizadorHttpClient.listarLocalizacoesAtuar();
        System.out.println(resposta);
    }

    private void listarLocalizacoesDeArtista() {
        System.out.print("Insira o Artista ID:");
        String artistaId = scanner.nextLine();
        try {
            long id = Long.parseLong(artistaId);
            String resposta = utilizadorHttpClient.listarLocalizacoesDeArtista(id);
            System.out.println(resposta);
        } catch (NumberFormatException e) {
            System.out.println("O ID fornecido não é um número válido.");
        }
    }

    private void listarAtuacaoFutura() {
        System.out.print("Insira o Artista ID:");
        String artistaId = scanner.nextLine();
        try {
            long id = Long.parseLong(artistaId);
            String resposta = utilizadorHttpClient.listarAtuacaoFutura(id);
            System.out.println(resposta);
        } catch (NumberFormatException e) {
            System.out.println("O ID fornecido não é um número válido.");
        }
    }

    private void realizarDonativo() {
        System.out.print("Artista ID:");
        String artistaId = scanner.nextLine();
        System.out.print("Valor do donativo:");
        BigDecimal valor = new BigDecimal(scanner.nextLine());
        System.out.print("Data (AAAA-MM-DD): ");
        String data = scanner.nextLine();
        System.out.print("Hora (HH:MM): ");
        String hora = scanner.nextLine();

        try {
            long id = Long.parseLong(artistaId);

            String resposta = utilizadorHttpClient.realizarDonativo(id, valor, LocalDateTime.parse(data + "T" + hora));
            System.out.println(resposta);
        } catch (NumberFormatException e) {
            System.out.println("Erro ao introduzir o numero.");
        }
    }

    private void listarDonativosArtista() {
        System.out.print("ID do Artista: ");
        Long artistaId = Long.parseLong(scanner.nextLine());

        String resposta = utilizadorHttpClient.listarDonativosArtista(artistaId);
        System.out.println(resposta);
    }

    private void darPermissaoAdmin(){
        System.out.print("Insira o username do utilizador: ");
        String username = scanner.nextLine();

        String resposta = utilizadorHttpClient.darPermissaoAdmin(username);
        System.out.println(resposta);
    }

    private void listarArtistasPorEstado() {
        System.out.print("Digite o estado para listar os artistas (aprovado/nao aprovado): ");
        String estado = scanner.nextLine();

        String resposta = utilizadorHttpClient.listarArtistasPorEstado(estado);
        System.out.println(resposta);
    }

    private void aprovarArtista() {
        System.out.print("Username do artista: ");
        String username = scanner.nextLine();
        String resposta = utilizadorHttpClient.aprovarArtista(username);
        System.out.println(resposta);
    }

    private void consultarArtista() {
        System.out.print("Username do artista: ");
        String username = scanner.nextLine();
        String resposta = utilizadorHttpClient.consultarArtista(username);
        System.out.println(resposta);
    }

    private void alterarInformacoesArtista() {
        System.out.print("Username do Artista: ");
        String username = scanner.nextLine();
        System.out.print("Novo Estado (aprovado/nao aprovado): ");
        String novoEstado = scanner.nextLine();
        System.out.print("Nova Arte: ");
        String novaArte = scanner.nextLine();

        if (novoEstado.isEmpty()) {
            novoEstado = "";
        }
        if (novaArte.isEmpty()) {
            novaArte = "";
        }


        String resposta = utilizadorHttpClient.alterarInformacoesArtista(username, novoEstado, novaArte);
        System.out.println(resposta);
    }

}
