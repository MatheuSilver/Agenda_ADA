import java.util.ArrayList;
import java.util.List;

public class Contato {
    private Long id;
    private String nome;
    private String sobreNome;
    private final List<Telefone> telefones;

    public Contato(Long id, String nome, String sobreNome) {
        this.id = id;
        this.nome = nome;
        this.sobreNome=sobreNome;
        this.telefones = new ArrayList<>();
    }

    public void add_phone_number(Telefone phone_number){
        this.telefones.add(phone_number);
    }

    public String print_formatted() {
        return String.format("%-3d | %s %s", this.id, this.nome, this.sobreNome);
    }

    public String get_formatted_phone_numbers() {
        if (telefones.isEmpty()) {
            return "Sem números de telefone";
        }

        StringBuilder formatted_numbers = new StringBuilder(telefones.get(0).get_formatted_number());
        for (int i = 1; i < telefones.size(); i++) {
            formatted_numbers.append(" | ").append(telefones.get(i).get_formatted_number());
        }
        return formatted_numbers.toString();
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long new_id) {
        this.id = new_id;
        for (Telefone phone_number : telefones){
            phone_number.setId(new_id);
        }
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) { this.nome = nome; }

    public String getSobreNome() {
        return this.sobreNome;
    }
    public void setSobreNome(String sobreNome) { this.sobreNome = sobreNome; }

    public List<Telefone> getTelefones() {
        return this.telefones;
    }

    public void setTelefone(int position, Telefone new_phone_number) { this.telefones.set(position, new_phone_number); }
    public boolean is_valid_position(int position){ return this.telefones.size() > position && position > -1; }
}