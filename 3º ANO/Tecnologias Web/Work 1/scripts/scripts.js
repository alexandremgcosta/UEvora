/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/ClientSide/javascript.js to edit this template
 */

/*
 * Função que realiza um pedido das informações de uma farmácia tendo em conta
 * o ID dado pelo utilizador.
 * No final adiciona um botão para que o cliente consiga agendar uma vacina.
 */
function obterDadosFarmacia() {
    const id = $("#FarmaciaID").val();
    $.ajax({
        type: "GET",
        url: "https://magno.di.uevora.pt/tweb/t1/farmacia/get/" + id,
        success: function (dados) {
            var farmacia = dados.farmacia;
            if (dados.status !== "ok") {
                var html = "<h5>Erro: Farmácia não existe.</h5>";
                $("#dadosFarmacia").html(html);
            } else {
                $("#dadosFarmacia").html("");
                var html = "<h4 style=\"text-align: center;\">Informações da Farmácia</h4>";
                html += "<p><strong>Nome:</strong> " + farmacia.name + "</p>";
                html += "<p><strong>Diretor:</strong> " + farmacia.director + "</p>";
                html += "<p><strong>Email:</strong> " + farmacia.email + "</p>";
                html += "<p><strong>ID:</strong> " + farmacia.entity_id + "</p>";
                html += "<p><strong>Latitude:</strong> " + farmacia.latitude + "</p>";
                html += "<p><strong>Longitude:</strong> " + farmacia.longitude + "</p>";
                html += "<p><strong>Localidade Código Postal:</strong> " + farmacia.postal_code_locality + "</p>";
                html += "<p><strong>Região Código Postal:</strong> " + farmacia.postal_code_region + "</p>";
                html += "<p><strong>Sufixo Código Postal:</strong> " + farmacia.postal_code_sufix + "</p>";
                html += "<p><strong>Zona Código Postal:</strong> " + farmacia.postal_code_zone + "</p>";
                html += "<p><strong>Rua:</strong> " + farmacia.street_name + "</p>";
                html += "<p><strong>Telefone:</strong> " + farmacia.telephone + "</p>";
                html += "<h4>Serviços</h4>";
                html += "<ul>";
                for (var i = 0; i < farmacia.services.length; i++) {
                    html += "<li>" + farmacia.services[i] + "</li>";
                }

                html += "<p><button onclick=\" agendarvacina(" + farmacia.entity_id + ")\">Agendar Vacina</button></p>";

                html += "</ul>";
                $("#dadosFarmacia").html(html);
            }
        },
        dataType: "json"
    });
}

/*
 * Função que mostra uma listagem das farmácias. Ao clicar no nome de uma
 * farmácia é mostrado os seus detalhes.
 */
var paginaAtual = 0;
var last_page;
function Listar() {
    $.ajax({
        type: "POST",
        url: "https://magno.di.uevora.pt/tweb/t1/farmacia/list/",
        data: {page: paginaAtual},
        success: function (dados) {
            var listaP = document.getElementById("paginas");
            listaP.innerHTML = "<button onclick=\"mudarPagina(-1)\">Anterior</button> <button onclick=\"mudarPagina(1)\">Próxima</button>";
            last_page = dados.last_page;
            if (dados.status !== "ok") {
                var html = "<h5>Erro: Não existem farmácias a mostrar.</h5>";
                $("#lista").html(html);
            } else {
                $("#lista").html("");
                var html = "<ul>";
                var farmacia = dados.farmacias;
                for (let f of farmacia) {
                    html += "<li onclick=\"mostrarDetalhes('" + f.entity_id + "', '" + f.name + "', '" + f.postal_code_locality + "')\">" +
                            f.name +
                            "</li>";
                }
                html += "</ul>";
                $("#lista").html(html);
            }

        },
        dataType: "json"
    });
}

/*
 * Função auxiliar para que quando é clicado no botão para mudar a página
 * Chame a função listar com a paginaAtual nova.
 */
function mudarPagina(direcao) {
    var novaPagina = paginaAtual + direcao;
    if (novaPagina >= 0 && novaPagina <= last_page) {
        paginaAtual = novaPagina;
        Listar();
    }
}

/*
 * Função auxiliar da função Listar() que recebe os detalhes da farmácia e
 * mostra essa informação numa caixa de alert.
 */
function mostrarDetalhes(id, nome, postal_code_locality) {
    alert("Detalhes da farmácia: \nID: " + id + " \nNome: " + nome + " \nLocaliade: " + postal_code_locality);
}

/*
 * Função que serve para limpar as divs que são preenchidas pelas consultas
 * dos utilizadores.
 */
function limparDiv(nomeDiv) {
    var div = document.getElementById(nomeDiv);
    while (div.firstChild) {
        div.removeChild(div.firstChild);
    }
    if (nomeDiv === "listaFarmacia") {
        var novaDiv = document.createElement('div');
        novaDiv.id = "lista";
        div.appendChild(novaDiv);
        var paginasDiv = document.createElement('div');
        paginasDiv.id = "paginas";
        div.appendChild(paginasDiv);
        paginaAtual = 0;
    }
}

/*
 * Função que recebe uma string e retira-lhe acentos, caracteres especiais e torna-a em letras minusculas.
 */
function removerAcentos(str) {
    return str.normalize("NFD").replace(/[\u0300-\u036f]/g, "").replace(/[^\w\s]/gi, '').toLowerCase();
}

/*
 * A função preenche a variavel farmaciasFiltradas com a lista das farmacias 
 * provenientes do servidor. Adiciona uma div com as caixas de pesquisa e
 * chama a função exibirFarmacias para preencher a div com as informações
 * da variavel farmaciasFiltradas.
 */
var farmaciasFiltradas;
function carregarFarmacias() {
    var preenche = document.getElementById("dadosDaProcura");
    preenche.innerHTML = "";
    var contt = "<div id=\"dadosDaProcura\"><div id=\"botoes\"><label for=\"nomeInput\">Nome: </label>  <input type=\"text\" id=\"nomeInput\"> <label for=\"localidadeInput\"> Localidade: </label>     <input type=\"text\" id=\"localidadeInput\"> <button onclick=\"procurarFarmacia()\">Filtrar</button></div><div id=\"informacao\"></div>";
    preenche.innerHTML = contt;
    $.ajax({
        type: "POST",
        url: "https://magno.di.uevora.pt/tweb/t1/farmacia/search/",
        data: {name: "", postal_code_locality: ""},
        success: function (dados) {
            var resultado = document.getElementById("informacao");
            resultado.innerHTML = "";
            if (dados.status !== "ok") {
                var html = "<h5>Nenhuma farmácia encontrada.</h5>";
                resultado.innerHTML = html;
            } else {

                farmaciasFiltradas = [];
                for (var i = 0; i < dados.farmacias.length; i++) {
                    var farmacia = dados.farmacias[i];
                    farmaciasFiltradas.push(farmacia);
                }

                if (farmaciasFiltradas.length === 0) {
                    var html = "<h5>Nenhuma farmácia encontrada.</h5>";
                    resultado.innerHTML = html;
                } else {
                    exibirFarmacias(farmaciasFiltradas, "informacao");
                }
            }
        },
        dataType: "json"
    }
    );
}

/*
 * A função filtra as farmacias tendo em conta as strings dadas pelo utilizador.
 * E chama a função exibirFarmacias para preencher a div com o resultado obtido.
 */
function procurarFarmacia() {
    var nome = $("#nomeInput").val();
    var localidade = $("#localidadeInput").val();
    nome = removerAcentos(nome);
    localidade = removerAcentos(localidade);
    var resultado = document.getElementById("informacao");
    resultado.innerHTML = "";

    var farmaciasFiltradas_aux = [];
    for (var i = 0; i < farmaciasFiltradas.length; i++) {
        var farmacia = farmaciasFiltradas[i];
        var nomedafarmacia = removerAcentos(farmacia.name);
        var localidaadedafarmacia = removerAcentos(farmacia.postal_code_locality);
        if (nomedafarmacia.includes(nome) && localidaadedafarmacia.includes(localidade)) {
            farmaciasFiltradas_aux.push(farmacia);
        }
    }

    if (farmaciasFiltradas_aux.length === 0) {
        var html = "<h5>Nenhuma farmácia encontrada para os critérios de pesquisa.</h5>";
        resultado.innerHTML = html;
    } else {
        exibirFarmacias(farmaciasFiltradas_aux, "informacao");
    }
}

/*
 * A função recebe uma lista de objetos e o nome da div onde deve preencher 
 * com a informação.
 */
function exibirFarmacias(farmacias, div_destino) {
    var resultado = document.getElementById(div_destino);
    resultado.innerHTML = "<h3>Resultados da Pesquisa:</h3>";
    farmacias.forEach(function (farmacia) {
        var html =
                "<p><strong>ID: </strong> " +
                farmacia.entity_id +
                "<br><strong>Nome: </strong> " +
                farmacia.name +
                "<br><strong>Localidade: </strong> " +
                farmacia.postal_code_locality +
                "</p>";
        resultado.innerHTML += html;
    });
}

/*
 * A função obtem o tipo de vacina selecionado e após a consulta ao servidor
 * chama a função exibirFarmacias para mostrar os dados na div.
 */
function procurarVacinas() {
    var opcaoSelecionada = document.querySelector('input[name="opcao"]:checked');

    if (opcaoSelecionada) {
        opcaoSelecionada = opcaoSelecionada.value;
        $.ajax({
            type: "POST",
            url: "https://magno.di.uevora.pt/tweb/t1/farmacia/searchvaccine",
            data: {vaccine: opcaoSelecionada},
            success: function (dados) {
                exibirFarmacias(dados.farmacias, "dadosDasVacinas");
            },
            dataType: "json"
        });
    } else {
        var html = "<h5>Erro: Selecione uma das vacinas.</h5>";
        $("#dadosDasVacinas").html(html);
    }
}

/*
 * Função chamada quando o utilizador clica no botão "Agendar Vacina"
 * Mostra os campos que são necessários preencher e chama a função validAgendarVacina.
 */
function agendarvacina(farmaciaid) {
    limparDiv('dadosFarmacia');

    var html = "<h4>Agendar Vacina</h4>";
    html += "<p><label for=\"utenteID\">Seu Número de Identificação Fiscal (NIF):</label>";
    html += "<input type=\"text\" id=\"utenteID\"></p>";
    html += "<p><label for=\"scheduleDate\">Data do Agendamento:</label>";
    html += "<input type=\"date\" id=\"scheduleDate\"></p>";
    html += '<label for="vacinaSelecionada">Selecione a Vacina:</label>';
    html += '<select id="vacinaSelecionada"></select>';
    document.getElementById('dadosFarmacia').innerHTML = html;

    var vacinas = [];
    $.ajax({
        type: "GET",
        url: "https://magno.di.uevora.pt/tweb/t1/farmacia/get/" + farmaciaid,
        success: function (farm) {
            var farmacia = farm.farmacia;

            for (let f of farmacia.services) {
                if ((f === "gripe" || f === "covid-19") && !vacinas.includes(f)) {
                    vacinas.push(f);
                }
            }

            var dropdownVacinas = $("#vacinaSelecionada");
            dropdownVacinas.empty();

            for (var i = 0; i < vacinas.length; i++) {
                dropdownVacinas.append("<option value='" + vacinas[i] + "'>" + vacinas[i] + "</option>");
            }
        },
        dataType: "json"
    });

    var botaoagendar = $("#dadosFarmacia");
    html = "<p><button onclick=\"validAgendarVacina(" + farmaciaid + ")\">Agendar</button></p>";
    botaoagendar.append(html);
}

/*
 * Função valida os campos e envia para o servidor registar o agendamento.
 */
function validAgendarVacina(farmacia_id) {
    var utenteID = $("#utenteID").val();
    var scheduleDate = $("#scheduleDate").val();
    var escolhadropdown = $('#vacinaSelecionada').val();

    $.ajax({
        type: "POST",
        url: "https://magno.di.uevora.pt/tweb/t1/schedule/add",
        data: {user_id: utenteID, vaccine: escolhadropdown, entity_id: farmacia_id, schedule_date: scheduleDate},
        success: function (resposta) {
            var mensagem;
            if (resposta.status === "ok") {
                mensagem = "Agendamento bem-sucedido. Código: " + resposta.schedule_code;
            } else {
                mensagem = "Falha ao agendar. Motivo: " + resposta.status;
            }
            alert(mensagem);
        },
        dataType: "json"
    });
}