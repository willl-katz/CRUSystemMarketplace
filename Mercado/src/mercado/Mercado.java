package mercado;

import interfaceComando.CommandLine;


public class Mercado {
    public static void main(String[] args) {
        System.out.println("Bem vindo ao sistema mercado");
        System.out.println("Certifique-se de ter um banco de dados 'Mercado' que esteja devidamente ativo");
        System.out.println("Certifique-se de cadastrar esse banco no arquivo persistence.xml");
        System.out.println("");
        CommandLine comando = new CommandLine();
    }
}