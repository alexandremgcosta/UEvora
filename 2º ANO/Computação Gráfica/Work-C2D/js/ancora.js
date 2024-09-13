//Função para transformações
function transformacao(gc, sc1, sc2, tr1, tr2){
    gc.save();
    gc.scale(sc1,sc2);
    gc.translate(tr1,tr2);
}

function areiaescura(gc) {
    let i = gc.createLinearGradient(128,50,128,200);
        i.addColorStop(0.0, "#4d2600");
        i.addColorStop(1.0, "#804000");
    gc.fillStyle = i;
    gc.strokeStyle = "#734d26";
    
    gc.beginPath();
    gc.moveTo(0, 192);
    gc.lineTo(0,256);
    gc.lineTo(456,256);
    gc.lineTo(456,185);
    gc.quadraticCurveTo(90, 130, 0, 192);

    gc.stroke();
    gc.fill();
}

function areiaclara(gc) {
    let i = gc.createLinearGradient(128,50,128,200);
        i.addColorStop(0.0, "#734d26");
        i.addColorStop(1.0, "#ac7339");
    gc.fillStyle = i;
    gc.strokeStyle = "#b38600";

    gc.beginPath();
    gc.moveTo(0, 230);
    gc.lineTo(0,256);
    gc.lineTo(456,256);
    gc.lineTo(456,200);
    gc.quadraticCurveTo(315, 150, 0, 230);

    gc.stroke();
    gc.fill();
}

function bolhas(gc){
    //gera as bolhas por cima da ancora
  for (var i=1;i<4;i++){  
        gc.save();
        gc.beginPath();
        transformacao(gc, 2 , 2 , 120+(i*2), i*16);
        gc.arc(0, 0, 2, 0, 2 *Math.PI, true);
        gc.fillStyle="#66ffff";
        gc.globalAlpha = 0.8;
        gc.fill();
        gc.restore();
    }

    for (var i=1;i<4;i++){  
        gc.save();
        gc.beginPath();
        transformacao(gc, 2 , 2 , 127+(i*2), i*13);
        gc.arc(0, 0, 2, 0, 2 *Math.PI, true);
        gc.fillStyle="#66ffff";
        gc.globalAlpha = 0.5;
        gc.fill();
        gc.restore();
    }
} 

function pedras(gc){
    //gera as pedras no fundo do mar
  for (var i=1;i<8;i++){  
        gc.save();
        gc.beginPath();
        transformacao(gc, 2 , 2 , -20+(i*30), 115);
        gc.ellipse(0, 0, 3, 2, 0, 0, 2 *Math.PI, true);
        gc.fillStyle="#4d2600";
        gc.fill();
        gc.restore();
    }

    for (var i=1;i<10;i++){  
        gc.save();
        gc.beginPath();
        transformacao(gc, 2 , 2 , 20+(i*25), 120);
        gc.ellipse(0, 0, 3, 2, 0, 0, 2 *Math.PI, true);
        gc.fillStyle="#4d2600";
     
        gc.fill();
        gc.restore();
    }
} 

function peixes(gc){
    //gera os peixes no mar
  for (var i=0;i<3;i++){  
        gc.save();
        gc.beginPath();
        transformacao(gc, 1 , 1 , 0+(i*25), 12*(i*1));
        gc.ellipse(20, 20, 8, 5, 0, 0, 2 *Math.PI, true);
        gc.moveTo(20,20);
        gc.lineTo(8,16);
        gc.lineTo(8,25);
        gc.lineTo(20,20);
        gc.fillStyle="#ff99ff";
        gc.strokeStyle="#ff99ff"
        gc.fill();
        gc.stroke();
        gc.restore();
    }
} 

function fundodomar(gc) {

    gc.fillStyle = "blue";
    gc.fillRect(0,0,456,256);
    gc.fill();

    areiaescura(gc);
    areiaclara(gc);
    bolhas(gc);
    pedras(gc);
    peixes(gc);
    gc.restore();
}

function centrodaancora(gc, a, b, c){
    gc.beginPath();
    gc.arc(a, b, c-4, 0, 2 *Math.PI, true);
    gc.fillStyle = "blue";

    gc.fill();
}

function ancora(gc) {
    gc.lineWidth = 8;
    
    gc.lineJoin = "round";
    
    gc.strokeStyle = "#666f7e";
    
    let g = gc.createLinearGradient(128,50,128,200);
      g.addColorStop(0.0, "#d9d9d9");
      g.addColorStop(1.0, "#808080");

    gc.fillStyle = g;


    gc.beginPath();

    // transformações na ancora
    transformacao(gc, 0.5 , 0.5 , 400, 200);

    gc.arc(128, 61.5, 33, (Math.PI/180)*80, (Math.PI/180)*100, true); 
    
    gc.moveTo(120, 90);
    gc.lineTo(120, 110);
    gc.lineTo(95, 110);
    gc.quadraticCurveTo(87, 116, 95, 122);
    gc.lineTo(120, 122);
    gc.lineTo(120, 170);
    
    gc.bezierCurveTo(120, 220, 60, 180, 55, 145);
    
    gc.lineTo(65, 145);
    gc.lineTo(46.5, 122);
    gc.lineTo(28, 145);
    gc.lineTo(38, 145);
    
    gc.quadraticCurveTo(40, 190, 100, 213);
    
    gc.quadraticCurveTo(122, 220, 128, 240);
    
    gc.quadraticCurveTo(134, 220, 156, 213);
    
    gc.quadraticCurveTo(216, 190, 218, 145);
    
    gc.lineTo(228, 145);
    gc.lineTo(209.5, 122);
    gc.lineTo(191, 145);
    gc.lineTo(201, 145);
    
    gc.bezierCurveTo(196, 180, 136, 220, 136, 170);
    
    gc.lineTo(136,122);
    gc.lineTo(161,122);
    
    gc.quadraticCurveTo(169, 116, 161, 110);
    
    gc.lineTo(136, 110);
    gc.lineTo(136, 90);  
    
    gc.moveTo(143, 61.5);
    gc.arc(128, 61.5, 15, 0, 2 *Math.PI, true);

    gc.fill();
    gc.stroke();
 
    //Valores a serem atribuidos ao centro da ancora
    let a=128;
    let b=61.5;
    let c=15;
    

    //Coloca o centro da ancora da cor do fundo do mar
    centrodaancora(gc, a, b, c);
}


function main() {
    let gc = document
        .getElementById("acanvas")
        .getContext("2d");

        fundodomar(gc);
        ancora(gc);

}

