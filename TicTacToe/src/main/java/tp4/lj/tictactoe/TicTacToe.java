/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package tp4.lj.tictactoe;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

class Pecas {
    int x;
    int y;
    char tipo;
    static int nJogadas = 0;

    Pecas(int y, int x) {
        this.x = x;
        this.y = y;
        if (nJogadas % 2 == 0) {
            tipo = 'X';
        } else {
            tipo = 'O';
        }
        nJogadas++;
    }
}

class Tabuleiro {
    char[][] localizacaoPecas;

    Tabuleiro() {
        localizacaoPecas = new char[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                localizacaoPecas[i][j] = ' '; // Inicializa o tabuleiro vazio
            }
        }
    }

    void escreverRegras(PrintWriter out1, PrintWriter out2) {
        out1.println("Regras do jogo:");
        out1.println("Cada jogador vai ter um formato de peça. O jogador1 tem o 'X' e o jogador2 tem o 'O'.");
        out1.println("Ganha quem fizer uma linha de três elementos primeiro. Podendo ser na vertical, horizontal, ou diagonal.");

        out2.println("Regras do jogo:");
        out2.println("Cada jogador vai ter um formato de peça. O jogador1 tem o 'X' e o jogador2 tem o 'O'.");
        out2.println("Ganha quem fizer uma linha de três elementos primeiro. Podendo ser na vertical, horizontal, ou diagonal.");
    }

    void atualizarTabuleiro(ArrayList<Pecas> todasPecas) {
        for (Pecas peca : todasPecas) {
            localizacaoPecas[peca.y][peca.x] = peca.tipo;
        }
    }

    void imprimirTabuleiro(PrintWriter out1, PrintWriter out2) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (j != 2) {
                    out1.print(localizacaoPecas[i][j] + "|");
                    out2.print(localizacaoPecas[i][j] + "|");
                } else {
                    out1.print(localizacaoPecas[i][j]);
                    out2.print(localizacaoPecas[i][j]);
                }
            }
            out1.println();
            out2.println();
        }
    }

    void resetarTabuleiro() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                localizacaoPecas[i][j] = ' '; // Reinicializa o tabuleiro vazio
            }
        }
    }
}

public class TicTacToe {
    public static void main(String[] args) {
        int port = 12345; // Porta do servidor

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Servidor iniciado na porta " + port);

            while (true) {
                System.out.println("Aguardando jogadores...");
                Socket player1Socket = serverSocket.accept();
                System.out.println("Jogador 1 conectado: " + player1Socket.getInetAddress().getHostAddress());

                Socket player2Socket = serverSocket.accept();
                System.out.println("Jogador 2 conectado: " + player2Socket.getInetAddress().getHostAddress());

                new GameHandler(player1Socket, player2Socket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ServidorJogo extends Thread {
    private Socket jogador1Socket;
    private Socket jogador2Socket;
    private PrintWriter out1;
    private PrintWriter out2;
    private BufferedReader in1;
    private BufferedReader in2;
    private Tabuleiro tabuleiro;
    private ArrayList<Pecas> todasPecas;

    public ServidorJogo(Socket jogador1Socket, Socket jogador2Socket) {
        this.jogador1Socket = jogador1Socket;
        this.jogador2Socket = jogador2Socket;
        tabuleiro = new Tabuleiro();
        todasPecas = new ArrayList<>();
        try {
            out1 = new PrintWriter(jogador1Socket.getOutputStream(), true);
            out2 = new PrintWriter(jogador2Socket.getOutputStream(), true);
            in1 = new BufferedReader(new InputStreamReader(jogador1Socket.getInputStream()));
            in2 = new BufferedReader(new InputStreamReader(jogador2Socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            while (true) {
                iniciarJogo();
                out1.println("Deseja jogar de novo? (s/n)");
                out2.println("Deseja jogar de novo? (s/n)");

                String continuar1 = in1.readLine();
                String continuar2 = in2.readLine();

                if (continuar1 == null || continuar2 == null || continuar1.equalsIgnoreCase("n") || continuar2.equalsIgnoreCase("n")) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                jogador1Socket.close();
                jogador2Socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void iniciarJogo() throws IOException {
        Pecas.nJogadas = 0; // Resetar o contador de jogadas para um novo jogo
        tabuleiro.resetarTabuleiro();
        todasPecas.clear();
        tabuleiro.escreverRegras(out1, out2);
        tabuleiro.imprimirTabuleiro(out1, out2); // Imprime o tabuleiro inicial

        while (true) {
            int currentPlayer = Pecas.nJogadas % 2;
            PrintWriter out = currentPlayer == 0 ? out1 : out2;
            BufferedReader in = currentPlayer == 0 ? in1 : in2;
            PrintWriter outOpponent = currentPlayer == 0 ? out2 : out1;

            out.println("Agora é a vez do jogador " + (currentPlayer + 1) + " (" + (currentPlayer == 0 ? 'X' : 'O') + ")");
            outOpponent.println("Aguardando jogada do jogador " + (currentPlayer + 1));

            int x, y;

            do {
                out.println("Posicao x (1 a 3):");
                x = Integer.parseInt(in.readLine()) - 1;
                if (x < 0 || x > 2) {
                    out.println("Valor invalido a posição x deve ser entre 1 e 3");
                }
            } while (x < 0 || x > 2);

            do {
                out.println("Posicao y (1 a 3):");
                y = Integer.parseInt(in.readLine()) - 1;
                if (y < 0 || y > 2) {
                    out.println("Valor invalido a posição y deve ser entre 1 e 3");
                }
            } while (y < 0 || y > 2);

            // Verifica se a posição já está ocupada por 'X' ou 'O'
            if (tabuleiro.localizacaoPecas[y][x] == 'X' || tabuleiro.localizacaoPecas[y][x] == 'O') {
                out.println("Posição já ocupada, tente novamente.");
                continue;
            }

             Pecas peca = new Pecas(y, x);
            todasPecas.add(peca);
            tabuleiro.atualizarTabuleiro(todasPecas);
            tabuleiro.imprimirTabuleiro(out1, out2);

            // Verificar se há um vencedor ou empate
            if (vencedor(tabuleiro)) {
                out.println("Temos um vencedor!");
                outOpponent.println("Temos um vencedor!");
                break;
            }
            if (Pecas.nJogadas == 9) {
                out.println("Deu velha!");
                outOpponent.println("Deu velha!");
                break;
            }
        }
    }

    private boolean vencedor(Tabuleiro tabuleiro) {
        char[][] p = tabuleiro.localizacaoPecas;

        for (int i = 0; i < 3; i++) {
            if (p[i][0] != ' ' && p[i][0] == p[i][1] && p[i][1] == p[i][2]) {
                return true;
            }
            if (p[0][i] != ' ' && p[0][i] == p[1][i] && p[1][i] == p[2][i]) {
                return true;
            }
        }

        if (p[0][0] != ' ' && p[0][0] == p[1][1] && p[1][1] == p[2][2]) {
            return true;
        }
        if (p[0][2] != ' ' && p[0][2] == p[1][1] && p[1][1] == p[2][0]) {
            return true;
        }

        return false;
    }
}

