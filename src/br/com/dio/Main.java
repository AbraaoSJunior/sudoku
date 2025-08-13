package br.com.dio;

import br.com.dio.model.Board;
import br.com.dio.model.Espaco;

import java.util.*;
import java.util.stream.Stream;

import static br.com.dio.util.BoardTemplate.BOARD_TEMPLATE;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toMap;

public class Main {

    private final static Scanner scanner = new Scanner(System.in);

    private static Board board;

    private final static int BOARD_LIMIT = 9;

    public static void main(String[] args) {
        final var posicoes = Stream.of(args)
                .collect(toMap(
                        k -> k.split(";")[0],
                        v -> v.split(";")[1]
                ));
        var opcao = -1;
        while (true){
            System.out.println("Selecione uma das opções a seguir");
            System.out.println("1 - Iniciar um novo Jogo");
            System.out.println("2 - Inserir um novo número");
            System.out.println("3 - Remover um número");
            System.out.println("4 - Visualizar jogo atual");
            System.out.println("5 - Verificar status do jogo");
            System.out.println("6 - limpar jogo");
            System.out.println("7 - Finalizar jogo");
            System.out.println("8 - Sair");

            opcao = scanner.nextInt();

            switch (opcao){
                case 1 -> iniciarJogo();
                case 2 -> inserirNumero();
                case 3 -> removerNumero();
                case 4 -> mostrarJogoAtual();
                case 5 -> mostrarStatusJogo();
                case 6 -> limparJogo();
                case 7 -> finalizarJogo();
                case 8 -> System.exit(0);
                default -> System.out.println("Opção inválida, selecione uma das opções do menu:");
            }
        }
    }

    private static void iniciarJogo() {
        if (nonNull(board)){
            System.out.println("O jogo já foi iniciado");
            return;
        }
        int[][] boardArray = new int[9][9];
        gerarSudoku(boardArray);
        List<List<Espaco>> espacos = new ArrayList<>();
        Random random = new Random();
        int numerosFixos = 33;

        for (int i = 0; i < BOARD_LIMIT; i++) {
            espacos.add(new ArrayList<>());
            for (int j = 0; j < BOARD_LIMIT; j++) {
                boolean fixo = random.nextInt(100) < (numerosFixos * 100 / 81);
                var espacoAtual = new Espaco(boardArray[i][j], fixo);
                if (!fixo) {
                    espacoAtual.limparEspaco();
                }
                espacos.get(i).add(espacoAtual);
            }
        }
        board = new Board(espacos);
        System.out.println("O jogo está pronto para começar");
    }

    private static boolean gerarSudoku(int[][] board) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] == 0) {
                    for (int num = 1; num <= 9; num++) {
                        if (ehValido(board, i, j, num)) {
                            board[i][j] = num;
                            if (gerarSudoku(board)) {
                                return true;
                            }
                            board[i][j] = 0;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean ehValido(int[][] board, int linha, int coluna, int num) {
        // Verificar linha
        for (int i = 0; i < 9; i++) {
            if (board[linha][i] == num) {
                return false;
            }
        }

        // Verificar coluna
        for (int i = 0; i < 9; i++) {
            if (board[i][coluna] == num) {
                return false;
            }
        }

        // Verificar região 3x3
        int regiaoLinha = linha - linha % 3;
        int regiaoColuna = coluna - coluna % 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[regiaoLinha + i][regiaoColuna + j] == num) {
                    return false;
                }
            }
        }

        return true;
    }


    private static void inserirNumero() {
        if (isNull(board)){
            System.out.println("O jogo ainda não foi iniciado");
            return;
        }

        System.out.println("Informe a coluna que em que o número será inserido");
        var col = runUntilGetValidNumber(0, 8);
        System.out.println("Informe a linha que em que o número será inserido");
        var row = runUntilGetValidNumber(0, 8);
        System.out.printf("Informe o número que vai entrar na posição [%s,%s]\n", col, row);
        var value = runUntilGetValidNumber(1, 9);
        if (!board.mudarValor(col, row, value)){
            System.out.printf("A posição [%s,%s] tem um valor fixo\n", col, row);
        }
    }

    private static void removerNumero() {
        if (isNull(board)){
            System.out.println("O jogo ainda não foi iniciado");
            return;
        }

        System.out.println("Informe a coluna que em que o número será inserido");
        var coluna = runUntilGetValidNumber(0, 8);
        System.out.println("Informe a linha que em que o número será inserido");
        var linha = runUntilGetValidNumber(0, 8);
        if (!board.excluirValor(coluna, linha)){
            System.out.printf("A posição [%s,%s] tem um valor fixo\n", coluna, linha);
        }
    }

    private static void mostrarJogoAtual() {
        if (isNull(board)){
            System.out.println("O jogo ainda não foi iniciado");
            return;
        }

        var args = new Object[81];
        var argPos = 0;
        for (int i = 0; i < BOARD_LIMIT; i++) {
            for (var coluna: board.getEspacos()){
                args[argPos ++] = " " + ((isNull(coluna.get(i).getAtual())) ? " " : coluna.get(i).getAtual());
            }
        }
        System.out.println("Seu jogo se encontra da seguinte forma");
        System.out.printf((BOARD_TEMPLATE) + "\n", args);
    }

    private static void mostrarStatusJogo() {
        if (isNull(board)){
            System.out.println("O jogo ainda não foi iniciado");
            return;
        }

        System.out.printf("O jogo atualmente se encontra no status %s\n", board.getStatus().getLabel());
        if(board.temErros()){
            System.out.println("O jogo contém erros");
        } else {
            System.out.println("O jogo não contém erros");
        }
    }

    private static void limparJogo() {
        if (isNull(board)){
            System.out.println("O jogo ainda não foi iniciado");
            return;
        }

        System.out.println("Tem certeza que deseja limpar seu jogo e perder todo seu progresso?");
        var confirm = scanner.next();
        while (!confirm.equalsIgnoreCase("sim") && !confirm.equalsIgnoreCase("não")){
            System.out.println("Informe 'sim' ou 'não'");
            confirm = scanner.next();
        }

        if(confirm.equalsIgnoreCase("sim")){
            board.resetar();
        }
    }

    private static void finalizarJogo() {
        if (isNull(board)){
            System.out.println("O jogo ainda não foi iniciado");
            return;
        }

        if (board.jogoFinalizado()){
            System.out.println("Parabéns você concluiu o jogo");
            mostrarJogoAtual();
            board = null;
        } else if (board.temErros()) {
            System.out.println("Seu jogo contém erros, verifique seu board e ajuste-o");
        } else {
            System.out.println("Você ainda precisa preencher algum espaço");
        }
    }


    private static int runUntilGetValidNumber(final int min, final int max){
        var current = scanner.nextInt();
        while (current < min || current > max){
            System.out.printf("Informe um número entre %s e %s\n", min, max);
            current = scanner.nextInt();
        }
        return current;
    }

}
