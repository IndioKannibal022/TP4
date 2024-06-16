/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package tp4.lj.tictactoe;
import java.util.*;

class Pecas {
    int x;
    int y;
    char tipo;
    static int nJogadas = 0;

    Pecas(int x, int y) {
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

    void escreverRegras() {
        System.out.println("Regras do jogo:");
        System.out.println("Cada jogador vai ter um formato de peça. O jogador1 tem o 'X' e o jogador2 tem o 'O'.");
        System.out.println("Ganha quem fizer uma linha de três elementos primeiro. Podendo ser na vertical, horizontal, ou diagonal.");
    }

    void atualizarTabuleiro(ArrayList<Pecas> todasPecas) {
        for (Pecas peca : todasPecas) {
            localizacaoPecas[peca.y][peca.x] = peca.tipo;
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (j != 2) {
                    System.out.print(localizacaoPecas[i][j] + "|");
                } else {
                    System.out.print(localizacaoPecas[i][j]);
                }
            }
            System.out.println();
        }
    }
}

public class TicTacToe{
    
    static Scanner scan = new Scanner(System.in);
    
    static void jogar(Tabuleiro tabuleiro, ArrayList<Pecas> todasPecas){
        int x;
        int y;
        
        System.out.println("");
        if(Pecas.nJogadas % 2 != 0){
            System.out.println("Agora e a vez do jogador2 (O)");
        }else{
            System.out.println("Agora e a vez do jogador1 (X)");
        }
        System.out.println("Posicao x:");
        x = scan.nextInt() - 1;
        if (x < 0 || x > 2){
            System.out.println("Valor invalido a posição x deve ser entre 1 e 3:");
            do{
                x = scan.nextInt() - 1;
            }while(x < 0 || x > 2);
        }
        System.out.println("Posicao y:");
        y = scan.nextInt() - 1;
                if (y < 0 || y > 2){
            System.out.println("Valor invalido a posição y deve ser entre 1 e 3:");
            do{
                y = scan.nextInt() - 1;
            }while(y < 0 || y > 2);
        }
        
        if (tabuleiro.localizacaoPecas[x][y] != ' ') {
           System.out.println("Posição já ocupada, tente novamente.");
           jogar(tabuleiro, todasPecas);
           return;
        }
        
        Pecas peca = new Pecas(x, y);
        todasPecas.add(peca);
        tabuleiro.atualizarTabuleiro(todasPecas);
        
        if(vencedor(tabuleiro)){
        System.out.println("Temos um vencedor");
        System.exit(0);
        }else if(Pecas.nJogadas == 9){
            System.out.println("Deu Velha");
            System.exit(0);
        }
        
    }
    static boolean vencedor(Tabuleiro tabuleiro){
        char[][] p = tabuleiro.localizacaoPecas;
        
        for (int i = 0; i < 3; i++){
            if(p[i][0] != ' ' && p[i][0] == p[i][1] && p[i][1] == p[i][2]){
                return true;
            }
            if(p[0][i] != ' ' && p[0][i] == p[1][i] && p[1][i] == p[2][i]){
                return true;
            }
        }
        
        if (p[0][0] != ' ' && p[0][0] == p[1][1] && p[1][1] == p[2][2]){
            return true;
        }
        if(p[0][2] != ' ' && p[0][2] == p[1][1] && p[1][1] == p[2][0]){
            return true;
        }
        
        return false;
    }
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int rodar = 1;
        Tabuleiro tabuleiro = new Tabuleiro();
        ArrayList<Pecas> todasPecas = new ArrayList<>();
        tabuleiro.escreverRegras();
        while(rodar == 1){
            System.out.print("Deseja continuar(s/n)?");
            String answer = scan.nextLine();
            if(answer.equalsIgnoreCase("n")){
                rodar = 2;
            }else{
            jogar(tabuleiro, todasPecas);
            }
        }
    }
}
