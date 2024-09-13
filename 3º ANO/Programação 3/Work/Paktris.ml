(* Definição de tipos *)

type piece_type = I | S | O | T

type piece = {
  p_type: piece_type;
  rotations: int; (* Número de rotações de 90° *)
}

type board = int array array

(* Funções para manipulação de peças *)

let rotate_piece piece =
  { piece with rotations = (piece.rotations + 1) mod 4 }

(* Função para posicionar uma peça no tabuleiro *)
let place_piece board piece col =
  let piece_matrix = (* Lógica para obter a matriz da peça baseada no tipo e rotações *) in
  let rows, cols = Array.length piece_matrix, Array.length piece_matrix.(0) in
  let updated_board = Array.copy board in

  for i = 0 to rows - 1 do
    for j = 0 to cols - 1 do
      if piece_matrix.(i).(j) = 1 then
        updated_board.(i).(col + j) <- 1 (* Atualiza o tabuleiro com a peça *)
    done;
  done;

  updated_board

(* Verificação de encaixe no tabuleiro *)
let check_fit board piece col =
  let piece_matrix = (* Lógica para obter a matriz da peça baseada no tipo e rotações *) in
  let rows, cols = Array.length piece_matrix, Array.length piece_matrix.(0) in

  (* Verifica se a peça cabe no tabuleiro sem exceder as bordas *)
  col + cols <= Array.length board.(0) &&
  rows <= Array.length board &&
  not (Array.exists (fun row -> row.(col) = 1) board) (* Verifica se não há colisão com peças existentes no tabuleiro *)

(* Função principal para processar jogadas *)
let process_moves board moves =
  let process_move board (piece_type, rotations, col) =
    let piece = { p_type = piece_type; rotations } in
    if check_fit board piece col then
      place_piece board piece col
    else
      board
  in
  List.fold_left process_move board moves

(* Exemplo de utilização *)
let () =
  let n = 10 in
  let m = 10 in
  let initial_board = init_board n m in
  let moves = [(I, 0, 0); (I, 0, 0); (O, 0, 2); (O, 0, 0);] in
  let final_board = process_moves initial_board moves in
  (* Exibir o estado final do tabuleiro ou realizar outras ações conforme necessário *)