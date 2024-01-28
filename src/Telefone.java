public class Telefone {
    private Long id;
    private final String ddd;
    private final Long numero;

    public Telefone(Long id, String ddd, Long numero) {
        this.id = id;
        this.ddd = ddd;
        this.numero = numero;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long newId){ this.id = newId; }

    public String getDDD(){
        return this.ddd;
    }

    public Long getNumero(){
        return this.numero;
    }

    public boolean check_match(String another_phone_ddd, Long another_phone_number){
        return (this.ddd.equals(another_phone_ddd) && this.numero.equals(another_phone_number));
    }
    public String get_formatted_number (){
        return "(" + this.ddd + ") " + this.numero;
    }


}
