# MedHub — execução local com PostgreSQL e Flyway

Este pacote contém os arquivos necessários para rodar o projeto MedHub com PostgreSQL local via Docker e versionamento de banco com Flyway.

## Arquivos incluídos

- `docker-compose.yml`: sobe PostgreSQL e pgAdmin localmente.
- `pom.xml`: adiciona PostgreSQL, Flyway e suporte do Flyway para PostgreSQL.
- `src/main/resources/application.yaml`: configura conexão com banco, JPA e Flyway.
- `src/main/resources/db/migration/V1__create_schema_medhub.sql`: cria as tabelas iniciais do MedHub.
- `.gitignore`: ignora arquivos locais e de build.

## 1. Pré-requisitos

Instale antes:

- Java 21.
- Maven.
- Docker Desktop ou Docker Engine.

Confirme as versões:

```bash
java -version
mvn -version
docker --version
docker compose version
```

## 2. Copiar arquivos para o projeto

Copie os arquivos deste pacote para a raiz do projeto Spring Boot, substituindo `pom.xml` e `src/main/resources/application.yaml` pelos arquivos deste pacote.

A estrutura esperada fica assim:

```text
medhub/
├── docker-compose.yml
├── pom.xml
├── src/
│   └── main/
│       └── resources/
│           ├── application.yaml
│           └── db/
│               └── migration/
│                   └── V1__create_schema_medhub.sql
└── .gitignore
```

## 3. Subir o PostgreSQL local

Na raiz do projeto, execute:

```bash
docker compose up -d
```

Verifique se o container subiu:

```bash
docker ps
```

O banco ficará disponível em:

```text
Host: localhost
Porta: 5432
Database: medhub
Usuário: medhub_user
Senha: medhub_password
```

## 4. Acessar pelo pgAdmin

Abra no navegador:

```text
http://localhost:5050
```

Login do pgAdmin:

```text
E-mail: admin@medhub.com
Senha: admin
```

Para registrar o servidor no pgAdmin:

```text
Name: MedHub Local
Host name/address: postgres
Port: 5432
Maintenance database: medhub
Username: medhub_user
Password: medhub_password
```

Observação: dentro do pgAdmin, o host é `postgres`, porque ele está na mesma rede Docker do banco. Fora do Docker, pela aplicação Spring, o host é `localhost`.

## 5. Rodar o projeto Spring Boot

Com o banco ativo, execute:

```bash
mvn clean spring-boot:run
```

Ao iniciar, o Spring Boot aciona o Flyway. O Flyway executa a migration `V1__create_schema_medhub.sql` e cria a tabela interna `flyway_schema_history`, usada para controlar quais migrations já foram aplicadas.

## 6. Conferir se as tabelas foram criadas

Você pode conferir pelo pgAdmin ou pelo terminal:

```bash
docker exec -it medhub-postgres psql -U medhub_user -d medhub
```

Dentro do `psql`, rode:

```sql
\dt
```

Para sair:

```sql
\q
```

## 7. Como criar novas alterações no banco

Depois que uma migration foi executada, não edite o arquivo antigo. Crie um novo arquivo na pasta `src/main/resources/db/migration`.

Exemplos:

```text
V2__add_telefone_secundario_paciente.sql
V3__create_tabela_endereco.sql
V4__add_observacao_consulta.sql
```

O padrão é:

```text
VNUMERO__descricao_da_alteracao.sql
```

Use dois underlines entre o número da versão e a descrição.

## 8. Quando usar `ddl-auto=validate`

A configuração está como:

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: validate
```

Isso significa que o Hibernate não cria nem altera tabelas automaticamente. Ele apenas valida se o banco criado pelo Flyway está coerente com as entidades Java.

Se aparecer erro de validação, normalmente há divergência entre uma coluna da entidade e uma coluna da migration. A correção deve ser feita ajustando a entidade ou criando uma nova migration.

## 9. Como resetar o banco local

Para apagar o banco local e recriar do zero:

```bash
docker compose down -v
```

Depois:

```bash
docker compose up -d
mvn clean spring-boot:run
```

Atenção: `down -v` apaga o volume do PostgreSQL, logo todos os dados locais serão perdidos.
