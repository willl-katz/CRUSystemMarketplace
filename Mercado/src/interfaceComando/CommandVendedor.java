package interfaceComando;
import dao.VendedorJpaController;
import dao.exceptions.NonexistentEntityException;
import entidades.Vendedor;
import java.util.Scanner;
import javax.persistence.Persistence;

public class CommandVendedor {
    protected String nome = "";
    protected String cpf = "";
    protected String entradaSalario = "";
    protected double salario;
    protected Vendedor vendedorGenerico;
    protected int id;
    protected int opcao;
    private final Scanner scan =  new Scanner(System.in, "latin1");
    private VendedorJpaController vendedorCtlr = new VendedorJpaController(Persistence.createEntityManagerFactory("MercadoPU"));
    private CommandHelper helper = new CommandHelper();
    
    public void adicionar(){
        System.out.println("Digite o nome");
        do{
            this.nome = scan.nextLine();
        } while (helper.nomeEstaCerto(this.nome));
        System.out.println("Digite o cpf");
        do{
           this.cpf = scan.nextLine();
        } while(helper.cpfEstaCerto(this.cpf));
        System.out.println("Digite o salario");
        System.out.println("O salario deve receber obrigatoriamente os centavos, mesmo que 00");
        System.out.println("O salario deve receber os centavos após uma virgula");
        System.out.println("O salario não deve receber o caractere ponto (.)");
        do {
            this.entradaSalario = scan.nextLine();
        }while (helper.valorEstaCerto(this.entradaSalario, "Salario", 900));
        this.salario = Double.parseDouble(this.entradaSalario.replaceAll( "," , "." ));
        
        try {
            vendedorCtlr.create(new Vendedor(Integer.SIZE, nome, cpf, salario));
            System.out.println("Vendedor adicionado com sucesso");
            System.out.println("");
        }  catch (Exception e){
            System.out.println("");
            System.out.println(e.getLocalizedMessage());
        }
        
    }
    
    public void alterar() throws NonexistentEntityException, Exception {
        System.out.println("Digite o id");
        this.id = scan.nextInt();
        System.out.println("Digite o nome");
        do{
            this.nome = scan.nextLine();
        } while (helper.nomeEstaCerto(this.nome));
        System.out.println("Digite o cpf");
        do{
           this.cpf = scan.nextLine();
        } while(helper.cpfEstaCerto(this.cpf));
        System.out.println("Digite o salario");
        System.out.println("O salario deve receber obrigatoriamente os centavos, mesmo que 00");
        System.out.println("O salario deve receber os centavos após uma virgula");
        System.out.println("O salario não deve receber o caractere ponto (.)");
        do {
            this.entradaSalario = scan.nextLine();
        }while (helper.valorEstaCerto(this.entradaSalario, "Salario", 900));
        try {
            vendedorCtlr.edit(new Vendedor(this.id, this.nome, this.cpf, this.salario));
            System.out.println("Pessoa alterada com sucesso");
            System.out.println("");
        }  catch (Exception e){
            System.out.println("");
            System.out.println(e.getLocalizedMessage());
        }
    }
    
    public void buscarTodos(){
        try {
            vendedorCtlr.findVendedorEntities().forEach((item)-> {
                System.out.println("");
                System.out.println("Id:        " + item.getId());
                System.out.println("Nome:     " + item.getNome());
                System.out.println("Cpf:       " + item.getCpf());
                System.out.println("Salario:   " + item.getSalario());
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
            this.vendedorGenerico = vendedorCtlr.findVendedor(id);
            if(this.vendedorGenerico != null){
                System.out.println("Id:        " + vendedorGenerico.getId());
                System.out.println("Nome:     " + vendedorGenerico.getNome());
                System.out.println("Cpf:       " + vendedorGenerico.getCpf());
                System.out.println("Salario:   " + vendedorGenerico.getSalario());
                System.out.println("");
            } else {
                System.out.println("");
                System.out.println("Vendedor não cadastrado");
                System.out.println("");
            }
        }  catch (Exception e){
            System.out.println("");
            System.out.println(e.getLocalizedMessage());
        }
    }
}
