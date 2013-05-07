//Pacote da classe de teste - Deyvid
package UI;

public class Pessoa {

    //atributos - precisa ser provado por questão de segurança
    private String nome;
    private int idade;
    private String endereco;
    
    //metodo publico para retornar um valor - acessores
    public String getNome()
    {
        return this.nome;
    }            
    public int getidate(){
        return this.idade;
    }
    public String getEndereco(){
        return this.endereco;
    }
    
    //metodos modificadores
    public void setNome(String p)
    {
        this.nome = p;
    }            
    public void setIdate(int p){
        this.idade = p;
    }
    public void setEndereco(String p){
       this.endereco = p;
    }    
    
}
