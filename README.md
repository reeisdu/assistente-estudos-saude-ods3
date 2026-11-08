# Assistente de Estudos de Saúde - ODS 3 (IA) 🩺📚

Protótipo funcional de um tutor acadêmico inteligente para estudantes da área da saúde, integrado à API do Google Gemini.

## 📌 Sobre o Projeto

Protótipo em Java, no modelo CLI (Interface de Linha de Comando). O objetivo é apoiar a aprendizagem de estudantes de cursos da saúde, adequando a explicação de temas complexos ao **semestre** do aluno e ao seu **estilo preferido de aprendizado** (Áudio, Vídeo ou Texto).

Alinhado ao **Objetivo de Desenvolvimento Sustentável 3 da ONU (Saúde e Bem-Estar)**, promovendo o acesso a conteúdos educacionais didáticos e personalizados na área da saúde.

Projeto do Itinerário Extensionista — Scrum Hub 2, Time 8 (grupo Didata).

## 👥 Divisão de Responsabilidades

| Item | Área | Tarefas |
|---|---|---|
| 1 | Interface CLI e fluxo de usuário | Captura de dados (nome, matéria, semestre, tema), validações de entrada e experiência visual no terminal |
| 2 | Engenharia de prompts | System prompts personalizados por estilo de aprendizagem (Áudio/Vídeo/Texto) e nível de complexidade do semestre |
| 3 | Integração com a API Gemini | Conectividade com a API do Google Gemini, gerenciamento seguro da chave e parse do JSON de resposta |
| 4 | Orquestração, testes e documentação | Integração no fluxo principal (`Main.java`), testes ponta a ponta (QA) e documentação |

## 📁 Estrutura do Código-Fonte

- `Main.java`: ponto de entrada. Gerencia o fluxo da CLI, as validações, o carregamento da chave de API e a exibição dos resultados.
- `PromptBuilder.java`: constrói o prompt didático e pedagógico a partir do perfil e do estilo escolhido.
- `GeminiApiClient.java`: cliente HTTP (`java.net.http.HttpClient`) que faz a requisição POST à API do Google Gemini, tratando escape de caracteres e codificação Unicode.

## 🛠️ Pré-requisitos

- **JDK 11 ou superior** (o projeto usa `java.net.http.HttpClient`).
- **Chave de API do Google Gemini** (gratuita), obtida no Google AI Studio.

## 🚀 Como configurar e executar

### 1. Obter a chave de API
1. Acesse o [Google AI Studio](https://aistudio.google.com/app/apikey) e faça login com sua conta Google.
2. Clique em **Create API key** e copie a chave gerada (começa com `AIzaSy...`).

### 2. Configurar a variável de ambiente
Por segurança, a chave **não** fica no código-fonte. Configure no terminal antes de executar:

```powershell
# Windows (PowerShell)
$env:GEMINI_API_KEY="SUA_CHAVE_AQUI"
```
```cmd
:: Windows (CMD)
set GEMINI_API_KEY=SUA_CHAVE_AQUI
```
```bash
# Linux / macOS
export GEMINI_API_KEY="SUA_CHAVE_AQUI"
```

> Se executar sem definir a chave, o programa detecta a ausência e mostra um tutorial no terminal.

### 3. Compilar e executar
No diretório dos arquivos `.java`:

```bash
javac Main.java PromptBuilder.java GeminiApiClient.java
java Main
```

## 📋 Fluxo de uso

1. **Identificação:** o usuário informa nome, matéria de saúde e semestre.
2. **Estilo de aprendizado:** escolhe entre
   - **1 — Áudio explicativo:** roteiro em tom de podcast, com marcações de entonação (ex.: `[Pausa didática]`);
   - **2 — Vídeo explicativo:** roteiro por cenas, com `[CENA/VISUAL]` e `[FALA/NARRAÇÃO]`;
   - **3 — Texto:** resumo didático em tópicos, com definições e destaques.
3. **Tema:** o usuário digita o assunto (ex.: Sistema Circulatório, Mecanismo dos Beta-Lactâmicos).
4. **Geração:** o sistema monta o prompt, exibe uma animação no terminal, envia a requisição à IA e mostra o conteúdo formatado.

## 🔒 Segurança

Após a apresentação, ou ao usar computadores públicos ou acadêmicos, limpe a variável de ambiente:

```powershell
$env:GEMINI_API_KEY=""
```

Ou simplesmente feche a janela do terminal.
