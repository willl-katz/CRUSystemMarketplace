package interfaceComando;
import dao.ItemJpaController;
import dao.exceptions.NonexistentEntityException;
import entidades.Item;
import java.util.Scanner;
import javax.persistence.Persistence;

public class CommandItem {
    protected String nome = "";
    protected String unidade = "";
    protected String entradaPreco = "";
    protected double valor;
    protected Item itemGenerico = new Item(9999, "teste", "teste", 10.0);
    protected Integer id;
    private final Scanner scan =  new Scanner(System.in,  "latin1");
    private ItemJpaController itemCtlr = new ItemJpaController(Persistence.createEntityManagerFactory("MercadoPU"));
    private CommandHelper helper = new CommandHelper();

    public void adicionar(){
        System.out.println("Digite o nome");
        do{
            this.nome = scan.nextLine();
        } while (helper.nomeEstaCerto(this.nome));
        System.out.println("Digite a unidade de medida");
        do{
            this.unidade = scan.nextLine();
        } while (this.unidade.length() < 2);
        System.out.println("Digite o preço por unidade");
        System.out.println("O preço deve receber obrigatoriamente os centavos, mesmo que 00");
        System.out.println("O preço deve receber os centavos após uma virgula");
        System.out.println("O preço não deve receber o caractere ponto (.)");
        do {
            this.entradaPreco = scan.nextLine();
        }while (helper.valorEstaCerto(this.entradaPreco, "Preço", 0));
        this.valor = Double.parseDouble(this.entradaPreco.replaceAll( "," , "." ));
        try {
            itemCtlr.create(new Item(Integer.SIZE, nome, unidade, valor));
            System.out.println("Item adicionado com sucesso");
            System.out.println("");
        }  catch (Exception e){
            System.out.println("");
            System.out.println(e.getLocalizedMessage());
        }
    }
    
    public void alterar() throws NonexistentEntityException, Exception {
        System.out.println("Digite o id");
        this.id = scan.nextInt();
        scan.nextLine();
        System.out.println("Digite o nome");
        do{
            this.nome = scan.nextLine();
        } while (helper.nomeEstaCerto(this.nome));
        System.out.println("Digite a unidade de medida");
        do{
            this.unidade = scan.nextLine();
        } while (this.unidade.length() < 2);
        System.out.println("Digite o preço por unidade");
        System.out.println("O preço deve receber obrigatoriamente os centavos, mesmo que 00");
        System.out.println("O preço deve receber os centavos após uma virgula");
        System.out.println("O preço não deve receber o caractere ponto (.)");
        do {
            this.entradaPreco = scan.nextLine();
        }while (helper.valorEstaCerto(this.entradaPreco, "Preço", 0));
        this.valor = Double.parseDouble(this.entradaPreco.replaceAll( "," , "." ));
        try {
            itemCtlr.edit(new Item(this.id, this.nome, this.unidade, this.valor));
            System.out.println("Item alterado com sucesso");
            System.out.println("");
        }  catch (Exception e){
            System.out.println("");
            System.out.println(e.getLocalizedMessage());
        }
    }
    
    public void buscarTodos(){
        try {
            itemCtlr.findItemEntities().forEach((item)-> {
                System.out.println("");
                System.out.println("Id:        " + item.getId());
                System.out.println("Nome:     " + item.getNome());
                System.out.println("Unidade:  " + item.getUnidade());
                System.out.println("Valor:     " + item.getValor());
            });
        }  catch (Exception e){
            System.out.println("");
            System.out.println(e.getLocalizedMessage());
        }
        System.out.println("");
    }
    
    public void buscarPorId(){
        System.out.println("Digite o id");
        this.id = scan.nextInt();
        System.out.println("");
        
        try {
            this.itemGenerico = itemCtlr.findItem(this.id);
            if(this.itemGenerico != null){
                System.out.println("Id:        " + this.itemGenerico.getId());
                System.out.println("Nome:     " + this.itemGenerico.getNome());
                System.out.println("Unidade:  " + this.itemGenerico.getUnidade());
                System.out.println("Valor:     " + this.itemGenerico.getValor());
                System.out.println("");
            } else {
                System.out.println("");
                System.out.println("Item não cadastrado");
                System.out.println("");
            }
        }  catch (Exception e){
            System.out.println("");
            System.out.println(e.getLocalizedMessage());
        }
    }
}
