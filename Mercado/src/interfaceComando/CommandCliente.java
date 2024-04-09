package interfaceComando;
import dao.ClienteJpaController;
import dao.exceptions.NonexistentEntityException;
import entidades.Cliente;
import java.util.Scanner;
import javax.persistence.Persistence;

public class CommandCliente {
    protected String nome = "";
    protected String cpf = "";
    protected String endereco = "";
    protected Cliente clienteGenerico;
    protected int id;
    protected int opcao;
    private final Scanner scan =  new Scanner(System.in, "latin1");
    private ClienteJpaController clienteCtlr = new ClienteJpaController(Persistence.createEntityManagerFactory("MercadoPU"));
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
        System.out.println("Digite o endereco");
        do{
            this.endereco = scan.nextLine();
        } while (helper.enderecoEstaCerto(this.endereco));
        try {
            clienteCtlr.create(new Cliente(Integer.SIZE, nome, cpf, endereco));
            System.out.println("Cliente adicionado com sucesso");
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
        System.out.println("Digite o cpf");
        do{
           this.cpf = scan.nextLine();
        } while(helper.cpfEstaCerto(this.cpf));
        System.out.println("Digite o endereco");
        do{
            this.endereco = scan.nextLine();
        } while (helper.enderecoEstaCerto(this.endereco));
        try {
            clienteCtlr.edit(new Cliente(this.id, this.nome, this.cpf, this.endereco));
            System.out.println("Pessoa alterada com sucesso");
        System.out.println("");
        }  catch (Exception e){
            System.out.println("");
            System.out.println(e.getLocalizedMessage());
        }
        
    }
    
    public void buscarTodos(){
        try{
            clienteCtlr.findClienteEntities().forEach((item)-> {
                System.out.println("");
                System.out.println("Id:        " + item.getId());
                System.out.println("Nome:     " + item.getNome());
                System.out.println("Cpf:       " + item.getCpf());
                System.out.println("Endereco: " + item.getEndereco());
            });
        }  catch (Exception e){
            System.out.println("");
            System.out.println(e.getLocalizedMessage());
        }finally{
            System.out.println("");
        }
    }
    
    public void buscarPorId(){
        System.out.println("Digite o id");
        this.id = scan.nextInt();
        scan.nextLine();
        System.out.println("");
        try {
            this.clienteGenerico = clienteCtlr.findCliente(id);
            if(this.clienteGenerico != null){
                System.out.println("Id:        " + clienteGenerico.getId());
                System.out.println("Nome:     " + clienteGenerico.getNome());
                System.out.println("Cpf:       " + clienteGenerico.getCpf());
                System.out.println("Endereco: " + clienteGenerico.getEndereco());
                System.out.println("");
            } else {
                System.out.println("");
                System.out.println("Cliente não cadastrado.");
                System.out.println("");
            }
        }  catch (Exception e){
            System.out.println("");
            System.out.println(e.getLocalizedMessage());
        }
    }
}
