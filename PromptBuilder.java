public class PromptBuilder {

    public static String buildFullPrompt(String nome, String materia, String semestre, String estilo, String tema) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("Você é um tutor acadêmico especialista e didático na área da saúde. ");
        prompt.append("Sua missão é explicar o tema '").append(tema).append("' ");
        prompt.append("para o(a) estudante '").append(nome).append("', ");
        prompt.append("que cursa a disciplina de '").append(materia).append("' ");
        prompt.append("e está no '").append(semestre).append("'.\n\n");

        prompt.append("DIRETRIZES PEDAGÓGICAS (ODS 3 - SAÚDE E BEM-ESTAR):\n");
        prompt.append("- Adeque o nível de complexidade acadêmica ao semestre (semestres iniciais exigem analogias e conceitos fundamentais; semestres avançados pedem terminologia técnica clínica detalhada).\n");
        prompt.append("- Torne o conteúdo prático, relacionando-o com a aplicação na rotina da saúde.\n\n");

        prompt.append("FORMATO DE RESPOSTA SOLICITADO: ").append(estilo.toUpperCase()).append("\n");

        switch (estilo) {
            case "Áudio explicativo":
                prompt.append("- Crie um roteiro corrido de áudio (estilo locução de podcast ou nota de voz didática).\n");
                prompt.append("- Inclua indicações de entonação entre colchetes, como [Pausa didática], [Entonação firme], etc.\n");
                break;

            case "Vídeo explicativo":
                prompt.append("- Estruture a resposta como um roteiro de vídeo didático dividido em duas partes por cena:\n");
                prompt.append("  * [CENA/VISUAL]: O que deve ser mostrado visualmente (ex: animação 3D da célula, diagrama, etc).\n");
                prompt.append("  * [FALA/NARRAÇÃO]: O texto falado pelo apresentador/explicador.\n");
                break;

            case "Textos":
            default:
                prompt.append("- Formate a explicação em texto limpo e bem estruturado.\n");
                prompt.append("- Utilize marcadores/tópicos (bullet points), definições claras de conceitos-chave e um resumo final em Destaque.\n");
                break;
        }

        return prompt.toString();
    }
}
