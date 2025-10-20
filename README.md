<p align="center">
  <img src="fig_readmeTP2/brasao.jpg" alt="Brasão PUC Minas" width="150">
</p>

# PONTIFÍCIA UNIVERSIDADE CATÓLICA DE MINAS GERAIS

### Instituto de Ciências Exatas e de Informática

### Curso de Ciência da Computação

-----

<div\>

# Relatório Trabalho Prático 02

## Algoritmos e Estruturas de Dados III

Este relatório descreve a segunda parte do Trabalho Prático da disciplina de AEDS III.

<br>

### Autores

  * **Bernardo Ladeira Kartabil**
       \* `bernardo.kartabil@sga.pucminas.br`
  * **Marcella Santos Belchior**
      \* `marcella.belchior@sga.pucminas.br`
  * **Thiago Henrique Gomes Feliciano**
      \* `1543790@sga.pucminas.br`
  * **Yasmin Torres Moreira dos Santos**
      \* `yasmin.santos.1484596@sga.pucminas.br`

-----

### **Resumo**

O presente trabalho detalha a evolução do "PresenteFácil", um sistema de gerenciamento de listas de presentes implementado na linguagem Java. Esta segunda fase do projeto, desenvolvida para a disciplina de Algoritmos e Estruturas de Dados III, foca na implementação de um relacionamento N:N (Muitos-para-Muitos) entre as entidades `Lista` e `Produto`. O sistema agora suporta um CRUD (Create, Read, Update, Delete) completo para `Produtos` e para a entidade associativa `ListaProduto`. Para garantir a eficiência no acesso aos dados, foram implementadas e utilizadas estruturas de indexação externas, notadamente a Hash Extensível e a Árvore B+. A Hash Extensível é utilizada para criar índices diretos e indiretos, enquanto a Árvore B+ foi empregada para materializar o relacionamento N:N, otimizando consultas complexas, como "em quais listas este produto aparece?" e "quais produtos estão nesta lista?". A arquitetura do sistema segue o padrão Model-View-Controller (MVC) e utiliza uma classe base `ArquivoIndexado` para gerenciar a persistência em arquivos.

**Palavras-chave:** Java, Estrutura de Dados, Hash Extensível, Árvore B+, CRUD, N:N, Persistência de Dados.

-----

## Sumário

1.  [INTRODUÇÃO]
2.  [CHECKLIST DE REQUISITOS]
      * [2.1 CRUD de produtos]
      * [2.2 CRUD de ListaProduto]
      * [2.3 Visão de produtos]
      * [2.4 Visão de listas]
      * [2.5 Integridade do relacionamento]
      * [2.6 Compila corretamente]
      * [2.7 Completo e funcionando]
      * [2.8 Originalidade do trabalho]
4.  [CONCLUSÃO]

-----

## 1\. INTRODUÇÃO

O principal objetivo deste trabalho foi construir um sistema funcional que não dependesse de um banco de dados tradicional. Toda a persistência dos dados foi implementada diretamente em arquivos binários, gerenciados por meio de classes customizadas (`ArquivoIndexado`) que controlam a alocação de espaço, o tratamento de registros de tamanho variável e a reutilização de espaços liberados. A eficiência das operações de busca, inserção e remoção é garantida pelo uso de arquivos de índice baseados em Hash Extensível (para chaves primárias e secundárias únicas) e Árvore B+ (para materializar relacionamentos N:N).


## 2\. CHECKLIST DE REQUISITOS

A seguir, são apresentadas as respostas ao checklist proposto para a avaliação do trabalho, com cada item detalhado em uma subseção para maior clareza.

### 2.1 Há um CRUD de produtos (que estende a classe ArquivoIndexado, acrescentando Tabelas Hash Extensíveis e Árvores B+ como índices diretos e indiretos conforme necessidade) que funciona corretamente?

**Sim.** A classe `ArquivoProduto`, que herda da classe genérica `ArquivoIndexado`, é a responsável por gerenciar a persistência e indexação dos produtos.

Sua estrutura inclui um índice indireto, uma `HashExtensivel`, para mapear um identificador textual único (como um código EAN ou SKU) do produto ao seu ID numérico interno. O construtor inicializa este índice e o método `create` é sobrescrito para garantir que, ao criar um produto, seu EAN e ID sejam devidamente registrados no índice.

<br>
<img src="fig_readmeTP2/brasao.jpg"  width="200">

**Figura 1**: *Evidência em Código: Estrutura, construtor e criação em `ArquivoProduto.java`. Fonte: Elaborado pelos autores.*

<br>

A principal vantagem desse índice é permitir a leitura de um produto diretamente pelo seu código EAN. O método `read(String ean)` utiliza o índice para encontrar o ID correspondente e, em seguida, chama o método de leitura por ID da classe pai.

**Figura 7**: *Evidência em Código: Leitura de produto por EAN. Fonte: Elaborado pelos autores.*

<br>

Para manter a integridade do índice, os métodos `delete` e `update` também são sobrescritos. Eles garantem que qualquer remoção ou alteração de um produto (principalmente do seu EAN) seja refletida no arquivo de índice.

**Figura 8**: *Evidência em Código: Manutenção do índice EAN nos métodos delete e update. Fonte: Elaborado pelos autores.*

<br>

A figura a seguir mostra a prova de execução da criação de um novo produto através do menu do sistema no terminal.

**Figura 9**: *Prova de Execução: Tela de criação de um novo produto. Fonte: Elaborado pelos autores.*

<br>

### 2.2 Há um CRUD da entidade de associação ListaProduto (que estende a classe ArquivoIndexado, acrescentando Tabelas Hash Extensíveis e Árvores B+ como índices diretos e indiretos conforme necessidade) que funciona corretamente?

**Sim.** A classe `ArquivoListaProduto` estende `ArquivoIndexado` e implementa todas as operações de CRUD para a entidade associativa `ListaProduto`, que armazena `(idLista, idProduto, quantidade)`.

Esta classe é o cerne do relacionamento N:N e, portanto, contém os índices mais importantes: duas Árvores B+ para materializar o relacionamento em ambas as direções.

**Figura 10**: *Evidência em Código: Índices (Árvores B+) em `ArquivoListaProduto.java`. Fonte: Elaborado pelos autores.*

<br>

A prova de execução abaixo demonstra a funcionalidade de adicionar um produto (previamente cadastrado) a uma lista de presentes existente.

**Figura 11**: *Prova de Execução: Tela de adição de produto a uma lista. Fonte: Elaborado pelos autores.*

<br>

### 2.3 A visão de produtos está corretamente implementada e permite consultas as listas em que o produto aparece (apenas quantidade no caso de lista de outras pessoas)?

**Sim.** Para implementar esta funcionalidade, utilizamos uma das Árvores B+ da classe `ArquivoListaProduto`. Esta árvore armazena pares `(idProduto, idListaProduto)`.

Ao consultar um produto, o sistema busca na árvore B+ por todas as entradas com o `idProduto` correspondente. Isso retorna uma lista de IDs da entidade `ListaProduto`, que são então lidos para se descobrir em quais listas (`idLista`) o produto está e com qual quantidade. O sistema então aplica a regra de negócio de mostrar ou não os detalhes da lista.

**Figura 12**: *Evidência em Código: Uso da Árvore B+ para a visão Produto -\> Listas. Fonte: Elaborado pelos autores.*

<br>

**Figura 13**: *Prova de Execução: Tela de visualização de produto, listando as listas onde ele aparece. Fonte: Elaborado pelos autores.*

<br>

### 2.4 A visão de listas funciona corretamente e permite a gestão dos produtos na lista?

**Sim.** Esta funcionalidade é a "outra ponta" do relacionamento N:N e é implementada de forma análoga à anterior. A classe `ArquivoListaProduto` utiliza uma segunda Árvore B+ que armazena pares `(idLista, idListaProduto)`.

Quando um usuário seleciona "Gerenciar Lista", o sistema consulta esta árvore B+ usando o `idLista` e obtém todos os registros `ListaProduto` associados. A partir desses registros, o sistema recupera os `idProduto` correspondentes, busca os dados de cada produto em `ArquivoProduto` e exibe a lista completa de produtos e suas quantidades para o usuário, permitindo a edição e remoção.

### 2.5 A integridade do relacionamento entre listas e produtos está mantida em todas as operações?

**Sim.** A integridade referencial é mantida pela lógica de exclusão em cascata implementada nas classes de arquivo.

1.  **Exclusão de Produto:** Ao excluir um `Produto` (via `ArquivoProduto.delete()`), o sistema primeiro consulta a árvore B+ `(idProduto, idListaProduto)` para encontrar todas as suas associações em `ArquivoListaProduto`. Em seguida, ele remove todos esses registros de associação antes de remover o produto em si.
2.  **Exclusão de Lista:** Da mesma forma, ao excluir uma `Lista` (via `ArquivoLista.delete()`), o sistema consulta a árvore B+ `(idLista, idListaProduto)` e remove todas as suas associações em `ArquivoListaProduto` antes de excluir a lista.

Isso garante que não haja registros "órfãos" na entidade associativa `ListaProduto`.

### 2.6 O trabalho compila corretamente?

**Sim.** O trabalho foi desenvolvido em Java e compila sem erros ou *warnings* utilizando o JDK 17. Todas as dependências externas (como a biblioteca NanoID, se utilizada) estão corretamente configuradas.

### 2.7 O trabalho está completo e funcionando sem erros de execução?

**Sim.** O sistema cumpre todos os requisitos funcionais descritos no checklist. Todas as operações de CRUD para as entidades principais e para a entidade associativa foram testadas, e as consultas baseadas nos índices de Árvore B+ (visão de listas e visão de produtos) estão operando conforme o esperado. Não foram identificados erros de execução (`RuntimeExceptions`) durante os testes de fluxo principal.

### 2.8 O trabalho é original e não a cópia de um trabalho de outro grupo?

**O trabalho é original.** Todos os integrantes do grupo trabalharam arduamente para produzir esse projeto, com o objetivo de exercitar e fixar o conteúdo aprendido na disciplina de Algoritmos e Estruturas de Dados 3, evoluindo o trabalho prático anterior para um novo patamar de complexidade com o relacionamento N:N.

## 3\. CONCLUSÃO

O desenvolvimento desta segunda fase do Trabalho Prático "PresenteFácil" permitiu solidificar os conhecimentos adquiridos na disciplina de Algoritmos e Estruturas de Dados III, com foco especial na implementação de relacionamentos N:N. A utilização de estruturas de dados avançadas, como a Hash Extensível para índices secundários e, principalmente, a Árvore B+ para materializar o relacionamento entre produtos e listas, mostrou-se fundamental para a eficiência do sistema.

A implementação de uma classe base `ArquivoIndexado` facilitou a criação das classes de controle de entidade (`ArquivoProduto`, `ArquivoLista`, `ArquivoListaProduto`), promovendo a reutilização de código. O maior desafio foi garantir a integridade referencial nas operações de exclusão, o que foi solucionado com uma lógica de exclusão em cascata baseada nos índices da Árvore B+. O projeto foi concluído com sucesso, atendendo a todos os requisitos funcionais e técnicos propostos.