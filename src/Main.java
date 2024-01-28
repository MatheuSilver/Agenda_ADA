import java.io.*;
import java.util.*;

public class Main {
    //Programa feito utilizando o Java 11
    static Scanner sc;
    private static final Map<Long, Contato> contact_list = new HashMap<>();

    public static void main(String[] args) {
        sc = new Scanner(System.in);
        initialize_contact_list();
        init_app();
    }

    private static void init_app() {
        System.out.println("##################\n"
                         + "##### AGENDA #####\n"
                         + "##################\n");

        show_contacts();
        show_menu();
        clean_scanner();
        exit_and_save();
    }

    private static void show_menu() {
        while (true) {
            System.out.println(">>>> Menu <<<<\n"
                             + "1 - Adicionar Contato\n"
                             + "2 - Remover Contato\n"
                             + "3 - Editar Contato\n"
                             + "4 - Consultar informações\n"
                             + "5 - Sair");
            try {
                int input = sc.nextInt();
                switch (input) {
                    case 1:
                        add_contact();
                        break;
                    case 2:
                        remove_contact();
                        break;
                    case 3:
                        edit_contact();
                        break;
                    case 4:
                        show_get_info_menu();
                        break;
                    case 5:
                        return;
                    default:
                        System.out.println("Opção inválida, por favor, selecione uma dentre as 4 opções.");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Por favor, insira um número inteiro correspondente à opção do menu.");
                clean_scanner();
            }
        }
    }

    private static void initialize_contact_list() {
        File contacts_file = new File("assets\\contacts.csv");
        File phone_numbers_file = new File("assets\\phone_numbers.csv");

        try (BufferedReader file_contents = new BufferedReader(new FileReader(contacts_file))) {
            String line;
            String[] contact_info;
            while ((line = file_contents.readLine()) != null) {
                contact_info = line.split(",");
                Contato contact = new Contato(Long.parseLong(contact_info[0]), contact_info[1], contact_info[2]);
                contact_list.put(contact.getId(), contact);
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo de contatos: " + e.getMessage());
        }

        try (BufferedReader file_contents = new BufferedReader(new FileReader(phone_numbers_file))) {
            String line;
            String[] number_info;
            while ((line = file_contents.readLine()) != null) {
                number_info = line.split(",");
                Telefone phone_number = new Telefone(Long.parseLong(number_info[0]), number_info[1], Long.parseLong(number_info[2]));
                if (contact_list.containsKey(phone_number.getId())) {
                    contact_list.get(phone_number.getId()).add_phone_number(phone_number);
                } else {
                    System.out.printf("Não existe um contato para o ID %d\n", phone_number.getId());
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo de números de telefone: " + e.getMessage());
        }
    }

    private static void add_contact() {
        System.out.println("Digite um ID para seu novo contato: ");
        Long new_contact_id;
        while (true) {
            try {
                new_contact_id = sc.nextLong();
                if (contact_list.containsKey(new_contact_id)) {
                    System.out.println("A ID escolhida já é usada por um contato. Por favor, escolha outra.");
                } else {
                    break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Por favor, insira um número inteiro para o ID do contato.");
                clean_scanner();
            }
        }

        clean_scanner();

        System.out.println("Digite o nome do seu novo contato: ");
        String new_contact_name = sc.nextLine();
        System.out.println("Digite o sobrenome do seu novo contato: ");
        String new_contact_surname = sc.nextLine();
        Contato contact = new Contato(new_contact_id, new_contact_name, new_contact_surname);
        String continue_option = "s";
        while (continue_option.equals("s")) {
            System.out.println("Digite o DDD do seu novo contato:");
            String new_contact_ddd = sc.nextLine();
            System.out.println("Digite o número do seu novo contato:");
            long new_contact_number;
            while (true) {
                try {
                    new_contact_number = sc.nextLong();
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("Por favor, insira um número inteiro para o número de telefone.");
                    clean_scanner();
                }
            }
            boolean already_added = false;
            for (Contato registered_contact : contact_list.values()) {
                for (int j = 0; j < registered_contact.getTelefones().size() && !already_added; j++) {
                    if (registered_contact.getTelefones().get(j).check_match(new_contact_ddd, new_contact_number)) {
                        already_added = true;
                    }
                }
                break;
            }
            if (!already_added){
                contact.add_phone_number(new Telefone(new_contact_id, new_contact_ddd, new_contact_number));
            }else{
                System.out.println("Este número de telefone já está associado a um contato.");
            }

            clean_scanner();
            System.out.println("Deseja adicionar outro número à este contato? (s/n)");
            continue_option = sc.nextLine().toLowerCase();
        }

        contact_list.put(contact.getId(), contact);
    }

    private static void remove_contact() {
        System.out.println("Digite o ID do contato que deseja excluir: ");
        long contact_id;
        while (true) {
            try {
                contact_id = sc.nextLong();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Por favor, insira um número inteiro para o ID do contato.");
                clean_scanner();
            }
        }
        if (contact_list.containsKey(contact_id)){
            System.out.println("O contato: "+contact_list.get(contact_id).getNome()+" foi excluído.");
            contact_list.remove(contact_id);
        } else {
            System.out.println("Não existe um contato com o ID escolhido");
        }
    }

    private static void edit_contact() {
        System.out.println("Digite o ID do contato que deseja editar: ");
        long contact_id;
        while (true) {
            try {
                contact_id = sc.nextLong();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Por favor, insira um número inteiro para o ID do contato.");
                clean_scanner();
            }
        }
        if (contact_list.containsKey(contact_id)){
            Contato contact = contact_list.get(contact_id);
            int input;
            while (true){
                System.out.println(">>>> Opções <<<<\n"
                        + "1 - Editar nome\n"
                        + "2 - Editar sobrenome\n"
                        + "3 - Editar número\n"
                        + "4 - Editar ID"
                        + "5 - Sair");

                input = sc.nextInt();

                switch (input) {
                    case 1:
                        System.out.println("Digite o novo nome do seu contato: ");
                        clean_scanner();
                        String new_contact_name = sc.nextLine();
                        contact.setNome(new_contact_name);
                        break;
                    case 2:
                        System.out.println("Digite o sobrenome do seu novo contato: ");
                        clean_scanner();
                        String new_contact_surname = sc.nextLine();
                        contact.setSobreNome(new_contact_surname);
                        break;
                    case 3:
                        System.out.println(contact.get_formatted_phone_numbers());
                        System.out.println("Escolha a posição de um dos números acima para modificar\nou digite -1 para cancelar");
                        int position = sc.nextInt();
                        if (position==-1){
                            break;
                        } else if (contact.is_valid_position(position)){
                            System.out.println("Escreva o novo ddd para este contato");
                            clean_scanner();
                            String new_ddd = sc.nextLine();
                            System.out.println("Escreva o novo número para este contato");
                            long new_number;
                            while (true) {
                                try {
                                    new_number = sc.nextLong();
                                    break;
                                } catch (InputMismatchException e) {
                                    System.out.println("Por favor, insira um número inteiro para o número de telefone.");
                                    clean_scanner();
                                }
                            }
                            Telefone new_phone_number = new Telefone(contact.getId(), new_ddd, new_number);
                            contact.setTelefone(position, new_phone_number);
                        } else {
                            System.out.println("Não existe um número de telefone para a posição especificada.");
                        }
                        break;
                    case 4:
                        System.out.println("Escreva o novo ID para o contato: ");
                        long new_id;
                        while (true) {
                            try {
                                new_id = sc.nextLong();
                                break;
                            } catch (InputMismatchException e) {
                                System.out.println("Por favor, insira um número inteiro para o ID do contato.");
                                clean_scanner();
                            }
                        }
                        if (!contact_list.containsKey(new_id)){
                            contact.setId(new_id);
                            Long previous_id = contact.getId();
                            contact_list.put(new_id, contact); //Não há como editar a key em hashmaps.
                            contact_list.remove(previous_id);
                        } else {
                            System.out.println("O novo ID escolhido já está em uso.");
                        }
                        break;
                    case 5:
                        return;
                    default:
                        System.out.println("Opção inválida, por favor, selecione uma dentre as 5 opções.");
                        break;
                }
            }
        } else {
            System.out.println("Não existe um contato com o ID escolhido");
        }
    }

    private static void exit_and_save() {
        System.out.println("Deseja salvar as mudanças na base de dados? (s/n)");
        String save_input = sc.nextLine().toLowerCase();
        if (save_input.equals("s")){
            save_contacts();
        }
        sc.close();
    }

    private static void save_contacts() {
        File contacts_file = new File("assets\\contacts.csv");
        File phone_numbers_file = new File("assets\\phone_numbers.csv");

        try (PrintWriter contacts_writer = new PrintWriter(new FileWriter(contacts_file));
             PrintWriter phone_numbers_writer = new PrintWriter(new FileWriter(phone_numbers_file))) {

            for (Contato contact : contact_list.values()) {
                contacts_writer.println(contact.getId() + "," + contact.getNome() + "," + contact.getSobreNome());
                for (Telefone phone : contact.getTelefones()) {
                    phone_numbers_writer.println(phone.getId() + "," + phone.getDDD() + "," + phone.getNumero());
                }
            }

            System.out.println("Contatos salvos com sucesso!");

        } catch (IOException e) {
            System.out.println("Erro ao salvar os contatos: " + e.getMessage());
        }
    }

    private static void show_get_info_menu(){
        int input;
        while (true) {
            System.out.println(">>>> Opções <<<<\n"
                    + "1 - Listar contatos \n"
                    + "2 - Listar telefones\n"
                    + "3 - Sair");
            input = sc.nextInt();
            switch (input){
                case 1:
                    show_contacts();
                    break;
                case 2:
                    System.out.println("Digite o ID do contato: ");
                    Long contact_id = sc.nextLong();
                    show_phone_numbers(contact_id);
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Opção inválida, por favor, selecione uma dentre as 3 opções.");
                    break;

            }
        }
    }

    private static void show_contacts() {
        if (contact_list.isEmpty()) {
            System.out.println("Não há contatos cadastrados ainda\n");
        } else {
            System.out.println(">>>> Contatos <<<<\n"
                    + "Id  |  Nome");
            for (Contato contact : contact_list.values()) {
                System.out.println(contact.print_formatted());
            }
        }
    }

    private static void show_phone_numbers(Long contact_ID) {
        if (contact_list.containsKey(contact_ID)) {
            Contato contact = contact_list.get(contact_ID);
            System.out.println(contact.getNome() + " | " + contact.get_formatted_phone_numbers());
        } else {
            System.out.println("Não existe um contato com o ID escolhido");
        }
    }
    
    private static void clean_scanner(){ sc.nextLine(); }//Nessas horas que prefiro o input do python;
}
