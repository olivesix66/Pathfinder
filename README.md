# Visualizador de Pathfinding

Um visualizador interativo de algoritmos de pathfinding, desenvolvido com LibGDX e disponível para uso no navegador via GWT.

🌐 **[Acesse a versão web](https://olivesix66.github.io/Pathfinder/) **

---

## Sobre o projeto

Este projeto é uma plataforma para visualizar e comparar diferentes algoritmos de pathfinding em tempo real. O objetivo é explorar como cada algoritmo navega por uma grade, observando suas diferenças de comportamento, eficiência e custo.

Atualmente implementado: **A\***. O A* é um dos algoritmos de pathfinding mais utilizados em jogos e robótica — ele encontra o caminho mais curto entre dois pontos combinando o custo real de chegar a um nó com uma estimativa heurística da distância restante, tornando-o ao mesmo tempo ótimo e eficiente.

Defina o ponto de início e o objetivo, desenhe paredes, gere labirintos e acompanhe um agente percorrendo o caminho encontrado.

---

## Como funciona

A implementação utiliza a **heurística de distância Octile**, que lida com movimentos diagonais de forma mais precisa do que a heurística Manhattan tradicional. Enquanto Manhattan calcula a distância considerando apenas movimentos horizontais e verticais, a Octile leva em conta que passos diagonais são mais baratos do que dois passos cardinais, produzindo caminhos mais naturais.

A grade é representada como uma matriz bidimensional de nós. Cada nó registra:
- **Custo G** — distância percorrida a partir do nó inicial
- **Custo H** — estimativa heurística até o objetivo
- **Custo F** — soma de G e H (o valor que o A* minimiza)

A cada iteração, o algoritmo seleciona o nó aberto com menor custo F, expande seus vizinhos e atualiza os custos até que o objetivo seja alcançado.

![pathfinder](https://github.com/user-attachments/assets/e180d62b-864d-42b7-ab6f-9bcef9d2df98)

---

## Como usar

| Entrada         | Ação                                                                                 |
|-----------------|--------------------------------------------------------------------------------------|
| Clique esquerdo | Define o nó inicial (primeiro clique) / Define o objetivo e executa (segundo clique) |
| Clique do meio  | Alterna parede no nó clicado                                                         |
| Barra de espaço | Gera um novo labirinto aleatório                                                     |

### Cores dos nós

| Cor          | Significado |
|--------------|-------------|
| 🔵 Ciano    | Nó inicial |
| 🟥 Vermelho | Nó objetivo |
| ⬛ Preto    | Parede (nó sólido) |
| 🟧 Salmão   | Lista aberta — nós sendo avaliados |
| 🟦 Azul     | Lista fechada — nós já avaliados |
| 🟩 Verde    | Caminho final encontrado |

---

## Rodando localmente

**Pré-requisitos:** Java 11, Gradle

```bash
# Clone o repositório
git clone https://github.com/olivesix66/Pathfinder.git
cd pathfinder

# Versão desktop
./gradlew lwjgl3:run

# Build da versão web
./gradlew html:dist
# Resultado em html/build/dist/ — sirva com qualquer servidor de arquivos estáticos

# Versão web em modo de desenvolvimento (com hot reload)
./gradlew html:superDev
# Acesse http://localhost:8080
...
```
---

## Tecnologias

- **[LibGDX](https://libgdx.com/)** — framework de desenvolvimento de jogos em Java
- **[GWT](https://www.gwtproject.org/)** — compila Java para JavaScript para deploy na web
- **Java**

---

## Funcionalidades planejadas

- Suporte a múltiplos algoritmos (Dijkstra, BFS, Greedy Best-First)
- Seleção de heurística (Octile, Manhattan, Euclidiana)
- Movimento diagonal opcional
- Tamanho da grade configurável
- Controles via interface gráfica
