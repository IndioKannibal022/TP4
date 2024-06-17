/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tp4.lj.tictactoe;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class TicTacToeClient {
    public static void main(String[] args) {
        String serverAddress = "127.0.0.1"; // Endereço IP do servidor
        int port = 12345; // Porta do servidor

        try (Socket socket = new Socket(serverAddress, port);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in)) {

            String resposta;
            while ((resposta = in.readLine()) != null) {
                System.out.println(resposta); // Exibe a mensagem recebida do servidor

                if (resposta.startsWith("Posicao x")) {
                    String x = scanner.nextLine();
                    out.println(x); // Envia a coordenada x para o servidor
                }

                if (resposta.startsWith("Posicao y")) {
                    String y = scanner.nextLine();
                    out.println(y); // Envia a coordenada y para o servidor
                }

                if (resposta.equals("Deseja jogar de novo? (s/n)")) {
                    String continuar = scanner.nextLine();
                    out.println(continuar); // Envia a resposta se deseja jogar novamente
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
