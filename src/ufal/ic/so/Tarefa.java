package ufal.ic.so;

/** @author Adeilson Lima */
public class Tarefa implements Comparable<Tarefa>{
	/** Identificador do processo */
	private String id;
	/** Entrada no sistema */
	private int criacao;
	/** Duração da tarefa */
	private int duracao;
	/** prioridade da tarefa. Crescente */
	private int prioridade;

	/** Estado da tarefa ex.:pronta, executando,encerrada */
	private Estado estado;
	/** Total já executado */
	private int tempoExecutado;

	public Tarefa() {

	}

	public Tarefa(int criacao, int duracao, Estado estado, int tempoExecutado) {
		this.criacao = criacao;
		this.duracao = duracao;
		this.estado = estado;
		this.tempoExecutado = tempoExecutado;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getCriacao() {
		return criacao;
	}

	public void setCriacao(int criacao) {
		this.criacao = criacao;
	}

	public int getDuracao() {
		return duracao;
	}

	public void setDuracao(int duracao) {
		this.duracao = duracao;
	}

	public int getPrioridade() {
		return prioridade;
	}

	public void setPrioridade(int prioridade) {
		this.prioridade = prioridade;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public int getTempoExecutado() {
		return tempoExecutado;
	}

	public void setTempoExecutado(int tempoExecutado) {
		this.tempoExecutado = tempoExecutado;
	}

	/**Compara duas tarefas
	 * @param o tarefa a ser comparada
	 * @return */
	@Override
	public int compareTo(Tarefa o) {
		int cmp = this.criacao > o.getCriacao() ? +1 : this.criacao < o.getCriacao() ? -1 : 0;
		return cmp;
	}
}
