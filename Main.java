import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Main {

    // Lê a chave diretamente da Variável de Ambiente do Terminal (Sem hardcode no código)
    private static final String GEMINI_API_KEY = System.getenv("GEMINI_API_KEY");

    public static void main(String[] args) {
        configurarAcentuacaoTerminal();

        // Se a variável de ambiente não estiver definida no terminal, exibe o tutorial completo
        if (GEMINI_API_KEY == null || GEMINI_API_KEY.isBlank()) {
            exibirTutorialEInterromper();
            return;
        }

        Scanner scanner = new Scanner(System.in, obterCharsetConsole());

        exibirCabecalho();

        // 1. Coleta de dados com validação
        String nome = lerEntradaObrigatoria(scanner, "Digite o seu nome: ", "[Erro] O nome não pode estar vazio.");
        String materia = lerEntradaObrigatoria(scanner, "Qual a matéria de saúde que você está cursando? ", "[Erro] A matéria não pode estar vazia.");
        String semestre = lerEntradaObrigatoria(scanner, "Qual semestre você está cursando? (Ex: 2º Semestre): ", "[Erro] O semestre não pode estar vazio.");

        // 2. Menu de seleção do estilo de aprendizagem
        int opcaoEstilo = selecionarEstiloAprendizado(scanner);
        String estiloAprendizado = obterNomeEstilo(opcaoEstilo);

        // 3. Captura do tema desejado
        String tema = lerEntradaObrigatoria(scanner, "\nQual o tema você quer aprender agora? ", "[Erro] O tema não pode estar vazio.");

        // 4. Construção do Prompt para IA
        String promptConsolidado = PromptBuilder.buildFullPrompt(nome, materia, semestre, estiloAprendizado, tema);

        // 5. Chamada da API com animação no terminal
        simularCarregamento();

        String respostaIA = GeminiApiClient.enviarPrompt(promptConsolidado, GEMINI_API_KEY);

        // 6. Exibição do Resultado no Terminal
        exibirRespostaFinal(respostaIA);

        scanner.close();
    }

    private static void configurarAcentuacaoTerminal() {
        try {
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                new ProcessBuilder("cmd", "/c", "chcp 65001 > nul").inheritIO().start().waitFor();
            }
            System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8.name()));
        } catch (Exception ignored) {}
    }

    private static Charset obterCharsetConsole() {
        try {
            return System.console() != null ? System.console().charset() : StandardCharsets.UTF_8;
        } catch (Exception e) {
            return StandardCharsets.UTF_8;
        }
    }

    private static void exibirTutorialEInterromper() {
        System.out.println("==================================================================");
        System.out.println("  [ERRO] VARIÁVEL DE AMBIENTE 'GEMINI_API_KEY' NÃO CONFIGURADA   ");
        System.out.println("==================================================================");
        System.out.println("O sistema não encontrou a chave de API no terminal para conectar à IA.\n");
        
        System.out.println("------------------------------------------------------------------");
        System.out.println("  PASSO A PASSO: COMO OBTER E CONFIGURAR A CHAVE GRATUITA DO GEMINI ");
        System.out.println("------------------------------------------------------------------");
        System.out.println("1. PASSO 1: GERAR A CHAVE NO GOOGLE");
        System.out.println("   a) Acesse o site do Google AI Studio: https://aistudio.google.com/app/apikey");
        System.out.println("   b) Faça login com sua conta do Google.");
        System.out.println("   c) Clique no botão azul 'Create API key'.");
        System.out.println("   d) Copie a chave gerada (começa com 'AIzaSy...').\n");

        System.out.println("2. PASSO 2: INSERIR A CHAVE NO TERMINAL (SEM ALTERAR O CÓDIGO)");
        System.out.println("   * No POWERSHELL, execute o comando com a sua chave:");
        System.out.println("     $env:GEMINI_API_KEY=\"AIzaSy_Sua_Chave_Aqui\"\n");
        System.out.println("   * No PROMPT DE COMANDO (CMD), execute o comando:");
        System.out.println("     set GEMINI_API_KEY=AIzaSy_Sua_Chave_Aqui\n");

        System.out.println("3. PASSO 3: EXECUTAR O PROGRAMA");
        System.out.println("   java Main\n");

        System.out.println("------------------------------------------------------------------");
        System.out.println("   COMO REMOVER A CHAVE APÓS O USO (SEGURANÇA EM PCS DO SERVIÇO) ");
        System.out.println("------------------------------------------------------------------");
        System.out.println("Para garantir que a chave não fique salva no computador do serviço/faculdade:");
        System.out.println("1. Apagar no PowerShell: $env:GEMINI_API_KEY=\"\"");
        System.out.println("2. Confirmar que foi apagada: echo $env:GEMINI_API_KEY");
        System.out.println("3. Ou simplesmente feche a janela do terminal (a variável apaga sozinha).\n");
        System.out.println("==================================================================\n");
    }

    private static void exibirCabecalho() {
        System.out.println("==================================================");
        System.out.println("   ASSISTENTE DE ESTUDOS DE SAÚDE - ODS 3 (IA)    ");
        System.out.println("==================================================\n");
    }

    private static String lerEntradaObrigatoria(Scanner scanner, String mensagemPrompt, String mensagemErro) {
        String entrada = "";
        while (entrada.isEmpty()) {
            System.out.print(mensagemPrompt);
            entrada = scanner.nextLine().trim();
            if (entrada.isEmpty()) {
                System.out.println(mensagemErro + " Tente novamente.\n");
            }
        }
        return entrada;
    }

    private static int selecionarEstiloAprendizado(Scanner scanner) {
        int opcao = 0;
        while (opcao < 1 || opcao > 3) {
            System.out.println("\nQual é a sua melhor forma de aprendizado?");
            System.out.println("1 - Áudio explicativo (Roteiro falado/Podcast)");
            System.out.println("2 - Vídeo explicativo (Roteiro visual com cenas)");
            System.out.println("3 - Textos (Resumo estruturado)");
            System.out.print("Digite o número da opção desejada (1-3): ");

            String entrada = scanner.nextLine().trim();
            try {
                opcao = Integer.parseInt(entrada);
                if (opcao < 1 || opcao > 3) {
                    System.out.println("[Erro] Opção inválida! Escolha um número entre 1 e 3.");
                }
            } catch (NumberFormatException e) {
                System.out.println("[Erro] Entrada inválida. Digite apenas o número (1, 2 ou 3).");
            }
        }
        return opcao;
    }

    private static String obterNomeEstilo(int opcao) {
        switch (opcao) {
            case 1: return "Áudio explicativo";
            case 2: return "Vídeo explicativo";
            case 3: return "Textos";
            default: return "Textos";
        }
    }

    private static void simularCarregamento() {
        System.out.println("\n--------------------------------------------------");
        System.out.print("Gerando explicação personalizada via Google Gemini");
        try {
            for (int i = 0; i < 5; i++) {
                Thread.sleep(400);
                System.out.print(".");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("\n--------------------------------------------------\n");
    }

    private static void exibirRespostaFinal(String resposta) {
        System.out.println("==================================================");
        System.out.println("              CONTEÚDO GERADO PELA IA             ");
        System.out.println("==================================================\n");
        System.out.println(resposta);
        System.out.println("\n==================================================");
        System.out.println("      FIM DA EXPLICAÇÃO - BONS ESTUDOS! ODS 3     ");
        System.out.println("==================================================");
    }
}
