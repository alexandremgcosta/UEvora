
/*
 * Função que mostra os agendamentos de cada utente.
 */
function listarAgendamentos() {
    var id_utente = $('#utenteID').val();
    $("#agendamentos").empty();

    if (!id_utente) {
        $("#agendamentos").html("Por favor insira um identificador válido.");
        return;
    }

    $.ajax({
        type: "POST",
        url: "https://magno.di.uevora.pt/tweb/t1/schedule/list",
        data: {user_id: id_utente},
        success: function (resposta) {
            var lista = resposta.schedule_list;
            var listaAgendamentos = $("#agendamentos");
            if (lista.length > 0) {
                listaAgendamentos.append("<ul>");

                for (var i = 0; i < lista.length; i++) {
                    listaAgendamentos.append("<li style=\"margin-left: 1em;\"><strong>ID da farmácia:</strong> " + lista[i][1] + ', <strong>Vacina:</strong> ' + lista[i][2] + ', <strong>Data:</strong> ' + lista[i][3] + ", <strong>Código:</strong> " + lista[i][4] + " <button onclick=\"removerAgendamento('" + id_utente + "', '" + lista[i][4] + "')\">Remover</button></li>");
                }
                listaAgendamentos.append("</ul>");
            } else {
                listaAgendamentos.append('<li>Nenhum agendamento encontrado.</li>');
            }
        },
        dataType: "json"
    });
}

/*
 * Função que remove o agendamento.
 */
function removerAgendamento(userid,codigo) {
    $.ajax({
        type: "POST",
        url: "https://magno.di.uevora.pt/tweb/t1/schedule/remove",
        data: {user_id: userid , schedule_code: codigo
        },
        success: function (resposta) {
            console.log(resposta);
            if(resposta.status ==="ok"){
                alert("Agendamento removido com sucesso.");
            } else{
                alert("Não foi possivel remover o agendamento.");
            }
        }
    });
}