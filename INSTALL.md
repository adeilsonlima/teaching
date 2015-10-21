#Instruções para Compilação e Execução

###Compilação

Entrar no diretório do projeto e criar o diretório **`bin`** para guardar os **`.class`** compilados.

```bash
mkdir bin

javac -d bin src/ufal/ic/so/*

```

###Execução

Mover o arquivo de entrada, por exemplo, **`input.txt`**, para a pasta **`bin`**.

```bash
input.txt bin

java ufal.ic.so.EscalonadorDeTarefas "arquivo" "politica"

```
`arquivo` - Nome do arquivo de entrada, por exemplo **`input.txt`**. <br>
`politica` - `fcfs`, `rr`, `rr2`, `rr3`. <br>

`fcfs` - As tarefas são executadas inteiramente, de acordo com a data de inicio da tarefa. Quem chega primeiro é executada primeiro. <br>
`rr` - As tarefas são executadas parcialmente, de acordo com a data de inicio da tarefa. Depois de **`2s`** a tarefa que está usando o processador é retirada e retorna para o estado **`pronta`**. <br> 
`rr2` - As tarefas são ordenadas de acordo com suas prioridades. A tarefa de maior prioridade usa o processador por **`2s`**, sendo substituída por outra de maior prioridade, ou retornando para o processador caso tenha maior prioridade.<br>
`rr3` - Semelhante ao `rr2`, porém, a cada tempo `t` a prioridade das tarefas que estão no estado **`pronta`** são incrementadas em `1`.<br>

Sistema Operacional utilizado: Ubuntu 15.04 x64.
