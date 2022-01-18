<p align="center">
  <a href="https://git.alunos.di.fc.ul.pt/fc55373/chessbookdb">
    <img src="https://checkra.in/img/icon.png" alt="Logo" width="80" height="80">
  </a>

  <h3 align="center">ChessBookDB</h3>

</p>
![Grupo](https://img.shields.io/badge/Grupo-1-green)
![Augusto](https://img.shields.io/badge/Augusto%20Gouveia-55371-blue)
![Jose](https://img.shields.io/badge/Jos%C3%A9%20Almeida-55373-blue)


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
- Deteção de vários finais de jogo, são atualmente suportados:
  - [x] Vitória por _checkmate_
  - [x] Vitória por desistência
  - [x] Vitória por _timeout_ (desde a implementação da gestão de tempo)
  - [x] Empate por _stalemate_
  - [x] Empate por material insuficiente (rei vs. rei)
  - [x] Empate por limite de _moves [(segundo regras FIDE)](https://en.wikipedia.org/wiki/Fifty-move_rule)
- Gestão de tempo:
  - Ao iniciar um jogo, o tempo começará a contar apenas quando a pessoa com as peças brancas fizer a primeira jogada.
  - O tempo pára de contar após a jogada de um jogador, retomando quando o jogador oposto vê a jogada pela primeira vez (primeira vez que entrar no jogo após uma jogada ser feita).
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

## Shortcomings (funcionalidades não suportadas ou em progresso, por ordem de importância):
- [x] ~~Gestão de tempo (praticamente funcional, será a próxima funcionalidade a ser implementada)~~
- [ ] Promoção de peão (praticamente funcional)
- [ ] Replay (praticamente funcional)
- [ ] Finalização de jogo por repetição de posição
- [ ] Estética melhorada
- [ ] _En passant_ 

## Notas:
- O jogo encontra-se em estado jogável, sendo que inclui as funcionalidades acima descritas, mas planeamos adicionar (_ASAP_) todas sa funcionalidades descritas nos _Shortcomings_, de modo a que o seu estado final seja, de longe, mais completo.
- Razões de entrega atrasada:
  - Sendo plena altura de exames, preenchida principalmente com exames de Minor, não nos foi possível completar todas as funcionalidades que tinhamos em mente a tempo. Assim, focamo-nos em desenvolver as funcionalidades principais que permitem que o jogo seja jogado, tomando a liberdade de deixar outras para a altura extra fornecida pelo professor.
  - Falta de um 3º aluno:
    - Para além da alta pressão de tempo exercida sobre nós, encontramo-nos em falta de 1 aluno, afetando a distribuição de tarefas e tornando o desenvolvimento mais lento.
    - Aluna em falta: Lívia Batalha (53741) (Razão: desistência)
- O modelo de domínio abaixo será atualizado quando for lançada a versão final.



<img src="https://git.alunos.di.fc.ul.pt/fc55373/chessbookdb/-/raw/master/planning/domain_model.png" width="50%" height="50%">