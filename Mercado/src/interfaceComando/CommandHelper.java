package interfaceComando;

public class CommandHelper {
    public boolean cpfEstaCerto(String cpf){
        try { 
            if(cpf.length() != 11){
                System.out.println("Cpf deve ter 11 digitos");
                return true;
            } else if (cpf.matches("^[0-9]+$")){
                return false;
            } else {
                System.out.println("Cpf deve ser numerico");
                return true;
            }
        } catch (NumberFormatException e) {
             System.out.println("Cpf deve ser numerico");
              return true;
        }catch (Exception e) {
             System.out.println("Erro não identificado");
             System.out.println(e.getLocalizedMessage());
              return true;
        }
    }
    
    public boolean nomeEstaCerto(String nome){
        try { 
            if(nome.length() < 3){
                System.out.println("Nome deve ter mais de 3 digitos");
                return true;
            } else if (!nome.matches("^[a-zA-Z\\u00C0-\\u00FF ]+$")){
                System.out.println("Nome contem caracteres inválidos");
                System.out.println(nome);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
             System.out.println("Erro não identificado");
             System.out.println(e.getLocalizedMessage());
              return true;
        }
    }
    
    public boolean valorEstaCerto(String valor, String nomeValor, double valorMinimo){
        try { 
            if(!valor.matches("(?:\\.|,|[0-9])*")){
                System.out.println(nomeValor + " contem caractere inválido");
                return true;
            }
            if(!valor.matches("^[1-9](\\d)*,\\d{2}$")){
                System.out.println(nomeValor + " esta no formato incorreto");
                return true;
            }else if(Double.parseDouble(valor.replaceAll( "," , "." )) <= valorMinimo){
                System.out.println(nomeValor + " deve ser maior que "+ valorMinimo);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
             System.out.println("Erro não identificado");
             System.out.println(e.getLocalizedMessage());
              return true;
        }
    }
    
    public boolean enderecoEstaCerto(String endereco){
        if(endereco.length() < 10){
            System.out.println("Endereço deve ter mais de 10 caracteres");
            return true;
        }else{
            return false;
        }
    }
    
    public void limpaConsole(){
        //tentamos implementar uma linpeza no console 
        //mas aparentemente o console do netbeans não limpa
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
