# MercadoExpress_CP05 – Spring Security MVC + PostgreSQL

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.0-brightgreen)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-21-orange)](https://adoptium.net/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-blue)](https://www.postgresql.org/)
[![Spring Security](https://img.shields.io/badge/Spring%20Security-green)](https://spring.io/projects/spring-security)
[![Lombok](https://img.shields.io/badge/Lombok-purple)](https://projectlombok.org/)

## Link de produção
**https://mercadoexpress-checkpoint5.onrender.com**

## Integrantes
Ana Clara de Oliveira Nascimento | RM 561957
Isis Macedo | RM 561497
Henrique Pereira | RM 565608
Rafael Carvalho Meireles | RM 563413

## Sobre o projeto
Evolução do **MercadoExpress_CP04**. O sistema de gestão do Mercado Express (CRUD de produtos) agora usa **PostgreSQL**, tem **login customizado**, **cadastro de usuários (Sign Up)** e **controle de acesso por perfil (ADMIN e USER)** com Spring Security. A API REST/HATEOAS do CP4 foi mantida em `/mercado` e também passou a ser protegida.

## Tecnologias
| Tecnologia | Descrição |
|---|---|
| Java 21 / Spring Boot 4.1.0 | Base do projeto |
| Spring MVC + Thymeleaf | Interface web |
| Spring Security | Autenticação, autorização e BCrypt |
| Spring Data JPA + PostgreSQL | Persistência |
| Spring HATEOAS + SpringDoc | API REST do CP4 (Swagger) |
| Lombok | Redução de boilerplate |
| Bootstrap 5 (WebJars) | Layout |
| Docker + Render | Deploy |
| InteliJ | IDE utilizada |

## Banco de dados (application.yml)
| Tabela | Colunas |
|---|---|
| `TDS_Sec_MVC_TB_Mercado` | id, nome, tipo, setor, tamanho, preco |
| `TDS_Users_Mercado` | id, nome, username (único), senha (BCrypt), perfil (ADMIN/USER) |

As tabelas são criadas automaticamente (`ddl-auto: update`).

## Perfis e rotas
| Rota | Público | USER | ADMIN |
|---|:-:|:-:|:-:|
| `/` (index.html), `/login`, `/cadastro` | ✅ | ✅ | ✅ |
| `GET /web/produtos`, `GET /web/produtos/{id}` (Read) | ❌ | ✅ | ✅ |
| `GET /web/produtos/novo` + `POST /web/produtos` (Create) | ❌ | ❌ | ✅ |
| `GET /web/produtos/{id}/editar` + `POST /web/produtos/{id}` (Update) | ❌ | ❌ | ✅ |
| `POST /web/produtos/{id}/delete` (Delete) | ❌ | ❌ | ✅ |
| `GET /admin/usuarios` (lista de usuários) | ❌ | ❌ | ✅ |
| `GET /mercado/**` (API) | ❌ | ✅ (Basic) | ✅ (Basic) |
| `POST/PUT/PATCH/DELETE /mercado/**` (API) | ❌ | ❌ | ✅ (Basic) |

- O cadastro público cria sempre o perfil **USER**.
- O **ADMIN** é criado na primeira execução com `ADMIN_USERNAME` / `ADMIN_SENHA`.
- Após o login, o usuário é redirecionado para `/web/produtos`. Os botões Novo/Editar/Deletar só aparecem para o ADMIN.

## Como rodar localmente
```
git clone https://github.com/SEU_USUARIO/MercadoExpress_CP05.git
cd MercadoExpress_CP05
createdb mercadoexpress
export DB_URL=jdbc:postgresql://localhost:5432/mercadoexpress
export DB_USERNAME=postgres
export DB_PASSWORD=sua_senha
mvn clean spring-boot:run
```
Acesse http://localhost:8082

## Deploy no Render
1. Criar um banco **PostgreSQL** no Render.
2. Criar um **Web Service** com o repositório (Runtime: Docker).
3. Variáveis de ambiente:
   - `DB_URL` = `jdbc:postgresql://HOST:5432/NOME_DO_BANCO`
   - `DB_USERNAME`, `DB_PASSWORD`
   - `ADMIN_USERNAME`, `ADMIN_SENHA`

## Telas (prints e explicações)

1. **Landing page (index.html)** – página inicial com os botões Entrar e Criar conta.
<img width="2506" height="918" alt="image" src="https://github.com/user-attachments/assets/7fc9d90c-1523-41bd-a6fc-cb1d19216ff9" />

2. **Login customizado** – tela própria, diferente da padrão do Spring Security.
<img width="805" height="501" alt="image" src="https://github.com/user-attachments/assets/ddea05a3-5dff-47bd-a100-1a3ce362a1b3" />

3. **Cadastro (Sign Up)** – validações de nome, usuário e senha; usuário duplicado é bloqueado.
<img width="510" height="493" alt="image" src="https://github.com/user-attachments/assets/9217894a-614a-4d71-b22c-1788e82ef2e0" />

4. **Lista de produtos como ADMIN** – com busca e botões Ver, Editar, Deletar e Novo.
<img width="1458" height="396" alt="image" src="https://github.com/user-attachments/assets/14044c30-0417-4d54-93aa-1226f7736f46" />

5. **Lista de produtos como USER** – somente leitura.
<img width="1307" height="361" alt="image" src="https://github.com/user-attachments/assets/5e72257c-0834-4694-8e38-e7e04456a175" />

6. **Detalhe do produto (Read)** – dados vindos da tabela `TDS_Sec_MVC_TB_Mercado`.
<img width="1321" height="519" alt="image" src="https://github.com/user-attachments/assets/f4fe4770-428f-4762-9939-11e7c0f54146" />

7. **Criar produto (Create)** e 8. **Editar produto (Update)**.
<img width="1320" height="763" alt="image" src="https://github.com/user-attachments/assets/51ff32c0-14e1-4a0f-86c8-534483bf1cad" />
<img width="1459" height="758" alt="image" src="https://github.com/user-attachments/assets/34ed564c-d46a-4569-a925-d5e76a3eb1b5" />

9. **Deletar produto (Delete)** – com confirmação.
<img width="1274" height="402" alt="image" src="https://github.com/user-attachments/assets/66a6f6f9-828c-4c1a-ba15-36cfc40d3a09" />

10. **Acesso negado (403)** – USER tentando abrir `/web/produtos/novo`.
<img width="2514" height="412" alt="image" src="https://github.com/user-attachments/assets/443eefe2-3342-4879-9602-aa215ff65922" />

11. **Área ADMIN** – `/admin/usuarios`.
<img width="1405" height="292" alt="image" src="https://github.com/user-attachments/assets/1dbfd601-2653-4e05-889e-dc5cf6135faf" />

12. **Banco PostgreSQL** – `SELECT * FROM TDS_Users_Mercado;` mostrando a senha criptografada.
<img width="1009" height="556" alt="image" src="https://github.com/user-attachments/assets/ed16ef20-68b0-4b7a-aa31-8f6c8e7e2516" />

13. **Deploy no Render** funcionando.
<img width="1906" height="778" alt="image" src="https://github.com/user-attachments/assets/6146d92e-a50e-44f5-9c7d-790c0bb61d27" />

---

# Parte 2 – Microsserviços com RabbitMQ/LavinMQ (Banco Tranquilo + e-mail)

## Fluxo da compra
```
Cliente (tela /loja ou Postman)
   │  POST /api/compras
   ▼
Mercado Express ── salva Compra PENDENTE (PostgreSQL)
   │  publica PagamentoRequest
   ▼
[mercado.pagamentos.exchange] ──pagamento.requisicao──▶ [pagamento.requisicao.queue]
                                                              │ consumidor "Banco"
                                                              ▼
                                              API do Banco Tranquilo (POST /banco-tranquilo/api/pagamentos)
                                                              │ APROVADO / RECUSADO
   ┌──────────────────────────────────────────────────────────┘
   ▼
[mercado.pagamentos.exchange] ──pagamento.resposta──▶ [pagamento.resposta.queue]
   │ consumidor "Mercado"
   ▼
Atualiza compra (APROVADA/RECUSADA) → baixa estoque → envia e-mail (se aprovada) → grava em TDS_Email_Log
```
Mensagens com erro vão para `mercado.pagamentos.dlx` → `pagamento.dlq`.

| Item | Nome |
|---|---|
| Exchange | `mercado.pagamentos.exchange` (direct) |
| Fila de requisição / routing key | `pagamento.requisicao.queue` / `pagamento.requisicao` |
| Fila de resposta / routing key | `pagamento.resposta.queue` / `pagamento.resposta` |
| Dead letter | `mercado.pagamentos.dlx` → `pagamento.dlq` |

## Novas tabelas (PostgreSQL)
- `TDS_Compras_Mercado`: id, cliente_nome, cliente_email, total, status, transacao_id, mensagem_banco, data_compra
- `TDS_Itens_Compra`: id, compra_id, produto_id, produto_nome, quantidade, preco_unitario, custo_unitario
- `TDS_Email_Log`: id, compra_id, destinatario, assunto, corpo, status (ENVIADO/FALHA), erro, data_envio
- `TDS_Sec_MVC_TB_Mercado` ganhou as colunas `custo` e `estoque` (usadas no lucro e no estoque).

## Como executar
1. **RabbitMQ local:** `docker compose up -d` e abrir o Dashboard em http://localhost:15672 (guest/guest).
   Em nuvem (LavinMQ/CloudAMQP) defina `SPRING_RABBITMQ_ADDRESSES=amqps://usuario:senha@host/vhost`.
2. **E-mail (SMTP):** defina `MAIL_USERNAME` e `MAIL_PASSWORD` (no Gmail use uma *senha de app*; também funciona com Mailtrap via `MAIL_HOST`, `MAIL_PORT`). Se o SMTP falhar, a compra continua aprovada e o e-mail é registrado como `FALHA` em `TDS_Email_Log`.
3. Rodar a aplicação (`mvn spring-boot:run`) e entrar com um ADMIN. Cadastrar produtos **com estoque** (pela tela ou pelo Postman).
4. **Tela de carrinho:** http://localhost:8082/loja
5. **Dashboard da gerência:** http://localhost:8082/admin/dashboard (somente ADMIN).

## API do Banco Tranquilo
Por padrão a aplicação usa um **simulador embutido** (`POST /banco-tranquilo/api/pagamentos`). Regras: cartão terminado em `0000` → RECUSADO; valor acima de R$ 5.000 → RECUSADO; demais → APROVADO. Para usar a API real, altere `banco.tranquilo.url` e `banco.tranquilo.path` (variáveis `BANCO_URL` e `BANCO_PATH`).

## Teste no Postman/Insomnia
Importe `postman/MercadoExpress_CP05.postman_collection.json` (autenticação Basic com `admin`/`admin123`). Exemplo de requisição de compra (`postman/exemplo-compra.json`):
```json
POST http://localhost:8082/api/compras
{
  "clienteNome": "Maria Silva",
  "clienteEmail": "maria@email.com",
  "numeroCartao": "4111111111111111",
  "itens": [ { "produtoId": 1, "quantidade": 2 } ]
}
```
Resposta `202 Accepted` com `status: PENDENTE`; consulte `GET /api/compras/{id}` e veja virar `APROVADA` (ou `RECUSADA`).



7. Dashboard da gerência com vendas, lucro e estoque.

## Prints da Parte 2
Tela de carrinho
<img width="1222" height="594" alt="image" src="https://github.com/user-attachments/assets/26db6ba1-8a73-4539-902b-95199f1b7d6a" />

status da compra

Dashboard do RabbitMQ (exchange/filas)
<img width="1335" height="756" alt="image" src="https://github.com/user-attachments/assets/717c944e-98de-44fd-8ffd-0c29da52a34a" />

Postman

e-mail recebido
<img width="948" height="339" alt="image" src="https://github.com/user-attachments/assets/24a7218d-d63b-494f-b84b-152353d169e5" />

`TDS_Email_Log` no banco e Dashboard da gerência.
<img width="1227" height="562" alt="image" src="https://github.com/user-attachments/assets/34b7bfa9-3828-4530-8cf6-bb709fce10b1" />

