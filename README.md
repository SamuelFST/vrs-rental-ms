# VRS-RENTAL-MS

O VRS-RENTAL-MS faz parte de um conjunto de Microsserviços responsáveis por um sistema fictício de locadora de veículos.

Esse Microsserviço tem a responsabilidade de lidar com as operações relacionadas a locaçoẽs de veículos dentro dessa stack de microsserviços do VRS.

Algumas funcionalidades incluídas nesse Microsserviço são:

- Listagem, criação e finalização de locações
- Cálculo prévio de preço da locação com base no valor do veículo e dias da locação
- Geração e Download de recibo PDF da locação
- Salvamento de recibos PDF no Bucket S3
- Autorização stateful, se comunicando com o [VRS-SECURITY-MS](https://github.com/SamuelFST/vrs-security-ms) para autorização aos recursos

🚀 Tecnologias Utilizadas

    Java 17
    Spring Boot 3.5.5
    Spring Security
    Spring Actuator
    Spring AMQP
    Sring Cloud Open Feign
    Gradle 8.14.3
    MongoDB
    Mapstruct
    Springdoc (Swagger)
    Resilience4j
    IText7
    AWS S3
    Lombok
    JUnit 5
    Mockito
    Jacoco
    Docker & Docker Compose
    Kubernetes (K8S)

📦 Pré-requisitos

Para rodar o projeto localmente, você precisa ter as seguintes ferramentas instaladas:

    Java Development Kit (JDK) 17 ou superior

    Docker & Docker Compose

    MicroK8s ou similar (Opcional) 

🛠️ Instalação e Execução

1. Clonando o repositório

Bash

```sh
git clone https://github.com/SamuelFST/vrs-rental-ms
```
```sh
cd vrs-rental-ms
```

2.1. Executando a aplicação com Docker Compose

Esta aplicação utiliza o Docker Compose para orquestrar a aplicação, o MongoDB, o RabbitMQ e o Bucket S3. Execute os seguintes comandos para iniciar todos os serviços:

#### Comando para dar permissão para os scripts de criação de bucket e de criação de rede externa no docker:
```sh
chmod +x ./aws/init-aws.sh ./docker-network.sh
```
#### Comando para executar script de criação de rede externa no docker (caso a rede externa já tenha sido criada por outro microsserviço do VRS, o script não fará alterações):
```sh
./docker-network.sh
```
#### Comando para fazer build da aplicação e iniciar todos os containers necessários para a aplicação ser funcional:
```sh
docker-compose up --build -d 
```

2.2. Executando a aplicação com Kubernetes (K8S) utilizando MicroK8s

#### Comando para fazer o build da imagem para o K8S
```sh
docker build --no-cache -t localhost:32000/vrs-rental:latest .
```

#### Comando para enviar a imagem para o registry do K8S
```sh
docker push localhost:32000/vrs-rental:latest
```

#### Comandos para aplicar os arquivos de configuração do K8S e iniciar as pods necessárias (Incluindo as pods com bancos de dados, RabbitMQ e S3, caso não tenham sido configuradas anteriormente)
```sh
microk8s kubectl apply -f k8s/infra/infra-vrs.yaml
microk8s kubectl apply -f k8s/vrs-rental-deployment.yaml
microk8s kubectl apply -f k8s/vrs-rental-service.yaml
microk8s kubectl apply -f k8s/vrs-rental-ingress.yaml
```

3. Executando os testes

Caso o docker-compose ou dockerfile seja executado, a etapa de build irá executar todos os testes da aplicação. Caso seja necessário rodar manualmente, o comando abaixo deve ser utilizado:

```sh
./gradlew test
```

🗺️ Estrutura do Projeto

    src/main/java: Contém o código-fonte da aplicação.
    src/main/resources: Arquivos de configuração de ambientes, como application.yml e application-k8s.yml.
    src/test/java: Contém os testes unitários e de integração da aplicação.
    k8s/: Arquivos para configuração da POD da aplicação no Kubernetes.
    build.gradle: Configurações de build do projeto.
    docker-compose.yml: Orquestração de contêineres Docker necessários para o projeto.
    Dockerfile: Imagem Docker da aplicação.
    /aws/init-aws.sh: Script para automatização da criação do Bucket no S3 local.
    docker-network.sh: Script para automatização da criação da rede externa no Docker.

🧩 Outros MS do VRS

| Nome                                                                | Descrição/Uso                                                    |
|:--------------------------------------------------------------------|:-----------------------------------------------------------------|
| **[vrs-security-ms](https://github.com/SamuelFST/vrs-security-ms)** | Gerenciamento e autenticação de usuários, documentos e endereços |
| **[vrs-vehicle-ms](https://github.com/SamuelFST/vrs-vehicle-ms)**   | Criação e gerenciamento de veículos                              |
| **[vrs-bff-ms](https://github.com/SamuelFST/vrs-bff-ms)**           | Gateway de chamadas HTTP para os MS do VRS                       |
| **[vrs-front-end](https://github.com/SamuelFST/vrs-front-end)**     | Camada de apresentação e cliente das APIs do VRS                 |

---

📄 Licença

Este projeto está sob a licença MIT License.

🤝 Contato

#### [Linkedin](https://www.linkedin.com/in/samuel-fernando2002/)  
