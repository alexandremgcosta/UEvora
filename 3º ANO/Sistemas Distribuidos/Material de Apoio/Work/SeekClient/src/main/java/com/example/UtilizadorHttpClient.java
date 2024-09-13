package com.example;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.*;
import java.time.LocalDateTime;

public class UtilizadorHttpClient {
    private final HttpClient httpClient;
    private String token;

    public UtilizadorHttpClient() {
        this.httpClient = HttpClient.newHttpClient();
    }

    public String login(String username, String password) {
        String json = String.format("{\"username\":\"%s\", \"password\":\"%s\"}", username, password);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/auth/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                String responseBody = response.body();
                JSONObject jsonObject = new JSONObject(responseBody);
                token = jsonObject.getString("token");
                return token;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean register(String username, String password, String email, String role) {
        String json = String.format("{\"username\":\"%s\", \"password\":\"%s\", \"email\":\"%s\", \"role\":\"%s\"}", username, password, email, role);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/auth/register"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 200;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String registarArtista(String username, String arte, String latitude, String longitude, LocalDateTime datahora) {
        System.out.println("HUM:" + datahora.toString());
        String json = String.format("{\"username\":\"%s\", \"arte\":\"%s\", \"latitude\":\"%s\", \"longitude\":\"%s\", \"data\":\"%s\"}", username, arte, latitude, longitude, datahora.toString());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/artistas/register"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return response.body();
            } else {
                return "Erro: " + response.statusCode() + " - " + response.body();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Erro na requisição: " + e.getMessage();
        }
    }


    public String adicionarDataArtista(String username, String latitude, String longitude, LocalDateTime datahora) {
        String json = String.format("{\"username_artista\":\"%s\", \"latitude\":\"%s\", \"longitude\":\"%s\", \"data\":\"%s\"}", username, latitude, longitude, datahora.toString());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/artistas/adicionarLocalizacaoFutura"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return response.body();
            } else {
                return "Erro: " + response.statusCode() + " - " + response.body();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Erro na requisição: " + e.getMessage();
        }
    }

    public String listarArtistasporLocArte(String arte, String latitude, String longitude) {
        String json = String.format("{\"arte\":\"%s\", \"longitude\":\"%s\", \"latitude\":\"%s\"}", arte, longitude, latitude);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/artistas/filtrarPorArteELocalizacao"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                String responseBody = response.body();
                JSONArray artistasArray = new JSONArray(responseBody);

                if (artistasArray.isEmpty()) {
                    return "Nenhum artista encontrado.";
                }
                StringBuilder sb = new StringBuilder();

                for (int i = 0; i < artistasArray.length(); i++) {
                    JSONObject artista = artistasArray.getJSONObject(i);
                    sb.append("ID: ").append(artista.getInt("id"))
                            .append(", Username: ").append(artista.getString("username"))
                            .append(", Estado: ").append(artista.getString("estado"))
                            .append(", Arte: ").append(artista.getString("arte"))
                            .append("\nLocalizações:\n");

                    JSONArray localizacoes = artista.getJSONArray("localizacoes");
                    for (int j = 0; j < localizacoes.length(); j++) {
                        JSONObject localizacao = localizacoes.getJSONObject(j);
                        sb.append("  - Longitude: ").append(localizacao.getDouble("longitude"))
                                .append(", Latitude: ").append(localizacao.getDouble("latitude"))
                                .append(", Data: ").append(localizacao.getString("data"))
                                .append("\n");
                    }

                    sb.append("Donativos:\n");
                    JSONArray donativos = artista.getJSONArray("donativo");
                    for (int k = 0; k < donativos.length(); k++) {
                        JSONObject donativo = donativos.getJSONObject(k);
                        sb.append("  - ID: ").append(donativo.getInt("id"))
                                .append(", Valor: ").append(donativo.getDouble("valor"))
                                .append(", Data: ").append(donativo.getString("data"))
                                .append("\n");
                    }
                    sb.append("\n");
                }
                return sb.toString();
            } else {
                return "Erro: " + response.statusCode() + " - " + response.body();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Erro na requisição: " + e.getMessage();
        }
    }

    public String listarLocalizacoesAtuar() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/artistas/listaAtuacoesNoMomento"))
                .header("Authorization", "Bearer " + token)
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                String responseBody = response.body();
                JSONArray localizacoesArray = new JSONArray(responseBody);

                if (localizacoesArray.isEmpty()) {
                    return "Nenhuma localização encontrada.";
                }

                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < localizacoesArray.length(); i++) {
                    JSONObject localizacao = localizacoesArray.getJSONObject(i);
                    sb.append("Localização ").append(i + 1).append(":\n");
                    sb.append("  - Latitude: ").append(localizacao.getDouble("latitude"));
                    sb.append(", Longitude: ").append(localizacao.getDouble("longitude"));
                    sb.append(", Data: ").append(localizacao.getString("data"));
                    sb.append("\n");
                }
                return sb.toString();
            } else {
                return "Erro: " + response.statusCode() + " - " + response.body();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return "Erro ao processar a resposta JSON: " + e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            return "Erro na requisição: " + e.getMessage();
        }
    }

    public String listarLocalizacoesDeArtista(Long artistaId) {
        String json = String.format("{\"Id\":%d}", artistaId);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/artistas/listaLocalizacoesPassadas"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                String responseBody = response.body();
                JSONArray atuacoesArray = new JSONArray(responseBody);

                if (atuacoesArray.isEmpty()) {
                    return "Nenhuma atuação encontrada para o artista.";
                }

                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < atuacoesArray.length(); i++) {
                    JSONObject atuacao = atuacoesArray.getJSONObject(i);
                    sb.append("Atuação ").append(i + 1).append(":\n");
                    sb.append("  - ID: ").append(atuacao.getLong("id"));
                    sb.append(", Longitude: ").append(atuacao.getDouble("longitude"));
                    sb.append(", Latitude: ").append(atuacao.getDouble("latitude"));
                    sb.append(", Data: ").append(atuacao.getString("data"));
                    sb.append("\n");
                }
                return sb.toString();
            } else {
                return "Erro: " + response.statusCode() + " - " + response.body();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return "Erro ao processar a resposta JSON: " + e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            return "Erro na requisição: " + e.getMessage();
        }
    }

    public String listarAtuacaoFutura(Long artistaId) {
        String json = String.format("{\"Id\":%d}", artistaId);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/artistas/listaLocalizacaoFutura"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                JSONObject localizacaoObjeto = new JSONObject(response.body());

                StringBuilder sb = new StringBuilder();
                sb.append("Próxima localização:\n");
                sb.append("  - ID: ").append(localizacaoObjeto.getLong("id"));
                sb.append(", Longitude: ").append(localizacaoObjeto.getDouble("longitude"));
                sb.append(", Latitude: ").append(localizacaoObjeto.getDouble("latitude"));
                sb.append(", Data: ").append(localizacaoObjeto.getString("data"));

                return sb.toString();
            } else {
                return "Erro: " + response.statusCode() + " - " + response.body();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return "Erro ao processar a resposta JSON: " + e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            return "Erro na requisição: " + e.getMessage();
        }
    }


    public String realizarDonativo(Long artistaId, BigDecimal valor, LocalDateTime datahora) {
        String json = String.format("{\"artista_id\":%d, \"valor\":%s, \"data\":\"%s\"}", artistaId, valor, datahora.toString());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/artistas/fazerDonativo"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return "Donativo enviado com sucesso!";
            } else {
                return "Erro: " + response.statusCode() + " - " + response.body();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Erro na requisição: " + e.getMessage();
        }
    }

    public String listarDonativosArtista(Long artistaId) {
        String json = String.format("{\"artista_id\":%d}", artistaId);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/artistas/listarDonativos"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                JSONArray donativosArray = new JSONArray(response.body());
                StringBuilder sb = new StringBuilder();

                for (int i = 0; i < donativosArray.length(); i++) {
                    JSONObject donativo = donativosArray.getJSONObject(i);
                    sb.append("Donativo: ")
                            .append(", Valor: ").append(donativo.getBigDecimal("valor"))
                            .append(", Data: ").append(donativo.getString("data"))
                            .append("\n");
                }

                return sb.toString();
            } else {
                return "Erro: " + response.statusCode() + " - " + response.body();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Erro na requisição: " + e.getMessage();
        }
    }

    public String darPermissaoAdmin(String username) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/admin/darPermissaoAdmin/" + username))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .PUT(HttpRequest.BodyPublishers.noBody())
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 403) {
                return "Você não tem permissões de administrador.";
            } else {
                if (response.statusCode() == 200) {
                    return "Utilizador promovido com sucesso.";
                } else {
                    return "Erro: " + response.statusCode() + " - " + response.body();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Erro na requisição: " + e.getMessage();
        }
    }

    public String listarArtistasPorEstado(String estado) {
        String json = String.format("{\"estado\":\"%s\"}", estado);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/admin/listarPorEstado"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 403) {
                return "Você não tem permissões de administrador.";
            } else {
                if (response.statusCode() == 200) {
                    JSONArray artistas = new JSONArray(response.body());
                    StringBuilder sb = new StringBuilder();

                    for (int i = 0; i < artistas.length(); i++) {
                        JSONObject artista = artistas.getJSONObject(i);
                        sb.append("ID: ").append(artista.getLong("id"))
                                .append("  Username: ").append(artista.getString("username"))
                                .append("  Estado: ").append(artista.getString("estado"))
                                .append("\n");
                    }

                    return sb.toString();
                } else {
                    return "Erro: " + response.statusCode() + " - " + response.body();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Erro na requisição: " + e.getMessage();
        }
    }

    public String aprovarArtista(String username) {
        String json = String.format("{\"username\":\"%s\"}", username);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/admin/aprovarArtista"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 403) {
                return "Você não tem permissões de administrador.";
            } else {
                if (response.statusCode() == 200) {
                    return "Artista aprovado com sucesso.";
                } else {
                    return "Erro: " + response.statusCode() + " - " + response.body();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Erro na requisição: " + e.getMessage();
        }
    }

    public String consultarArtista(String username) {
        String json = String.format("{\"username\":\"%s\"}", username);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/admin/consultarDadosArtista"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 403) {
                return "Você não tem permissões de administrador.";
            } else {
                if (response.statusCode() == 200) {
                    JSONObject artistaObj = new JSONObject(response.body());
                    StringBuilder sb = new StringBuilder();
                    sb.append("ID: ").append(artistaObj.getInt("id"))
                            .append("\nUsername: ").append(artistaObj.getString("username"))
                            .append("\nEstado: ").append(artistaObj.getString("estado"))
                            .append("\nArte: ").append(artistaObj.getString("arte"))
                            .append("\nLocalizações:\n");

                    JSONArray localizacoes = artistaObj.getJSONArray("localizacoes");
                    for (int i = 0; i < localizacoes.length(); i++) {
                        JSONObject loc = localizacoes.getJSONObject(i);
                        sb.append("  - ID: ").append(loc.getInt("id"))
                                .append(", Longitude: ").append(loc.getDouble("longitude"))
                                .append(", Latitude: ").append(loc.getDouble("latitude"))
                                .append(", Data: ").append(loc.getString("data"))
                                .append("\n");
                    }

                    sb.append("Donativos:\n");
                    JSONArray donativos = artistaObj.getJSONArray("donativo");
                    for (int i = 0; i < donativos.length(); i++) {
                        JSONObject donativo = donativos.getJSONObject(i);
                        sb.append("  - ID: ").append(donativo.getInt("id"))
                                .append(", Valor: ").append(donativo.getDouble("valor"))
                                .append(", Data: ").append(donativo.getString("data"))
                                .append("\n");
                    }
                    return sb.toString();
                } else {
                    return "Erro: " + response.statusCode() + " - " + response.body();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Erro na requisição: " + e.getMessage();
        }
    }

    public String alterarInformacoesArtista(String username, String novoEstado, String novaArte) {
        String json = String.format("{\"estado\":\"%s\", \"arte\":\"%s\"}", novoEstado, novaArte);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/admin/atualizar/" + username))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + this.token)
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if(response.statusCode() == 403){
                return "Você não tem permissões de administrador.";
            } else{
                if (response.statusCode() == 200) {
                    return response.body();
                } else {
                    return "Erro: " + response.statusCode() + " - " + response.body();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Erro na requisição: " + e.getMessage();
        }
    }

}
