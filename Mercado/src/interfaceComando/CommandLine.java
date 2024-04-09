package interfaceComando;
import java.util.Scanner;

public class CommandLine {
    private final Scanner scan =  new Scanner(System.in);
    private String opcao = "";
    private CommandCliente clienteCmd = new CommandCliente();
    private CommandVendedor vendedorCmd = new CommandVendedor();
    private CommandItem itemCmd = new CommandItem();
    public CommandLine(){
        init();
    }
    
    public void init(){
        System.out.println("Digite a opção desejada:");
        System.out.println("1 - Cliente");
        System.out.println("2 - Vendedor");
        System.out.println("3 - Item");
        System.out.println("4 - Sair");
        this.opcao = scan.nextLine();
        switch (this.opcao)
        {
            case "1":
                System.out.println("");
                System.out.println("");
                this.cliente();
           case "2":
                System.out.println("");
                System.out.println("");
                this.vendedor();
           case "3":
                System.out.println("");
                System.out.println("");
                this.item();
           case "4":
                System.out.println("");
                System.out.println("Obrigado por usar o sistema Mercado");
                System.exit(0);
           default:
                System.out.println("Opção invalida");
                System.out.println("");
                System.out.println("");
                this.init();
        }
    }
    
    public void cliente(){
        System.out.println("Digite a opção desejada:");
        System.out.println("1 - Adicionar");
        System.out.println("2 - Alterar");
        System.out.println("3 - Buscar");
        System.out.println("4 - Voltar");
        this.opcao = scan.nextLine();
        switch (this.opcao)
        {
            case "1":
                System.out.println("");
                System.out.println("");
                clienteCmd.adicionar();
                this.init();
           case "2":
                System.out.println("");
                System.out.println("");
                try {
                    clienteCmd.alterar();
                } catch (Exception e){
                    System.out.println(e.getClass());
                    e.printStackTrace();
                }
                this.init();
           case "3":
                System.out.println("");
                System.out.println("");
                this.buscaCliente();
            case "4":
                System.out.println("");
                System.out.println("");
                this.init();
            default:
                System.out.println("Opção invalida");
                System.out.println("");
                System.out.println("");
                this.cliente();
        }
    }
    
    public void buscaCliente(){
        System.out.println("Digite a opção desejada:");
        System.out.println("1 - Buscar todos");
        System.out.println("2 - Buscar por id");
        System.out.println("3 - Voltar");
        this.opcao = scan.nextLine();
        switch(this.opcao){
            case "1":
                System.out.println("");
                System.out.println("");
                clienteCmd.buscarTodos();
                this.init();
            case "2":
                System.out.println("");
                System.out.println("");
                clienteCmd.buscarPorId();
                this.init();
            case"3":
                System.out.println("");
                System.out.println("");
                this.cliente();
            default:
                System.out.println("Opção invalida");
                System.out.println("");
                System.out.println("");
                this.buscaCliente();
        }
    }
    
    public void vendedor(){
        System.out.println("Digite a opção desejada:");
        System.out.println("1 - Adicionar");
        System.out.println("2 - Alterar");
        System.out.println("3 - Buscar");
        System.out.println("4 - Voltar");
        this.opcao = scan.nextLine();
        switch (this.opcao)
        {
            case "1":
                System.out.println("");
                System.out.println("");
                vendedorCmd.adicionar();
                this.init();
           case "2":
                System.out.println("");
                System.out.println("");
                try {
                    vendedorCmd.alterar();
                } catch (Exception e){
                    System.out.println(e.getClass());
                    e.printStackTrace();
                }
                this.init();
           case "3":
                System.out.println("");
                System.out.println("");
                this.buscaVendedor();
            case "4":
                System.out.println("");
                System.out.println("");
                this.init();
            default:
                System.out.println("Opção invalida");
                System.out.println("");
                System.out.println("");
                this.vendedor();
        }
    }
    
    public void buscaVendedor(){
        System.out.println("Digite a opção desejada:");
        System.out.println("1 - Buscar todos");
        System.out.println("2 - Buscar por id");
        System.out.println("3 - Voltar");
        this.opcao = scan.nextLine();
        switch(this.opcao){
            case "1":
                System.out.println("");
                System.out.println("");
                vendedorCmd.buscarTodos();
                this.init();
            case "2":
                System.out.println("");
                System.out.println("");
                vendedorCmd.buscarPorId();
                this.init();
            case "3":
                System.out.println("");
                System.out.println("");
                this.vendedor();
            default:
                System.out.println("Opção invalida");
                System.out.println("");
                System.out.println("");
                this.buscaVendedor();
        }
    }
    
    public void item(){
        System.out.println("Digite a opção desejada:");
        System.out.println("1 - Adicionar");
        System.out.println("2 - Alterar");
        System.out.println("3 - Buscar");
        System.out.println("4 - Voltar");
        this.opcao = scan.nextLine();
        switch (this.opcao)
        {
            case "1":
                System.out.println("");
                System.out.println("");
                itemCmd.adicionar();
                this.init();
           case "2":
                System.out.println("");
                System.out.println("");
                try {
                    itemCmd.alterar();
                } catch (Exception e){
                    System.out.println(e.getClass());
                    e.printStackTrace();
                }
                this.init();
           case "3":
                System.out.println("");
                System.out.println("");
                this.buscaItem();
            case "4":
                System.out.println("");
                System.out.println("");
                this.init();
            default:
                System.out.println("Opção invalida");
                System.out.println("");
                System.out.println("");
                this.cliente();
        }
    }
        
    public void buscaItem(){
        System.out.println("Digite a opção desejada:");
        System.out.println("1 - Buscar todos");
        System.out.println("2 - Buscar por id");
        System.out.println("3 - Voltar");
        this.opcao = scan.nextLine();
        switch(this.opcao){
            case "1":
                System.out.println("");
                System.out.println("");
                itemCmd.buscarTodos();
                this.init();
            case "2":
                System.out.println("");
                System.out.println("");
                itemCmd.buscarPorId();
                this.init();
            case "3":
                System.out.println("");
                System.out.println("");
                this.item();
            default:
                System.out.println("Opção invalida");
                System.out.println("");
                System.out.println("");
                this.buscaItem();
        }
    }
}
