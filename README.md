<p align="center">
  <a href="https://git.alunos.di.fc.ul.pt/fc55373/chessbookdb">
    <img src="https://checkra.in/img/icon.png" alt="Logo" width="80" height="80">
  </a>

  <h3 align="center">ChessBookDB</h3>

</p>
![Grupo](https://img.shields.io/badge/Grupo-1-green)
![Augusto](https://img.shields.io/badge/Augusto%20Gouveia-55371-blue)
![Jose](https://img.shields.io/badge/Jos%C3%A9%20Almeida-55373-blue)

## Instruções:
* Dentro de um jogo, a interface suporta as seguintes interações:
  * Escrever duas coordernadas válidas no formato `xy xy` em que `x` é a letra correspondente à *file*/coluna (de "a" a "h") e `y` é o número inteiro correspondente à *rank*/linha (de 1 a 8).
  * Escrever `castle long` para *queen-side castling*, e `castle short` para *king-side castling*, quando possível e válido.
  * Escrever `resign` para desistir e facultar a vitória ao adversário.

## Funcionalidades 1ª fase:
* Construído em Maven
* Integração com JPA 
* Verificação da validade de jogada para cada peça
* Verificação de cheque
* Test Coverage de 78%
* Versão human-readable de cada classe (`toString()`)

## Funcionalidades 2ª fase:
- [x] Verificação de validade de jogadas para todo o tipo de peças (capturas + _pushes_)
- [x] Verificação de _check_:
  - A verificação de _check_ é sempre correta, sendo que emula uma jogada e verifica se esta causará ainda _check_, permitindo-nos avaliar até as possições mais teoricamente complicadas, como [_absolute_ e _relative pins_](https://en.wikipedia.org/wiki/Pin_(chess)).
- [x] _Castling_ (completamente suportado!):
  - A nossa implementação de _castling_ funciona para os dois lados, apenas nas condições corretas [segundo as regras FIDE](https://en.wikipedia.org/wiki/Castling). Invocável através de *castle long* (para _queen-side castling_) e *castle short* (para _king-side castling_)
- Desenvolvemos um gerador de jogadas [legais e _pseudo_-legais](https://www.chessprogramming.org/Legal_Move), que nos permite determinar com toda a certeza se uma posição é _checkmate_ (_check_ e sem jogadas legais para uma equipa) ou _stalemate_ (sem jogadas legais, mas sem estar em _check_).
- Deteção de vários **finais de jogo**, são atualmente suportados:
  - [x] Vitória por _checkmate_
  - [x] Vitória por desistência
  - [x] Vitória por _timeout_
  - [x] Empate por _stalemate_
  - [x] Empate por material insuficiente (rei vs. rei)
  - [x] Empate por limite de _moves [(segundo regras FIDE)](https://en.wikipedia.org/wiki/Fifty-move_rule)
- **Gestão de tempo**:
  - A nossa implementação de gestão de tempo é flexível de forma a suportar vários tipos de duração de jogo, sendo que toda a lógica do programa relativa à gestão de tempo utiliza o atributo ChessGame.timeControl, e funcionaria corretamente para qualquer valor que este tivesse (em minutos). No entanto, no âmbito do projeto, decidimos apenas que cada jogo fosse de 15 minutos, por questões de simplicidade.
  - Ao iniciar um jogo, o tempo começará a contar apenas quando a pessoa com as peças brancas estiver pronta para fazer a primeira jogada (estiver na página de jogo sendo a sua vez).
  - O tempo para de contar após a jogada de um jogador, retomando quando o jogador oposto vê a jogada pela primeira vez (primeira vez que entrar no jogo após uma jogada ser feita).
    - Portanto, a seguinte situação de exemplo é possível:
      - O jogador A faz uma jogada
      - Até o jogador B ver a nova jogada de A, nenhum dos tempos será decrementado
      - O tempo utilizado pelo jogador B entre o momento em que este vê a jogada de A e faz a sua própria jogada será decrementado no seu tempo total
      - O jogador B faz uma jogada
      - Até o jogador A ver a nova jogada de B, nenhum dos tempos será decrementado
      - (e por aí em diante)
  - Através da página de perfil (My Games) é possível ver, em tempo real (atualizando) quanto tempo um jogador têm no respetivo jogo.
  - Um total de uso de tempo para lá do *time control* resultará em vitória do adversário por *timeout*.
    - Se for necessário alterar o *time control* (duração de um jogo) por razões de teste, este poderá facilmente ser editado no atributo ChessGame.timeControl, em minutos (default 15)
- [x] **Replay**:
  - Esta funcionalidade permite rever um jogo, jogada a jogada, após este terminar. 
- [x] **Promoção de peão**:
  - De acordo com as regras FIDE, o jogador tem a possibilidade de escolher uma nova peça conforme um dos seus peões chega ao fim do tabuleiro. A suporta a promoção de peões, e é flexível de forma a que se pudesse escolher qualquer tipo de nova peça, no entanto, por falta de uma boa maneira de interface de escolha, decidímos implementar (à semelhança de alguns *sites* de xadrez) o que se chama de *auto-queening*, em que o peão é automaticamente promovido para uma rainha.
- [x] **Estética melhorada**:
  - Graças ao tempo extra fornecido pelo professor, foi-nos possível não só implementar praticamente todas as funcionalidades que previamente descrevemos como *shortcomings*, mas também melhorar a interface do programa de modo a que a utilização seja mais fácil, prática, e bonita. (e.g. uso de *bootstrap*, *board* personalizado para além do `toString`, aviso de rei em *check*, uso de *Font Awesome Icons*)
- Estrutura:
  -  A nossa implementação conta com uma estrutura que divide a camada de apresentação das outras camadas de forma muito clara e explícita, o que torna o código organizado e fácil de manter/alterar.

## Shortcomings (funcionalidades não suportadas ou em progresso, por ordem de importância):
- [x] ~~Gestão de tempo (praticamente funcional, será a próxima funcionalidade a ser implementada)~~
- [x] ~~Promoção de peão~~
- [x] ~~Replay~~ 
- [x] ~~Estética melhorada~~
- [ ] Finalização de jogo por repetição de posição (simples de implementar, passado à frente por questões de tempo)
- [ ] _En passant_ 

## Notas:
- Razões de entrega atrasada:
  - Sendo plena altura de exames, preenchida principalmente com exames de Minor, não nos foi possível completar todas as funcionalidades que tinhamos em mente a tempo. Assim, focamo-nos em desenvolver as funcionalidades principais que permitem que o jogo seja jogado, tomando a liberdade de deixar outras para a altura extra fornecida pelo professor.
  - Falta de um 3º aluno:
    - Para além da alta pressão de tempo exercida sobre nós, encontramo-nos em falta de 1 aluno, afetando a distribuição de tarefas e tornando o desenvolvimento mais lento.
    - Aluna em falta: Lívia Batalha (53741) (Razão: desistência)



<img src="https://git.alunos.di.fc.ul.pt/fc55373/chessbookdb/-/raw/master/planning/domain_model.png" width="50%" height="50%">