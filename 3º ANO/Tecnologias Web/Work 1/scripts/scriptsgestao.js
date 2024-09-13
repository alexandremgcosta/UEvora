
/*
 * Função com o objetivo de listar todas as farmácias que tem disponivel a
 * vacina da gripe.
 * No fim da div mostra o gráfico com o numero de farmácias por localidade.
 */
function listarFarmaciasGripe() {
    var div;
    $.ajax({
        type: "GET",
        url: "https://magno.di.uevora.pt/tweb/t1/program/gripe/list",
        success: function (dados) {
            var localidades = {};
            div = $('#listaFarmaciasGripe').empty();
            var html = "";

            for (let f of dados.farmacias) {
                html += "<ul>";
                html += "<p> <strong>ID:</strong> " + f.entity_id + "</p> ";
                html += "<p> <strong>Nome:</strong> " + f.name + "</p> ";
                html += "<p> <strong>Localidade:</strong> " + f.postal_code_locality + "</p> ";
                html += "</ul>";
                html += "<button onclick=\"removerFarmacia(" + f.entity_id + ")\">Remover</button>";
                html += "<br>";

                if (localidades[f.postal_code_locality]) {
                    localidades[f.postal_code_locality]++;
                } else {
                    localidades[f.postal_code_locality] = 1;
                }
            }

            var localidadesArray = Object.entries(localidades).map(([localidade, contagem]) => ({localidade, contagem}));

            // Ordenar o array pelo campo contagem em ordem decrescente
            localidadesArray.sort((a, b) => b.contagem - a.contagem);
            localidadesArray = localidadesArray.slice(0, 5);
            
            html += "<canvas id=\"myChart_gripe\" style=\"width:100%;max-width:600px\"></canvas>";
            var div_aux = $("#listaFarmaciasGripe");
            div_aux.append(html);

            // Criar gráfico
            const ctx = document.getElementById('myChart_gripe');
            const myChart_gripe = new Chart(ctx, {
                type: 'bar',
                data: {
                    labels: localidadesArray.map(item => item.localidade),
                    datasets: [{
                            label: 'Número de Farmácias por localidade',
                            data: localidadesArray.map(item => item.contagem),
                            backgroundColor: [
                                'rgba(255, 99, 132, 0.7)',
                                'rgba(54, 162, 235, 0.7)',
                                'rgba(255, 206, 86, 0.7)',
                                'rgba(75, 192, 192, 0.7)',
                                'rgba(153, 102, 255, 0.7)',
                                'rgba(255, 159, 64, 0.7)',
                            ],
                            borderColor: [
                                'rgba(255, 99, 132, 1)',
                                'rgba(54, 162, 235, 1)',
                                'rgba(255, 206, 86, 1)',
                                'rgba(75, 192, 192, 1)',
                                'rgba(153, 102, 255, 1)',
                                'rgba(255, 159, 64, 1)',
                            ],
                            borderWidth: 1
                        }]
                },
                options: {
                    scales: {
                        y: {
                            beginAtZero: true
                        }
                    },
                    responsive: false //Para que o gráfico não mude de tamanho
                }
            });
        },
        dataType: "json"
    });
}

/*
 * Função com o objetivo de listar todas as farmácias que tem disponivel a
 * vacina da covid.
 * No fim da div mostra o gráfico com o numero de farmácias por localidade.
 */
function listarFarmaciasCovid() {
    var div;
    $.ajax({
        type: "GET",
        url: "https://magno.di.uevora.pt/tweb/t1/program/covid19/list",
        success: function (dados) {
            var localidades = {};
            div = $('#listaFarmaciasCovid').empty();
            var html = "";
            for (let f of dados.farmacias) {
                html += "<ul>";
                html += "<p> <strong>ID:</strong> " + f.entity_id + "</p> ";
                html += "<p> <strong>Nome:</strong> " + f.name + "</p> ";
                html += "<p> <strong>Localidade:</strong> " + f.postal_code_locality + "</p> ";
                html += "</ul>";
                html += "<button onclick=\"removerFarmacia(" + f.entity_id + ")\">Remover</button>";
                html += "<br>";
                $("#listaFarmaciasCovid").html(html);

                if (localidades[f.postal_code_locality]) {
                    localidades[f.postal_code_locality]++;
                } else {
                    localidades[f.postal_code_locality] = 1;
                }

            }

            var localidadesArray = Object.entries(localidades).map(([localidade, contagem]) => ({localidade, contagem}));

            // Ordenar o array pelo campo contagem em ordem decrescente
            localidadesArray.sort((a, b) => b.contagem - a.contagem);
            localidadesArray = localidadesArray.slice(0, 5);

            html += "<canvas id=\"myChart_covid\" style=\"width:100%; max-width:600px\"></canvas>";
            var div_aux = $("#listaFarmaciasCovid");
            div_aux.append(html);

            // Criar gráfico
            const ctx = document.getElementById('myChart_covid');
            const myChart_covid = new Chart(ctx, {
                type: 'bar',
                data: {
                    labels: localidadesArray.map(item => item.localidade),
                    datasets: [{
                            label: 'Número de Farmácias por localidade',
                            data: localidadesArray.map(item => item.contagem),
                            backgroundColor: [
                                'rgba(255,255,255, 0.7)',
                                'rgba(54, 162, 235, 0.7)',
                                'rgba(255, 206, 86, 0.7)',
                                'rgba(75, 192, 192, 0.7)',
                                'rgba(153, 102, 255, 0.7)',
                                'rgba(255, 159, 64, 0.7)',
                            ],
                            borderColor: [
                                'rgba(255, 99, 132, 1)',
                                'rgba(54, 162, 235, 1)',
                                'rgba(255, 206, 86, 1)',
                                'rgba(75, 192, 192, 1)',
                                'rgba(153, 102, 255, 1)',
                                'rgba(255, 159, 64, 1)',
                            ],
                            borderWidth: 1
                        }]
                },
                options: {
                    scales: {
                        y: {
                            beginAtZero: true
                        }
                    },
                    responsive: false //Para que o gráfico não mude de tamanho
                }
            });
        },
        dataType: "json"
    });
}

/*
 * Função permite adicionar uma nova farmácia.
 */
function adicionarFarmacia() {
    var idFarmacia = $("#idFarmacia").val();
    var nomeFarmacia = $("#nomeFarmacia").val();
    var localidadeFarmacia = $("#localidadeFarmacia").val();
    var vacinaGripe = $("#gripe").prop("checked");
    var vacinaCovid = $("#covid").prop("checked");

    var servicos = "";
    if (vacinaGripe && vacinaCovid) {
        servicos = "gripe, covid-19";
    } else if (vacinaGripe && !vacinaCovid) {
        servicos = "gripe";
    } else if (!vacinaGripe && vacinaCovid) {
        servicos = "covid-19";
    }

    $.ajax({
        type: "POST",
        url: "https://magno.di.uevora.pt/tweb/t1/program/add",
        data: {entity_id: idFarmacia,
            name: nomeFarmacia,
            postal_code_locality: localidadeFarmacia,
            services: servicos
        },
        success: function (resposta) {
            console.log(resposta);
            if (resposta === "ok") {
                alert("Farmácia adicionada com sucesso.");
            } else {
                alert("Erro na criação da farmácia. Erro: " + resposta.status);
            }
        },
        dataType: "json"
    });
}

/*
 * Função para remover a farmácia.
 */
function removerFarmacia(id) {
    $.ajax({
        type: "POST",
        url: "https://magno.di.uevora.pt/tweb/t1/program/remove",
        data: {entity_id: id},
        success: function (resposta) {
            console.log(resposta);
            if (resposta.status === "ok") {
                alert("Farmácia removida com sucesso.");
            } else {
                alert("Erro ao tentar remover farmácia.");
            }
        },
        dataType: "json"
    });
}
