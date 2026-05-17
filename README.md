TCC do curso de Analise e desenvolvimento de sistemas do IFRS

# App de Gestão para Vendedores Autônomos

Aplicativo Android desenvolvido para auxiliar vendedores autônomos no gerenciamento de vendas, estoque e clientes, permitindo maior organização das informações comerciais em um único sistema.

# Funcionalidades
## Controle de Estoque
* Cadastro de produtos
* Atualização de quantidades
* Controle de estoque
## Gestão de Vendas
* Registro de vendas
* Associação de produtos às vendas
* Valor da venda e produtos vendidos
## Cadastro de Clientes
* Registro de clientes
* Armazenamento de informações de contato
* Valor devido
# Tecnologias Utilizadas
* Android Studio
* Java
* SQLite
* Gradle
## Estrutura do Projeto
```plaintext
/Sellers_Book_TCC
 ├── Docs                            # Documentos Relacionados ao trabalho
 └── TCC/app/src/main/               # Arquivos do aplicativo desenvolvido
     ├── res                         # Recursos Visuais, telas e drawables
     └── java/com/example/tcc/       # Classes e Codigos desenvolvidos para o projeto
         ├── clientes                # Codigos da classe cliente
         ├── inicio                  # Codigos da tela de inicio
         ├── vendas                  # Codigos da classe vendas
         └── produtos                # Codigos da classe produtos
```
## Classes
O aplicativo possui três classes principais, Clientes, Vendas e Produtos. Cada classe principal possui suas classes auxiliares para trabalhar com a interface do android studio (fragment e adapter), e uma classe para trabalhar com o banco de dados SQLite (DAO). O aplicativo tambem possui a classe inicio que trabalha com a interface inicial do aplicativo, que leva para as outras telas das classes principais.

